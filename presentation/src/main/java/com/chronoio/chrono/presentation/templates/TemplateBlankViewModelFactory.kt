package com.chronoio.chrono.presentation.templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.objects.StoreOfRelations
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.presentation.editor.render.DefaultBlockViewRenderer
import javax.inject.Inject

class TemplateBlankViewModelFactory @Inject constructor(
    private val renderer: DefaultBlockViewRenderer,
    private val storeOfRelations: StoreOfRelations,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val fieldParser: FieldParser
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TemplateBlankViewModel(
            renderer = renderer,
            storeOfRelations = storeOfRelations,
            storeOfObjectTypes = storeOfObjectTypes,
            fieldParser = fieldParser
        ) as T
    }
}