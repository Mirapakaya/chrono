package com.chronoio.chrono.ui.objects.types.pickers

import com.chronoio.chrono.core_models.Id

interface OnDataViewSelectSourceAction {
    fun onProceedWithSelectSource(id: Id)
}