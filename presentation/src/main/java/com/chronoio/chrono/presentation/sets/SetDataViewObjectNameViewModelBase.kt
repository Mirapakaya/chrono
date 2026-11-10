package com.chronoio.chrono.presentation.sets

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.presentation.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

abstract class SetDataViewObjectNameViewModelBase : BaseViewModel() {
    val isCompleted = MutableStateFlow(false)

    /**
     * @param [target] used when object is already created and we need to set name for it.
     */
    abstract fun onActionDone(target: Id, space: Id, input: String)
    abstract fun onActionDone(input: String)
    /**
     * @param [target] used when object is already created and we need to set name for it.
     */
    abstract fun onButtonClicked(target: Id, space: Id, input: String)
    abstract fun onButtonClicked(input: String)
}