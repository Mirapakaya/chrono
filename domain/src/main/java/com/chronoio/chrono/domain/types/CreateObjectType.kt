package com.chronoio.chrono.domain.types

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository

class CreateObjectType(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreateObjectType.Params, String>(dispatchers.io) {
    override suspend fun doWork(params: Params): String {
        val command = Command.CreateObjectType(
            details = mapOf(
                Relations.NAME to params.name,
                Relations.ICON_NAME to params.iconName,
                Relations.PLURAL_NAME to params.pluralName,
                Relations.ICON_OPTION to params.iconColor
            ),
            spaceId = params.space,
            internalFlags = listOf()
        )
        return repo.createType(command)
    }

    class Params(
        val space: Id,
        val name: String,
        val iconName: String,
        val pluralName: String,
        val iconColor: Double?
    )
}