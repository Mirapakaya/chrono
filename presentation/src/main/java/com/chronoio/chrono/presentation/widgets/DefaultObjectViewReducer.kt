package com.chronoio.chrono.presentation.widgets

import com.chronoio.chrono.core_models.Event
import com.chronoio.chrono.core_models.ObjectView
import com.chronoio.chrono.core_models.ext.process
import com.chronoio.chrono.core_utils.ext.replace
import com.chronoio.chrono.domain.objects.ObjectWatcher
import javax.inject.Inject
import timber.log.Timber

class DefaultObjectViewReducer @Inject constructor() : ObjectWatcher.Reducer {
    override fun invoke(given: ObjectView, events: List<Event>): ObjectView {
        var curr = given
        events.forEach { e ->
            when (e) {
                is Event.Command.AddBlock -> {
                    curr = curr.copy(blocks = curr.blocks + e.blocks)
                }
                is Event.Command.DeleteBlock -> {
                    curr = curr.copy(
                        blocks = curr.blocks.filter { !e.targets.contains(it.id) }
                    )
                }
                is Event.Command.UpdateStructure -> {
                    curr = curr.copy(
                        blocks = curr.blocks.replace(
                            replacement = { target ->
                                target.copy(children = e.children)
                            },
                            target = { block -> block.id == e.id }
                        )
                    )
                }
                is Event.Command.Details -> {
                    curr = curr.copy(details = curr.details.process(e))
                }
                else -> {
                    Timber.d("Skipping event: $e")
                }
            }
        }
        return curr
    }
}