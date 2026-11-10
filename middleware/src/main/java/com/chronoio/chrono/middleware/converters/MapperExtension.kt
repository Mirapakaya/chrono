package com.chronoio.chrono.middleware.converters

import anytype.Event
import com.chronoio.chrono.data.auth.model.AccountEntity

fun Event.Account.Show.toAccountEntity(): AccountEntity {
    val acc = account
    checkNotNull(acc)
    return AccountEntity(
        id = acc.id
    )
}