package com.example.mviapplication

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.BaseFeature
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class Feature : BaseFeature<Feature.Wish, Feature.Wish, Feature.Effect, Feature.State, Nothing>(
    initialState = State(),
    wishToAction = { wish -> wish },
    actor = ActorImpl(),
    reducer = ReducerImpl()
) {
    data class State(
        val randomInteger: String = "Nothing to generate",
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
                    randomInteger = effect.text
                )
            }
    }

    sealed class Effect {
        object StartedLoading : Effect()
        class IntegerGenerated(val text: String) : Effect()
    }

    class ActorImpl : Actor<State, Wish, Effect> {

        override fun invoke(state: State, action: Wish): Observable<out Effect> =
            when (action) {
                is Wish.GenerateInteger ->
                    Observable.timer(2L, TimeUnit.SECONDS)
                        .map { Effect.IntegerGenerated("Random value = ${Random.nextInt(Int.MAX_VALUE)}") as Effect }
                        .startWith(Observable.just(Effect.StartedLoading))
                        .observeOn(AndroidSchedulers.mainThread())
            }
    }
}
