package com.chronoio.chrono.data.auth.repo

import com.chronoio.chrono.core_models.NetworkModeConfig
import com.chronoio.chrono.data.auth.model.AccountEntity

interface AuthCache {

    suspend fun saveAccount(account: AccountEntity)
    suspend fun updateAccount(account: AccountEntity)

    suspend fun saveMnemonic(mnemonic: String)
    suspend fun getMnemonic(): String?

    suspend fun getCurrentAccount(): AccountEntity
    suspend fun getCurrentAccountId(): String

    suspend fun logout()
    suspend fun getAccounts(): List<AccountEntity>
    suspend fun setCurrentAccount(id: String)

    suspend fun getNetworkMode(): NetworkModeConfig
    suspend fun setNetworkMode(modeConfig: NetworkModeConfig)
}