package com.vincentmasselis.rxui.utils

import io.reactivex.subjects.Subject

interface LifecycleProvider<T> {
    val lifecycleObs: Subject<T>
}

interface ActivityLifecycleProvider : LifecycleProvider<ActivityState>
interface FragmentLifecycleProvider : LifecycleProvider<FragmentState>
interface ViewLifecycleProvider : LifecycleProvider<ViewState>
interface ServiceLifecycleProvider : LifecycleProvider<ServiceState>