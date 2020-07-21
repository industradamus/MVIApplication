package com.example.mviapplication.ui.main

/**
 * @author s.buvaka
 */
class MainViewModelTransformer : (MainFeature.State) -> MainViewModel {

    override fun invoke(state: MainFeature.State): MainViewModel =
        MainViewModel(
            isLoading = state.isLoading,
            positionalDataSource = state.positionalDataSource
        )
}
