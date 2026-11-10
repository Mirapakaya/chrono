package com.chronoio.chrono.ui.onboarding.screens.signin

import androidx.compose.runtime.Composable
import com.chronoio.chrono.core_ui.common.DefaultPreviews
import com.chronoio.chrono.presentation.onboarding.login.OnboardingMnemonicLoginViewModel.SetupState

@DefaultPreviews
@Composable
private fun RecoveryScreenPreview() {
    RecoveryScreen(
        onBackClicked = {},
        onNextClicked = {},
        onActionDoneClicked = {},
        onScanQrClicked = {},
        state = SetupState.Idle,
        onEnterMyVaultClicked = {},
        onDebugAccountTraceClicked = {},
        onRetryMigrationClicked = {},
        onStartMigrationClicked = {}
    )
}

@DefaultPreviews
@Composable
private fun RecoveryScreenLoadingPreview() {
    RecoveryScreen(
        onBackClicked = {},
        onNextClicked = {},
        onActionDoneClicked = {},
        onScanQrClicked = {},
        state = SetupState.InProgress,
        onEnterMyVaultClicked = {},
        onDebugAccountTraceClicked = {},
        onRetryMigrationClicked = {},
        onStartMigrationClicked = {}
    )
}