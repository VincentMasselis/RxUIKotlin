package com.vincentmasselis.rxui

import com.vincentmasselis.rxui.utils.*
import io.reactivex.disposables.Disposable

//TODO Add securities to avoid memory leaks caused by a wrong state send by the user that cause a
// sub that's never released. For example if I listen for fragment onCreate after the onCreateView
// is called. Warning, some previous methods from lifecycle can be called again. For example,
// onCreateView can be called again after onStart in a FragmentPagerStateAdapter

//TODO Remove disposeOnLifecycle method, that's lead the developer to mistake, like calling this on
// onStart and displaying a Dialog (that's call onResume immediately and dispose the sub).

fun Disposable.disposeOnLifecycle(provider: ActivityLifecycleProvider) =
        provider
                .lifecycleObs
                .firstElement()
                .subscribe({ disposeOnState(it.opposite(), provider) }, { throw IllegalStateException("lifecycleObs should never emit any error") }, { if (isDisposed.not()) dispose() })
                .let { this@disposeOnLifecycle }

fun Disposable.disposeOnState(state: ActivityState, provider: ActivityLifecycleProvider) =
        provider
                .lifecycleObs
                .filter { it == state }
                .firstElement()
                .subscribe({ if (isDisposed.not()) dispose() }, { throw IllegalStateException("lifecycleObs should never emit any error") }, { if (isDisposed.not()) dispose() })
                .let { this@disposeOnState }

fun Disposable.disposeOnLifecycle(provider: FragmentLifecycleProvider) =
        provider
                .lifecycleObs
                .firstElement()
                .subscribe({ disposeOnState(it.opposite(), provider) }, { throw IllegalStateException("lifecycleObs should never emit any error") }, { if (isDisposed.not()) dispose() })
                .let { this@disposeOnLifecycle }

fun Disposable.disposeOnState(state: FragmentState, provider: FragmentLifecycleProvider) =
        provider
                .lifecycleObs
                .filter { it == state }
                .firstElement()
                .subscribe({ if (isDisposed.not()) dispose() }, { throw IllegalStateException("lifecycleObs should never emit any error") }, { if (isDisposed.not()) dispose() })
                .let { this@disposeOnState }

fun Disposable.disposeOnServiceDestroy(provider: ServiceLifecycleProvider) =
        provider
                .lifecycleObs
                .filter { it == ServiceState.DESTROY }
                .firstElement()
                .subscribe({ if (isDisposed.not()) dispose() }, { throw IllegalStateException("lifecycleObs should never emit any error") }, { if (isDisposed.not()) dispose() })
                .let { this@disposeOnServiceDestroy }

fun Disposable.disposeOnDetach(provider: ViewLifecycleProvider) =
        provider
                .lifecycleObs //This observable never complete, does the subscribe cause memory leaks ?
                .filter { it == ViewState.DETACH }
                .firstElement()
                .subscribe({ if (isDisposed.not()) dispose() }, { throw IllegalStateException("lifecycleObs should never emit any error") }, { if (isDisposed.not()) dispose() })
                .let { this@disposeOnDetach }

/*fun Disposable.disposeOnViewDetach(view: View) =
        object : View.OnAttachStateChangeListener, LifecycleProvider<ViewState> {
            override val lifecycleObs = BehaviorSubject.create<ViewState>()

            override fun onViewAttachedToWindow(v: View?) = lifecycleObs.onNext(ViewState.ATTACH)

            override fun onViewDetachedFromWindow(v: View?) {
                lifecycleObs.onNext(ViewState.DETACH)
                lifecycleObs.onComplete()
            }
        }
                .let { provider ->
                    MainThreadDisposable.verifyMainThread()
                    view.addOnAttachStateChangeListener(provider)
                    provider.lifecycleObs
                            .filter { it == ViewState.DETACH }
                            .firstElement()
                            .subscribe({
                                view.removeOnAttachStateChangeListener(provider)
                                if (isDisposed.not()) dispose()
                            }, { throw IllegalStateException("lifecycleObs should never emit any error") }, {
                                view.removeOnAttachStateChangeListener(provider)
                                if (isDisposed.not()) dispose()
                            })
                            .let { this@disposeOnViewDetach }
                }*/