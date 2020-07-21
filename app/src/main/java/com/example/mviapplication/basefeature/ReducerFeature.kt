package com.example.mviapplication.basefeature

import com.badoo.mvicore.consumer.middlewareconfig.NonWrappable
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.BaseFeature
import io.reactivex.Observable

/**
 * @author s.buvaka
 */
abstract class ReducerFeature<Wish : Any, State : Any, News : Any>(
    initialState: State,
    reducer: Reducer<State, Wish>,
    bootstrapper: Bootstrapper<Wish>? = null,
    newsPublisher: SimpleNewsPublisher<Wish, State, News>? = null
) : BaseFeature<Wish, Wish, Wish, State, News>(
    initialState = initialState,
    bootstrapper = bootstrapper,
    wishToAction = { wish -> wish },
    actor = BypassActor(),
    reducer = reducer,
    newsPublisher = newsPublisher
) {
    class BypassActor<in State : Any, Wish : Any> : Actor<State, Wish, Wish>, NonWrappable {
        override fun invoke(state: State, wish: Wish): Observable<Wish> =
            Observable.just(wish)
    }

    abstract class SimpleNewsPublisher<in Wish : Any, in State : Any, out News : Any> :
        NewsPublisher<Wish, Wish, State, News> {
        override fun invoke(wish: Wish, effect: Wish, state: State): News? =
            invoke(wish, state)

        abstract fun invoke(wish: Wish, state: State): News?
    }
}
