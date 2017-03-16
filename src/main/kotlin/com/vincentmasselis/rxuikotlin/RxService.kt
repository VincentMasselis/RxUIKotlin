package com.vincentmasselis.rxuikotlin

import android.app.Service
import com.vincentmasselis.rxuikotlin.utils.ServiceLifecycleProvider
import com.vincentmasselis.rxuikotlin.utils.ServiceState
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