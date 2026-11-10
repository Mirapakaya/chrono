package com.chronoio.chrono.presentation.sets.state

import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.DV
import com.chronoio.chrono.core_models.DVViewer
import com.chronoio.chrono.core_models.DVViewerType
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectTypeIds
import com.chronoio.chrono.core_models.ObjectViewDetails
import com.chronoio.chrono.core_models.restrictions.DataViewRestrictions
import com.chronoio.chrono.core_models.restrictions.ObjectRestriction

sealed class ObjectState {

    abstract val isInitialized: Boolean

    sealed class DataView : ObjectState() {

        abstract val root: Id
        abstract val blocks: List<Block>
        abstract val details: ObjectViewDetails
        abstract val objectRestrictions: List<ObjectRestriction>
        abstract val dataViewRestrictions: List<DataViewRestrictions>

        abstract val dataViewContent: DV
        abstract val dataViewBlock: Block
        abstract val viewers: List<DVViewer>

        abstract val hasObjectLayoutConflict: Boolean

        data class Set(
            override val root: Id,
            override val blocks: List<Block> = emptyList(),
            override val details: ObjectViewDetails = ObjectViewDetails.EMPTY,
            override val objectRestrictions: List<ObjectRestriction> = emptyList(),
            override val dataViewRestrictions: List<DataViewRestrictions> = emptyList(),
            override val hasObjectLayoutConflict: Boolean = false,
        ) : DataView() {

            override val isInitialized get() = blocks.any { it.content is DV }
            override val dataViewBlock get() = blocks.first { it.content is DV }
            override val dataViewContent get() = dataViewBlock.content as DV
            override val viewers get() = dataViewContent.viewers
        }

        data class Collection(
            override val root: Id,
            override val blocks: List<Block> = emptyList(),
            override val details: ObjectViewDetails = ObjectViewDetails.EMPTY,
            override val objectRestrictions: List<ObjectRestriction> = emptyList(),
            override val dataViewRestrictions: List<DataViewRestrictions> = emptyList(),
            override val hasObjectLayoutConflict: Boolean = false,
        ) : DataView() {

            override val isInitialized get() = blocks.any { it.content is DV }
            override val dataViewBlock get() = blocks.first { it.content is DV }
            override val dataViewContent get() = dataViewBlock.content as DV
            override val viewers get() = dataViewContent.viewers
        }

        data class TypeSet(
            override val root: Id,
            override val blocks: List<Block> = emptyList(),
            override val details: ObjectViewDetails = ObjectViewDetails.EMPTY,
            override val objectRestrictions: List<ObjectRestriction> = emptyList(),
            override val dataViewRestrictions: List<DataViewRestrictions> = emptyList(),
            override val hasObjectLayoutConflict: Boolean = false,
        ) : DataView() {

            override val isInitialized get() = blocks.any { it.content is DV }
            override val dataViewBlock get() = blocks.first { it.content is DV }
            override val dataViewContent get() = dataViewBlock.content as DV
            override val viewers get() = dataViewContent.viewers
        }
    }

    object Init : ObjectState() {
        override val isInitialized: Boolean
            get() = false
    }

    object ErrorLayout : ObjectState() {
        override val isInitialized: Boolean
            get() = false
    }

    companion object {
        const val VIEW_DEFAULT_OBJECT_TYPE = ObjectTypeIds.PAGE
        val VIEW_TYPE_UNSUPPORTED = DVViewerType.BOARD
    }
}
