package com.vincentmasselis.rxuikotlin

import android.app.Activity
import android.os.Looper
import androidx.fragment.app.Fragment
import com.vincentmasselis.rxuikotlin.utils.ActivityState
import com.vincentmasselis.rxuikotlin.utils.FragmentState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import java.util.concurrent.TimeUnit

/**
 * Compact helper method to execute a [lambda] on the main thread if the current [Activity]'s view
 * is created and not destroyed.
 * By default, It executes the [lambda] until [ActivityState.DESTROY] is called, you can change
 * this by using the param [state].
 *
 * @return The disposable of the inner subscription.
 */
fun Activity.postForUI(delay: Pair<Long, TimeUnit>? = null, state: ActivityState = ActivityState.DESTROY, lambda: () -> Unit) =
    buildChain(delay, lambda).disposeOnState(state, this)

/**
 * Compact helper method to execute a [lambda] on the main thread if the current [Fragment]'s view
 * is created and not destroyed.
 * By default, It executes the [lambda] until [FragmentState.DESTROY_VIEW] is called, you can change
 * this by using the param [state].
 *
 * @return The disposable of the inner subscription.
 */
fun Fragment.postForUI(delay: Pair<Long, TimeUnit>? = null, state: FragmentState = FragmentState.DESTROY_VIEW, lambda: () -> Unit) =
    buildChain(delay, lambda).disposeOnState(state, this)

private fun buildChain(delay: Pair<Long, TimeUnit>? = null, lambda: () -> Unit) =
    (if (delay != null) Completable.timer(delay.first, delay.second, AndroidSchedulers.mainThread())
    else Completable.complete())
        .compose {
            if (Looper.myLooper() != Looper.getMainLooper() && delay == null) it.observeOn(AndroidSchedulers.mainThread())
            else it
        }
        .subscribe(lambda)