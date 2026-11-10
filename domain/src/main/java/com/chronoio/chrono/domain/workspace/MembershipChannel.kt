package com.chronoio.chrono.domain.workspace

import com.chronoio.chrono.core_models.membership.Membership
import com.chronoio.chrono.core_models.membership.MembershipTiers
import kotlinx.coroutines.flow.Flow

interface MembershipChannel {
    fun observe(): Flow<List<Membership.Event>>
    fun observeTiers(): Flow<List<MembershipTiers.Event>>
}