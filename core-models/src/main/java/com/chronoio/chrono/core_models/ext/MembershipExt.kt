package com.chronoio.chrono.core_models.ext

import com.chronoio.chrono.core_models.membership.MembershipConstants
import com.chronoio.chrono.core_models.membership.MembershipUpgradeReason
import com.chronoio.chrono.core_models.membership.TierId

fun TierId.isPossibleToUpgrade(
    reason: MembershipUpgradeReason
): Boolean {
    // Chrono: all features are free; no upgrade prompts.
    return false
}

fun TierId.isPossibleToUpgradeNumberOfSpaceMembers(): Boolean {
    // Chrono: all features are free; no upgrade prompts.
    return false
}

fun TierId.isPossibleToUpgradeStorageSpace(): Boolean {
    // Chrono: all features are free; no upgrade prompts.
    return false
}