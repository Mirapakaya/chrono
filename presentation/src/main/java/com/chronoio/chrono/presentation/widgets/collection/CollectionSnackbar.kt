package com.chronoio.chrono.presentation.widgets.collection

data class CollectionSnackbar(
    val message: String,
    val action: () -> Unit = { }
)