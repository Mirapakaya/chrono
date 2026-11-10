package com.chronoio.chrono.presentation.home

import com.chronoio.chrono.core_models.ObjectWrapper
import com.chronoio.chrono.domain.spaces.ResolveSpaceHomepage

object SpaceHomepageResolver {

    fun isExplicitObjectHomepage(homepage: String?): Boolean =
        !homepage.isNullOrEmpty() && homepage !in ResolveSpaceHomepage.HOMEPAGE_SPECIAL_CONSTANTS

    fun shouldSkipWidgetInjection(spaceView: ObjectWrapper.SpaceView?): Boolean =
        isExplicitObjectHomepage(spaceView?.homepage)
}
