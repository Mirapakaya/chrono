package com.chronoio.chrono.feature_discussions.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.core_models.UrlBuilder
import com.chronoio.chrono.domain.auth.interactor.GetAccount
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.chats.ChatContainer
import com.chronoio.chrono.domain.misc.DateProvider
import com.chronoio.chrono.domain.library.StorelessSubscriptionContainer
import com.chronoio.chrono.domain.media.PreloadFile
import com.chronoio.chrono.domain.media.UploadFile
import com.chronoio.chrono.domain.multiplayer.ActiveSpaceMemberSubscriptionContainer
import com.chronoio.chrono.domain.objects.StoreOfObjectTypes
import com.chronoio.chrono.domain.primitives.FieldParser
import com.chronoio.chrono.presentation.util.CopyFileToCacheDirectory
import javax.inject.Inject

class DiscussionViewModelFactory @Inject constructor(
    private val params: DiscussionViewModel.Params,
    private val chatContainer: ChatContainer,
    private val members: ActiveSpaceMemberSubscriptionContainer,
    private val getAccount: GetAccount,
    private val urlBuilder: UrlBuilder,
    private val dispatchers: AppCoroutineDispatchers,
    private val addChatMessage: AddComment,
    private val deleteComment: DeleteComment,
    private val toggleCommentReaction: ToggleCommentReaction,
    private val dateProvider: DateProvider,
    private val storeOfObjectTypes: StoreOfObjectTypes,
    private val fieldParser: FieldParser,
    private val subscription: StorelessSubscriptionContainer,
    private val uploadFile: UploadFile,
    private val preloadFile: PreloadFile,
    private val copyFileToCacheDirectory: CopyFileToCacheDirectory,
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = DiscussionViewModel(
        vmParams = params,
        chatContainer = chatContainer,
        members = members,
        getAccount = getAccount,
        urlBuilder = urlBuilder,
        dispatchers = dispatchers,
        addChatMessage = addChatMessage,
        deleteComment = deleteComment,
        toggleCommentReaction = toggleCommentReaction,
        dateProvider = dateProvider,
        storeOfObjectTypes = storeOfObjectTypes,
        fieldParser = fieldParser,
        subscription = subscription,
        uploadFile = uploadFile,
        preloadFile = preloadFile,
        copyFileToCacheDirectory = copyFileToCacheDirectory,
        contentResolver = context.contentResolver
    ) as T
}
