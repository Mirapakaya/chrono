package com.chronoio.chrono.di.feature.notifications

import android.content.Context
import com.chronoio.chrono.device.AnytypePushService
import com.chronoio.chrono.device.DefaultPushMessageProcessor
import com.chronoio.chrono.device.PushMessageProcessor
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.device.DeviceTokenStoringService
import com.chronoio.chrono.domain.notifications.NotificationBuilder
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.presentation.notifications.CryptoService
import com.chronoio.chrono.presentation.notifications.CryptoServiceImpl
import com.chronoio.chrono.presentation.notifications.DecryptionPushContentService
import com.chronoio.chrono.presentation.notifications.DecryptionPushContentServiceImpl
import com.chronoio.chrono.presentation.notifications.SignatureVerificationService
import com.chronoio.chrono.presentation.notifications.SignatureVerificationServiceImpl
import com.chronoio.chrono.domain.notifications.PushKeyProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json

@Singleton
@Component(
    dependencies = [PushContentDependencies::class],
    modules = [PushContentModule::class],
)
interface PushContentComponent {
    @Component.Factory
    interface Factory {
        fun create(dependency: PushContentDependencies): PushContentComponent
    }

    fun inject(service: AnytypePushService)
}

@Module
object PushContentModule {

    @JvmStatic
    @Provides
    @Singleton
    fun providePushMessageProcessor(
        decryptionService: DecryptionPushContentService,
        notificationBuilder: NotificationBuilder
    ): PushMessageProcessor = DefaultPushMessageProcessor(
        decryptionService = decryptionService,
        notificationBuilder = notificationBuilder
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideCryptoService(): CryptoService = CryptoServiceImpl()

    @JvmStatic
    @Provides
    @Singleton
    fun provideSignatureVerificationService(): SignatureVerificationService = SignatureVerificationServiceImpl()

    @JvmStatic
    @Provides
    @Singleton
    fun provideDecryptionPushContentService(
        pushKeyProvider: PushKeyProvider,
        cryptoService: CryptoService,
        signatureVerificationService: SignatureVerificationService,
        json: Json
    ): DecryptionPushContentService = DecryptionPushContentServiceImpl(
        pushKeyProvider = pushKeyProvider,
        cryptoService = cryptoService,
        signatureVerificationService = signatureVerificationService,
        json = json
    )
}

interface PushContentDependencies : ComponentDependencies {
    fun deviceTokenSavingService(): DeviceTokenStoringService
    fun pushKeyProvider(): PushKeyProvider
    fun context(): Context
    @Named(DEFAULT_APP_COROUTINE_SCOPE) fun scope(): CoroutineScope
    fun dispatchers(): AppCoroutineDispatchers
    fun provider(): StringResourceProvider
    fun notificationBuilder(): NotificationBuilder
    fun json(): Json
    fun authRepository(): AuthRepository
}