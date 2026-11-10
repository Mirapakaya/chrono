package com.chronoio.chrono.core_models

data class AccountSetup(
    val account: Account,
    val config: Config,
    val status: AccountStatus
)