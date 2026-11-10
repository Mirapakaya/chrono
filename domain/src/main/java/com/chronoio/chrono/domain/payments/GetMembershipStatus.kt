package com.chronoio.chrono.domain.payments

import com.chronoio.chrono.core_models.membership.Membership
import com.chronoio.chrono.core_models.membership.MembershipPaymentMethod
import com.chronoio.chrono.core_models.membership.NameServiceNameType
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetMembershipStatus @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<GetMembershipStatus.Params, Membership?>(dispatchers.io) {

    override suspend fun doWork(params: Params): Membership? {
        // Chrono: all features are free for everyone. Return active unlimited status.
        return Membership(
            tier = ULTRA_TIER_ID,
            membershipStatusModel = Membership.Status.STATUS_ACTIVE,
            dateStarted = 0L,
            dateEnds = Long.MAX_VALUE,
            isAutoRenew = false,
            paymentMethod = MembershipPaymentMethod.METHOD_NONE,
            nameServiceName = "",
            nameServiceType = NameServiceNameType.ANY_NAME,
            userEmail = "",
            subscribeToNewsletter = false
        )
    }

    data class Params(
        val noCache: Boolean
    )

    private companion object {
        const val ULTRA_TIER_ID = 45
    }
}