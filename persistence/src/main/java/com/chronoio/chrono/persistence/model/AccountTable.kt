package com.chronoio.chrono.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chronoio.chrono.persistence.common.Config

@Entity(tableName = Config.ACCOUNT_TABLE_NAME)
data class AccountTable(
    @PrimaryKey val id: String,
    val timestamp: Long,
    @Deprecated("Should not be used")
    val color: String? = null,
    @Deprecated("Should not be used")
    val name: String,
)