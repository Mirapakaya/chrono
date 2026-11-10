package com.chronoio.chrono.domain.payments

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * First step of the membership code-redemption flow: validate the code and learn which tier it
 * unlocks. Returns the `requestedTier`. Throws [com.chronoio.chrono.core_models.membership.MembershipErrors.CodeGetInfo]
 * on a non-NULL backend error.
 */
class GetMembershipCodeInfo @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<GetMembershipCodeInfo.Params, Int>(dispatchers.io) {

    override suspend fun doWork(params: Params): Int {
        val command = Command.Membership.CodeGetInfo(
            code = params.code
        )
        return repo.membershipCodeGetInfo(command)
    }

    data class Params(
        val code: String
    )
}
