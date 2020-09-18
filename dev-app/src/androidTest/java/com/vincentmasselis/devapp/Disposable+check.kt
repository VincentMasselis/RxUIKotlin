package com.vincentmasselis.devapp

import io.reactivex.rxjava3.disposables.Disposable


fun Disposable.checkDisposed() = check(isDisposed) { "Situation: $currentSituation, disposable: $this" }
fun Disposable.checkNotDisposed() = check(isDisposed.not()) { "Situation: $currentSituation, disposable: $this" }