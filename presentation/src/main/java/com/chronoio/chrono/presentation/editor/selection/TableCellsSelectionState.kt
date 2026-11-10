package com.chronoio.chrono.presentation.editor.selection

import com.chronoio.chrono.presentation.editor.editor.model.BlockView

class TableCellsSelectionState {

    private var memory = mapOf<Int, CellSelection>()

    fun set(cells: List<BlockView.Table.Cell>, rowsSize: Int) {
        cells.forEach { cell ->
            memory = updateTableCellsSelectionState(
                cellId = cell.getId(),
                rowIndex = cell.row.index,
                columnIndex = cell.column.index,
                selectionState = current(),
                rowsSize = rowsSize
            )
        }
    }

    fun clear() {
        memory = emptyMap()
    }

    fun current(): Map<Int, BlockView.Table.CellSelection> = memory
}
