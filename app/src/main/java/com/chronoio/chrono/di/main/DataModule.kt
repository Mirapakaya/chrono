package com.chronoio.chrono.di.main

import android.content.Context
import android.content.SharedPreferences
import com.chronoio.chrono.app.DefaultAppActionManager
import com.chronoio.chrono.app.DefaultInitialParamsProvider
import com.chronoio.chrono.core_utils.tools.ThreadInfo
import com.chronoio.chrono.data.auth.repo.AuthCache
import com.chronoio.chrono.data.auth.repo.AuthCacheDataStore
import com.chronoio.chrono.data.auth.repo.AuthDataRepository
import com.chronoio.chrono.data.auth.repo.AuthDataStoreFactory
import com.chronoio.chrono.data.auth.repo.AuthRemote
import com.chronoio.chrono.data.auth.repo.AuthRemoteDataStore
import com.chronoio.chrono.data.auth.repo.DebugSettingsCache
import com.chronoio.chrono.data.auth.repo.InfrastructureDataRepository
import com.chronoio.chrono.data.auth.repo.UserSettingsCache
import com.chronoio.chrono.data.auth.repo.UserSettingsDataRepository
import com.chronoio.chrono.data.auth.repo.block.BlockDataRepository
import com.chronoio.chrono.data.auth.repo.block.BlockRemote
import com.chronoio.chrono.data.auth.repo.favorites.PersonalFavoritesDataRepository
import com.chronoio.chrono.data.auth.repo.unsplash.UnsplashDataRepository
import com.chronoio.chrono.data.auth.repo.unsplash.UnsplashRemote
import com.chronoio.chrono.data.auth.types.DefaultObjectTypesProvider
import com.chronoio.chrono.device.AppStateService
import com.chronoio.chrono.presentation.device.BuildProvider
import com.chronoio.chrono.presentation.device.DefaultBuildProvider
import com.chronoio.chrono.device.DefaultPathProvider
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.favorites.PersonalFavoritesRepository
import com.chronoio.chrono.domain.config.InfrastructureRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository
import com.chronoio.chrono.domain.debugging.DebugConfig
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.device.PathProvider
import com.chronoio.chrono.domain.misc.AppActionManager
import com.chronoio.chrono.domain.`object`.ObjectTypesProvider
import com.chronoio.chrono.domain.objects.DefaultObjectStore
import com.chronoio.chrono.domain.objects.ObjectStore
import com.chronoio.chrono.domain.platform.InitialParamsProvider
import com.chronoio.chrono.domain.unsplash.UnsplashRepository
import com.chronoio.chrono.domain.invite.SpaceInviteLinkStore
import com.chronoio.chrono.domain.invite.SpaceInviteLinkStoreImpl
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.widgets.OsWidgetDataCleaner
import com.chronoio.chrono.domain.widgets.OsWidgetDataViewSync
import com.chronoio.chrono.domain.widgets.OsWidgetSpacesSync
import com.chronoio.chrono.domain.`object`.GetObject
import com.chronoio.chrono.domain.search.SearchObjects
import com.chronoio.chrono.feature_os_widgets.persistence.OsWidgetDataCleanerImpl
import com.chronoio.chrono.feature_os_widgets.persistence.OsWidgetDataViewSyncImpl
import com.chronoio.chrono.feature_os_widgets.persistence.OsWidgetSpacesSyncImpl
import com.chronoio.chrono.middleware.EventProxy
import com.chronoio.chrono.middleware.UnsplashMiddleware
import com.chronoio.chrono.middleware.auth.AuthMiddleware
import com.chronoio.chrono.middleware.block.BlockMiddleware
import com.chronoio.chrono.middleware.interactor.Middleware
import com.chronoio.chrono.middleware.interactor.MiddlewareFactory
import com.chronoio.chrono.middleware.interactor.MiddlewareProtobufLogger
import com.chronoio.chrono.middleware.interactor.ProtobufConverterProvider
import com.chronoio.chrono.middleware.service.MiddlewareService
import com.chronoio.chrono.middleware.service.MiddlewareServiceImplementation
import com.chronoio.chrono.persistence.db.AnytypeDatabase
import com.chronoio.chrono.device.providers.AppDefaultDateFormatProvider
import com.chronoio.chrono.device.providers.AppDefaultDateFormatProviderImpl
import com.chronoio.chrono.di.main.ConfigModule.DEFAULT_APP_COROUTINE_SCOPE
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.device.SetAppState
import com.chronoio.chrono.domain.misc.LocaleProvider
import com.chronoio.chrono.domain.network.NetworkModeProvider
import com.chronoio.chrono.persistence.repo.DefaultAuthCache
import com.chronoio.chrono.persistence.repo.DefaultDebugSettingsCache
import com.chronoio.chrono.persistence.repo.DefaultUserSettingsCache
import com.chronoio.chrono.presentation.util.downloader.UriFileProvider
import com.chronoio.chrono.providers.DefaultUriFileProvider
import com.chronoio.chrono.security.KeystoreManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope

@Module(includes = [DataModule.Bindings::class])
object DataModule {

    @JvmStatic
    @Provides
    fun providePathProvider(context: Context): PathProvider {
        return DefaultPathProvider(context)
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideAuthRepository(
        factory: AuthDataStoreFactory,
        debugConfig: DebugConfig
    ): AuthRepository {
        return AuthDataRepository(
            factory = factory,
            debugConfig = debugConfig
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideAuthDataStoreFactory(
        authCacheDataStore: AuthCacheDataStore,
        authRemoteDataStore: AuthRemoteDataStore
    ): AuthDataStoreFactory {
        return AuthDataStoreFactory(
            cache = authCacheDataStore,
            remote = authRemoteDataStore
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideAuthCacheDataStore(
        authCache: AuthCache
    ): AuthCacheDataStore {
        return AuthCacheDataStore(
            cache = authCache
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideAuthCache(
        db: AnytypeDatabase,
        @Named("default") defaultPrefs: SharedPreferences,
        @Named("encrypted") encryptedPrefs: SharedPreferences,
        networkModeProvider: NetworkModeProvider
    ): AuthCache {
        return DefaultAuthCache(
            db = db,
            defaultPrefs = defaultPrefs,
            encryptedPrefs = encryptedPrefs,
            networkModeProvider = networkModeProvider
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideDebugSettingsCache(
        @Named("default") prefs: SharedPreferences,
    ): DebugSettingsCache {
        return DefaultDebugSettingsCache(
            prefs = prefs
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideInfrastructureRepository(
        cache: DebugSettingsCache
    ): InfrastructureRepository {
        return InfrastructureDataRepository(
            cache = cache
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideAnytypeDatabase(context: Context): AnytypeDatabase {
        return AnytypeDatabase.get(context)
    }

    @JvmStatic
    @Provides
    @Singleton
    @Named("default")
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    @JvmStatic
    @Provides
    @Singleton
    @Named("encrypted")
    fun provideEncryptedSharedPreferences(
        keystoreManager: KeystoreManager
    ): SharedPreferences = keystoreManager.initializeEncryptedPreferences()

    @JvmStatic
    @Provides
    @Singleton
    fun provideAuthRemoteDataStore(
        authRemote: AuthRemote
    ): AuthRemoteDataStore {
        return AuthRemoteDataStore(
            authRemote = authRemote
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideAuthRemote(
        middleware: Middleware,
        proxy: EventProxy
    ): AuthRemote {
        return AuthMiddleware(
            middleware = middleware,
            events = proxy
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideBlockRepository(
        blockRemote: BlockRemote,
        debugConfig: DebugConfig,
        logger: Logger
    ): BlockRepository {
        return BlockDataRepository(
            remote = blockRemote,
            debugConfig = debugConfig,
            logger = logger
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideBlockRemote(
        middleware: Middleware
    ): BlockRemote = BlockMiddleware(middleware = middleware)

    @JvmStatic
    @Provides
    @Singleton
    fun providePersonalFavoritesRepository(
        blocks: BlockRepository
    ): PersonalFavoritesRepository = PersonalFavoritesDataRepository(blockRepo = blocks)

    @JvmStatic
    @Provides
    @Singleton
    fun provideMiddleware(
        service: MiddlewareService,
        factory: MiddlewareFactory,
        logger: MiddlewareProtobufLogger,
        protobufConverter: ProtobufConverterProvider,
        threadInfo: ThreadInfo
    ): Middleware = Middleware(
        service = service,
        factory = factory,
        logger = logger,
        protobufConverter = protobufConverter,
        threadInfo = threadInfo
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideMiddlewareFactory(): MiddlewareFactory = MiddlewareFactory()

    @JvmStatic
    @Provides
    @Singleton
    fun provideObjectTypesProvider(): ObjectTypesProvider {
        return DefaultObjectTypesProvider()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideUserSettingsCache(
        @Named("default") prefs: SharedPreferences,
        context: Context,
        appDefaultDateFormatProvider: AppDefaultDateFormatProvider,
        osWidgetDataCleaner: OsWidgetDataCleaner
    ): UserSettingsCache = DefaultUserSettingsCache(
        prefs = prefs,
        context = context,
        appDefaultDateFormatProvider = appDefaultDateFormatProvider,
        osWidgetDataCleaner = osWidgetDataCleaner
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideOsWidgetDataCleaner(
        context: Context
    ): OsWidgetDataCleaner = OsWidgetDataCleanerImpl(context)

    @JvmStatic
    @Provides
    @Singleton
    fun provideUserSettingsRepo(
        cache: UserSettingsCache
    ): UserSettingsRepository = UserSettingsDataRepository(cache)

    @JvmStatic
    @Provides
    @Singleton
    fun provideOsWidgetSpacesSync(
        context: Context,
        urlBuilder: UrlBuilder
    ): OsWidgetSpacesSync = OsWidgetSpacesSyncWithUpdate(
        delegate = OsWidgetSpacesSyncImpl(context, urlBuilder),
        context = context
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideOsWidgetDataViewSync(
        context: Context,
        getObject: GetObject,
        searchObjects: SearchObjects,
        blockRepository: BlockRepository
    ): OsWidgetDataViewSync = OsWidgetDataViewSyncWithUpdate(
        delegate = OsWidgetDataViewSyncImpl(
            context = context,
            getObject = getObject,
            searchObjects = searchObjects,
            blockRepository = blockRepository
        ),
        context = context
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppActionManager(context: Context): AppActionManager = DefaultAppActionManager(
        context = context
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideObjectStore(): ObjectStore = DefaultObjectStore()

    //region Unsplash

    @JvmStatic
    @Provides
    @Singleton
    fun provideUnsplashRepo(
        remote: UnsplashRemote
    ): UnsplashRepository = UnsplashDataRepository(
        remote = remote
    )
    //endregion

    @JvmStatic
    @Provides
    @Singleton
    fun provideBuildProvider(): BuildProvider = DefaultBuildProvider()

    @JvmStatic
    @Provides
    @Singleton
    fun provideMetricsProvider(context: Context): InitialParamsProvider =
        DefaultInitialParamsProvider(context)

    @JvmStatic
    @Provides
    @Singleton
    fun provideFileProvider(
        context: Context,
        logger: Logger
    ): UriFileProvider = DefaultUriFileProvider(context, logger)

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppDefaultDateFormatProvider(
        localeProvider: LocaleProvider
    ): AppDefaultDateFormatProvider = AppDefaultDateFormatProviderImpl(
        localeProvider = localeProvider
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideSetDeviceState(
        blockRepository: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetAppState = SetAppState(blockRepository, dispatchers)

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppStateService(
        setAppState: SetAppState,
        @Named(DEFAULT_APP_COROUTINE_SCOPE) scope: CoroutineScope,
        userSettingsRepository: UserSettingsRepository
    ): AppStateService = AppStateService(
        setAppState = setAppState,
        coroutineScope = scope,
        settings = userSettingsRepository
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideSpaceInviteLinkStore(
    ): SpaceInviteLinkStore = SpaceInviteLinkStoreImpl()

    @Provides
    @Singleton
    fun provideKeystoreManager(context: Context): KeystoreManager = KeystoreManager(context)

    @Module
    interface Bindings {

        @Binds
        @Singleton
        fun bindUnsplashRemote(middleware: UnsplashMiddleware): UnsplashRemote

        @Binds
        @Singleton
        fun bindMiddlewareService(middleware: MiddlewareServiceImplementation): MiddlewareService
    }
}

const val ENCRYPTED_PREFS_NAME = "encrypted_prefs"
