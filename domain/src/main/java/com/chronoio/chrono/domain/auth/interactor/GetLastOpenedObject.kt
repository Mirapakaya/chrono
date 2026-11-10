package com.chronoio.chrono.domain.auth.interactor

import com.chronoio.chrono.core_models.DVFilter
import com.chronoio.chrono.core_models.DVFilterCondition
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.config.UserSettingsRepository

/**
 * Use case for fetching last open object's id for restoring user session.
 * @see SaveLastOpenedObject
 * @see ClearLastOpenedObject
 */
class GetLastOpenedObject(
    private val settings: UserSettingsRepository,
    private val blockRepo: BlockRepository
) : BaseUseCase<GetLastOpenedObject.Response, GetLastOpenedObject.Params>() {

    override suspend fun run(params: Params) = safe {
        val lastOpenObject = settings.getLastOpenedObject(space = params.space)
        if (lastOpenObject == null) {
            Response.Empty
        } else {
            val searchResults = blockRepo.searchObjects(
                space = params.space,
                limit = 1,
                filters = listOf(
                    DVFilter(
                        relation = Relations.ID,
                        condition = DVFilterCondition.IN,
                        value = listOf(lastOpenObject, params.space.id)
                    )
                )
            )
            val wrappedObjects = searchResults.map { ObjectWrapper.Basic(it) }
            val lastOpenedObject = wrappedObjects.firstOrNull { it.id == lastOpenObject }
            if (lastOpenedObject != null) {
                Response.Success(lastOpenedObject)
            } else {
                Response.NotFound(lastOpenObject)
            }
        }
    }

    sealed class Response {
        /**
         * There was no information about the last opened object.
         */
        data object Empty : Response()

        /**
         * The last opened object could not be found. It might have been deleted.
         */
        data class NotFound(val id: Id) : Response()

        /**
         * The last opened object has been found.
         */
        data class Success(val obj: ObjectWrapper.Basic) : Response()
    }

    data class Params(val space: SpaceId)
}