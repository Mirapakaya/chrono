package com.chronoio.chrono.data.auth.event

import com.chronoio.chrono.core_models.membership.Membership
import com.chronoio.chrono.core_models.membership.MembershipTiers
import com.chronoio.chrono.domain.workspace.MembershipChannel
import kotlinx.coroutines.flow.Flow

interface MembershipRemoteChannel {
    fun observe(): Flow<List<Membership.Event>>
    fun observeTiers(): Flow<List<MembershipTiers.Event>>
}

class MembershipDateChannel(
    private val channel: MembershipRemoteChannel
) : MembershipChannel {

    override fun observe(): Flow<List<Membership.Event>> {
        return channel.observe()
    }

    override fun observeTiers(): Flow<List<MembershipTiers.Event>> {
        return channel.observeTiers()
    }
}