package com.vincentmasselis.rxuikotlin.fragmentmanager

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.*
import com.vincentmasselis.rxuikotlin.utils.FragmentState
import io.reactivex.rxjava3.annotations.CheckReturnValue
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

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

/**
 * Returns the list of fragments which have a view created. Emits every fragment with a view created at subscription.
 *
 * The order of the fragments in the list is the order in which they were added or attached.
 */
@CheckReturnValue
fun FragmentManager.rxViewCreatedFragments(): Observable<List<Fragment>> = Observable
        .defer {
            val viewCreatedFragments = fragments.filter { it.isViewCreated() }.toMutableList()
            rxFragmentsLifecycle(false)
                    .filter { (state, fragment) ->
                        when (state) {
                            FragmentState.VIEW_CREATED -> {
                                viewCreatedFragments += fragment
                                true
                            }
                            FragmentState.DESTROY_VIEW -> {
                                viewCreatedFragments -= fragment
                                true
                            }
                            else -> false
                        }
                    }
                    .map { viewCreatedFragments.toList() }
                    .startWith(Single.just(viewCreatedFragments.toList()))
        }

/**
 * Returns a set of created fragments. Emits every already created fragment at subscription.
 *
 * Unlike [rxViewCreatedFragments] the order of the returned set has no meaning.
 */
@CheckReturnValue
fun FragmentManager.rxCreatedFragments(): Observable<Set<Fragment>> = Observable
        .defer {
            val createdFragments = activeFragments.apply { retainAll { it.isCreated() } }.toMutableSet()
            rxFragmentsLifecycle(false)
                    .filter { (state, fragment) ->
                        when (state) {
                            FragmentState.CREATE -> {
                                createdFragments += fragment
                                true
                            }
                            FragmentState.DESTROY -> {
                                createdFragments -= fragment
                                true
                            }
                            else -> false
                        }
                    }
                    .map { createdFragments.toSet() }
                    .startWith(Single.just(createdFragments.toSet()))
        }

@CheckReturnValue
@Deprecated(
        "This method doesn't work very well when combining addToBackStack and orientation changes",
        ReplaceWith("rxCreatedFragments()")
)
fun FragmentManager.rxFragments(): Observable<Set<Fragment>> = rxCreatedFragments()