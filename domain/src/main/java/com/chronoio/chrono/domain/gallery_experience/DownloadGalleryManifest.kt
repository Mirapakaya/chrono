package com.chronoio.chrono.domain.gallery_experience

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.ManifestInfo
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class DownloadGalleryManifest @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repo: BlockRepository
) : ResultInteractor<DownloadGalleryManifest.Params, ManifestInfo?>(dispatchers.io) {

    override suspend fun doWork(params: Params): ManifestInfo? {
        val command = Command.DownloadGalleryManifest(
            url = params.url
        )
        return repo.downloadGalleryManifest(command)
    }

    class Params(val url: String)
}