package com.example.mviapplication

/**
 * @author s.buvaka
 */
class UiEventTransformer : (UiEvent) -> Feature.Wish {

    override fun invoke(event: UiEvent): Feature.Wish =
        when (event) {
            is UiEvent.GenerateButtonClicked -> Feature.Wish.GenerateInteger
        }
}
