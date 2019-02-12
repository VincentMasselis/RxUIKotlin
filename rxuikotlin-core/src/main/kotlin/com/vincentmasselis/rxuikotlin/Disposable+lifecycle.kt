// Inspired by https://github.com/mg6maciej/DisposeOnDestroy/
package com.vincentmasselis.rxuikotlin

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.vincentmasselis.rxuikotlin.utils.ActivityState
import com.vincentmasselis.rxuikotlin.utils.FragmentState
import io.reactivex.disposables.Disposable

/**
 * Automatically dispose the [Disposable] when the filled [exceptedState] is reached for the [activityToMonitor]
 *
 * Be careful while setting up a value to [exceptedState]. As long as the state is not reached, the code listen to the lifecycle even if the [activityToMonitor] is
 * [ActivityState.DESTROY], this state can lead to memory leaks.
 *
 * @see FragmentManager.registerFragmentLifecycleCallbacks
 */
fun Disposable.disposeOnState(exceptedState: ActivityState, activityToMonitor: Activity): Disposable {
    activityToMonitor.application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

        fun disposeAndUnregisterIfRequired(currentState: ActivityState, activity: Activity) {
            if (activity == activityToMonitor && exceptedState == currentState)
                dispose()

            if (isDisposed)
                activityToMonitor.application.unregisterActivityLifecycleCallbacks(this)
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = disposeAndUnregisterIfRequired(ActivityState.CREATE, activity)

        override fun onActivityStarted(activity: Activity) = disposeAndUnregisterIfRequired(ActivityState.START, activity)

        override fun onActivityResumed(activity: Activity) = disposeAndUnregisterIfRequired(ActivityState.RESUME, activity)

        override fun onActivityPaused(activity: Activity) = disposeAndUnregisterIfRequired(ActivityState.PAUSE, activity)

        override fun onActivityStopped(activity: Activity) = disposeAndUnregisterIfRequired(ActivityState.STOP, activity)

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) = disposeAndUnregisterIfRequired(ActivityState.SAVE_INSTANCE_STATE, activity)

        override fun onActivityDestroyed(activity: Activity) = disposeAndUnregisterIfRequired(ActivityState.DESTROY, activity)
    })
    return this
}

/**
 * Automatically dispose the [Disposable] when the filled [exceptedState] is reached for the [fragmentToMonitor]
 *
 * Be careful while setting up a value to [exceptedState]. As long as the state is not reached, the code listen to the lifecycle even if the [fragmentToMonitor] is
 * [FragmentState.DETACH], this state can lead to memory leaks.
 *
 * @see FragmentManager.registerFragmentLifecycleCallbacks
 */
fun Disposable.disposeOnState(exceptedState: FragmentState, fragmentToMonitor: Fragment): Disposable {
    val fragmentManager = fragmentToMonitor.fragmentManager
            ?: throw IllegalStateException("disposeOnState is called too early for the fragment $fragmentToMonitor, disposeOnState method must called, at least, inside or after onCreate(Bundle?). See Fragment.getFragmentManager() method documentation")

    fragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {

        fun disposeAndUnregisterIfRequired(currentState: FragmentState, fragmentManager: FragmentManager, fragment: Fragment) {
            if (fragmentManager == fragmentToMonitor.fragmentManager && fragment == fragmentToMonitor && currentState == exceptedState)
                dispose()

            if (isDisposed)
                fragmentManager.unregisterFragmentLifecycleCallbacks(this)
        }

        override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) = disposeAndUnregisterIfRequired(FragmentState.PRE_ATTACH, fm, f)

        override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) = disposeAndUnregisterIfRequired(FragmentState.ATTACH, fm, f)

        override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) = disposeAndUnregisterIfRequired(FragmentState.PRE_CREATE, fm, f)

        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) = disposeAndUnregisterIfRequired(FragmentState.CREATE, fm, f)

        override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) =
            disposeAndUnregisterIfRequired(FragmentState.ACTIVITY_CREATED, fm, f)

        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) =
            disposeAndUnregisterIfRequired(FragmentState.VIEW_CREATED, fm, f)

        override fun onFragmentStarted(fm: FragmentManager, f: Fragment) = disposeAndUnregisterIfRequired(FragmentState.START, fm, f)

        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) = disposeAndUnregisterIfRequired(FragmentState.RESUME, fm, f)

        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) = disposeAndUnregisterIfRequired(FragmentState.PAUSE, fm, f)

        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) = disposeAndUnregisterIfRequired(FragmentState.STOP, fm, f)

        override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) = disposeAndUnregisterIfRequired(FragmentState.SAVE_INSTANCE_STATE, fm, f)

        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) = disposeAndUnregisterIfRequired(FragmentState.DESTROY_VIEW, fm, f)

        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) = disposeAndUnregisterIfRequired(FragmentState.DESTROY, fm, f)

        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) = disposeAndUnregisterIfRequired(FragmentState.DETACH, fm, f)
    }, false)
    return this
}