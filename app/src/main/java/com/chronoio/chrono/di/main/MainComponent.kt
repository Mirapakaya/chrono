package com.chronoio.chrono.di.main

import com.chronoio.chrono.app.AndroidApplication
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.di.common.ComponentDependenciesKey
import com.chronoio.chrono.di.feature.AllContentDependencies
import com.chronoio.chrono.di.feature.AppPreferencesDependencies
import com.chronoio.chrono.di.feature.BacklinkOrAddToObjectDependencies
import com.chronoio.chrono.di.feature.CreateBookmarkSubComponent
import com.chronoio.chrono.di.feature.CreateObjectFeatureDependencies
import com.chronoio.chrono.di.feature.CreateObjectSubComponent
import com.chronoio.chrono.di.feature.CreateObjectTypeDependencies
import com.chronoio.chrono.di.feature.DateObjectDependencies
import com.chronoio.chrono.di.feature.DebugSettingsSubComponent
import com.chronoio.chrono.di.feature.EditTypePropertiesDependencies
import com.chronoio.chrono.di.feature.EditorSubComponent
import com.chronoio.chrono.di.feature.KeychainPhraseSubComponent
import com.chronoio.chrono.di.feature.LinkToObjectDependencies
import com.chronoio.chrono.di.feature.MainEntrySubComponent
import com.chronoio.chrono.di.feature.MediaDependencies
import com.chronoio.chrono.di.feature.MoveToDependencies
import com.chronoio.chrono.di.feature.MySitesDependencies
import com.chronoio.chrono.di.feature.ObjectSetSubComponent
import com.chronoio.chrono.di.feature.ObjectTypeChangeSubComponent
import com.chronoio.chrono.di.feature.ObjectTypeDependencies
import com.chronoio.chrono.di.feature.PublishToWebDependencies
import com.chronoio.chrono.di.feature.SpacePropertiesDependencies
import com.chronoio.chrono.di.feature.SpaceTypesDependencies
import com.chronoio.chrono.di.feature.SplashDependencies
import com.chronoio.chrono.di.feature.auth.DeletedAccountDependencies
import com.chronoio.chrono.di.feature.chats.ChatComponentDependencies
import com.chronoio.chrono.di.feature.discussions.DiscussionComponentDependencies
import com.chronoio.chrono.di.feature.chats.ChatReactionDependencies
import com.chronoio.chrono.di.feature.chats.SelectChatIconDependencies
import com.chronoio.chrono.di.feature.chats.SelectChatReactionDependencies
import com.chronoio.chrono.di.feature.gallery.GalleryInstallationComponentDependencies
import com.chronoio.chrono.di.feature.home.HomeScreenDependencies
import com.chronoio.chrono.di.feature.membership.MembershipComponentDependencies
import com.chronoio.chrono.di.feature.multiplayer.RequestJoinSpaceDependencies
import com.chronoio.chrono.di.feature.multiplayer.ShareSpaceDependencies
import com.chronoio.chrono.di.feature.multiplayer.SpaceJoinRequestDependencies
import com.chronoio.chrono.di.feature.notifications.NotificationDependencies
import com.chronoio.chrono.di.feature.notifications.PushContentDependencies
import com.chronoio.chrono.di.feature.objects.SelectObjectTypeDependencies
import com.chronoio.chrono.di.feature.onboarding.OnboardingDependencies
import com.chronoio.chrono.di.feature.oswidgets.CreateObjectWidgetConfigDependencies
import com.chronoio.chrono.di.feature.oswidgets.DataViewWidgetConfigDependencies
import com.chronoio.chrono.di.feature.oswidgets.ObjectShortcutWidgetConfigDependencies
import com.chronoio.chrono.di.feature.onboarding.OnboardingStartDependencies
import com.chronoio.chrono.di.feature.onboarding.login.OnboardingMnemonicLoginDependencies
import com.chronoio.chrono.di.feature.onboarding.signup.OnboardingMnemonicDependencies
import com.chronoio.chrono.di.feature.onboarding.signup.OnboardingSoulCreationDependencies
import com.chronoio.chrono.di.feature.participant.ParticipantComponentDependencies
import com.chronoio.chrono.di.feature.relations.RelationCreateFromLibraryDependencies
import com.chronoio.chrono.di.feature.search.GlobalSearchDependencies
import com.chronoio.chrono.di.feature.search.SearchV2Dependencies
import com.chronoio.chrono.di.feature.settings.AboutAppDependencies
import com.chronoio.chrono.di.feature.settings.AppearanceDependencies
import com.chronoio.chrono.di.feature.settings.DebugDependencies
import com.chronoio.chrono.di.feature.quickcapture.QuickCaptureDependencies
import com.chronoio.chrono.di.feature.settings.ExperimentalFeaturesDependencies
import com.chronoio.chrono.di.feature.settings.FilesStorageDependencies
import com.chronoio.chrono.di.feature.settings.LogoutWarningSubComponent
import com.chronoio.chrono.di.feature.settings.ProfileSubComponent
import com.chronoio.chrono.di.feature.settings.SpacesStorageDependencies
import com.chronoio.chrono.di.feature.sharing.SharingDependencies
import com.chronoio.chrono.di.feature.spaces.CreateSpaceDependencies
import com.chronoio.chrono.di.feature.spaces.ManageSectionsDependencies
import com.chronoio.chrono.di.feature.spaces.SpaceListDependencies
import com.chronoio.chrono.di.feature.spaces.SpaceSettingsDependencies
import com.chronoio.chrono.di.feature.templates.TemplateBlankDependencies
import com.chronoio.chrono.di.feature.templates.TemplateSelectDependencies
import com.chronoio.chrono.di.feature.vault.VaultComponentDependencies
import com.chronoio.chrono.di.feature.widgets.CreateChatObjectDependencies
import com.chronoio.chrono.di.feature.widgets.SelectWidgetSourceDependencies
import com.chronoio.chrono.di.feature.widgets.SelectWidgetTypeDependencies
import com.chronoio.chrono.domain.chats.ChatPreviewContainer
import com.chronoio.chrono.domain.chats.ChatsDetailsSubscriptionContainer
import com.chronoio.chrono.domain.objects.CrossSpaceObjectTypesContainer
import com.chronoio.chrono.ui.widgets.collection.CollectionDependencies
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ComponentDependenciesModule::class,
        ContextModule::class,
        AiModule::class,
        DataModule::class,
        EventModule::class,
        ConfigModule::class,
        WorkspaceModule::class,
        DeviceModule::class,
        UtilModule::class,
        EmojiModule::class,
        ClipboardModule::class,
        AnalyticsModule::class,
        LocalNetworkProviderModule::class,
        SubscriptionsModule::class,
        CrashReportingModule::class,
        TemplatesModule::class,
        NetworkModeModule::class,
        NotificationsModule::class,
        MembershipModule::class,
        DispatcherModule::class,
        NavigationModule::class
    ]
)
interface MainComponent :
    AppearanceDependencies,
    HomeScreenDependencies,
    CollectionDependencies,
    RelationCreateFromLibraryDependencies,
    SplashDependencies,
    DeletedAccountDependencies,
    BacklinkOrAddToObjectDependencies,
    FilesStorageDependencies,
    OnboardingDependencies,
    OnboardingStartDependencies,
    OnboardingMnemonicDependencies,
    OnboardingMnemonicLoginDependencies,
    OnboardingSoulCreationDependencies,
    AboutAppDependencies,
    TemplateBlankDependencies,
    TemplateSelectDependencies,
    CreateSpaceDependencies,
    SpaceListDependencies,
    SpaceSettingsDependencies,
    ManageSectionsDependencies,
    SelectObjectTypeDependencies,
    SpacesStorageDependencies,
    AppPreferencesDependencies,
    ShareSpaceDependencies,
    SpaceJoinRequestDependencies,
    RequestJoinSpaceDependencies,
    MembershipComponentDependencies,
    GalleryInstallationComponentDependencies,
    NotificationDependencies,
    GlobalSearchDependencies,
    SearchV2Dependencies,
    VaultComponentDependencies,
    AllContentDependencies,
    ChatComponentDependencies,
    DiscussionComponentDependencies,
    SelectWidgetSourceDependencies,
    SelectWidgetTypeDependencies,
    LinkToObjectDependencies,
    MoveToDependencies,
    DateObjectDependencies,
    ObjectTypeDependencies,
    SelectChatReactionDependencies,
    SelectChatIconDependencies,
    ChatReactionDependencies,
    ParticipantComponentDependencies,
    EditTypePropertiesDependencies,
    DebugDependencies,
    ExperimentalFeaturesDependencies,
    QuickCaptureDependencies,
    CreateObjectTypeDependencies,
    SpaceTypesDependencies,
    SpacePropertiesDependencies,
    PushContentDependencies,
    PublishToWebDependencies,
    MySitesDependencies,
    MediaDependencies,
    CreateChatObjectDependencies,
    SharingDependencies,
    CreateObjectFeatureDependencies,
    CreateObjectWidgetConfigDependencies,
    DataViewWidgetConfigDependencies,
    ObjectShortcutWidgetConfigDependencies
{

    fun inject(app: AndroidApplication)

    fun editorComponentBuilder(): EditorSubComponent.Builder
    fun createBookmarkBuilder(): CreateBookmarkSubComponent.Builder
    fun mainEntryComponentBuilder(): MainEntrySubComponent.Builder
    fun objectSetComponentBuilder(): ObjectSetSubComponent.Builder
    fun objectTypeChangeComponent(): ObjectTypeChangeSubComponent.Factory
    fun createObjectComponent(): CreateObjectSubComponent.Builder

    //region Settings

    fun profileComponent(): ProfileSubComponent.Builder
    fun debugSettingsBuilder(): DebugSettingsSubComponent.Builder
    fun keychainPhraseComponentBuilder(): KeychainPhraseSubComponent.Builder
    fun logoutWarningComponent(): LogoutWarningSubComponent.Builder

    //endregion

    fun chatPreviewContainer(): ChatPreviewContainer
    fun chatsDetailsSubscriptionContainer(): ChatsDetailsSubscriptionContainer
    fun crossSpaceObjectTypesContainer(): CrossSpaceObjectTypesContainer
}

@Module
abstract class ComponentDependenciesModule {

    @Binds
    @IntoMap
    @ComponentDependenciesKey(AppearanceDependencies::class)
    abstract fun provideAppearanceDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(HomeScreenDependencies::class)
    abstract fun provideHomeScreenDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(CollectionDependencies::class)
    abstract fun provideHomeWidgetDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(RelationCreateFromLibraryDependencies::class)
    abstract fun provideRelationCreateFromLibraryDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SplashDependencies::class)
    abstract fun provideSplashDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(DeletedAccountDependencies::class)
    abstract fun provideDeletedAccountDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(BacklinkOrAddToObjectDependencies::class)
    abstract fun provideBackLinkDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(FilesStorageDependencies::class)
    abstract fun provideFilesStorageDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(OnboardingDependencies::class)
    abstract fun provideOnboardingDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(OnboardingStartDependencies::class)
    abstract fun provideOnboardingStartDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(OnboardingMnemonicDependencies::class)
    abstract fun provideOnboardingMnemonicDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(OnboardingMnemonicLoginDependencies::class)
    abstract fun provideOnboardingMnemonicLoginDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(OnboardingSoulCreationDependencies::class)
    abstract fun provideOnboardingSoulCreationDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(AboutAppDependencies::class)
    abstract fun provideAboutAppDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(TemplateBlankDependencies::class)
    abstract fun provideTemplateBlankDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(TemplateSelectDependencies::class)
    abstract fun provideTemplateSelectDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SpaceListDependencies::class)
    abstract fun provideSpaceListDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(CreateSpaceDependencies::class)
    abstract fun provideCreateSpaceDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SpaceSettingsDependencies::class)
    abstract fun provideSpaceSettingsDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ManageSectionsDependencies::class)
    abstract fun provideManageSectionsDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SelectObjectTypeDependencies::class)
    abstract fun provideCreateObjectOfTypeDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SpacesStorageDependencies::class)
    abstract fun provideSpacesStorageDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(AppPreferencesDependencies::class)
    abstract fun providePreferencesDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ShareSpaceDependencies::class)
    abstract fun provideShareSpaceDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SpaceJoinRequestDependencies::class)
    abstract fun provideSpaceJoinRequestDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(MembershipComponentDependencies::class)
    abstract fun provideMembershipComponentDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(RequestJoinSpaceDependencies::class)
    abstract fun provideRequestToJoinSpaceDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(GalleryInstallationComponentDependencies::class)
    abstract fun provideGalleryInstallationDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(NotificationDependencies::class)
    abstract fun provideNotificationDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(GlobalSearchDependencies::class)
    abstract fun provideGlobalSearchDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SearchV2Dependencies::class)
    abstract fun provideSearchV2Dependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ChatComponentDependencies::class)
    abstract fun provideChatComponentDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(DiscussionComponentDependencies::class)
    abstract fun provideDiscussionComponentDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(VaultComponentDependencies::class)
    abstract fun provideVaultComponentDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(AllContentDependencies::class)
    abstract fun provideAllContentDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SelectWidgetSourceDependencies::class)
    abstract fun provideSelectWidgetSourceDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SelectWidgetTypeDependencies::class)
    abstract fun provideSelectWidgetTypeDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(LinkToObjectDependencies::class)
    abstract fun provideLinkToObjectDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(MoveToDependencies::class)
    abstract fun provideMoveToDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(DateObjectDependencies::class)
    abstract fun provideDateObjectDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ObjectTypeDependencies::class)
    abstract fun provideObjectTypeDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SelectChatReactionDependencies::class)
    abstract fun provideChatReactionPickerDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SelectChatIconDependencies::class)
    abstract fun provideSelectChatIconDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ChatReactionDependencies::class)
    abstract fun provideChatReactionDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ParticipantComponentDependencies::class)
    abstract fun provideParticipantComponentDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(DebugDependencies::class)
    abstract fun provideDebugDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ExperimentalFeaturesDependencies::class)
    abstract fun provideExperimentalFeaturesDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(QuickCaptureDependencies::class)
    abstract fun provideQuickCaptureDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(EditTypePropertiesDependencies::class)
    abstract fun provideEditTypePropertiesDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(CreateObjectTypeDependencies::class)
    abstract fun provideCreateObjectTypeDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SpaceTypesDependencies::class)
    abstract fun provideSpaceTypesDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SpacePropertiesDependencies::class)
    abstract fun provideSpacePropertiesDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(PushContentDependencies::class)
    abstract fun providePushContentDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(PublishToWebDependencies::class)
    abstract fun providePublishToWebDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(MySitesDependencies::class)
    abstract fun mySitesDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(MediaDependencies::class)
    abstract fun mediaDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(CreateChatObjectDependencies::class)
    abstract fun createChatObjectDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(SharingDependencies::class)
    abstract fun sharingDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(CreateObjectFeatureDependencies::class)
    abstract fun provideCreateObjectFeatureDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(CreateObjectWidgetConfigDependencies::class)
    abstract fun provideCreateObjectWidgetConfigDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(ObjectShortcutWidgetConfigDependencies::class)
    abstract fun provideObjectShortcutWidgetConfigDependencies(component: MainComponent): ComponentDependencies

    @Binds
    @IntoMap
    @ComponentDependenciesKey(DataViewWidgetConfigDependencies::class)
    abstract fun provideDataViewWidgetConfigDependencies(component: MainComponent): ComponentDependencies
}
