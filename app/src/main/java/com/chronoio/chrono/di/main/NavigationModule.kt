package com.chronoio.chrono.di.main

import com.chronoio.chrono.navigation.DefaultNavigationBackStackInspector
import com.chronoio.chrono.presentation.navigation.backstack.NavigationBackStackInspector
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindNavigationBackStackInspector(
        impl: DefaultNavigationBackStackInspector
    ): NavigationBackStackInspector
}
