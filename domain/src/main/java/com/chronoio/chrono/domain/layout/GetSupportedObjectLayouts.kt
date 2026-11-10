package com.chronoio.chrono.domain.layout

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.domain.base.BaseUseCase
import com.chronoio.chrono.domain.base.Either

class GetSupportedObjectLayouts :
    BaseUseCase<List<ObjectType.Layout>, GetSupportedObjectLayouts.Params>() {

    override suspend fun run(params: Params) = safe {
        listOf(
            ObjectType.Layout.NOTE,
            ObjectType.Layout.BASIC,
            ObjectType.Layout.PROFILE,
            ObjectType.Layout.TODO,
        )
    }

    data class Params(val ctx: Id)
}