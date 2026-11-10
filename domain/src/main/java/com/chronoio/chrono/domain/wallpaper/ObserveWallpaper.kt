package com.chronoio.chrono.domain.wallpaper

import com.chronoio.chrono.core_models.Wallpaper
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.FlowUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveWallpaper @Inject constructor () : FlowUseCase<Wallpaper, BaseUseCase.None>() {
    override fun build(params: BaseUseCase.None?): Flow<Wallpaper> {
        return WallpaperStore.Default.observe()
    }
}