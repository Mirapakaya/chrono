package com.chronoio.chrono.feature_allcontent.ui

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chronoio.chrono.core_models.DVSortType
import com.chronoio.chrono.core_ui.common.DefaultPreviews
import com.chronoio.chrono.core_ui.foundation.Divider
import com.chronoio.chrono.core_ui.lists.objects.menu.ObjectsListMenuItem
import com.chronoio.chrono.core_ui.lists.objects.menu.ObjectsListSortingMenuContainer
import com.chronoio.chrono.feature_allcontent.R
import com.chronoio.chrono.feature_allcontent.models.AllContentMenuMode
import com.chronoio.chrono.feature_allcontent.models.UiMenuState
import com.chronoio.chrono.presentation.objects.MenuSortsItem
import com.chronoio.chrono.presentation.objects.ObjectsListSort

@Composable
fun AllContentMenu(
    uiMenuState: UiMenuState.Visible,
    onModeClick: (AllContentMenuMode) -> Unit,
    onSortClick: (ObjectsListSort) -> Unit,
    onBinClick: () -> Unit
) {
    var sortingExpanded by remember { mutableStateOf(false) }

    uiMenuState.mode.forEach { item ->
        ObjectsListMenuItem(
            title = getModeTitle(item),
            isSelected = item.isSelected,
            modifier = Modifier.clickable {
                onModeClick(item)
            }
        )
        Divider(
            height = 0.5.dp,
            paddingStart = 0.dp,
            paddingEnd = 0.dp,
            color = colorResource(R.color.shape_secondary)
        )
    }

    if (uiMenuState.mode.isNotEmpty()) {
        Divider(
            height = 7.5.dp,
            paddingStart = 0.dp,
            paddingEnd = 0.dp,
            color = colorResource(R.color.shape_secondary)
        )
    }

    ObjectsListSortingMenuContainer(
        container = uiMenuState.container,
        sorts = uiMenuState.sorts,
        types = uiMenuState.types,
        sortingExpanded = sortingExpanded,
        onSortClick = onSortClick,
        onChangeSortExpandedState = { sortingExpanded = it }
    )

    if (uiMenuState.showBin && !sortingExpanded) {
        Divider(
            height = 7.5.dp,
            paddingStart = 0.dp,
            paddingEnd = 0.dp,
            color = colorResource(R.color.shape_secondary)
        )
        ObjectsListMenuItem(
            title = stringResource(id = R.string.all_content_view_bin),
            isSelected = false,
            modifier = Modifier.clickable { onBinClick() }
        )
    }
}

//region RESOURCES
@Composable
private fun getModeTitle(mode: AllContentMenuMode): String = stringResource(
    when (mode) {
        is AllContentMenuMode.AllContent -> R.string.all_content_title_all_content
        is AllContentMenuMode.Unlinked -> R.string.all_content_title_only_unlinked
    }
)
//endregion

//region PREVIEW
@DefaultPreviews
@Composable
fun AllContentMenuPreview() {
    AllContentMenu(
        uiMenuState = UiMenuState.Visible(
            mode = listOf(
                AllContentMenuMode.AllContent(isSelected = true),
                AllContentMenuMode.Unlinked(isSelected = false)
            ),
            sorts = listOf(
                MenuSortsItem.Sort(
                    sort = ObjectsListSort.ByName(isSelected = true)
                ),
                MenuSortsItem.Sort(
                    ObjectsListSort.ByDateUpdated(isSelected = false)
                ),
                MenuSortsItem.Sort(
                    ObjectsListSort.ByDateCreated(isSelected = false)
                )
            ),
            types = listOf(
                MenuSortsItem.SortType(
                    sortType = DVSortType.ASC,
                    isSelected = true,
                    sort = ObjectsListSort.ByName(isSelected = true)
                ),
                MenuSortsItem.SortType(
                    sortType = DVSortType.DESC,
                    isSelected = false,
                    sort = ObjectsListSort.ByName(isSelected = false)
                ),
            ),
            container = MenuSortsItem.Container(ObjectsListSort.ByName())
        ),
        onModeClick = {},
        onSortClick = {},
        onBinClick = {}
    )
}
//endregion