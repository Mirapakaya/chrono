package com.chronoio.chrono.domain.multiplayer

import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.multiplayer.SpaceMemberPermissions
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.debugging.Logger
import javax.inject.Inject

class CheckIsUserSpaceMember @Inject constructor(
    private val repo: BlockRepository,
    private val auth: AuthRepository,
    private val logger: Logger,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SpaceId, Boolean>(dispatchers.io) {

    override suspend fun doWork(params: SpaceId): Boolean {
        val account = auth.getCurrentAccountId()
        val results = repo.searchObjects(
            space = params,
            limit = 0,
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
            keys = listOf(
                Relations.ID,
                Relations.NAME,
                Relations.PLURAL_NAME,
                Relations.SPACE_ID,
                Relations.PARTICIPANT_PERMISSIONS,
                Relations.PARTICIPANT_STATUS,
                Relations.IDENTITY,
                Relations.LAYOUT
            )
        )
        val struct = results.firstOrNull()
        return if (!struct.isNullOrEmpty()) {
            val member = ObjectWrapper.SpaceMember(struct)
            val permission = member.permissions
            if (permission == null || permission == SpaceMemberPermissions.NO_PERMISSIONS)
                false
            else
                true
        } else {
            false
        }
    }
}