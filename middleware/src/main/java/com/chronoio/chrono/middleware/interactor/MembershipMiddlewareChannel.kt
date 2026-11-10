package com.chronoio.chrono.middleware.interactor
import com.chronoio.chrono.middleware.EventGroup

import com.chronoio.chrono.core_models.membership.Membership
import com.chronoio.chrono.core_models.membership.MembershipTiers
import com.chronoio.chrono.data.auth.event.MembershipRemoteChannel
import com.chronoio.chrono.middleware.EventProxy
import com.chronoio.chrono.middleware.mappers.toCoreModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull

class MembershipMiddlewareChannel(
    private val eventsProxy: EventProxy
): MembershipRemoteChannel {

    override fun observe(): Flow<List<Membership.Event>> {
        return eventsProxy.flow(EventGroup.MEMBERSHIP)
            .mapNotNull { emission ->
                emission.messages.mapNotNull { message ->
                    when {
                        message.membershipUpdate != null -> {
                            val event = message.membershipUpdate
                            checkNotNull(event)
                            val membership = event.data_
                            if (membership != null) {
                                Membership.Event(
                                    membership = membership.toCoreModel()
                                )
                            } else {
                                null
                            }
                        }
                        else -> null
                    }
                }
            }.filter { events -> events.isNotEmpty() }
    }

    override fun observeTiers(): Flow<List<MembershipTiers.Event>> {
        return eventsProxy.flow(EventGroup.MEMBERSHIP)
            .mapNotNull { emission ->
                emission.messages.mapNotNull { message ->
                    when {
                        message.membershipTiersUpdate != null -> {
                            val event = message.membershipTiersUpdate
                            checkNotNull(event)
                            val tiers = event.tiers.map { it.toCoreModel() }
                            MembershipTiers.Event(tiers = tiers)
                        }
                        else -> null
                    }
                }
            }.filter { events -> events.isNotEmpty() }
    }
}