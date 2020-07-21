package com.example.mviapplication.ui.main

import androidx.paging.PositionalDataSource
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.BaseFeature
import com.example.mviapplication.core.models.Photo
import com.example.mviapplication.core.models.PixelsResponse
import com.example.mviapplication.core.network.PixelsApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class MainFeature(pixelsApi: PixelsApi) : BaseFeature<MainFeature.Wish, MainFeature.Wish, MainFeature.Effect, MainFeature.State, Nothing>(
    initialState = State(),
    wishToAction = { wish -> wish },
    actor = ActorImpl(),
    reducer = ReducerImpl(),
    bootstrapper = BootstrapperImpl(pixelsApi)
) {
    data class State(
        val images: List<Photo> = emptyList(),
        val isLoading: Boolean = false,
        val positionalDataSource: PositionalDataSource<Photo>? = null
    )

    sealed class Wish {
        class StartPagination(val positionalDataSource: PositionalDataSource<Photo>) : Wish()
    }

    class ReducerImpl : Reducer<State, Effect> {

        override fun invoke(state: State, effect: Effect): State =
            when (effect) {
                is Effect.StartedLoading -> state.copy(isLoading = true)
                is Effect.ErrorLoading -> state.copy(isLoading = false)
                is Effect.StartPagination -> state.copy(positionalDataSource = effect.positionalDataSource)
            }
    }

    sealed class Effect {
        object StartedLoading : Effect()
        class StartPagination(val positionalDataSource: PositionalDataSource<Photo>) : Effect()
        data class ErrorLoading(val throwable: Throwable? = null) : Effect()
    }

    class ActorImpl : Actor<State, Wish, Effect> {

        override fun invoke(state: State, action: Wish): Observable<out Effect> =
            when (action) {
                is Wish.StartPagination -> Observable.just(Effect.StartPagination(action.positionalDataSource))
            }

        private fun parseResponse(response: Response<PixelsResponse>): Effect {
            if (response.isSuccessful) {
                val body = response.body() ?: return Effect.ErrorLoading()
            }
            return Effect.ErrorLoading()
        }
    }

    class BootstrapperImpl(private val pixelsApi: PixelsApi) : Bootstrapper<Wish>, PositionalDataSource<Photo>() {

        private var page = 1
        private val paginationDisposables = CompositeDisposable()

        override fun invoke(): Observable<Wish> = Observable.just(Wish.StartPagination(this))

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Photo>) {
            pixelsApi.getImageList(page)
                .doOnNext { page++ }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doAfterTerminate { paginationDisposables.clear() }
                .subscribe { callback.onResult(it.body()!!.photos) }
                .addTo(paginationDisposables)
        }

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Photo>) {
            pixelsApi.getImageList(page)
                .doOnNext { page++ }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doAfterTerminate { paginationDisposables.clear() }
                .subscribe { callback.onResult(it.body()!!.photos, 0) }
                .addTo(paginationDisposables)

        }

    }
}
