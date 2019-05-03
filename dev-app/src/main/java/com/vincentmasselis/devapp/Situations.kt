package com.vincentmasselis.devapp

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

var currentSituation: Situations? = null

sealed class Situations {
    sealed class Singles(val single: Single<Long>) : Situations() {
        object Immediate : Singles(Single.just(0))
        object Fast : Singles(Single.timer(5, TimeUnit.MILLISECONDS))
        object Regular : Singles(Single.timer(1, TimeUnit.SECONDS))
        object Slow : Singles(Single.timer(1, TimeUnit.DAYS))
    }

    sealed class Maybes(val maybe: Maybe<Long>) : Situations() {
        object Immediate : Maybes(Maybe.just(0))
        object Fast : Maybes(Maybe.timer(5, TimeUnit.MILLISECONDS))
        object Regular : Maybes(Maybe.timer(1, TimeUnit.SECONDS))
        object Slow : Maybes(Maybe.timer(1, TimeUnit.DAYS))
    }

    sealed class Observables(val observable: Observable<Long>) : Situations() {
        object Immediate : Observables(Observable.just(0))
        object Fast : Observables(Observable.timer(5, TimeUnit.MILLISECONDS))
        object Regular : Observables(Observable.timer(1, TimeUnit.SECONDS))
        object Slow : Observables(Observable.timer(1, TimeUnit.DAYS))
    }

    fun subscribe(): Disposable = when (val situation = currentSituation) {
        is Situations.Singles -> situation.single.subscribe()
        is Situations.Maybes -> situation.maybe.subscribe()
        is Situations.Observables -> situation.observable.subscribe()
        else -> throw IllegalArgumentException("Cannot handle this situation $situation")
    }

    fun subscribe(consumer: Consumer<Unit>) = when (val situation = currentSituation) {
        is Situations.Singles -> situation.single.map { Unit }.subscribe(consumer)
        is Situations.Maybes -> situation.maybe.map { Unit }.subscribe(consumer)
        is Situations.Observables -> situation.observable.map { Unit }.subscribe(consumer)
        else -> throw IllegalArgumentException("Cannot handle this situation $situation")
    }
}