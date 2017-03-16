package com.vincentmasselis.rxui.support

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import com.vincentmasselis.rxui.utils.FragmentLifecycleProvider
import com.vincentmasselis.rxui.utils.FragmentState
import io.reactivex.subjects.BehaviorSubject

open class RxDialogFragment : DialogFragment(), FragmentLifecycleProvider {

    override val lifecycleObs: BehaviorSubject<FragmentState> = BehaviorSubject.create()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        lifecycleObs.onNext(FragmentState.ATTACH)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleObs.onNext(FragmentState.CREATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleObs.onNext(FragmentState.VIEW_CREATED)
    }

    override fun onStart() {
        super.onStart()
        lifecycleObs.onNext(FragmentState.START)
    }

    override fun onResume() {
        super.onResume()
        lifecycleObs.onNext(FragmentState.RESUME)
    }

    override fun onPause() {
        lifecycleObs.onNext(FragmentState.PAUSE)
        super.onPause()
    }

    override fun onStop() {
        lifecycleObs.onNext(FragmentState.STOP)
        super.onStop()
    }

    override fun onDestroyView() {
        lifecycleObs.onNext(FragmentState.DESTROY_VIEW)
        super.onDestroyView()
    }

    override fun onDestroy() {
        lifecycleObs.onNext(FragmentState.DESTROY)
        super.onDestroy()
    }

    override fun onDetach() {
        lifecycleObs.onNext(FragmentState.DETACH)
        super.onDetach()
        lifecycleObs.onComplete()
    }
}