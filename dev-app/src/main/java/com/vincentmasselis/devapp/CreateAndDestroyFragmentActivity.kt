package com.vincentmasselis.devapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vincentmasselis.rxuikotlin.fragmentmanager.rxFragmentsLifecycle
import com.vincentmasselis.rxuikotlin.utils.FragmentState
import io.reactivex.Notification
import io.reactivex.subjects.ReplaySubject

@SuppressLint("CheckResult")
class CreateAndDestroyFragmentActivity : AppCompatActivity() {

    companion object {
        private const val FRAGMENT_1_TAG = "fragment1"
        private const val FRAGMENT_2_TAG = "fragment2"
    }

    val fragment1 get() = supportFragmentManager.findFragmentByTag(FRAGMENT_1_TAG)!!
    val fragment2 get() = supportFragmentManager.findFragmentByTag(FRAGMENT_2_TAG)!!
    val events = ReplaySubject.create<Notification<Pair<FragmentState, Fragment>>>()

    init {
        supportFragmentManager.rxFragmentsLifecycle(false)
            .materialize()
            .subscribe(events)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_holder)
        if (supportFragmentManager.findFragmentById(R.id.activity_fragment_holder_fragment_1) == null)
            supportFragmentManager.beginTransaction().replace(R.id.activity_fragment_holder_fragment_1, BlankFragment.builder(FRAGMENT_1_TAG), FRAGMENT_1_TAG).commit()
    }

    fun putIntoBackStackFragment2() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_fragment_holder_fragment_1, BlankFragment.builder(FRAGMENT_2_TAG), FRAGMENT_2_TAG)
            .addToBackStack(null)
            .commit()
    }

    fun popBackStack() = supportFragmentManager.popBackStack()
}