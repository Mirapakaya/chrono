package com.chronoio.chrono.domain.payments

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.membership.Membership
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetMembershipStatus @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<GetMembershipStatus.Params, Membership?>(dispatchers.io) {

    override suspend fun doWork(params: Params): Membership? {
        val command = Command.Membership.GetStatus(
            noCache = params.noCache
        )
        return repo.membershipStatus(command)
    }

    data class Params(
        val noCache: Boolean
    )
}