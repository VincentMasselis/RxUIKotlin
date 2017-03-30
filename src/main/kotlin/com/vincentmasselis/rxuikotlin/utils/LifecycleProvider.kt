package com.vincentmasselis.rxuikotlin.utils

import io.reactivex.subjects.Subject

interface LifecycleProvider<T> {
    val lifecycleObs: Subject<T>
}

interface ViewLifecycleProvider : LifecycleProvider<ViewState>
interface ServiceLifecycleProvider : LifecycleProvider<ServiceState>