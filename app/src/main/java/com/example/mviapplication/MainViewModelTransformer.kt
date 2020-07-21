package com.example.mviapplication

/**
 * @author s.buvaka
 */
class MainViewModelTransformer : (Feature.State) -> ViewModel {

    override fun invoke(state: Feature.State): ViewModel =
        ViewModel(
            url = state.randomInteger,
            isLoading = state.isLoading
        )
}
