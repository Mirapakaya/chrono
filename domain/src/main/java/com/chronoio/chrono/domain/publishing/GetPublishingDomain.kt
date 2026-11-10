package com.chronoio.chrono.domain.publishing

import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetPublishingDomain @Inject constructor(
    private val auth: AuthRepository,
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers,
) : ResultInteractor<GetPublishingDomain.Params, String?>(dispatchers.io) {

    override suspend fun doWork(params: Params): String? {
        val account = auth.getCurrentAccountId()
        val results = repo.searchObjects(
            space = params.space,
            limit = 1,
            keys = listOf(
                Relations.ID,
                Relations.NAME,
                Relations.GLOBAL_NAME
            ),
            filters = buildList {
                add(
                    DVFilter(
                        relation = Relations.LAYOUT,
                        value = ObjectType.Layout.PARTICIPANT.code.toDouble(),
                        condition = DVFilterCondition.EQUAL
                    )
                )
                add(
                    DVFilter(
                        relation = Relations.IDENTITY,
                        value = account,
                        condition = DVFilterCondition.EQUAL
                    )
                )
            },
        )
        if (results.isNotEmpty()) {
            val wrapper = ObjectWrapper.SpaceMember(results.first())
            val globalName = wrapper.globalName
            return if (globalName.isNullOrEmpty()) {
                "$FREE_DOMAIN/$account"
            } else {
                "$globalName.$PAID_DOMAIN"
            }
        } else {
            return "$FREE_DOMAIN/$account"
        }
    }

    data class Params(val space: SpaceId)

    companion object {
        private const val FREE_DOMAIN = "chrono.app"
        private const val PAID_DOMAIN = "org"
    }
}