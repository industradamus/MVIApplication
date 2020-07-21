package com.example.mviapplication

import com.badoo.mvicore.android.AndroidBindings
import com.badoo.mvicore.binder.using

/**
 * @author s.buvaka
 */
class MainActivityBinder(
    view: MainActivity,
    private val feature: Feature
) : AndroidBindings<MainActivity>(view) {

    override fun setup(view: MainActivity) {
        binder.bind(feature to view using MainViewModelTransformer())
        binder.bind(view to feature using UiEventTransformer())
    }
}
