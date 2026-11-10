package com.chronoio.chrono.di.common

import com.chronoio.chrono.BuildConfig
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.di.feature.CreateBookmarkModule
import com.chronoio.chrono.di.feature.CreateObjectModule
import com.chronoio.chrono.di.feature.DaggerAllContentComponent
import com.chronoio.chrono.di.feature.DaggerAppPreferencesComponent
import com.chronoio.chrono.di.feature.DaggerBacklinkOrAddToObjectComponent
import com.chronoio.chrono.di.feature.DaggerCreateObjectFeatureComponent
import com.chronoio.chrono.di.feature.DaggerCreateObjectTypeComponent
import com.chronoio.chrono.di.feature.DaggerDateObjectComponent
import com.chronoio.chrono.di.feature.DaggerEditTypePropertiesComponent
import com.chronoio.chrono.di.feature.DaggerLinkToObjectComponent
import com.chronoio.chrono.di.feature.DaggerMediaComponent
import com.chronoio.chrono.di.feature.DaggerMoveToComponent
import com.chronoio.chrono.di.feature.DaggerMySitesComponent
import com.chronoio.chrono.di.feature.DaggerObjectTypeComponent
import com.chronoio.chrono.di.feature.DaggerPublishToWebComponent
import com.chronoio.chrono.di.feature.DaggerSpacePropertiesComponent
import com.chronoio.chrono.di.feature.DaggerSpaceTypesComponent
import com.chronoio.chrono.di.feature.DaggerSplashComponent
import com.chronoio.chrono.di.feature.DebugSettingsModule
import com.chronoio.chrono.di.feature.DefaultComponentParam
import com.chronoio.chrono.di.feature.EditorSessionModule
import com.chronoio.chrono.di.feature.EditorUseCaseModule
import com.chronoio.chrono.di.feature.KeychainPhraseModule
import com.chronoio.chrono.di.feature.LinkToObjectOrWebModule
import com.chronoio.chrono.di.feature.MainEntryModule
import com.chronoio.chrono.di.feature.ModifyViewerSortModule
import com.chronoio.chrono.di.feature.ObjectAppearanceIconModule
import com.chronoio.chrono.di.feature.ObjectAppearancePreviewLayoutModule
import com.chronoio.chrono.di.feature.ObjectAppearanceSettingModule
import com.chronoio.chrono.di.feature.ObjectIconPickerBaseModule
import com.chronoio.chrono.di.feature.ObjectIconPickerModule
import com.chronoio.chrono.di.feature.ObjectMenuModule
import com.chronoio.chrono.di.feature.ObjectMenuModuleBase
import com.chronoio.chrono.di.feature.ObjectRelationListModule
import com.chronoio.chrono.di.feature.ObjectSetCreateBookmarkRecordModule
import com.chronoio.chrono.di.feature.ObjectSetIconPickerModule
import com.chronoio.chrono.di.feature.ObjectSetMenuModule
import com.chronoio.chrono.di.feature.ObjectSetModule
import com.chronoio.chrono.di.feature.ObjectSetRecordModule
import com.chronoio.chrono.di.feature.ObjectSetSettingsModule
import com.chronoio.chrono.di.feature.RelationDataViewDateValueModule
import com.chronoio.chrono.di.feature.RelationDataViewTextValueModule
import com.chronoio.chrono.di.feature.RelationDateValueModule
import com.chronoio.chrono.di.feature.RelationTextValueModule
import com.chronoio.chrono.di.feature.SelectCoverObjectModule
import com.chronoio.chrono.di.feature.SelectCoverObjectSetModule
import com.chronoio.chrono.di.feature.SelectSortRelationModule
import com.chronoio.chrono.di.feature.TextBlockIconPickerModule
import com.chronoio.chrono.di.feature.ViewerFilterModule
import com.chronoio.chrono.di.feature.ViewerSortModule
import com.chronoio.chrono.di.feature.auth.DaggerDeletedAccountComponent
import com.chronoio.chrono.di.feature.chats.DaggerChatComponent
import com.chronoio.chrono.di.feature.discussions.DaggerDiscussionComponent
import com.chronoio.chrono.di.feature.chats.DaggerChatReactionComponent
import com.chronoio.chrono.di.feature.chats.DaggerSelectChatIconComponent
import com.chronoio.chrono.di.feature.chats.DaggerSelectChatReactionComponent
import com.chronoio.chrono.di.feature.cover.UnsplashModule
import com.chronoio.chrono.di.feature.gallery.DaggerGalleryInstallationComponent
import com.chronoio.chrono.di.feature.home.DaggerHomeScreenComponent
import com.chronoio.chrono.di.feature.membership.DaggerMembershipComponent
import com.chronoio.chrono.di.feature.multiplayer.DaggerRequestJoinSpaceComponent
import com.chronoio.chrono.di.feature.multiplayer.DaggerShareSpaceComponent
import com.chronoio.chrono.di.feature.multiplayer.DaggerSpaceJoinRequestComponent
import com.chronoio.chrono.di.feature.notifications.DaggerNotificationComponent
import com.chronoio.chrono.di.feature.notifications.DaggerPushContentComponent
import com.chronoio.chrono.di.feature.objects.DaggerSelectObjectTypeComponent
import com.chronoio.chrono.di.feature.onboarding.DaggerOnboardingComponent
import com.chronoio.chrono.di.feature.onboarding.DaggerOnboardingStartComponent
import com.chronoio.chrono.di.feature.onboarding.login.DaggerOnboardingMnemonicLoginComponent
import com.chronoio.chrono.di.feature.onboarding.signup.DaggerOnboardingMnemonicComponent
import com.chronoio.chrono.di.feature.onboarding.signup.DaggerOnboardingSoulCreationComponent
import com.chronoio.chrono.di.feature.participant.DaggerParticipantComponent
import com.chronoio.chrono.di.feature.relations.DaggerRelationCreateFromLibraryComponent
import com.chronoio.chrono.di.feature.relations.LimitObjectTypeModule
import com.chronoio.chrono.di.feature.relations.RelationAddToDataViewModule
import com.chronoio.chrono.di.feature.relations.RelationAddToObjectModule
import com.chronoio.chrono.di.feature.relations.RelationCreateFromScratchForDataViewModule
import com.chronoio.chrono.di.feature.relations.RelationCreateFromScratchForObjectBlockModule
import com.chronoio.chrono.di.feature.relations.RelationCreateFromScratchForObjectModule
import com.chronoio.chrono.di.feature.search.DaggerGlobalSearchComponent
import com.chronoio.chrono.di.feature.search.DaggerSearchV2Component
import com.chronoio.chrono.feature_search.presentation.SearchViewModel
import com.chronoio.chrono.di.feature.sets.CreateFilterModule
import com.chronoio.chrono.di.feature.sets.ModifyFilterModule
import com.chronoio.chrono.di.feature.sets.PickConditionModule
import com.chronoio.chrono.di.feature.sets.SelectFilterRelationModule
import com.chronoio.chrono.di.feature.quickcapture.DaggerQuickCaptureComponent
import com.chronoio.chrono.di.feature.settings.DaggerAboutAppComponent
import com.chronoio.chrono.di.feature.settings.DaggerAppearanceComponent
import com.chronoio.chrono.di.feature.settings.DaggerDebugComponent
import com.chronoio.chrono.di.feature.settings.DaggerExperimentalFeaturesComponent
import com.chronoio.chrono.di.feature.settings.DaggerFilesStorageComponent
import com.chronoio.chrono.di.feature.settings.DaggerSpacesStorageComponent
import com.chronoio.chrono.di.feature.settings.LogoutWarningModule
import com.chronoio.chrono.di.feature.settings.ProfileModule
import com.chronoio.chrono.di.feature.sharing.DaggerSharingComponent
import com.chronoio.chrono.di.feature.spaces.DaggerCreateSpaceComponent
import com.chronoio.chrono.di.feature.spaces.DaggerManageSectionsComponent
import com.chronoio.chrono.di.feature.spaces.DaggerSpaceListComponent
import com.chronoio.chrono.di.feature.spaces.DaggerSpaceSettingsComponent
import com.chronoio.chrono.di.feature.templates.DaggerTemplateBlankComponent
import com.chronoio.chrono.di.feature.templates.DaggerTemplateSelectComponent
import com.chronoio.chrono.di.feature.vault.DaggerVaultComponent
import com.chronoio.chrono.di.feature.widgets.DaggerCreateChatObjectComponent
import com.chronoio.chrono.di.feature.oswidgets.DaggerCreateObjectWidgetConfigComponent
import com.chronoio.chrono.di.feature.oswidgets.DaggerDataViewWidgetConfigComponent
import com.chronoio.chrono.di.feature.oswidgets.DaggerObjectShortcutWidgetConfigComponent
import com.chronoio.chrono.di.feature.widgets.DaggerSelectWidgetSourceComponent
import com.chronoio.chrono.di.feature.widgets.DaggerSelectWidgetTypeComponent
import com.chronoio.chrono.di.main.MainComponent
import com.chronoio.chrono.feature_allcontent.presentation.AllContentViewModel
import com.chronoio.chrono.feature_chats.presentation.ChatReactionViewModel
import com.chronoio.chrono.feature_chats.presentation.ChatViewModel
import com.chronoio.chrono.feature_discussions.presentation.DiscussionViewModel
import com.chronoio.chrono.feature_chats.presentation.SelectChatReactionViewModel
import com.chronoio.chrono.feature_create_object.presentation.NewCreateObjectViewModel
import com.chronoio.chrono.feature_date.viewmodel.DateObjectVmParams
import com.chronoio.chrono.feature_object_type.ui.ObjectTypeVmParams
import com.chronoio.chrono.feature_object_type.viewmodel.CreateTypeVmParams
import com.chronoio.chrono.feature_properties.add.EditTypePropertiesVmParams
import com.chronoio.chrono.feature_properties.space.SpacePropertiesViewModel
import com.chronoio.chrono.gallery_experience.viewmodel.GalleryInstallationViewModel
import com.chronoio.chrono.presentation.editor.EditorViewModel
import com.chronoio.chrono.presentation.history.VersionHistoryViewModel
import com.chronoio.chrono.presentation.home.HomeScreenVmParams
import com.chronoio.chrono.presentation.linking.LinkToObjectOrWebViewModel
import com.chronoio.chrono.presentation.moving.MoveToViewModel
import com.chronoio.chrono.presentation.multiplayer.RequestJoinSpaceViewModel
import com.chronoio.chrono.presentation.multiplayer.ShareSpaceViewModel
import com.chronoio.chrono.presentation.multiplayer.SpaceJoinRequestViewModel
import com.chronoio.chrono.presentation.objects.ObjectTypeChangeViewModel
import com.chronoio.chrono.presentation.objects.SelectObjectTypeViewModel
import com.chronoio.chrono.presentation.profile.ParticipantViewModel
import com.chronoio.chrono.presentation.publishtoweb.MySitesViewModel
import com.chronoio.chrono.presentation.publishtoweb.PublishToWebViewModel
import com.chronoio.chrono.presentation.relations.RelationAddViewModelBase
import com.chronoio.chrono.presentation.relations.RelationListViewModel
import com.chronoio.chrono.presentation.relations.option.CreateOrEditOptionViewModel
import com.chronoio.chrono.presentation.relations.value.`object`.ObjectValueViewModel
import com.chronoio.chrono.presentation.relations.value.tagstatus.TagOrStatusValueViewModel
import com.chronoio.chrono.presentation.search.GlobalSearchViewModel
import com.chronoio.chrono.presentation.search.ObjectSearchViewModel
import com.chronoio.chrono.presentation.sets.ObjectSetViewModel
import com.chronoio.chrono.presentation.settings.SpacesStorageViewModel
import com.chronoio.chrono.presentation.spaces.CreateSpaceViewModel
import com.chronoio.chrono.presentation.spaces.SpaceSettingsViewModel
import com.chronoio.chrono.presentation.types.SpaceTypesViewModel
import com.chronoio.chrono.presentation.widgets.CreateChatObjectViewModel
import com.chronoio.chrono.presentation.widgets.collection.CollectionViewModel
import com.chronoio.chrono.ui.widgets.collection.DaggerCollectionComponent
import timber.log.Timber

class ComponentManager(
    private val main: MainComponent,
    private val provider: HasComponentDependencies
) {

    val mainEntryComponent = Component {
        main.mainEntryComponentBuilder().module(MainEntryModule).build()
    }

    val debugSettingsComponent = Component {
        main
            .debugSettingsBuilder()
            .module(DebugSettingsModule())
            .build()
    }

    val debugComponent = Component {
        DaggerDebugComponent
            .factory()
            .create(findComponentDependencies())
    }

    val experimentalFeaturesComponent = Component {
        DaggerExperimentalFeaturesComponent
            .factory()
            .create(findComponentDependencies())
    }

    val quickCaptureComponent = Component {
        DaggerQuickCaptureComponent
            .factory()
            .create(findComponentDependencies())
    }

    val splashLoginComponent = Component {
       DaggerSplashComponent
           .factory()
           .create(findComponentDependencies())
    }

    val keychainPhraseComponent = Component {
        main
            .keychainPhraseComponentBuilder()
            .keychainPhraseModule(KeychainPhraseModule)
            .build()
    }

    val homeScreenComponent = ComponentWithParams { vmParams: HomeScreenVmParams ->
        DaggerHomeScreenComponent
            .factory()
            .create(
                vmParams = vmParams, dependencies = findComponentDependencies()
            )
    }

    /**
     * Single-slot cache for the widget overlay. Each fragment open fires get(),
     * each fragment destroy fires release() which clears the slot so the next
     * open builds a fresh DaggerHomeScreenComponent.
     *
     * IMPORTANT: the overlay must NOT be opened while [WidgetsScreenFragment]
     * is alive for the same space. Both fragments use HomeScreenViewModel,
     * and HomeScreenViewModel.onCleared() calls closeObject against the shared
     * widget object. Running both simultaneously causes the first cleared VM
     * to close the widget object the other VM is still subscribed to.
     *
     * In practice the overlay is only triggered from Chat/Editor/ObjectSet
     * destinations where the root widgets screen is not in the back stack,
     * so this invariant is preserved structurally — but any future
     * "show overlay from the widgets screen itself" path must be prevented.
     */
    val widgetOverlayComponent = ComponentWithParams { vmParams: HomeScreenVmParams ->
        DaggerHomeScreenComponent
            .factory()
            .create(
                vmParams = vmParams, dependencies = findComponentDependencies()
            )
    }

    val collectionComponent = ComponentWithParams { vmParams: CollectionViewModel.VmParams ->
        DaggerCollectionComponent
            .factory()
            .create(
                dependencies = findComponentDependencies(),
                vmParams = vmParams
            )
    }

    val selectWidgetSourceSubcomponent =
        ComponentWithParams { params: ObjectSearchViewModel.VmParams ->
            DaggerSelectWidgetSourceComponent.factory()
                .create(params, findComponentDependencies())
        }

    val selectWidgetTypeSubcomponent = Component {
        DaggerSelectWidgetTypeComponent.factory().create(
            findComponentDependencies()
        )
    }

    val createObjectComponent = Component {
        main
            .createObjectComponent()
            .module(CreateObjectModule)
            .build()
    }

    val templateSelectComponent = Component {
        DaggerTemplateSelectComponent.factory().create(findComponentDependencies())
    }

    val editorComponent = ComponentMapWithParam { param: DefaultComponentParam ->
        main
            .editorComponentBuilder()
            .session(EditorSessionModule)
            .usecase(EditorUseCaseModule)
            .withParams(
                EditorViewModel.Params(
                    ctx = param.ctx,
                    space = param.space
                )
            )
            .build()
    }

    val objectIconPickerComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectIconPickerComponent()
            .base(ObjectIconPickerBaseModule)
            .module(ObjectIconPickerModule)
            .build()
    }

    val textBlockIconPickerComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .textBlockIconPickerComponent()
            .module(TextBlockIconPickerModule)
            .build()
    }

    val objectSetIconPickerComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectSetIconPickerComponent()
            .base(ObjectIconPickerBaseModule)
            .module(ObjectSetIconPickerModule)
            .build()
    }

    val objectAppearanceSettingComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectAppearanceSettingComponent()
            .module(ObjectAppearanceSettingModule)
            .build()
    }

    val objectAppearanceIconComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectAppearanceIconComponent()
            .module(ObjectAppearanceIconModule)
            .build()
    }

    val objectAppearancePreviewLayoutComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectAppearancePreviewLayoutComponent()
            .module(ObjectAppearancePreviewLayoutModule)
            .build()
    }

    val objectAppearanceChooseDescriptionComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectAppearanceChooseDescription()
            .build()
    }

    val setTextBlockValueComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .setBlockTextValueComponent()
            .build()
    }

    val createBookmarkSubComponent = Component {
        main
            .createBookmarkBuilder()
            .createBookmarkModule(CreateBookmarkModule())
            .build()
    }


    val linkToObjectComponent = ComponentWithParams { param: ObjectSearchViewModel.VmParams ->
        DaggerLinkToObjectComponent
            .factory()
            .create(
                params = param,
                dependencies = findComponentDependencies()
            )
    }

    val linkToObjectOrWebComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .linkToObjectOrWebBuilder()
            .withParams(
                params = LinkToObjectOrWebViewModel.VmParams(
                    space = param.space
                )
            )
            .module(LinkToObjectOrWebModule)
            .build()
    }

    val moveToComponent = ComponentWithParams { params: MoveToViewModel.VmParams ->
        DaggerMoveToComponent.factory()
            .create(params, findComponentDependencies())
    }

    val globalSearchComponent = ComponentWithParams { params: GlobalSearchViewModel.VmParams ->
        DaggerGlobalSearchComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val searchV2Component = ComponentWithParams { params: SearchViewModel.VmParams ->
        DaggerSearchV2Component
            .factory()
            .create(params, findComponentDependencies())
    }

    val allContentComponent = ComponentWithParams { params: AllContentViewModel.VmParams ->
        DaggerAllContentComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val participantScreenComponent = ComponentWithParams { params: ParticipantViewModel.VmParams ->
        DaggerParticipantComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val dateObjectComponent = ComponentWithParams { params: DateObjectVmParams  ->
        DaggerDateObjectComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val createObjectFeatureComponent = ComponentMapWithParam { params: NewCreateObjectViewModel.VmParams ->
        DaggerCreateObjectFeatureComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val objectSetComponent = ComponentMapWithParam { param: DefaultComponentParam ->
        main.objectSetComponentBuilder()
            .module(ObjectSetModule)
            .withParams(
                ObjectSetViewModel.Params(
                    ctx = param.ctx,
                    space = param.space
                )
            )
            .build()
    }

    val objectRelationListComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectRelationListComponent()
            .withVmParams(
                RelationListViewModel.VmParams(
                    objectId = param.ctx,
                    spaceId = param.space
                )
            )
            .module(ObjectRelationListModule)
            .build()
    }

    val objectSetRelationListComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectRelationListComponent()
            .withVmParams(
                RelationListViewModel.VmParams(
                    objectId = param.ctx,
                    spaceId = param.space
                )
            )
            .module(ObjectRelationListModule)
            .build()
    }

    val setOrCollectionRelationTextValueComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .relationTextValueComponent()
            .module(RelationTextValueModule)
            .build()
    }

    val dataViewRelationTextValueComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .relationDataViewTextValueComponent()
            .module(RelationDataViewTextValueModule)
            .build()
    }

    val relationTextValueComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .relationTextValueComponent()
            .module(RelationTextValueModule)
            .build()
    }

    val dataViewRelationDateValueComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .dataViewRelationDateValueComponent()
            .module(RelationDataViewDateValueModule)
            .build()
    }

    val setOrCollectionRelationDateValueComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .relationDateValueComponent()
            .module(RelationDateValueModule)
            .build()
    }

    val objectRelationDateValueComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .editRelationDateComponent()
            .module(RelationDateValueModule)
            .build()
    }

    val viewerFilterComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .viewerFilterBySubComponent()
            .module(ViewerFilterModule)
            .build()
    }

    val objectSetRecordComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectSetRecordComponent()
            .module(ObjectSetRecordModule)
            .build()
    }

    val objectSetCreateBookmarkRecordComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectSetCreateBookmarkRecordComponent()
            .module(ObjectSetCreateBookmarkRecordModule)
            .build()
    }

    val objectsSetSettingsComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectSetSettingsComponent()
            .module(ObjectSetSettingsModule)
            .build()
    }

    val selectSortRelationComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .selectSortRelationComponent()
            .module(SelectSortRelationModule)
            .build()
    }

    val selectFilterRelationComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .selectFilterRelationComponent()
            .module(SelectFilterRelationModule)
            .build()
    }

    val createFilterComponent = DependentComponentMap { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .createFilterComponent()
            .module(CreateFilterModule)
            .build()
    }

    val pickFilterConditionComponentCreate = ComponentWithParams { param: DefaultComponentParam ->
        createFilterComponent
            .get(key = param.ctx, param = param)
            .createPickConditionComponent()
            .module(PickConditionModule)
            .build()
    }

    val pickFilterConditionComponentModify = ComponentWithParams { param: DefaultComponentParam ->
        modifyFilterComponent
            .get(key = param.ctx, param = param)
            .createPickConditionComponent()
            .module(PickConditionModule)
            .build()
    }

    val modifyFilterComponent = DependentComponentMap { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .modifyFilterComponent()
            .module(ModifyFilterModule)
            .build()
    }

    val viewerSortComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .viewerSortComponent()
            .module(ViewerSortModule)
            .build()
    }

    val modifyViewerSortComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .modifyViewerSortComponent()
            .module(ModifyViewerSortModule)
            .build()
    }

    val objectCoverComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectCoverComponent()
            .module(SelectCoverObjectModule)
            .build()
    }

    val objectUnsplashComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectUnsplashComponent()
            .module(UnsplashModule)
            .build()
    }

    val objectSetUnsplashComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectUnsplashComponent()
            .module(UnsplashModule)
            .build()
    }

    val objectSetCoverComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectSetCoverComponent()
            .module(SelectCoverObjectSetModule)
            .build()
    }

    val objectMenuComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .objectMenuComponent()
            .base(ObjectMenuModuleBase)
            .module(ObjectMenuModule)
            .build()
    }

    val objectSetMenuComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .objectSetMenuComponent()
            .base(ObjectMenuModuleBase)
            .module(ObjectSetMenuModule)
            .build()
    }

    val relationAddToObjectComponent = ComponentWithParams { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .relationAddToObjectComponent()
            .withVmParams(
                vmParams = RelationAddViewModelBase.VmParams(
                    objectId = param.ctx,
                    space = param.space
                )
            )
            .module(RelationAddToObjectModule)
            .build()
    }

    val relationAddToObjectSetComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .relationAddToObjectComponent()
            .withVmParams(
                vmParams = RelationAddViewModelBase.VmParams(
                    objectId = param.ctx,
                    space = param.space
                )
            )
            .module(RelationAddToObjectModule)
            .build()
    }

    val relationAddToDataViewComponent = ComponentWithParams { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .relationAddToDataViewComponent()
            .withVmParams(
                vmParams = RelationAddViewModelBase.VmParams(
                    objectId = param.ctx,
                    space = param.space
                )
            )
            .module(RelationAddToDataViewModule)
            .build()
    }

    val relationCreateFromScratchForObjectComponent = DependentComponentMap { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .relationCreateFromScratchForObjectComponent()
            .module(RelationCreateFromScratchForObjectModule)
            .build()
    }

    val relationCreateFromScratchForObjectSetComponent = DependentComponentMap { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .relationCreateFromScratchForObjectComponent()
            .module(RelationCreateFromScratchForObjectModule)
            .build()
    }

    val relationCreateFromScratchForObjectBlockComponent = DependentComponentMap { param: DefaultComponentParam ->
        editorComponent
            .get(key = param.ctx, param = param)
            .relationCreateFromScratchForObjectBlockComponent()
            .module(RelationCreateFromScratchForObjectBlockModule)
            .build()
    }

    val relationCreateFromScratchForDataViewComponent = DependentComponentMap { param: DefaultComponentParam ->
        objectSetComponent
            .get(key = param.ctx, param = param)
            .relationCreateFromScratchForDataViewComponent()
            .module(RelationCreateFromScratchForDataViewModule)
            .build()
    }

    val relationFormatPickerObjectComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForObjectComponent
            .get(key = param.ctx, param = param)
            .relationFormatPickerComponent()
            .build()
    }

    val relationFormatPickerLibraryComponent = ComponentWithParams { ctx: Id ->
        relationCreationFromLibraryComponent
            .get()
            .relationFormatPickerComponent()
            .build()
    }

    val relationFormatPickerBlockComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForObjectBlockComponent
            .get(key = param.ctx, param = param)
            .relationFormatPickerComponent()
            .build()
    }

    val relationFormatPickerDataViewComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForDataViewComponent
            .get(key = param.ctx, param = param)
            .relationFormatPickerComponent()
            .build()
    }

    val relationFormatPickerSetOrCollectionComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForObjectSetComponent
            .get(key = param.ctx, param = param)
            .relationFormatPickerComponent()
            .build()
    }

    val limitObjectTypeObjectComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForObjectComponent
            .get(key = param.ctx, param = param)
            .limitObjectTypeComponent()
            .module(LimitObjectTypeModule)
            .build()
    }

    val limitObjectTypeBlockComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForObjectBlockComponent
            .get(key = param.ctx, param = param)
            .limitObjectTypeComponent()
            .module(LimitObjectTypeModule)
            .build()
    }

    val limitObjectTypeDataViewComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForDataViewComponent
            .get(key = param.ctx, param = param)
            .limitObjectTypeComponent()
            .module(LimitObjectTypeModule)
            .build()
    }

    val limitObjectTypeSetOrCollectionComponent = ComponentWithParams { param: DefaultComponentParam ->
        relationCreateFromScratchForObjectSetComponent
            .get(key = param.ctx, param = param)
            .limitObjectTypeComponent()
            .module(LimitObjectTypeModule)
            .build()
    }

    val limitObjectTypeLibraryComponent = ComponentWithParams { ctx: Id ->
        relationCreationFromLibraryComponent.get()
            .limitObjectTypeComponent()
            .module(LimitObjectTypeModule)
            .build()
    }

    val objectTypeChangeComponent = ComponentWithParams { vmParams: ObjectTypeChangeViewModel.VmParams ->
        main
            .objectTypeChangeComponent()
            .create(vmParams)
    }

    val templateBlankComponent = Component {
        DaggerTemplateBlankComponent.factory().create(findComponentDependencies())
    }

    // Settings

    val aboutAppComponent = Component {
        DaggerAboutAppComponent.factory().create(findComponentDependencies())
    }

    val profileComponent = Component {
        main.profileComponent().module(ProfileModule).build()
    }

    val logoutWarningComponent = Component {
        main.logoutWarningComponent().module(LogoutWarningModule).build()
    }

    val filesStorageComponent = Component {
        DaggerFilesStorageComponent.builder()
            .withDependencies(findComponentDependencies())
            .build()
    }

    val spacesStorageComponent = ComponentWithParams { vmParams: SpacesStorageViewModel.VmParams ->
        DaggerSpacesStorageComponent
            .factory()
            .create(
                vmParams = vmParams,
                dependency = findComponentDependencies()
            )
    }

    val appearanceComponent = Component {
        DaggerAppearanceComponent
            .factory()
            .create(findComponentDependencies())
    }

    val backLinkOrAddToObjectComponent = ComponentWithParams { vmParams: ObjectSearchViewModel.VmParams ->
        DaggerBacklinkOrAddToObjectComponent.builder()
            .withParams(vmParams)
            .withDependencies(findComponentDependencies())
            .build()
    }

    val relationCreationFromLibraryComponent = Component {
        DaggerRelationCreateFromLibraryComponent
            .factory()
            .create(findComponentDependencies())
    }

    val deletedAccountComponent = Component {
        DaggerDeletedAccountComponent
            .factory()
            .create(findComponentDependencies())
    }

    val onboardingComponent = Component {
        DaggerOnboardingComponent
            .factory()
            .create(findComponentDependencies())
    }

    val onboardingStartComponent = Component {
        DaggerOnboardingStartComponent
            .factory()
            .create(findComponentDependencies())
    }

    val onboardingMnemonicComponent = Component {
        DaggerOnboardingMnemonicComponent
            .factory()
            .create(findComponentDependencies())
    }

    val onboardingSoulCreationComponent = Component {
        DaggerOnboardingSoulCreationComponent
            .factory()
            .create(findComponentDependencies())
    }

    val onboardingMnemonicLoginComponent = Component {
        DaggerOnboardingMnemonicLoginComponent
            .factory()
            .create(findComponentDependencies())
    }

    val spaceListComponent = Component {
        DaggerSpaceListComponent
            .factory()
            .create(findComponentDependencies())
    }

    val createSpaceComponent = ComponentWithParams { vmParams: CreateSpaceViewModel.VmParams ->
        DaggerCreateSpaceComponent
            .factory()
            .create(vmParams, findComponentDependencies())
    }

    val spaceSettingsComponent = ComponentWithParams { vmParams: SpaceSettingsViewModel.VmParams ->
        DaggerSpaceSettingsComponent
            .factory()
            .create(
                vmParams = vmParams,
                dependencies = findComponentDependencies()
            )
    }

    val manageSectionsComponent = Component {
        DaggerManageSectionsComponent
            .factory()
            .create(findComponentDependencies())
    }

    val selectObjectTypeComponent = ComponentWithParams { params: SelectObjectTypeViewModel.Params ->
        DaggerSelectObjectTypeComponent
            .factory()
            .create(
                params = params,
                dependencies = findComponentDependencies()
            )
    }

    val appPreferencesComponent = Component {
        DaggerAppPreferencesComponent
            .factory()
            .create(findComponentDependencies())
    }

    val sharingComponent = Component {
        DaggerSharingComponent
            .factory()
            .create(findComponentDependencies())
    }

    val notificationsComponent = Component {
        DaggerNotificationComponent
            .factory()
            .create(findComponentDependencies())
    }

    val tagStatusObjectComponent = ComponentWithParams { params: TagOrStatusValueViewModel.ViewModelParams ->
        editorComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .tagStatusObjectComponent()
            .params(params)
            .build()
    }

    val tagStatusSetComponent = ComponentWithParams { params: TagOrStatusValueViewModel.ViewModelParams ->
        objectSetComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .tagStatusSetComponent()
            .params(params)
            .build()
    }

    val tagStatusDataViewComponent = ComponentWithParams { params: TagOrStatusValueViewModel.ViewModelParams ->
        objectSetComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .tagStatusDataViewComponent()
            .params(params)
            .build()
    }

    val optionObjectComponent = ComponentWithParams { params: CreateOrEditOptionViewModel.ViewModelParams ->
        editorComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .optionObjectComponent()
            .params(params)
            .build()
    }

    val optionSetComponent = ComponentWithParams { params: CreateOrEditOptionViewModel.ViewModelParams ->
        objectSetComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .optionSetComponent()
            .params(params)
            .build()
    }

    val optionDataViewComponent = ComponentWithParams { params: CreateOrEditOptionViewModel.ViewModelParams ->
        objectSetComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .optionDataViewComponent()
            .params(params)
            .build()
    }

    val objectValueObjectComponent = ComponentWithParams { params: ObjectValueViewModel.ViewModelParams ->
        editorComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .objectValueComponent()
            .params(params)
            .build()
    }

    val objectValueSetComponent = ComponentWithParams { params: ObjectValueViewModel.ViewModelParams ->
        objectSetComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .objectValueSetComponent()
            .params(params)
            .build()
    }

    val objectValueDataViewComponent = ComponentWithParams { params: ObjectValueViewModel.ViewModelParams ->
        objectSetComponent
            .get(
                key = params.ctx,
                param = DefaultComponentParam(
                    ctx = params.ctx,
                    space = params.space
                )
            )
            .objectValueDataViewComponent()
            .params(params)
            .build()
    }

    val shareSpaceComponent = ComponentWithParams { space: SpaceId ->
        DaggerShareSpaceComponent
            .builder()
            .withDependencies(findComponentDependencies())
            .withParams(params = ShareSpaceViewModel.VmParams(space))
            .build()
    }

    val spaceJoinRequestComponent = ComponentMapWithParam { params: SpaceJoinRequestViewModel.VmParams ->
        DaggerSpaceJoinRequestComponent
            .builder()
            .withDependencies(findComponentDependencies())
            .withParams(params = params)
            .build()
    }

    val requestToJoinSpaceComponent = ComponentWithParams { params: RequestJoinSpaceViewModel.Params ->
        DaggerRequestJoinSpaceComponent
            .builder()
            .withDependencies(findComponentDependencies())
            .withParams(params = params)
            .build()
    }

    val membershipComponent = Component {
        DaggerMembershipComponent.factory().create(findComponentDependencies())
    }

    val galleryInstallationsComponent =
        ComponentWithParams { params: GalleryInstallationViewModel.ViewModelParams ->
            DaggerGalleryInstallationComponent.builder()
                .withDependencies(findComponentDependencies())
                .withParams(params)
                .build()
        }

    val versionHistoryComponent =
        ComponentWithParams { vmParams: VersionHistoryViewModel.VmParams ->
            editorComponent.get(
                key = vmParams.objectId,
                param = DefaultComponentParam(
                    ctx = vmParams.objectId,
                    space = vmParams.spaceId
                )
            ).versionHistoryComponent()
                .vmParams(vmParams)
                .build()
        }

    val chatComponent = ComponentMapWithParam { params: ChatViewModel.Params.Default ->
        DaggerChatComponent
            .builder()
            .withDependencies(findComponentDependencies())
            .withParams(params)
            .build()
    }

    val discussionComponent = ComponentMapWithParam { params: DiscussionViewModel.Params ->
        DaggerDiscussionComponent
            .builder()
            .withDependencies(findComponentDependencies())
            .withParams(params)
            .build()
    }

    val selectChatReactionComponent = ComponentMapWithParam { params: SelectChatReactionViewModel.Params ->
        DaggerSelectChatReactionComponent
            .builder()
            .withDependencies(findComponentDependencies())
            .withParams(params)
            .build()
    }

    val chatReactionComponent = ComponentMapWithParam { params: ChatReactionViewModel.Params ->
        DaggerChatReactionComponent
            .builder()
            .withDependencies(findComponentDependencies())
            .withParams(params)
            .build()
    }

    val selectChatIconComponent = Component {
        DaggerSelectChatIconComponent
            .builder()
            .withDependencies(findComponentDependencies())
            .build()
    }

    val vaultComponent = Component {
        DaggerVaultComponent
            .factory()
            .create(findComponentDependencies())
    }

    val objectTypeComponent = ComponentWithParams { params: ObjectTypeVmParams ->
        DaggerObjectTypeComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val editTypePropertiesComponent = ComponentWithParams { params: EditTypePropertiesVmParams ->
        DaggerEditTypePropertiesComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val createObjectTypeComponent = ComponentWithParams { params: CreateTypeVmParams ->
        DaggerCreateObjectTypeComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val spaceTypesComponent = ComponentWithParams { params: SpaceTypesViewModel.VmParams ->
        DaggerSpaceTypesComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val spacePropertiesComponent = ComponentWithParams { params: SpacePropertiesViewModel.VmParams ->
        DaggerSpacePropertiesComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val pushContentComponent = Component {
        DaggerPushContentComponent
            .factory()
            .create(findComponentDependencies())
    }

    val publishToWebComponent = ComponentWithParams { params: PublishToWebViewModel.Params ->
        DaggerPublishToWebComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val mySitesComponent = ComponentWithParams { params: MySitesViewModel.VmParams ->
        DaggerMySitesComponent
            .factory()
            .create(params, findComponentDependencies())
    }

    val mediaComponent = Component {
        DaggerMediaComponent
            .factory()
            .create(findComponentDependencies())
    }

    val createChatObjectComponent = ComponentWithParams { vmParams: CreateChatObjectViewModel.VmParams ->
        DaggerCreateChatObjectComponent
            .factory()
            .create(vmParams, findComponentDependencies())
    }

    val createObjectWidgetConfigComponent = Component {
        DaggerCreateObjectWidgetConfigComponent
            .factory()
            .create(findComponentDependencies())
    }

    val objectShortcutWidgetConfigComponent = Component {
        DaggerObjectShortcutWidgetConfigComponent
            .factory()
            .create(findComponentDependencies())
    }

    val dataViewWidgetConfigComponent = Component {
        DaggerDataViewWidgetConfigComponent
            .factory()
            .create(findComponentDependencies())
    }

    class Component<T>(private val builder: () -> T) {

        private var instance: T? = null

        fun get() = instance ?: builder().also { instance = it }

        fun new() = builder().also { instance = it }

        fun release() {
            instance = null
        }

        fun isInitialized() = instance != null

        override fun toString(): String {
            return if (BuildConfig.DEBUG) {
                instance?.toString().orEmpty()
            } else {
                super.toString()
            }
        }
    }

    class ComponentMap<T>(private val builder: () -> T) {

        private val map = mutableMapOf<String, T>()

        fun get(id: String) = map[id] ?: builder().also { map[id] = it }

        fun new(id: String) = builder().also { map[id] = it }

        fun release(id: String) {
            map.remove(id)
        }

        fun isInitialized() = map.isNotEmpty()

        override fun toString(): String {
            return if (BuildConfig.DEBUG) {
                map.toString()
            } else {
                super.toString()
            }
        }
    }

    class ComponentMapWithParam<out T, in PARAMETER>(private val builder: (PARAMETER) -> T) {

        private val map = mutableMapOf<String, T>()

        fun get(key: String, param: PARAMETER) = map[key] ?: builder(param).also { map[key] = it }

        fun new(id: String, param: PARAMETER) = builder(param).also { map[id] = it }

        fun release(id: String) {
            map.remove(id)
        }

        fun isInitialized() = map.isNotEmpty()

        override fun toString(): String {
            return if (BuildConfig.DEBUG) {
                map.toString()
            } else {
                super.toString()
            }
        }
    }

    class DependentComponentMap<out T, in PARAMETER>(private val builder: (PARAMETER) -> T) {

        private val map = mutableMapOf<String, T>()

        fun get(key: String, param: PARAMETER) = map[key] ?: builder(param).also { map[key] = it }

        fun new(key: String, param: PARAMETER) = builder(param).also { map[key] = it }

        fun release(id: Id) {
            map.remove(id)
        }

        fun isInitialized() = map.isNotEmpty()

        override fun toString(): String {
            return if (BuildConfig.DEBUG) {
                map.toString()
            } else {
                super.toString()
            }
        }
    }

    class ComponentWithParams<out T, in PARAMETER>(private val builder: (PARAMETER) -> T) {

        private var instance: T? = null

        fun get(params: PARAMETER) = instance ?: builder(params).also { instance = it }

        fun new(params: PARAMETER) = builder(params).also { instance = it }

        fun release() {
            instance = null
        }
    }

    private inline fun <reified T : ComponentDependencies> findComponentDependencies(): T {
        return provider.dependencies[T::class.java] as T
    }

    /**
     * Can be used for debugging and tracing unreleased components.
     */
    fun logUnreleasedComponents() {
        if (BuildConfig.DEBUG) {
            javaClass.declaredFields.forEach { field ->
                val component = field.get(this)
                if (component != null) {
                    when (component) {
                        is Component<*> -> {
                            if (component.isInitialized()) {
                                Timber.d("Unreleased component: $component")
                            }
                        }
                        is DependentComponentMap<*, *> -> {
                            if (component.isInitialized()) {
                                Timber.d("Unreleased component: $component")
                            }
                        }
                        is ComponentMap<*> -> {
                            if (component.isInitialized()) {
                                Timber.d("Unreleased component: $component")
                            }
                        }
                    }
                }
            }
        }
    }
}