package com.chronoio.chrono.presentation.objects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.multiplayer.SpaceViewSubscriptionContainer
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import javax.inject.Inject

class ObjectTypeChangeViewModelFactory @Inject constructor(
    private val vmParams: ObjectTypeChangeViewModel.VmParams,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val dispatchers: AppCoroutineDispatchers,
    private val spaceViews: SpaceViewSubscriptionContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ObjectTypeChangeViewModel(
            vmParams = vmParams,
            storeOfObjectTypes = storeOfObjectTypes,
            dispatchers = dispatchers,
            spaceViews = spaceViews
        ) as T
    }
}