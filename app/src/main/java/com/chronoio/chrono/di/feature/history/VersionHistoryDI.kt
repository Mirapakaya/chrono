package com.chronoio.chrono.di.feature.history

import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.presentation.history.VersionHistoryVMFactory
import com.chronoio.chrono.presentation.history.VersionHistoryViewModel
import com.chronoio.chrono.presentation.sets.state.DefaultObjectStateReducer
import com.chronoio.chrono.presentation.sets.state.ObjectStateReducer
import com.chronoio.chrono.ui.history.VersionHistoryFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(
    modules = [
        VersionHistoryModule::class,
        VersionHistoryModule.Declarations::class
    ]
)
@PerModal
interface VersionHistoryComponent {

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun vmParams(vmParams: VersionHistoryViewModel.VmParams): Builder
        fun build(): VersionHistoryComponent
    }

    fun inject(fragment: VersionHistoryFragment)
}

@Module
object VersionHistoryModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideObjectStateReducer(): ObjectStateReducer = DefaultObjectStateReducer()

    @Module
    interface Declarations {

        @PerModal
        @Binds
        fun bindViewModelFactory(
            factory: VersionHistoryVMFactory
        ): ViewModelProvider.Factory
    }
}

