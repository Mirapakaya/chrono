package com.chronoio.chrono.domain.spaces

import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.config.ConfigStorage
import com.chronoio.chrono.domain.debugging.Logger
import com.chronoio.chrono.domain.library.StoreSearchByIdsParams
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.workspace.SpaceManager
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SpaceDeletedStatusWatcher @Inject constructor(
    private val scope: CoroutineScope,
    private val container: StorelessSubscriptionContainer,
    private val spaceManager: SpaceManager,
    private val dispatchers: AppCoroutineDispatchers,
    private val configStorage: ConfigStorage,
    private val logger: Logger
) {

    private val jobs = mutableListOf<Job>()

    fun onStart() {
        jobs += scope.launch(dispatchers.io) {
            spaceManager
                .observe()
                .flatMapLatest { config ->
                    container.subscribe(
                        searchParams = StoreSearchByIdsParams(
                            space = SpaceId(config.techSpace),
                            subscription = DELETED_SPACE_VIEW_SUBSCRIPTION,
                            targets = listOf(config.spaceView),
                            keys = buildList {
                                add(Relations.ID)
                                add(Relations.TARGET_SPACE_ID)
                                add(Relations.SPACE_ACCOUNT_STATUS)
                            }
                        )
                    )
                }
                .mapNotNull { results -> results.firstOrNull() }
                .onEach { result ->
                    val spaceView = result.let {
                        ObjectWrapper.SpaceView(it.map)
                    }
                    if (spaceView.spaceAccountStatus.isDeletedOrRemoving()) {
                        logger.logWarning("Current space is deleted")
                        val accountConfig = configStorage.getOrNull()
                        if (accountConfig == null) {
                            logger.logWarning("Account config not found. Clearing space manager.")
                            spaceManager.clear()
                        }
                    }
                }
                .catch {
                    logger.logException(it, "Failed to observe space deletion status.")
                }
                .collect()
        }
    }

    fun onStop() {
        scope.launch(dispatchers.io) {
            container.unsubscribe(listOf(DELETED_SPACE_VIEW_SUBSCRIPTION))
            with(jobs) {
                forEach { it.cancel() }
                clear()
            }
        }
    }

    companion object {
        const val DELETED_SPACE_VIEW_SUBSCRIPTION = "subscription.global.space-view-deleted-status-watcher"
    }
}