package com.vincentmasselis.rxuikotlin

import android.app.Activity
import android.os.Bundle
import com.vincentmasselis.rxuikotlin.utils.ActivityLifecycleProvider
import com.vincentmasselis.rxuikotlin.utils.ActivityState
import io.reactivex.subjects.BehaviorSubject

open class RxActivity : Activity(), ActivityLifecycleProvider {
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