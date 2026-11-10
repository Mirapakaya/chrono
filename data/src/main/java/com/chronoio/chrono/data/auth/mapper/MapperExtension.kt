package com.chronoio.chrono.data.auth.mapper

import com.chronoio.chrono.core_models.Account
import com.chronoio.chrono.data.auth.model.AccountEntity
import com.chronoio.chrono.data.auth.model.WalletEntity
import com.chronoio.chrono.domain.auth.model.Wallet

fun AccountEntity.toDomain(): Account {
    return Account(
        id = id
    )
}

fun Account.toEntity(): AccountEntity {
    return AccountEntity(
        id = id
    )
}

fun WalletEntity.toDomain(): Wallet {
    return Wallet(
        mnemonic = mnemonic
    )
}