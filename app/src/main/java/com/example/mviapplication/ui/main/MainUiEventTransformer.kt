package com.example.mviapplication.ui.main

/**
 * @author s.buvaka
 */
class MainUiEventTransformer : (MainUiEvent) -> MainFeature.Wish {

    override fun invoke(event: MainUiEvent): MainFeature.Wish =
        when (event) {
            is MainUiEvent.LoadImages -> MainFeature.Wish.LoadImages
        }
}
