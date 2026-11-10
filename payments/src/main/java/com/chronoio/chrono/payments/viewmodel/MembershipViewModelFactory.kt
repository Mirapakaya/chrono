package com.chronoio.chrono.payments.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chronoio.chrono.analytics.base.Analytics
import com.chronoio.chrono.domain.auth.interactor.GetAccount
import com.chronoio.chrono.domain.payments.GetMembershipCodeInfo
import com.chronoio.chrono.domain.payments.GetMembershipEmailStatus
import com.chronoio.chrono.domain.payments.GetMembershipPaymentUrl
import com.chronoio.chrono.domain.payments.IsMembershipNameValid
import com.chronoio.chrono.domain.payments.RedeemMembershipCode
import com.chronoio.chrono.domain.payments.SetMembershipEmail
import com.chronoio.chrono.domain.payments.VerifyMembershipEmailCode
import com.chronoio.chrono.payments.playbilling.BillingClientLifecycle
import com.chronoio.chrono.presentation.membership.provider.MembershipProvider
import javax.inject.Inject

class MembershipViewModelFactory @Inject constructor(
    private val analytics: Analytics,
    private val billingClientLifecycle: BillingClientLifecycle,
    private val getAccount: GetAccount,
    private val membershipProvider: MembershipProvider,
    private val getMembershipPaymentUrl: GetMembershipPaymentUrl,
    private val isMembershipNameValid: IsMembershipNameValid,
    private val setMembershipEmail: SetMembershipEmail,
    private val verifyMembershipEmailCode: VerifyMembershipEmailCode,
    private val getMembershipEmailStatus: GetMembershipEmailStatus,
    private val getMembershipCodeInfo: GetMembershipCodeInfo,
    private val redeemMembershipCode: RedeemMembershipCode
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MembershipViewModel(
            analytics = analytics,
            billingClientLifecycle = billingClientLifecycle,
            getAccount = getAccount,
            membershipProvider = membershipProvider,
            getMembershipPaymentUrl = getMembershipPaymentUrl,
            isMembershipNameValid = isMembershipNameValid,
            setMembershipEmail = setMembershipEmail,
            verifyMembershipEmailCode = verifyMembershipEmailCode,
            getMembershipEmailStatus = getMembershipEmailStatus,
            getMembershipCodeInfo = getMembershipCodeInfo,
            redeemMembershipCode = redeemMembershipCode
        ) as T
    }
}