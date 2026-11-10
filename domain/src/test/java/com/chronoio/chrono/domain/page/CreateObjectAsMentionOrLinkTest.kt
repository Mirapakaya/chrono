package com.chronoio.chrono.domain.page

import com.chronoio.chrono.core_models.Command
import com.chronoio.chrono.core_models.CoroutineTestRule
import com.chronoio.chrono.core_models.CreateObjectResult
import com.chronoio.chrono.core_models.InternalFlags
import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_models.Relations
import com.chronoio.chrono.core_models.primitives.SpaceId
import com.chronoio.chrono.core_models.primitives.TypeId
import com.chronoio.chrono.core_models.primitives.TypeKey
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.launch.GetDefaultObjectType
import com.chronoio.chrono.domain.templates.GetTemplates
import com.chronoio.chrono.domain.util.dispatchers
import com.chronoio.chrono.domain.workspace.SpaceManager
import com.chronoio.chrono.test_utils.MockDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking
import org.mockito.kotlin.verifyNoInteractions

class CreateObjectAsMentionOrLinkTest {

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var rule = CoroutineTestRule()

    @Mock
    lateinit var repo: BlockRepository

    @Mock
    lateinit var getDefaultObjectType: GetDefaultObjectType

    @Mock
    lateinit var getTemplates: GetTemplates

    @Mock
    lateinit var spaceManager: SpaceManager

    lateinit var createObjectAsMentionOrLink: CreateObjectAsMentionOrLink

    private val spaceId = MockDataFactory.randomString()

    @Before
    fun setup() {
        createObjectAsMentionOrLink = CreateObjectAsMentionOrLink(
            repo = repo,
            dispatchers = dispatchers,
            spaceManager = spaceManager
        )
        stubSpaceManager()
    }

    @Test
    fun `when type is null and default type is null - should send proper params`() = runBlocking {

        //SETUP
        val type = null
        val name = MockDataFactory.randomString()

        val defaultTypeKey = MockDataFactory.randomString()
        val defaultTemplate = MockDataFactory.randomString()
        givenGetDefaultObjectType(
            type = TypeKey(defaultTypeKey),
            name = name,
            defaultTemplate = defaultTemplate
        )
        stubCreateObject()

        //TESTING
        val params = CreateObjectAsMentionOrLink.Params(
            name = name,
            typeKey = TypeKey(defaultTypeKey),
            defaultTemplate = defaultTemplate
        )
        createObjectAsMentionOrLink.run(params)

        //ASSERT
        verifyNoInteractions(getTemplates)
        val commands = Command.CreateObject(
            prefilled = buildMap {
                put(Relations.NAME, name)
            },
            template = defaultTemplate,
            internalFlags = listOf(InternalFlags.ShouldSelectTemplate, InternalFlags.ShouldSelectType),
            space = SpaceId(spaceId),
            typeKey = TypeKey(defaultTypeKey)
        )
        verifyBlocking(repo, times(1)) { createObject(commands) }
    }

    @Test
    fun `when type is null and default type is not null without template - should send proper params`() =
        runBlocking {

            //SETUP
            val type = null
            val name = MockDataFactory.randomString()
            val typeDefault = MockDataFactory.randomString()
            val typeDefaultName = MockDataFactory.randomString()
            givenGetDefaultObjectType(
                type = TypeKey(typeDefault),
                name = typeDefaultName
            )
            givenGetTemplates()
            stubCreateObject()

            //TESTING
            val params = CreateObjectAsMentionOrLink.Params(
                name = name,
                typeKey = TypeKey(typeDefault)
            )
            createObjectAsMentionOrLink.run(params)

            //ASSERT
            val commands = Command.CreateObject(
                prefilled = buildMap {
                    put(Relations.NAME, name)
                },
                template = null,
                internalFlags = listOf(InternalFlags.ShouldSelectTemplate, InternalFlags.ShouldSelectType),
                space = SpaceId(spaceId),
                typeKey = TypeKey(typeDefault)
            )
            verifyBlocking(repo, times(1)) { createObject(commands) }
        }

    @Test
    fun `when type is null and default type is not null with one template - should send proper params`() =
        runBlocking {

            //SETUP
            val type = null
            val name = MockDataFactory.randomString()
            val typeDefault = MockDataFactory.randomString()
            val typeDefaultName = MockDataFactory.randomString()
            val template = MockDataFactory.randomString()
            givenGetDefaultObjectType(
                type = TypeKey(typeDefault),
                name = typeDefaultName,
                defaultTemplate = template
            )
            givenGetTemplates(listOf(ObjectWrapper.Basic(buildMap {
                put(Relations.ID, template)
            })))
            stubCreateObject()

            //TESTING
            val params = CreateObjectAsMentionOrLink.Params(
                name = name,
                typeKey = TypeKey(typeDefault),
                defaultTemplate = template
            )
            createObjectAsMentionOrLink.run(params)

            //ASSERT
            val commands = Command.CreateObject(
                prefilled = buildMap {
                    put(Relations.NAME, name)
                },
                template = template,
                internalFlags = listOf(InternalFlags.ShouldSelectTemplate, InternalFlags.ShouldSelectType),
                space = SpaceId(spaceId),
                typeKey = TypeKey(typeDefault)
            )
            verifyBlocking(repo, times(1)) { createObject(commands) }
        }

    @Test
    fun `when type is null and default type is not null with two templates - should send proper params`() =
        runBlocking {

            //SETUP
            val type = null
            val name = MockDataFactory.randomString()
            val typeDefault = MockDataFactory.randomString()
            val typeDefaultName = MockDataFactory.randomString()
            val templateOne = MockDataFactory.randomString()
            val templateTwo = MockDataFactory.randomString()
            givenGetTemplates(
                listOf(
                    ObjectWrapper.Basic(buildMap {
                        put(Relations.ID, templateOne)
                    }),
                    ObjectWrapper.Basic(buildMap {
                        put(Relations.ID, templateTwo)
                    })
                )
            )
            givenGetDefaultObjectType(
                type = TypeKey(typeDefault),
                name = typeDefaultName
            )
            stubCreateObject()

            //TESTING
            val params = CreateObjectAsMentionOrLink.Params(
                name = name,
                typeKey = TypeKey(typeDefault),

            )
            createObjectAsMentionOrLink.run(params)

            //ASSERT
            val commands = Command.CreateObject(
                prefilled = buildMap {
                    put(Relations.NAME, name)
                },
                template = null,
                internalFlags = listOf(InternalFlags.ShouldSelectTemplate, InternalFlags.ShouldSelectType),
                space = SpaceId(spaceId),
                typeKey = TypeKey(typeDefault)
            )
            verifyBlocking(repo, times(1)) { createObject(commands) }
        }

    private fun givenGetDefaultObjectType(
        type: TypeKey,
        name: String? = null,
        defaultTemplate: String? = null
    ) {
        getDefaultObjectType.stub {
            onBlocking { run(SpaceId(spaceId)) } doReturn GetDefaultObjectType.Response(
                type = type,
                name = name,
                id = TypeId(MockDataFactory.randomString()),
                defaultTemplate = defaultTemplate
            )
        }
    }

    private fun givenGetTemplates(objects: List<ObjectWrapper.Basic> = listOf()) {
        getTemplates.stub {
            onBlocking { run(any()) } doReturn objects
        }
    }

    private fun stubCreateObject() {
        repo.stub {
            onBlocking { createObject(any()) } doReturn CreateObjectResult(
                id = "",
                event = Payload(context = "", events = listOf()),
                details = emptyMap()
            )
        }
    }

    private fun stubSpaceManager() {
        spaceManager.stub {
            onBlocking { get() } doReturn spaceId
        }
    }
}