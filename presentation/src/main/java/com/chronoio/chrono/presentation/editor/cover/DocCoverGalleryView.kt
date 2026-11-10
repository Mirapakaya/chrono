package com.chronoio.chrono.presentation.editor.cover

import com.chronoio.chrono.core_models.Hash
import com.chronoio.chrono.core_models.Url

sealed class DocCoverGalleryView {
    sealed class Section : DocCoverGalleryView() {
        data class Collection(val title: String) : Section()
        object Color : Section()
        object Gradient : Section()
    }

    data class Color(val color: CoverColor) : DocCoverGalleryView()
    data class Gradient(val gradient: String) : DocCoverGalleryView()
    data class Image(val url: Url, val hash: Hash) : DocCoverGalleryView()
}