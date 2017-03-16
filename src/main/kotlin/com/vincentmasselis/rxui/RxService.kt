package com.vincentmasselis.rxui

import android.app.Service
import com.vincentmasselis.rxui.utils.ServiceLifecycleProvider
import com.vincentmasselis.rxui.utils.ServiceState
import io.reactivex.subjects.BehaviorSubject

abstract class RxService : Service(), ServiceLifecycleProvider {

    override val lifecycleObs: BehaviorSubject<ServiceState> = BehaviorSubject.create<ServiceState>()

    override fun onCreate() {
        super.onCreate()
        lifecycleObs.onNext(ServiceState.CREATE)
    }

    override fun onDestroy() {
        lifecycleObs.onNext(ServiceState.DESTROY)
        super.onDestroy()
        lifecycleObs.onComplete()
    }
}