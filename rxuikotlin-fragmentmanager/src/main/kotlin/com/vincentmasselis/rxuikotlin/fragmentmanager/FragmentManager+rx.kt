package com.vincentmasselis.rxuikotlin.fragmentmanager

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.vincentmasselis.rxuikotlin.utils.FragmentState
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue

/**
 * Emits [onNext] event when a [Fragment] from the [FragmentManager] obtains a new [FragmentState]. It never fails and it never completes.
 *
 * It doesn't emit at sub.
 *
 * @see FragmentManager.registerFragmentLifecycleCallbacks
 */
@CheckReturnValue
fun FragmentManager.rxFragmentsLifecycle(recursive: Boolean): Observable<Pair<FragmentState, Fragment>> = Observable.create { downStream ->
    val callbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) = downStream.onNext(FragmentState.VIEW_CREATED to f)

        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) = downStream.onNext(FragmentState.STOP to f)

        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) = downStream.onNext(FragmentState.CREATE to f)

        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) = downStream.onNext(FragmentState.RESUME to f)

        override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) = downStream.onNext(FragmentState.ATTACH to f)

        override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) = downStream.onNext(FragmentState.PRE_ATTACH to f)

        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) = downStream.onNext(FragmentState.DESTROY to f)

        override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) = downStream.onNext(FragmentState.SAVE_INSTANCE_STATE to f)

        override fun onFragmentStarted(fm: FragmentManager, f: Fragment) = downStream.onNext(FragmentState.START to f)

        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) = downStream.onNext(FragmentState.DESTROY_VIEW to f)

        override fun onFragmentPreCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) = downStream.onNext(FragmentState.PRE_CREATE to f)

        override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) = downStream.onNext(FragmentState.ACTIVITY_CREATED to f)

        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) = downStream.onNext(FragmentState.PAUSE to f)

        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) = downStream.onNext(FragmentState.DETACH to f)
    }
    registerFragmentLifecycleCallbacks(callbacks, recursive)
    downStream.setCancellable { unregisterFragmentLifecycleCallbacks(callbacks) }
}

@CheckReturnValue
@Deprecated("This method doesn't work very well when combining addToBackStack and orientation changes, consider using rxFragmentsLifecycle")
fun FragmentManager.rxFragments(): Observable<Set<Fragment>> = rxFragmentsLifecycle(false)
    .filter { it.first == FragmentState.ATTACH || it.first == FragmentState.DETACH }
    .compose { source ->
        val fragments = fragments.toMutableSet()
        source
            .map { (state, fragment) ->
                when (state) {
                    FragmentState.ATTACH -> fragments += fragment
                    FragmentState.DETACH -> fragments -= fragment
                    else -> throw IllegalStateException("Cannot handle the state $state")
                }
                fragments
            }
            .startWith(fragments)
            .map { it.toSet() }
    }
