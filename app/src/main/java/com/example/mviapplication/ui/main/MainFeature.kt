package com.example.mviapplication.ui.main

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.BaseFeature
import com.example.mviapplication.core.models.Photo
import com.example.mviapplication.core.models.PixelsResponse
import com.example.mviapplication.core.network.PixelsApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class MainFeature(pixelsApi: PixelsApi) : BaseFeature<MainFeature.Wish, MainFeature.Wish, MainFeature.Effect, MainFeature.State, Nothing>(
    initialState = State(),
    wishToAction = { wish -> wish },
    actor = ActorImpl(pixelsApi),
    reducer = ReducerImpl(),
    bootstrapper = BootstrapperImpl()
) {
    data class State(
        val images: List<Photo> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class Wish {
        object LoadImages : Wish()
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
        class IntegerGenerated(val images: List<Photo>) : Effect()
        data class ErrorLoading(val throwable: Throwable? = null) : Effect()
    }

    class ActorImpl(private val pixelsApi: PixelsApi) : Actor<State, Wish, Effect> {

        override fun invoke(state: State, action: Wish): Observable<out Effect> =
            when (action) {
                is Wish.LoadImages ->
                    pixelsApi.getImageList()
                        .map { response -> parseResponse(response) }
                        .startWith(Observable.just(Effect.StartedLoading))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn { Effect.ErrorLoading(it) }
            }

        private fun parseResponse(response: Response<PixelsResponse>): Effect {
            if (response.isSuccessful) {
                val body = response.body() ?: return Effect.ErrorLoading()
                return Effect.IntegerGenerated(body.photos)
            }
            return Effect.ErrorLoading()
        }
    }

    class BootstrapperImpl : Bootstrapper<Wish> {

        override fun invoke(): Observable<Wish> = Observable.just(Wish.LoadImages)

    }
}
