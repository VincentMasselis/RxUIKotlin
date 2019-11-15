package com.vincentmasselis.devapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vincentmasselis.rxuikotlin.disposeOnState
import com.vincentmasselis.rxuikotlin.fragmentmanager.rxCreatedFragments
import com.vincentmasselis.rxuikotlin.fragmentmanager.rxCreatedFragments
import com.vincentmasselis.rxuikotlin.utils.ActivityState
import io.reactivex.Notification
import io.reactivex.subjects.ReplaySubject

@SuppressLint("CheckResult")
class RxCreatedFragmentActivity : AppCompatActivity() {

    companion object {
        private const val FRAGMENT_1_TAG = "fragment1"
        private const val FRAGMENT_2_TAG = "fragment2"
        private const val PARAM_STATE_TO_SUBSCRIBE = "PARAM_STATE_TO_SUBSCRIBE"
        private const val PARAM_STATE_TO_PUT_FRAGMENT = "PARAM_STATE_TO_PUT_FRAGMENT"
        fun intent(context: Context, stateToSubscribe: ActivityState, stateToPutFragment: ActivityState) = Intent(context, RxCreatedFragmentActivity::class.java)
            .putExtra(PARAM_STATE_TO_SUBSCRIBE, stateToSubscribe)
            .putExtra(PARAM_STATE_TO_PUT_FRAGMENT, stateToPutFragment)
    }

    val fragment1 get() = supportFragmentManager.findFragmentByTag(FRAGMENT_1_TAG)!!
    val fragment2 get() = supportFragmentManager.findFragmentByTag(FRAGMENT_2_TAG)!!
    val events = ReplaySubject.create<Notification<Set<Fragment>>>()

    private val stateToSubscribe by lazy { intent.getSerializableExtra(PARAM_STATE_TO_SUBSCRIBE) as ActivityState }
    private val stateToPutFragment by lazy { intent.getSerializableExtra(PARAM_STATE_TO_PUT_FRAGMENT) as ActivityState }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_holder)

        if (stateToSubscribe == ActivityState.CREATE) supportFragmentManager
            .rxCreatedFragments()
            .materialize()
            .subscribe { events.onNext(it) }
            .disposeOnState(ActivityState.DESTROY, this)

        if (stateToPutFragment == ActivityState.CREATE && supportFragmentManager.findFragmentById(R.id.activity_fragment_holder_fragment_1) == null)
            supportFragmentManager.beginTransaction().replace(R.id.activity_fragment_holder_fragment_1, BlankFragment.builder(FRAGMENT_1_TAG), FRAGMENT_1_TAG).commit()
    }

    override fun onStart() {
        super.onStart()
        if (stateToSubscribe == ActivityState.START) supportFragmentManager
            .rxCreatedFragments()
            .materialize()
            .subscribe { events.onNext(it) }
            .disposeOnState(ActivityState.STOP, this)

        if (stateToPutFragment == ActivityState.START && supportFragmentManager.findFragmentById(R.id.activity_fragment_holder_fragment_1) == null)
            supportFragmentManager.beginTransaction().replace(R.id.activity_fragment_holder_fragment_1, BlankFragment.builder(FRAGMENT_1_TAG), FRAGMENT_1_TAG).commit()
    }

    override fun onResume() {
        super.onResume()
        if (stateToSubscribe == ActivityState.RESUME) supportFragmentManager
            .rxCreatedFragments()
            .materialize()
            .subscribe { events.onNext(it) }
            .disposeOnState(ActivityState.PAUSE, this)

        if (stateToPutFragment == ActivityState.RESUME && supportFragmentManager.findFragmentById(R.id.activity_fragment_holder_fragment_1) == null)
            supportFragmentManager.beginTransaction().replace(R.id.activity_fragment_holder_fragment_1, BlankFragment.builder(FRAGMENT_1_TAG), FRAGMENT_1_TAG).commit()
    }

    fun replaceByFragment2() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_fragment_holder_fragment_1, BlankFragment.builder(FRAGMENT_2_TAG), FRAGMENT_2_TAG)
            .commit()
    }

    fun putIntoBackStackFragment2() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_fragment_holder_fragment_1, BlankFragment.builder(FRAGMENT_2_TAG), FRAGMENT_2_TAG)
            .addToBackStack(null)
            .commit()
    }

    fun popBackStack() = supportFragmentManager.popBackStack()
}