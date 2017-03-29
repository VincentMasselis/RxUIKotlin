package com.vincentmasselis.rxuikotlin

import com.vincentmasselis.rxuikotlin.utils.*
import io.reactivex.disposables.Disposable

//TODO Add securities to avoid memory leaks caused by a wrong state sent by the developer that
// causes a sub that's never released. For example, if I listen for fragment onCreate() after the
// onCreateView() method is called, the subscription is keep forever and never disposed.
// Be careful : onCreateView can be called again after onStart in a FragmentPagerStateAdapter for
// example.

fun Disposable.disposeOnState(state: ActivityState, provider: ActivityLifecycleProvider) =
        provider
                .lifecycleObs
                .filter { it == state }
                .firstElement()
                .subscribe({ if (isDisposed.not()) dispose() }, { throw IllegalStateException("lifecycleObs should never emit any error") }, { if (isDisposed.not()) dispose() })
                .let { this@disposeOnState }

fun Disposable.disposeOnState(state: FragmentState, provider: FragmentLifecycleProvider) =
        provider
                .lifecycleObs
                .filter { it == state }
                .firstElement()
                .subscribe({ if (isDisposed.not()) dispose() }, { throw IllegalStateException("lifecycleObs should never emit any error") }, { if (isDisposed.not()) dispose() })
                .let { this@disposeOnState }

fun Disposable.disposeOnState(state: ServiceState, provider: ServiceLifecycleProvider) =
        provider
                .lifecycleObs
                .filter { it == state }
                .firstElement()
                .subscribe({ if (isDisposed.not()) dispose() }, { throw IllegalStateException("lifecycleObs should never emit any error") }, { if (isDisposed.not()) dispose() })
                .let { this@disposeOnState }

fun Disposable.disposeOnState(state: ViewState, provider: ViewLifecycleProvider) =
        provider
                .lifecycleObs
                .filter { it == state }
                .firstElement()
                .subscribe({ if (isDisposed.not()) dispose() }, { throw IllegalStateException("lifecycleObs should never emit any error") }, { if (isDisposed.not()) dispose() })
                .let { this@disposeOnState }