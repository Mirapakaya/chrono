package com.chronoio.chrono.domain.multiplayer

import com.chronoio.chrono.core_models.Id
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.base.ResultInteractor
import javax.inject.Inject

/**
 * Generates a deep link for initiating a 1-1 chat with the current user.
 * The link format is: https://hi.chrono.app/{identity}#{metaDataKey}
 */
class GenerateOneToOneChatLink @Inject constructor(
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GenerateOneToOneChatLink.Params, OneToOneLinkData>(dispatchers.io) {

    data class Params(
        val identity: Id,
        val metaDataKey: String,
        val name: String
    )

    override suspend fun doWork(params: Params): OneToOneLinkData {
        if (params.metaDataKey.isBlank()) {
            throw IllegalStateException("MetaData key not available")
        }
        if (params.identity.isBlank()) {
            throw IllegalStateException("Identity not available")
        }

        return OneToOneLinkData(
            link = "$BASE_URL${params.identity}#${params.metaDataKey}",
            name = params.name,
            identity = params.identity
        )
    }

    companion object {
        const val BASE_URL = "https://hi.chrono.app/"
    }
}

/**
 * Data class containing the generated 1-1 chat link and associated profile information.
 *
 * @property link The generated deep link URL
 * @property name The current user's profile name
 * @property identity The current user's identity ID
 */
data class OneToOneLinkData(
    val link: String,
    val name: String,
    val identity: Id
)
