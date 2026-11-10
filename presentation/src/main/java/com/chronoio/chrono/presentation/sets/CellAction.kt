package com.chronoio.chrono.presentation.sets

import com.chronoio.chrono.presentation.sets.model.CellView

sealed class CellAction {

    abstract val cell: CellView

    sealed class Email : CellAction() {
        data class MailTo(override val cell: CellView) : Email()
    }

    sealed class Url : CellAction() {
        data class GoTo(override val cell: CellView) : Url()
    }

    data class Edit(override val cell: CellView) : CellAction()

    data class Call(override val cell: CellView) : CellAction()
}