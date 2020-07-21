@file:Suppress("RemoveExplicitTypeArguments")

package com.example.mviapplication.core.di

import com.example.mviapplication.core.network.PixelsApi
import com.example.mviapplication.ui.main.MainActivity
import com.example.mviapplication.ui.main.MainActivityBinder
import com.example.mviapplication.ui.main.MainFeature
import org.koin.dsl.module

/**
 * @author s.buvaka
 */
val futureModule = module {

    factory<MainFeature> { MainFeature(get<PixelsApi>()) }
    factory<MainActivityBinder> { (activity: MainActivity) -> MainActivityBinder(activity, get<MainFeature>()) }
}
