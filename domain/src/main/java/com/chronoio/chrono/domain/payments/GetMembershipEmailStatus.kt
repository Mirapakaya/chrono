package com.chronoio.chrono.domain.payments

import com.chronoio.chrono.core_models.membership.EmailVerificationStatus
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetMembershipEmailStatus @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<Unit, EmailVerificationStatus>(dispatchers.io) {

    override suspend fun doWork(params: Unit): EmailVerificationStatus {
        return repo.membershipGetVerificationEmailStatus()
    }
}