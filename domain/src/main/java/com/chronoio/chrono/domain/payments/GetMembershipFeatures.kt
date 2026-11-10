package com.chronoio.chrono.domain.payments

import com.chronoio.chrono.core_models.membership.MembershipFeatures
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetMembershipFeatures @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<Unit, MembershipFeatures>(dispatchers.io) {

    override suspend fun doWork(params: Unit): MembershipFeatures {
        return repo.membershipV2GetFeatures()
    }
}
