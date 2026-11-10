package com.chronoio.chrono.domain.payments

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class VerifyMembershipEmailCode @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<VerifyMembershipEmailCode.Params, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Params) {
        val command = Command.Membership.VerifyEmailCode(
            code = params.code
        )
        repo.membershipVerifyEmailCode(command)
    }

    data class Params(
        val code: String
    )
}