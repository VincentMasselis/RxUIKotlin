package com.vincentmasselis.devapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vincentmasselis.rxuikotlin.disposeOnState
import com.vincentmasselis.rxuikotlin.utils.FragmentState
import io.reactivex.rxjava3.disposables.Disposable

class MainFragment : Fragment() {

    lateinit var createDisposable: Disposable
    lateinit var viewCreatedDisposable: Disposable
    lateinit var resumeDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createDisposable = currentSituation!!.subscribe().disposeOnState(FragmentState.DESTROY, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCreatedDisposable = currentSituation!!.subscribe().disposeOnState(FragmentState.DESTROY_VIEW, this)
    }

    override fun onResume() {
        super.onResume()
        resumeDisposable = currentSituation!!.subscribe().disposeOnState(FragmentState.PAUSE, this)
    }
}