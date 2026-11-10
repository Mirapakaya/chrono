package com.chronoio.chrono.di.feature

import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_models.Payload
import com.chronoio.chrono.core_utils.di.scope.PerModal
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.icon.RemoveDocumentIcon
import com.chronoio.chrono.domain.icon.RemoveTextBlockIcon
import com.chronoio.chrono.domain.icon.SetDocumentEmojiIcon
import com.chronoio.chrono.domain.icon.SetDocumentImageIcon
import com.chronoio.chrono.domain.icon.SetTextBlockEmoji
import com.chronoio.chrono.domain.icon.SetTextBlockImage
import com.chronoio.chrono.emojifier.data.Emoji
import com.chronoio.chrono.emojifier.suggest.EmojiSuggester
import com.chronoio.chrono.presentation.editor.picker.ObjectIconPickerViewModelFactory
import com.chronoio.chrono.presentation.editor.picker.ObjectSetIconPickerViewModelFactory
import com.chronoio.chrono.presentation.editor.picker.TextBlockIconPickerViewModelFactory
import com.chronoio.chrono.presentation.util.Dispatcher
import com.chronoio.chrono.ui.editor.modals.ObjectIconPickerFragment
import com.chronoio.chrono.ui.editor.modals.TextBlockIconPickerFragment
import com.chronoio.chrono.ui.sets.ObjectSetIconPickerFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [ObjectIconPickerBaseModule::class, ObjectIconPickerModule::class])
@PerModal
interface ObjectIconPickerComponent {
    @Subcomponent.Builder
    interface Builder {
        fun base(module: ObjectIconPickerBaseModule): Builder
        fun module(module: ObjectIconPickerModule): Builder
        fun build(): ObjectIconPickerComponent
    }

    fun inject(fragment: ObjectIconPickerFragment)
}

@Subcomponent(modules = [TextBlockIconPickerModule::class])
@PerModal
interface TextBlockIconPickerComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: TextBlockIconPickerModule): Builder
        fun build(): TextBlockIconPickerComponent
    }

    fun inject(fragment: TextBlockIconPickerFragment)
}

@Subcomponent(modules = [ObjectIconPickerBaseModule::class, ObjectSetIconPickerModule::class])
@PerModal
interface ObjectSetIconPickerComponent {
    @Subcomponent.Builder
    interface Builder {
        fun base(module: ObjectIconPickerBaseModule): Builder
        fun module(module: ObjectSetIconPickerModule): Builder
        fun build(): ObjectSetIconPickerComponent
    }

    fun inject(fragment: ObjectSetIconPickerFragment)
}

@Module
object ObjectIconPickerBaseModule {

    @Provides
    @PerModal
    fun provideSetDocumentEmojiIconUseCase(
        repo: BlockRepository
    ): SetDocumentEmojiIcon = SetDocumentEmojiIcon(
        repo = repo
    )

    @Provides
    @PerModal
    fun provideRemoveDocumentIconUseCase(
        repo: BlockRepository
    ): RemoveDocumentIcon = RemoveDocumentIcon(
        repo = repo
    )
}

@Module
object ObjectIconPickerModule {

    @Provides
    @PerModal
    fun provideViewModelFactory(
        setEmojiIcon: SetDocumentEmojiIcon,
        setImageIcon: SetDocumentImageIcon,
        removeDocumentIcon: RemoveDocumentIcon,
        emojiSuggester: EmojiSuggester,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics
    ): ObjectIconPickerViewModelFactory = ObjectIconPickerViewModelFactory(
        setEmojiIcon = setEmojiIcon,
        setImageIcon = setImageIcon,
        removeDocumentIcon = removeDocumentIcon,
        emojiSuggester = emojiSuggester,
        emojiProvider = Emoji,
        dispatcher = dispatcher,
        analytics = analytics
    )
}

@Module
object TextBlockIconPickerModule {

    @Provides
    @PerModal
    fun provideSetEmojiIcon(
        repo: BlockRepository
    ): SetTextBlockEmoji = SetTextBlockEmoji(
        repo = repo
    )

    @Provides
    @PerModal
    fun provideRemoveIcon(
        repo: BlockRepository
    ): RemoveTextBlockIcon = RemoveTextBlockIcon(
        repo = repo
    )

    @Provides
    @PerModal
    fun provideSetImage(
        repo: BlockRepository
    ): SetTextBlockImage = SetTextBlockImage(
        repo = repo
    )

    @Provides
    @PerModal
    fun provideViewModelFactory(
        setEmojiIcon: SetTextBlockEmoji,
        setImageIcon: SetTextBlockImage,
        removeDocumentIcon: RemoveTextBlockIcon,
        emojiSuggester: EmojiSuggester,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics
    ): TextBlockIconPickerViewModelFactory = TextBlockIconPickerViewModelFactory(
        setEmojiIcon = setEmojiIcon,
        setImageIcon = setImageIcon,
        removeDocumentIcon = removeDocumentIcon,
        emojiSuggester = emojiSuggester,
        emojiProvider = Emoji,
        dispatcher = dispatcher,
        analytics = analytics
    )
}

@Module
object ObjectSetIconPickerModule {
    @Provides
    @PerModal
    fun provideViewModelFactory(
        setEmojiIcon: SetDocumentEmojiIcon,
        setImageIcon: SetDocumentImageIcon,
        removeDocumentIcon: RemoveDocumentIcon,
        emojiSuggester: EmojiSuggester,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics
    ): ObjectSetIconPickerViewModelFactory = ObjectSetIconPickerViewModelFactory(
        setEmojiIcon = setEmojiIcon,
        setImageIcon = setImageIcon,
        removeDocumentIcon = removeDocumentIcon,
        emojiSuggester = emojiSuggester,
        emojiProvider = Emoji,
        dispatcher = dispatcher,
        analytics = analytics
    )
}