package com.chronoio.chrono.domain.debugging

import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either
import com.chronoio.chrono.domain.block.repo.BlockRepository

class DebugLocalStore(private val repo: BlockRepository) : BaseUseCase<String, DebugLocalStore.Params>() {

    override suspend fun run(params: Params): Either<Throwable, String> = safe {
        repo.debugLocalStore(path = params.path)
    }

    class Params(val path: String)
}