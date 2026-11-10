package com.chronoio.chrono.sample.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}