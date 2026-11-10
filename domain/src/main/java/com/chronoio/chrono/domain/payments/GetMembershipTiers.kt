package com.chronoio.chrono.domain.payments

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.membership.MembershipTierData
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetMembershipTiers @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<GetMembershipTiers.Params, List<MembershipTierData>>(dispatchers.io) {

    override suspend fun doWork(params: Params): List<MembershipTierData> {
        val command = Command.Membership.GetTiers(
            locale = params.locale,
            noCache = params.noCache
        )
        return repo.membershipGetTiers(command)
    }

    data class Params(
        val locale: String,
        val noCache: Boolean
    )
}