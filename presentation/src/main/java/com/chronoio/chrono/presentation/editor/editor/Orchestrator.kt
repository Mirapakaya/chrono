package com.chronoio.chrono.presentation.editor.editor

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.analytics.event.EventAnalytics
import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.fold
import com.chronoio.chrono.domain.base.suspendFold
import com.chronoio.chrono.domain.block.UpdateDivider
import com.chronoio.chrono.domain.block.interactor.ClearBlockContent
import com.chronoio.chrono.domain.block.interactor.ClearBlockStyle
import com.chronoio.chrono.domain.block.interactor.CreateBlock
import com.chronoio.chrono.domain.block.interactor.DuplicateBlock
import com.chronoio.chrono.domain.block.interactor.MergeBlocks
import com.chronoio.chrono.domain.block.interactor.Move
import com.chronoio.chrono.domain.block.interactor.MoveOld
import com.chronoio.chrono.domain.block.interactor.ReplaceBlock
import com.chronoio.chrono.domain.block.interactor.SplitBlock
import com.chronoio.chrono.domain.block.interactor.TurnIntoDocument
import com.chronoio.chrono.domain.block.interactor.TurnIntoStyle
import com.chronoio.chrono.domain.block.interactor.UnlinkBlocks
import com.chronoio.chrono.domain.block.interactor.UpdateAlignment
import com.chronoio.chrono.domain.block.interactor.UpdateBackgroundColor
import com.chronoio.chrono.domain.block.interactor.UpdateBlocksMark
import com.chronoio.chrono.domain.block.interactor.UpdateCheckbox
import com.chronoio.chrono.domain.block.interactor.UpdateFields
import com.chronoio.chrono.domain.block.interactor.UpdateText
import com.chronoio.chrono.domain.block.interactor.UpdateTextColor
import com.chronoio.chrono.domain.block.interactor.UpdateTextStyle
import com.chronoio.chrono.domain.block.interactor.UploadBlock
import com.chronoio.chrono.domain.clipboard.Copy
import com.chronoio.chrono.domain.clipboard.Paste
import com.chronoio.chrono.domain.download.DownloadFile
import com.chronoio.chrono.domain.editor.Editor.Cursor
import com.chronoio.chrono.domain.editor.Editor.Focus
import com.chronoio.chrono.domain.page.Redo
import com.chronoio.chrono.domain.page.Undo
import com.chronoio.chrono.domain.page.bookmark.CreateBookmarkBlock
import com.chronoio.chrono.domain.page.bookmark.SetupBookmark
import com.chronoio.chrono.domain.relations.SetRelationKey
import com.chronoio.chrono.domain.table.CreateTable
import com.chronoio.chrono.domain.table.FillTableRow
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.presentation.analytics.AnalyticSpaceHelperDelegate
import com.chronoio.chrono.presentation.editor.Editor
import com.chronoio.chrono.presentation.extension.sendAnalyticsChangeTextBlockStyleEvent
import com.chronoio.chrono.presentation.extension.sendAnalyticsCopyBlockEvent
import com.chronoio.chrono.presentation.extension.sendAnalyticsCreateBlockEvent
import com.chronoio.chrono.presentation.extension.sendAnalyticsDeleteBlockEvent
import com.chronoio.chrono.presentation.extension.sendAnalyticsDownloadMediaEvent
import com.chronoio.chrono.presentation.extension.sendAnalyticsDuplicateBlockEvent
import com.chronoio.chrono.presentation.extension.sendAnalyticsPasteBlockEvent
import com.chronoio.chrono.presentation.extension.sendAnalyticsRedoEvent
import com.chronoio.chrono.presentation.extension.sendAnalyticsReorderBlockEvent
import com.chronoio.chrono.presentation.extension.sendAnalyticsSplitBlockEvent
import com.chronoio.chrono.presentation.extension.sendAnalyticsUndoEvent
import com.chronoio.chrono.presentation.extension.sendAnalyticsUploadMediaEvent
import com.chronoio.chrono.presentation.util.downloader.DocumentFileShareDownloader
import com.chronoio.chrono.presentation.util.downloader.MiddlewareShareDownloader
import timber.log.Timber

class Orchestrator(
    private val createBlock: CreateBlock,
    private val replaceBlock: ReplaceBlock,
    private val updateTextColor: UpdateTextColor,
    private val updateBackgroundColor: UpdateBackgroundColor,
    private val duplicateBlock: DuplicateBlock,
    private val splitBlock: SplitBlock,
    private val updateDivider: UpdateDivider,
    private val mergeBlocks: MergeBlocks,
    private val unlinkBlocks: UnlinkBlocks,
    private val updateTextStyle: UpdateTextStyle,
    private val turnIntoStyle: TurnIntoStyle,
    private val updateCheckbox: UpdateCheckbox,
    private val downloadFile: DownloadFile,
    private val documentFileShareDownloader: DocumentFileShareDownloader,
    val updateText: UpdateText,
    private val updateAlignment: UpdateAlignment,
    private val uploadBlock: UploadBlock,
    private val setupBookmark: SetupBookmark,
    private val createBookmarkBlock: CreateBookmarkBlock,
    private val turnIntoDocument: TurnIntoDocument,
    private val updateFields: UpdateFields,
    private val createTable: CreateTable,
    private val fillTableRow: FillTableRow,
    private val move: Move,
    private val copy: Copy,
    private val paste: Paste,
    private val undo: Undo,
    private val redo: Redo,
    private val setRelationKey: SetRelationKey,
    private val updateBlocksMark: UpdateBlocksMark,
    val memory: Editor.Memory,
    val stores: Editor.Storage,
    val proxies: Editor.Proxer,
    val textInteractor: Interactor.TextInteractor,
    private val analytics: Analytics,
    private val clearBlockContent: ClearBlockContent,
    private val clearBlockStyle: ClearBlockStyle,
    private val analyticsSpaceHelperDelegate: AnalyticSpaceHelperDelegate,
    private val spaceManager: SpaceManager
) : AnalyticSpaceHelperDelegate by analyticsSpaceHelperDelegate {

    private val defaultOnError: suspend (Throwable) -> Unit = { Timber.e(it) }

    suspend fun start(space: SpaceId) {
        proxies.intents.stream().collect { intent ->
            val spaceParams = provideParams(space.id)
            when (intent) {
                is Intent.CRUD.Create -> {
                    val startTime = System.currentTimeMillis()
                    createBlock.async(
                        params = CreateBlock.Params(
                            context = intent.context,
                            target = intent.target,
                            prototype = intent.prototype,
                            position = intent.position
                        )
                    ).suspendFold(
                        onSuccess = { (id, payload) ->
                            val middlewareTime = System.currentTimeMillis()
                            memory.sessionCreatedBlockIds.add(id)
                            stores.focus.update(Focus.id(id = id))
                            // Invoked before dispatching the payload, so that callers can
                            // record the new id before the payload-triggered re-render.
                            intent.onSuccess?.invoke(id)
                            proxies.payloads.send(payload)
                            analytics.sendAnalyticsCreateBlockEvent(
                                prototype = intent.prototype,
                                startTime = startTime,
                                middlewareTime = middlewareTime,
                                spaceParams = spaceParams,
                                isDate = intent.isDate,
                                route = intent.route
                            )
                        },
                        onFailure = {
                            intent.onFailure?.invoke()
                            defaultOnError(it)
                        }
                    )
                }
                is Intent.CRUD.Replace -> {
                    val startTime = System.currentTimeMillis()
                    replaceBlock(
                        params = ReplaceBlock.Params(
                            context = intent.context,
                            target = intent.target,
                            prototype = intent.prototype
                        )
                    ).proceed(
                        failure = {
                            intent.onFailure?.invoke()
                            defaultOnError(it)
                        },
                        success = { (id, payload) ->
                            val middlewareTime = System.currentTimeMillis()
                            memory.sessionCreatedBlockIds.add(id)
                            stores.focus.update(Focus.id(id = id))
                            // Invoked before dispatching the payload, so that callers can
                            // record the new id before the payload-triggered re-render.
                            intent.onSuccess?.invoke(id)
                            proxies.payloads.send(payload)
                            analytics.sendAnalyticsCreateBlockEvent(
                                prototype = intent.prototype,
                                startTime = startTime,
                                middlewareTime = middlewareTime,
                                spaceParams = spaceParams,
                                route = intent.route
                            )
                        }
                    )
                }
                is Intent.CRUD.Duplicate -> {
                    duplicateBlock(
                        params = DuplicateBlock.Params(
                            context = intent.context,
                            blocks = intent.blocks,
                            target = intent.target
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { (ids, payload) ->
                            memory.sessionCreatedBlockIds.addAll(ids)
                            stores.focus.update(
                                Focus(
                                    target = Focus.Target.Block(ids.last()),
                                    cursor = Cursor.End
                                )
                            )
                            proxies.payloads.send(payload)
                            analytics.sendAnalyticsDuplicateBlockEvent(intent.blocks.size)
                        }
                    )
                }
                is Intent.CRUD.Unlink -> {
                    unlinkBlocks(
                        params = UnlinkBlocks.Params(
                            context = intent.context,
                            targets = intent.targets
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { payload ->
                            val focus = intent.previous ?: intent.next
                            if (focus != null) {
                                stores.focus.update(
                                    Focus(
                                        target = Focus.Target.Block(focus),
                                        cursor = Cursor.End
                                    )
                                )
                            }
                            if (intent.effects.isNotEmpty()) {
                                processSideEffects(intent.effects)
                            }
                            proxies.payloads.send(payload)
                            analytics.sendAnalyticsDeleteBlockEvent(count = intent.targets.size)
                        }
                    )
                }
                is Intent.CRUD.UpdateFields -> {
                    updateFields(
                        params = UpdateFields.Params(
                            context = intent.context,
                            fields = intent.fields
                        )
                    ).proceed(
                        failure = {},
                        success = { proxies.payloads.send(it) }
                    )
                }
                is Intent.Text.Split -> {
                    val startTime = System.currentTimeMillis()
                    splitBlock(
                        params = SplitBlock.Params(
                            context = intent.context,
                            block = intent.block,
                            range = intent.range,
                            isToggled = intent.isToggled
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { (id, payload) ->
                            val middlewareTime = System.currentTimeMillis()
                            memory.sessionCreatedBlockIds.add(id)
                            stores.focus.update(
                                Focus(
                                    target = Focus.Target.Block(id),
                                    cursor = Cursor.Start
                                )
                            )
                            proxies.payloads.send(payload)
                            analytics.sendAnalyticsSplitBlockEvent(
                                startTime = startTime,
                                middlewareTime = middlewareTime,
                                style = intent.style,
                                spaceParams = spaceParams
                            )
                        }
                    )
                }
                is Intent.Text.Merge -> {
                    mergeBlocks(
                        params = MergeBlocks.Params(
                            context = intent.context,
                            pair = intent.pair
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { payload ->
                            if (intent.previousLength != null) {
                                stores.focus.update(
                                    Focus(
                                        target = Focus.Target.Block(intent.previous),
                                        cursor = Cursor.Range(
                                            intent.previousLength..intent.previousLength
                                        )
                                    )
                                )
                            } else {
                                stores.focus.update(Focus.id(intent.previous))
                            }
                            proxies.payloads.send(payload)
                            analytics.sendAnalyticsDeleteBlockEvent(count = 1)
                        }
                    )
                }
                is Intent.Text.UpdateColor -> {
                    updateTextColor(
                        params = UpdateTextColor.Params(
                            context = intent.context,
                            targets = intent.targets,
                            color = intent.color
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { payload ->
                            proxies.payloads.send(payload)
                        }
                    )
                }
                is Intent.Text.UpdateBackgroundColor -> {
                    updateBackgroundColor(
                        params = UpdateBackgroundColor.Params(
                            context = intent.context,
                            targets = intent.targets,
                            color = intent.color
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { payload ->
                            proxies.payloads.send(payload)
                        }
                    )
                }
                is Intent.Text.UpdateMark -> {
                    updateBlocksMark(
                        params = UpdateBlocksMark.Params(
                            context = intent.context,
                            targets = intent.targets,
                            mark = intent.mark
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { payload ->
                            proxies.payloads.send(payload)
                        }
                    )
                }
                is Intent.Text.UpdateStyle -> {
                    updateTextStyle(
                        params = UpdateTextStyle.Params(
                            context = intent.context,
                            targets = intent.targets,
                            style = intent.style
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { payload ->
                            proxies.payloads.send(payload)
                        }
                    )
                }
                is Intent.Text.TurnInto -> {
                    turnIntoStyle(
                        params = TurnIntoStyle.Params(
                            context = intent.context,
                            targets = intent.targets,
                            style = intent.style
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { payload ->
                            analytics.sendAnalyticsChangeTextBlockStyleEvent(
                                style = intent.style,
                                count = intent.targets.size,
                                route = intent.route
                            )
                            proxies.payloads.send(payload)
                        }
                    )
                }
                is Intent.Text.UpdateCheckbox -> {
                    updateCheckbox(
                        params = UpdateCheckbox.Params(
                            context = intent.context,
                            target = intent.target,
                            isChecked = intent.isChecked
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { payload ->
                            proxies.payloads.send(payload)
                        }
                    )
                }
                is Intent.Text.UpdateText -> {
                    updateText(
                        params = UpdateText.Params(
                            context = intent.context,
                            target = intent.target,
                            text = intent.text,
                            marks = intent.marks
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = {}
                    )
                }
                is Intent.Text.Align -> {
                    updateAlignment(
                        params = UpdateAlignment.Params(
                            context = intent.context,
                            targets = intent.targets,
                            alignment = intent.alignment
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { payload ->
                            proxies.payloads.send(payload)
                        }
                    )
                }
                is Intent.Document.Redo -> {
                    val params = Redo.Params(
                        context = intent.context
                    )
                    redo.async(
                        params = params
                    ).fold(
                        onFailure = {
                            Timber.e("Error while redoing: $it")
                        },
                        onSuccess = { result ->
                            when (result) {
                                Redo.Result.Exhausted -> {
                                    analytics.sendAnalyticsRedoEvent(false)
                                    intent.onRedoExhausted()
                                }
                                is Redo.Result.Success -> {
                                    analytics.sendAnalyticsRedoEvent(true)
                                    proxies.payloads.send(result.payload)
                                }
                            }
                        }
                    )
                }
                is Intent.Document.Undo -> {
                    val params = Undo.Params(
                        context = intent.context
                    )
                    undo.async(params).fold(
                        onFailure = {
                            Timber.e("Error while undoing: $it")
                        },
                        onSuccess = { result ->
                            when (result) {
                                Undo.Result.Exhausted -> {
                                    analytics.sendAnalyticsUndoEvent(false)
                                    intent.onUndoExhausted()
                                }

                                is Undo.Result.Success -> {
                                    analytics.sendAnalyticsUndoEvent(true)
                                    proxies.payloads.send(result.payload)
                                }
                            }
                        }
                    )
                }
                is Intent.Document.Move -> {
                    move.async(
                        params = Move.Params(
                            context = intent.context,
                            targetContext = intent.targetContext,
                            targetId = intent.target,
                            blockIds = intent.blocks,
                            position = intent.position
                        )
                    ).fold(
                        onSuccess = {
                            intent.onSuccess?.invoke()
                            proxies.payloads.send(it)
                            analytics.sendAnalyticsReorderBlockEvent(intent.blocks.size)
                        },
                        onFailure = {
                            Timber.e(it, "Error while moving blocks")
                            intent.onFailure?.invoke()
                        }
                    )
                }
                is Intent.Document.TurnIntoDocument -> {
                    turnIntoDocument(
                        params = TurnIntoDocument.Params(
                            context = intent.context,
                            targets = intent.targets
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { }
                    )
                }
                is Intent.Media.DownloadFile -> {
                    downloadFile(
                        params = DownloadFile.Params(
                            url = intent.url,
                            name = intent.name
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { analytics.sendAnalyticsDownloadMediaEvent(intent.type) }
                    )
                }
                is Intent.Media.ShareFile -> {
                    documentFileShareDownloader.async(
                        params = MiddlewareShareDownloader.Params(
                            objectId = intent.objectId,
                            name = intent.name
                        )
                    ).suspendFold(
                        onSuccess = { result ->
                            intent.onDownloaded(result.uri)
                            analytics.sendAnalyticsDownloadMediaEvent(intent.type)
                        },
                        onFailure = { e -> Timber.e(e, "Error while sharing a file") }
                    )
                }
                is Intent.Media.Upload -> {
                    uploadBlock(
                        params = UploadBlock.Params(
                            contextId = intent.context,
                            blockId = intent.description.blockId,
                            url = intent.url,
                            filePath = intent.filePath
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { payload ->
                            proxies.payloads.send(payload)
                            analytics.sendAnalyticsUploadMediaEvent(intent.description.type)
                        }
                    )
                }
                is Intent.Bookmark.SetupBookmark -> {
                    setupBookmark(
                        params = SetupBookmark.Params(
                            context = intent.context,
                            target = intent.target,
                            url = intent.url
                        )
                    ).proceed(
                        failure = { throwable ->
                            proxies.errors.send(throwable)
                        },
                        success = { payload ->
                            proxies.payloads.send(payload)
                        }
                    )
                }
                is Intent.Bookmark.CreateBookmark -> {
                    val startTime = System.currentTimeMillis()
                    createBookmarkBlock(
                        params = CreateBookmarkBlock.Params(
                            context = intent.context,
                            target = intent.target,
                            url = intent.url,
                            position = intent.position
                        )
                    ).proceed(
                        failure = { throwable ->
                            proxies.errors.send(throwable)
                        },
                        success = { payload ->
                            val middlewareTime = System.currentTimeMillis()
                            proxies.payloads.send(payload)
                            analytics.sendAnalyticsCreateBlockEvent(
                                prototype = Block.Prototype.Bookmark.New,
                                startTime = startTime,
                                middlewareTime = middlewareTime,
                                spaceParams = spaceParams,
                                route = null
                            )
                        }
                    )
                }
                is Intent.Clipboard.Paste -> {
                    paste(
                        params = Paste.Params(
                            context = intent.context,
                            focus = intent.focus,
                            range = intent.range,
                            selected = intent.selected,
                            isPartOfBlock = intent.isPartOfBlock
                        )
                    ).proceed(
                        failure = defaultOnError,
                        success = { response ->
                            if (response.cursor >= 0)
                                stores.focus.update(
                                    stores.focus.current().copy(
                                        cursor = Cursor.Range(response.cursor..response.cursor)
                                    )
                                )
                            else if (response.blocks.isNotEmpty()) {
                                stores.focus.update(
                                    Focus(
                                        target = Focus.Target.Block(response.blocks.last()),
                                        cursor = Cursor.End
                                    )
                                )
                            }
                            proxies.payloads.send(response.payload)
                            analytics.sendAnalyticsPasteBlockEvent()
                        }
                    )
                }
                is Intent.Clipboard.Copy -> {
                    copy(
                        params = Copy.Params(
                            context = intent.context,
                            blocks = intent.blocks,
                            range = intent.range
                        )
                    ).proceed(
                        success = {
                            proxies.toasts.send("Copied!")
                            analytics.sendAnalyticsCopyBlockEvent()
                        },
                        failure = defaultOnError
                    )
                }
                is Intent.Divider.UpdateStyle -> {
                    val startTime = System.currentTimeMillis()
                    updateDivider(
                        params = UpdateDivider.Params(
                            context = intent.context,
                            targets = intent.targets,
                            style = intent.style
                        )
                    ).proceed(
                        success = { payload ->
                            proxies.payloads.send(payload)
                        },
                        failure = defaultOnError
                    )
                }
                is Intent.Document.SetRelationKey -> {
                    setRelationKey(
                        params = SetRelationKey.Params(
                            contextId = intent.context,
                            blockId = intent.blockId,
                            key = intent.key
                        )
                    ).process(
                        failure = defaultOnError,
                        success = { payload -> proxies.payloads.send(payload) }
                    )
                }
                is Intent.Table.CreateTable -> {
                    val startTime = System.currentTimeMillis()
                    createTable(
                        params = CreateTable.Params(
                            ctx = intent.ctx,
                            target = intent.target,
                            position = intent.position,
                            rowCount = intent.rows,
                            columnCount = intent.columns
                        )
                    ).process(
                        failure = defaultOnError,
                        success = { payload ->
                            val middlewareTime = System.currentTimeMillis()
                            proxies.payloads.send(payload)
                            analytics.sendAnalyticsCreateBlockEvent(
                                prototype = Block.Prototype.SimpleTable,
                                startTime = startTime,
                                middlewareTime = middlewareTime,
                                spaceParams = spaceParams,
                                route = null
                            )
                        }
                    )
                }
                is Intent.Table.FillTableRow -> {
                    fillTableRow(
                        params = FillTableRow.Params(
                            ctx = intent.ctx,
                            targetIds = intent.targetIds
                        )
                    ).process(
                        failure = defaultOnError,
                        success = { payload -> proxies.payloads.send(payload) }
                    )
                }
                is Intent.Text.ClearContent -> {
                    clearBlockContent(
                        params = ClearBlockContent.Params(
                            ctx = intent.context,
                            blockIds = intent.targets
                        )
                    ).process(
                        failure = defaultOnError,
                        success = { payload -> proxies.payloads.send(payload) }
                    )
                }
                is Intent.Text.ClearStyle -> {
                    clearBlockStyle(
                        params = ClearBlockStyle.Params(
                            ctx = intent.context,
                            blockIds = intent.targets
                        )
                    ).process(
                        failure = defaultOnError,
                        success = { payload -> proxies.payloads.send(payload) }
                    )
                }
            }
        }
    }

    private suspend fun processSideEffects(effects: List<SideEffect>) {
        effects.forEach { effect ->
            when(effect) {
                SideEffect.ClearMultiSelectSelection -> {
                    memory.selections.clearSelections()
                }
                SideEffect.ResetFocusToFirstTextBlock -> {
                    stores.focus.update(
                        Focus(
                            target = Focus.Target.FirstTextBlock,
                            cursor = Cursor.End,
                            isPending = true
                        )
                    )
                }
            }
        }
    }

    private suspend fun sendEvent(event: EventAnalytics.Anytype) {
        analytics.registerEvent(event)
    }
}