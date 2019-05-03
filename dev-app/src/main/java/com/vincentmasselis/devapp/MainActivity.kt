package com.vincentmasselis.devapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vincentmasselis.rxuikotlin.disposeOnState
import com.vincentmasselis.rxuikotlin.utils.ActivityState
import io.reactivex.disposables.Disposable

class MainActivity : AppCompatActivity() {

    lateinit var createDisposable: Disposable
    lateinit var resumeDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createDisposable = currentSituation!!.subscribe().disposeOnState(ActivityState.DESTROY, this)
    }

    override fun onResume() {
        super.onResume()
        resumeDisposable = currentSituation!!.subscribe().disposeOnState(ActivityState.PAUSE, this)
    }
}
