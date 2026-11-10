package com.chronoio.chrono.domain.payments

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.membership.NameServiceNameType
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class IsMembershipNameValid @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<IsMembershipNameValid.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.Membership.IsNameValid(
            tier = params.tier,
            name = params.name,
            nameType = params.nameType
        )
        repo.membershipIsNameValid(command)
    }

    data class Params(
        val tier: Int,
        val name: String,
        val nameType: NameServiceNameType = NameServiceNameType.ANY_NAME
    )
}