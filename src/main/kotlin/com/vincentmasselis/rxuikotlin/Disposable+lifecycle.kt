package com.vincentmasselis.rxuikotlin

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import com.vincentmasselis.rxuikotlin.utils.*
import io.reactivex.disposables.Disposable

//TODO Add securities to avoid memory leaks caused by a wrong state sent by the developer that
// causes a sub that's never released. For example, if I listen for fragment onCreate() after the
// onCreateView() method is called, the subscription is keep forever and never disposed.
// Be careful : onCreateView can be called again after onStart in a FragmentPagerStateAdapter for
// example.

// Inspired by https://github.com/mg6maciej/DisposeOnDestroy/
fun Disposable.disposeOnState(exceptedState: ActivityState, activity: Activity): Disposable {
    activity.application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

        fun disposeAndUnregisterIfRequired(currentState: ActivityState, activityCb: Activity) {
            if (activityCb == activity && exceptedState == currentState) {
                if (isDisposed.not()) dispose()
                activity.application.unregisterActivityLifecycleCallbacks(this)
            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = disposeAndUnregisterIfRequired(ActivityState.CREATE, activity)

        override fun onActivityStarted(changedActivity: Activity) = disposeAndUnregisterIfRequired(ActivityState.START, activity)

        override fun onActivityResumed(changedActivity: Activity) = disposeAndUnregisterIfRequired(ActivityState.RESUME, activity)

        override fun onActivityPaused(changedActivity: Activity) = disposeAndUnregisterIfRequired(ActivityState.PAUSE, activity)

        override fun onActivityStopped(changedActivity: Activity) = disposeAndUnregisterIfRequired(ActivityState.STOP, activity)

        override fun onActivitySaveInstanceState(changedActivity: Activity, outState: Bundle?) = disposeAndUnregisterIfRequired(ActivityState.SAVE_INSTANCE_STATE, activity)

        override fun onActivityDestroyed(changedActivity: Activity) = disposeAndUnregisterIfRequired(ActivityState.DESTROY, activity)
    })
    return this
}

// Inspired by https://github.com/mg6maciej/DisposeOnDestroy/
fun Disposable.disposeOnState(exceptedState: FragmentState, fragment: Fragment): Disposable {
    fragment.fragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {

        fun disposeAndUnregisterIfRequired(currentState: FragmentState, fragmentManagerCb: FragmentManager, fragmentCb: Fragment) {
            if (fragmentManagerCb == fragment.fragmentManager && fragmentCb == fragment && currentState == exceptedState) {
                if (isDisposed.not()) dispose()
                fragmentManagerCb.unregisterFragmentLifecycleCallbacks(this)
            }
        }

        override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) = disposeAndUnregisterIfRequired(FragmentState.PRE_ATTACH, fm, f)

        override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context?) = disposeAndUnregisterIfRequired(FragmentState.ATTACH, fm, f)

        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) = disposeAndUnregisterIfRequired(FragmentState.CREATE, fm, f)

        override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) = disposeAndUnregisterIfRequired(FragmentState.ACTIVITY_CREATED, fm, f)

        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) = disposeAndUnregisterIfRequired(FragmentState.VIEW_CREATED, fm, f)

        override fun onFragmentStarted(fm: FragmentManager, f: Fragment) = disposeAndUnregisterIfRequired(FragmentState.START, fm, f)

        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) = disposeAndUnregisterIfRequired(FragmentState.RESUME, fm, f)

        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) = disposeAndUnregisterIfRequired(FragmentState.PAUSE, fm, f)

        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) = disposeAndUnregisterIfRequired(FragmentState.STOP, fm, f)

        override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle?) = disposeAndUnregisterIfRequired(FragmentState.SAVE_INSTANCE_STATE, fm, f)

        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) = disposeAndUnregisterIfRequired(FragmentState.DESTROY_VIEW, fm, f)

        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) = disposeAndUnregisterIfRequired(FragmentState.DESTROY, fm, f)

        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) = disposeAndUnregisterIfRequired(FragmentState.DETACH, fm, f)
    }, false)
    return this
}

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
                .subscribe({ if (this.isDisposed.not()) this.dispose() }, { throw IllegalStateException("lifecycleObs should never emit any error") }, { if (this.isDisposed.not()) this.dispose() })
                .let { this@disposeOnState }