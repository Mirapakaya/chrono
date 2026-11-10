package com.chronoio.chrono.core_models.membership

object MembershipConstants {

    const val NONE_ID = 0
    const val OLD_EXPLORER_ID = 1
    const val STARTER_ID = 21
    const val PIONEER_ID = 22
    const val NEW_EXPLORER_ID = 20
    const val BUILDER_ID = 4
    const val CO_CREATOR_ID = 5
    const val ANY_TEAM_ID = 7

    const val MEMBERSHIP_LEVEL_DETAILS = "https://chrono.app/pricing"
    const val PRIVACY_POLICY = "https://chrono.app/app_privacy"
    const val TERMS_OF_SERVICE = "https://chrono.app/terms_of_use"

    val ACTIVE_TIERS_WITH_BANNERS = listOf(NONE_ID, OLD_EXPLORER_ID, STARTER_ID, PIONEER_ID)

    const val ERROR_PRODUCT_NOT_FOUND = "Product not found"
    const val ERROR_PRODUCT_PRICE = "Price of the product is not available"

    const val MEMBERSHIP_NAME_MIN_LENGTH = 7
}