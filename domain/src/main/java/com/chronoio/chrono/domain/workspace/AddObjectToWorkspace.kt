package com.chronoio.chrono.domain.workspace

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

/**
 * Use-case for adding one or more object to the current workspace.
 * Returns of list of ids of object added to workspace.
 */
// TODO rename to "AddObjectListToSpace"
class AddObjectToWorkspace @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
): BaseUseCase<List<Id>, AddObjectToWorkspace.Params>(dispatchers.io) {

    override suspend fun run(params: Params) = safe {
        repo.addObjectListToSpace(
            objects = params.objects,
            space = params.space
        )
    }

    data class Params(
        val objects: List<Id>,
        val space: Id
    )
}