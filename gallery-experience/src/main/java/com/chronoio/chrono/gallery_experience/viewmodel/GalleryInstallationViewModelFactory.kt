package com.chronoio.chrono.gallery_experience.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.gallery_experience.DownloadGalleryManifest
import com.chronoio.chrono.domain.gallery_experience.ImportExperience
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.multiplayer.UserPermissionProvider
import com.chronoio.chrono.domain.spaces.CreateSpace
import com.chronoio.chrono.domain.spaces.GetSpaceViews
import com.chronoio.chrono.domain.workspace.EventProcessImportChannel
import com.chronoio.chrono.presentation.spaces.SpaceGradientProvider
import javax.inject.Inject

class GalleryInstallationViewModelFactory @Inject constructor(
    private val viewModelParams: GalleryInstallationViewModel.ViewModelParams,
    private val downloadGalleryManifest: DownloadGalleryManifest,
    private val importExperience: ImportExperience,
    private val analytics: Analytics,
    private val getSpaceViews: GetSpaceViews,
    private val createSpace: CreateSpace,
    private val urlBuilder: UrlBuilder,
    private val spaceGradientProvider: SpaceGradientProvider,
    private val  userPermissionProvider: UserPermissionProvider,
    private val eventProcessChannel: EventProcessImportChannel,
    private val configStorage: ConfigStorage
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryInstallationViewModel(
            viewModelParams = viewModelParams,
            downloadGalleryManifest = downloadGalleryManifest,
            importExperience = importExperience,
            analytics = analytics,
            getSpaceViews = getSpaceViews,
            urlBuilder = urlBuilder,
            spaceGradientProvider = spaceGradientProvider,
            createSpace = createSpace,
            userPermissionProvider = userPermissionProvider,
            eventProcessChannel = eventProcessChannel,
            configStorage = configStorage
        ) as T
    }
}