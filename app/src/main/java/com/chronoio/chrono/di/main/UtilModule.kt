package com.chronoio.chrono.di.main

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.chronoio.chrono.BuildConfig
import com.chronoio.chrono.app.DefaultFeatureToggles
import com.chronoio.chrono.app.TogglePrefs
import com.chronoio.chrono.core_utils.tools.AppInfo
import com.chronoio.chrono.core_utils.tools.DefaultAppInfo
import com.chronoio.chrono.core_utils.tools.DefaultThreadInfo
import com.chronoio.chrono.core_utils.tools.DefaultUrlValidator
import com.chronoio.chrono.core_utils.tools.FeatureToggles
import com.chronoio.chrono.core_utils.tools.ThreadInfo
import com.chronoio.chrono.core_utils.tools.UrlValidator
import com.chronoio.chrono.device.providers.AppDefaultDateFormatProvider
import com.chronoio.chrono.device.providers.DateProviderImpl
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.debugging.DebugConfig
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.misc.DateTypeNameProvider
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.domain.objects.GetDateObjectByTimestamp
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.domain.primitives.FieldParserImpl
import com.chronoio.chrono.domain.resources.StringResourceProvider
import com.chronoio.chrono.middleware.interactor.MiddlewareProtobufLogger
import com.chronoio.chrono.middleware.interactor.ProtobufConverterProvider
import com.chronoio.chrono.other.BasicLogger
import com.chronoio.chrono.other.DefaultDateTypeNameProvider
import com.chronoio.chrono.other.DefaultDebugConfig
import com.chronoio.chrono.presentation.util.StringResourceProviderImpl
import com.chronoio.chrono.presentation.util.UrlHelper
import com.chronoio.chrono.presentation.widgets.collection.ResourceProvider
import com.chronoio.chrono.presentation.widgets.collection.ResourceProviderImpl
import com.chronoio.chrono.util.UrlHelperImpl
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.time.ZoneId
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Module(includes = [UtilModule.Bindings::class])
object UtilModule {

    @JvmStatic
    @Provides
    @Singleton
    @TogglePrefs
    fun providesSharedPreferences(
        context: Context
    ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppInfo(): AppInfo = DefaultAppInfo(BuildConfig.VERSION_NAME)

    @JvmStatic
    @Provides
    @Singleton
    fun provideDateProvider(
        localeProvider: LocaleProvider,
        appDefaultDateFormatProvider: AppDefaultDateFormatProvider,
        stringResourceProvider: StringResourceProvider
    ): DateProvider = DateProviderImpl(
        defaultZoneId = ZoneId.systemDefault(),
        localeProvider = localeProvider,
        appDefaultDateFormatProvider = appDefaultDateFormatProvider,
        stringResourceProvider = stringResourceProvider
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideDateByTimestamp(
        repository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetDateObjectByTimestamp = GetDateObjectByTimestamp(repository, dispatchers)

    @JvmStatic
    @Provides
    @Singleton
    fun provideFieldsProvider(
        dateProvider: DateProvider,
        logger: Logger,
        getDateObjectByTimestamp: GetDateObjectByTimestamp,
        stringResourceProvider: StringResourceProvider
    ): FieldParser = FieldParserImpl(dateProvider, logger, getDateObjectByTimestamp, stringResourceProvider)

    @JvmStatic
    @Provides
    @Singleton
    fun provideResourceProvider(context: Context): ResourceProvider =
        ResourceProviderImpl(context)

    @JvmStatic
    @Provides
    @Singleton
    fun provideStringResourceProvider(context: Context): StringResourceProvider =
        StringResourceProviderImpl(context)

    @JvmStatic
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @JvmStatic
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideUrlHelper(): UrlHelper = UrlHelperImpl()

    @Module
    interface Bindings {

        @Binds
        @Singleton
        fun bindLogger(logger: BasicLogger): Logger

        @Binds
        @Singleton
        fun bindDebugConfig(config: DefaultDebugConfig): DebugConfig

        @Binds
        @Singleton
        fun bindThreadInfo(info: DefaultThreadInfo): ThreadInfo

        @Binds
        @Singleton
        fun bindUrlValidator(applicator: DefaultUrlValidator): UrlValidator

        @Binds
        @Singleton
        fun bindFeatureToggles(applicator: DefaultFeatureToggles): FeatureToggles

        @Binds
        @Singleton
        fun bindMiddlewareProtobufLogger(logger: MiddlewareProtobufLogger.Impl): MiddlewareProtobufLogger

        @Binds
        @Singleton
        fun bindProtobufConverterProvider(provider: ProtobufConverterProvider.Impl): ProtobufConverterProvider

        @Binds
        @Singleton
        fun bindDateTypeNameProvider(provider: DefaultDateTypeNameProvider): DateTypeNameProvider
    }
}