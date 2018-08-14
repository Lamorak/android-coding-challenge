package cz.lamorak.captionthis.extensions

import io.reactivex.Completable
import io.reactivex.Single

fun <T : Any> T.toSingle() : Single<T> = Single.just(this)

fun <T> Completable.either(onComplete: T, onError: (Throwable) -> T): Single<T> {
    return this.toSingleDefault(onComplete)
            .onErrorReturn(onError)
}

fun <T, U> Single<T>.either(onComplete: (T) -> U, onError: (Throwable) -> U): Single<U> {
    return this.map(onComplete)
            .onErrorReturn(onError)
}