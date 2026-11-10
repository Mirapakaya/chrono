package com.chronoio.chrono.features.sets.dv

import androidx.core.os.bundleOf
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.chronoio.chrono.R
import com.chronoio.chrono.core_models.Block
import com.chronoio.chrono.core_models.DVViewerRelation
import com.chronoio.chrono.core_models.ObjectType
import com.chronoio.chrono.core_models.Relation
import com.chronoio.chrono.emojifier.data.DefaultDocumentEmojiIconProvider
import com.chronoio.chrono.presentation.MockBlockContentFactory.StubTextContent
import com.chronoio.chrono.core_models.ObjectViewDetails
import com.chronoio.chrono.presentation.relations.ObjectSetConfig
import com.chronoio.chrono.test_utils.MockDataFactory
import com.chronoio.chrono.test_utils.utils.checkHasText
import com.chronoio.chrono.test_utils.utils.checkIsRecyclerSize
import com.chronoio.chrono.test_utils.utils.onItemView
import com.chronoio.chrono.test_utils.utils.rVMatcher
import com.chronoio.chrono.ui.sets.ObjectSetFragment
import com.bartoszlipinski.disableanimationsrule.DisableAnimationsRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ObjectSetGridObjectCellRenderingTest : TestObjectSetSetup() {

    @get:Rule
    val animationsRule = DisableAnimationsRule()

    override val title: Block = Block(
        id = MockDataFactory.randomUuid(),
        content = StubTextContent(
            style = Block.Content.Text.Style.TITLE,
            text = "Data View UI Testing",
            marks = emptyList()
        ),
        children = emptyList(),
        fields = Block.Fields.empty()
    )

    @Before
    override fun setup() {
        super.setup()
    }

    @Test
    fun shouldRenderOneHumanObjectFromEachOfTwoRecords() {

        // SETUP

        val relationName = "Starring"
        val object1Name = "Charlie Chaplin"
        val object1Id = MockDataFactory.randomUuid()
        val object2Name = "Jean-Pierre Léaud"
        val object2Id = MockDataFactory.randomUuid()

        val relationId = MockDataFactory.randomUuid()
        val record1Id = MockDataFactory.randomUuid()
        val record2Id = MockDataFactory.randomUuid()

        val record1: Map<String, Any?> = mapOf(
            ObjectSetConfig.ID_KEY to record1Id,
            ObjectSetConfig.NAME_KEY to "The Great Dictator",
            ObjectSetConfig.EMOJI_KEY to DefaultDocumentEmojiIconProvider.DOCUMENT_SET.random(),
            relationId to object1Id
        )

        val record2: Map<String, Any?> = mapOf(
            ObjectSetConfig.ID_KEY to record2Id,
            ObjectSetConfig.NAME_KEY to "Les Quatre Cents Coups",
            ObjectSetConfig.EMOJI_KEY to DefaultDocumentEmojiIconProvider.DOCUMENT_SET.random(),
            relationId to object2Id
        )

        val relation = Relation(
            key = relationId,
            name = relationName,
            format = Relation.Format.OBJECT,
            source = Relation.Source.values().random()
        )

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = emptyList(),
            sorts = emptyList(),
            viewerRelations = listOf(
                DVViewerRelation(
                    key = relation.key,
                    isVisible = true
                )
            ),
            type = Block.Content.DataView.Viewer.Type.GRID
        )

        val dataview = Block(
            id = MockDataFactory.randomUuid(),
            children = emptyList(),
            fields = Block.Fields.empty(),
            content = Block.Content.DataView(
                viewers = listOf(viewer),
                
            )
        )

        val details = ObjectViewDetails(
            details = defaultDetails.details + mapOf(
                object1Id to
                    mapOf(
                        ObjectSetConfig.NAME_KEY to object1Name,
                        "iconEmoji" to "👤"
                    )
                ,
                object2Id to
                    mapOf(
                        ObjectSetConfig.NAME_KEY to object2Name,
                        "iconEmoji" to "👤"
                    )
            )
        )

        val root = Block(
            id = ctx,
            fields = Block.Fields(emptyMap()),
            content = Block.Content.Smart,
            children = listOf(header.id, dataview.id)
        )

        val set = listOf(root, header, title, dataview)

        stubInterceptEvents()
        stubInterceptThreadStatus()
        stubOpenObjectSetWithRecord(
            set = set,
            details = details
        )

        // TESTING

        launchFragment(bundleOf(ObjectSetFragment.CONTEXT_ID_KEY to ctx))

        with(R.id.rvRows.rVMatcher()) {
            checkIsRecyclerSize(2)
            onItemView(0, R.id.tvTitle).checkHasText("The Great Dictator")
            onItemView(0, R.id.object0).check(matches(hasDescendant(withText(object1Name))))
            onItemView(1, R.id.tvTitle).checkHasText("Les Quatre Cents Coups")
            onItemView(1, R.id.object0).check(matches(hasDescendant(withText(object2Name))))
        }
    }

    @Test
    fun shouldRenderTwoHumanObjectsFromOneRecord() {

        // SETUP

        val relationName = "Starring"
        val object1Name = "Maurice Ronet"
        val object1Id = MockDataFactory.randomUuid()
        val object2Name = "Jeanne Moreau"
        val object2Id = MockDataFactory.randomUuid()

        val objectType = ObjectType(
            url = MockDataFactory.randomUuid(),
            name = "Movie",
            relationLinks = emptyList(),
            emoji = MockDataFactory.randomString(),
            layout = ObjectType.Layout.PROFILE,
            description = "",
            isHidden = false,
            isArchived = false,
            isReadOnly = false
        )

        val relationId = MockDataFactory.randomUuid()
        val recordId = MockDataFactory.randomUuid()

        val record1: Map<String, Any?> = mapOf(
            ObjectSetConfig.ID_KEY to recordId,
            ObjectSetConfig.NAME_KEY to "Le Feu Follet",
            ObjectSetConfig.EMOJI_KEY to DefaultDocumentEmojiIconProvider.DOCUMENT_SET.random(),
            ObjectSetConfig.TYPE_KEY to objectType.url,
            relationId to listOf(object1Id, object2Id)
        )

        val relation = Relation(
            key = relationId,
            name = relationName,
            format = Relation.Format.OBJECT,
            source = Relation.Source.values().random()
        )

        val viewer = Block.Content.DataView.Viewer(
            id = MockDataFactory.randomUuid(),
            name = MockDataFactory.randomString(),
            filters = emptyList(),
            sorts = emptyList(),
            viewerRelations = listOf(
                DVViewerRelation(
                    key = relation.key,
                    isVisible = true
                )
            ),
            type = Block.Content.DataView.Viewer.Type.GRID
        )

        val dataview = Block(
            id = MockDataFactory.randomUuid(),
            children = emptyList(),
            fields = Block.Fields.empty(),
            content = Block.Content.DataView(
                viewers = listOf(viewer),
                
            )
        )

        val details = ObjectViewDetails(
            details = defaultDetails.details + mapOf(
                object1Id to
                    mapOf(
                        ObjectSetConfig.NAME_KEY to object1Name,
                        ObjectSetConfig.TYPE_KEY to objectType.url,
                        "iconEmoji" to "👤"
                    ),
                object2Id to
                    mapOf(
                        ObjectSetConfig.NAME_KEY to object2Name,
                        ObjectSetConfig.TYPE_KEY to objectType.url,
                        "iconEmoji" to "👤"
                    )
            )
        )

        val root = Block(
            id = ctx,
            fields = Block.Fields(emptyMap()),
            content = Block.Content.Smart,
            children = listOf(header.id, dataview.id)
        )

        val set = listOf(root, header, title, dataview)

        stubInterceptEvents()
        stubInterceptThreadStatus()
        stubOpenObjectSetWithRecord(
            set = set,

            details = details,
        )

        // TESTING

        launchFragment(bundleOf(ObjectSetFragment.CONTEXT_ID_KEY to ctx))

        with(R.id.rvRows.rVMatcher()) {
            checkIsRecyclerSize(1)
            onItemView(0, R.id.tvTitle).checkHasText("Le Feu Follet")
            onItemView(0, R.id.object0).check(matches(hasDescendant(withText(object1Name))))
            onItemView(0, R.id.object1).check(matches(hasDescendant(withText(object2Name))))
        }
    }
}