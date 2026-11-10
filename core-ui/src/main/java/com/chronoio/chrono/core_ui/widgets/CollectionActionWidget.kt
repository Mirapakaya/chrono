package com.chronoio.chrono.core_ui.widgets

import android.content.Context
import android.util.AttributeSet
import com.chronoio.chrono.core_ui.features.objects.ObjectActionAdapter
import com.chronoio.chrono.presentation.objects.ObjectAction

class CollectionActionWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseActionWidget<ObjectAction>(context, attrs) {

    override fun provideAdapter() = ObjectActionAdapter { action -> actionListener(action) }
}