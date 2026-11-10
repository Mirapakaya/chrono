package com.chronoio.chrono.di.feature

import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.presentation.objects.ObjectTypeChangeViewModel
import com.chronoio.chrono.ui.objects.types.pickers.AppDefaultObjectTypeFragment
import com.chronoio.chrono.ui.objects.types.pickers.CollectionAddObjectTypeFragment
import com.chronoio.chrono.ui.objects.types.pickers.DataViewSelectSourceFragment
import com.chronoio.chrono.ui.objects.types.pickers.EditorObjectTypeUpdateFragment
import com.chronoio.chrono.ui.objects.types.pickers.EmptyDataViewSelectSourceFragment
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent

@Subcomponent(modules = [ObjectTypeChangeModule::class])
@PerScreen
interface ObjectTypeChangeSubComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: ObjectTypeChangeViewModel.VmParams
        ): ObjectTypeChangeSubComponent
    }

    fun inject(fragment: DataViewSelectSourceFragment)
    fun inject(fragment: EmptyDataViewSelectSourceFragment)
    fun inject(fragment: AppDefaultObjectTypeFragment)
    fun inject(fragment: CollectionAddObjectTypeFragment)
    fun inject(fragment: EditorObjectTypeUpdateFragment)
}

@Module
object ObjectTypeChangeModule {

}