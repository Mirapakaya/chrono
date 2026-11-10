package com.chronoio.chrono.domain.payments

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Second step of the membership code-redemption flow: redeem the validated code and activate the
 * membership. The any-name is sent empty (name selection is not part of this flow), matching desktop.
 */
class RedeemMembershipCode @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<RedeemMembershipCode.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.Membership.CodeRedeem(
            code = params.code
        )
        repo.membershipCodeRedeem(command)
    }

    data class Params(
        val code: String
    )
}
