package com.vincentmasselis.rxuikotlinapp

import io.reactivex.disposables.Disposable


fun Disposable.checkDisposed() = check(isDisposed) { "Situation: $currentSituation, disposable: $this" }
fun Disposable.checkNotDisposed() = check(isDisposed.not()) { "Situation: $currentSituation, disposable: $this" }