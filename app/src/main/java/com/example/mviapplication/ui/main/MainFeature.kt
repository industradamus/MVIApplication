package com.example.mviapplication.ui.main

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.BaseFeature
import com.example.mviapplication.core.models.PicsumImage
import com.example.mviapplication.core.network.PicsumApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainFeature(picsumApi: PicsumApi) : BaseFeature<MainFeature.Wish, MainFeature.Wish, MainFeature.Effect, MainFeature.State, Nothing>(
    initialState = State(),
    wishToAction = { wish -> wish },
    actor = ActorImpl(picsumApi),
    reducer = ReducerImpl()
) {
    data class State(
        val images: List<PicsumImage> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class Wish {
        object GenerateInteger : Wish()
    }

    class ReducerImpl : Reducer<State, Effect> {

        override fun invoke(state: State, effect: Effect): State =
            when (effect) {
                is Effect.StartedLoading -> state.copy(isLoading = true)
                is Effect.IntegerGenerated -> state.copy(
                    isLoading = false,
                    images = effect.images
                )
                is Effect.ErrorLoading -> state.copy(isLoading = false)
            }
    }

    sealed class Effect {
        object StartedLoading : Effect()
        class IntegerGenerated(val images: List<PicsumImage>) : Effect()
        data class ErrorLoading(val throwable: Throwable) : Effect()
    }

    class ActorImpl(private val picsumApi: PicsumApi) : Actor<State, Wish, Effect> {

        override fun invoke(state: State, action: Wish): Observable<out Effect> =
            when (action) {
                is Wish.GenerateInteger ->
                    picsumApi.getImageList()
                        .map { Effect.IntegerGenerated(it.body()!!) as Effect }
                        .startWith(Observable.just(Effect.StartedLoading))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn { Effect.ErrorLoading(it) }
            }
    }
}
