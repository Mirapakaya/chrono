package com.chronoio.chrono.persistence.mapper

import com.chronoio.chrono.data.auth.model.AccountEntity
import com.chronoio.chrono.persistence.model.AccountTable

fun AccountTable.toEntity(): AccountEntity {
    return AccountEntity(
        id = id
    )
}

fun AccountEntity.toTable(): AccountTable {
    return AccountTable(
        id = id,
        timestamp = System.currentTimeMillis(),
        name = "",
        color = null
    )
}