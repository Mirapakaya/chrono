package com.chronoio.chrono.domain.templates

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import com.chronoio.chrono.domain.block.repo.BlockRepository
import javax.inject.Inject

class CreateTemplateFromObject @Inject constructor(
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<CreateTemplateFromObject.Params, Id>(
    dispatchers.io
) {

    override suspend fun doWork(params: Params): Id {
        return repo.createTemplateFromObject(params.obj)
    }

    data class Params(val obj: Id)
}