package com.chronoio.chrono.middleware.auth
import com.chronoio.chrono.middleware.EventGroup

import com.chronoio.chrono.core_models.AccountSetup
import com.chronoio.chrono.core_models.AccountStatus
import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.data.auth.model.WalletEntity
import com.chronoio.chrono.data.auth.repo.AuthRemote
import com.chronoio.chrono.middleware.EventProxy
import com.chronoio.chrono.middleware.converters.toAccountEntity
import com.chronoio.chrono.middleware.interactor.Middleware
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AuthMiddleware(
    private val middleware: Middleware,
    private val events: EventProxy
) : AuthRemote {

    override suspend fun selectAccount(
        command: Command.AccountSelect
    ): AccountSetup = middleware.accountSelect(command)

    override suspend fun createAccount(
        command: Command.AccountCreate
    ) : AccountSetup = middleware.accountCreate(command)

    override suspend fun preloadRemainingSpaces() {
        middleware.accountPreloadRemainingSpaces()
    }

    override suspend fun migrateAccount(
        account: Id,
        path: String
    ) {
        middleware.accountMigrate(
            account = account,
            path = path
        )
    }

    override suspend fun cancelAccountMigration(account: Id) {
        middleware.accountMigrateCancel(
            account = account
        )
    }

    override suspend fun deleteAccount(): AccountStatus = middleware.accountDelete()
    override suspend fun restoreAccount(): AccountStatus = middleware.accountRestore()

    override suspend fun recoverAccount() = withContext(Dispatchers.IO) {
        middleware.accountRecover()
    }

    override fun observeAccounts() = events
        .flow(EventGroup.ACCOUNT)
        .filter { event ->
            event.messages.any { message ->
                message.accountShow != null
            }
        }
        .map { event ->
            event.messages.filter { message ->
                message.accountShow != null
            }
        }
        .flatMapConcat { messages -> messages.asFlow() }
        .map {
            val event = it.accountShow
            checkNotNull(event)
            event.toAccountEntity()
        }

    override suspend fun createWallet(
        path: String
    ) = WalletEntity(mnemonic = middleware.walletCreate(path).mnemonic)

    override suspend fun recoverWallet(path: String, mnemonic: String) {
        middleware.walletRecover(path, mnemonic)
    }

    override suspend fun convertWallet(entropy: String): String = middleware.walletConvert(entropy)

    override suspend fun logout(clearLocalRepositoryData: Boolean) {
        middleware.accountStop(clearLocalRepositoryData)
    }

    override suspend fun getVersion(): String {
        return middleware.versionGet().version
    }

    override suspend fun setInitialParams(command: Command.SetInitialParams) {
        middleware.metricsSetParameters(command)
    }

    override suspend fun debugExportLogs(dir: String): String {
        return middleware.debugExportLogs(dir)
    }

    override suspend fun debugRunProfiler(durationInSeconds: Int): String {
        return middleware.debugRunProfiler(durationInSeconds)
    }

    override suspend fun registerDeviceToken(command: Command.RegisterDeviceToken) {
        middleware.registerDeviceToken(command = command)
    }

    override suspend fun appShutdown() {
        middleware.appShutdown()
    }
}