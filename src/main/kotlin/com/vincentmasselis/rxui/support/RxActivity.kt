package com.vincentmasselis.rxui.support

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.vincentmasselis.rxui.utils.ActivityLifecycleProvider
import com.vincentmasselis.rxui.utils.ActivityState
import io.reactivex.subjects.BehaviorSubject

open class RxAppCompatActivity : AppCompatActivity(), ActivityLifecycleProvider {
    override val lifecycleObs: BehaviorSubject<ActivityState> = BehaviorSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleObs.onNext(ActivityState.CREATE)
    }

    override fun onStart() {
        super.onStart()
        lifecycleObs.onNext(ActivityState.START)
    }

    override fun onResume() {
        super.onResume()
        lifecycleObs.onNext(ActivityState.RESUME)
    }

    override fun onPause() {
        lifecycleObs.onNext(ActivityState.PAUSE)
        super.onPause()
    }

    override fun onStop() {
        lifecycleObs.onNext(ActivityState.STOP)
        super.onStop()
    }

    override fun onDestroy() {
        lifecycleObs.onNext(ActivityState.DESTROY)
        super.onDestroy()
        lifecycleObs.onComplete()
    }
}