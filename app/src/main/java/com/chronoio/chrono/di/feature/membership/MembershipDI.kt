package com.chronoio.chrono.di.feature.membership

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.core_utils.di.scope.PerScreen
import com.chronoio.chrono.di.common.ComponentDependencies
import com.chronoio.chrono.domain.auth.interactor.GetAccount
import com.chronoio.chrono.domain.auth.repo.AuthRepository
import com.chronoio.chrono.domain.base.AppCoroutineDispatchers
import com.chronoio.chrono.domain.block.repo.BlockRepository
import com.chronoio.chrono.domain.payments.GetMembershipCodeInfo
import com.chronoio.chrono.domain.payments.GetMembershipEmailStatus
import com.chronoio.chrono.domain.payments.GetMembershipPaymentUrl
import com.chronoio.chrono.domain.payments.IsMembershipNameValid
import com.chronoio.chrono.domain.payments.RedeemMembershipCode
import com.chronoio.chrono.domain.payments.SetMembershipEmail
import com.chronoio.chrono.domain.payments.VerifyMembershipEmailCode
import com.chronoio.chrono.payments.playbilling.BillingClientLifecycle
import com.chronoio.chrono.ui.payments.MembershipFragment
import com.chronoio.chrono.payments.viewmodel.MembershipViewModelFactory
import com.chronoio.chrono.presentation.membership.provider.MembershipProvider
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    dependencies = [MembershipComponentDependencies::class],
    modules = [
        MembershipModule::class,
        MembershipModule.Declarations::class
    ]
)
@PerScreen
interface MembershipComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: MembershipComponentDependencies): MembershipComponent
    }

    fun inject(fragment: MembershipFragment)
}

@Module
object MembershipModule {

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetAccountUseCase(
        repo: AuthRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetAccount = GetAccount(repo = repo, dispatcher = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetPaymentsUrl(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetMembershipPaymentUrl = GetMembershipPaymentUrl(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideIsNameValid(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): IsMembershipNameValid = IsMembershipNameValid(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetEmailStatus(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetMembershipEmailStatus = GetMembershipEmailStatus(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideSetMembershipEmail(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetMembershipEmail = SetMembershipEmail(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideVerifyEmailCode(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): VerifyMembershipEmailCode = VerifyMembershipEmailCode(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideGetMembershipCodeInfo(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): GetMembershipCodeInfo = GetMembershipCodeInfo(repo = repo, dispatchers = dispatchers)

    @JvmStatic
    @Provides
    @PerScreen
    fun provideRedeemMembershipCode(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): RedeemMembershipCode = RedeemMembershipCode(repo = repo, dispatchers = dispatchers)

    @Module
    interface Declarations {

        @PerScreen
        @Binds
        fun bindViewModelFactory(
            factory: MembershipViewModelFactory
        ): ViewModelProvider.Factory

    }
}

interface MembershipComponentDependencies : ComponentDependencies {
    fun analytics(): Analytics
    fun context(): Context
    fun billingListener(): BillingClientLifecycle
    fun authRepository(): AuthRepository
    fun blockRepository(): BlockRepository
    fun appCoroutineDispatchers(): AppCoroutineDispatchers
    fun provideMembershipProvider(): MembershipProvider
}