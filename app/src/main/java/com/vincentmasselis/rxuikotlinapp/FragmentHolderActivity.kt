package com.vincentmasselis.rxuikotlinapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vincentmasselis.rxuikotlin.disposeOnState
import com.vincentmasselis.rxuikotlin.fragmentmanager.rxFragments
import com.vincentmasselis.rxuikotlin.utils.ActivityState
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

@SuppressLint("CheckResult")
class FragmentHolderActivity : AppCompatActivity() {

    var fragmentsFromRx = setOf<Fragment>()
        set(value) {
            field = value
            fragmentsFromRxSubject.onNext(value)
        }

    val fragmentsFromRxSubject = PublishSubject.create<Set<Fragment>>()

    val fragment1 by lazy { BlankFragment() }
    val fragment2 by lazy { BlankFragment() }
    val fragment3 by lazy { BlankFragment() }

    val fragments by lazy { listOf(fragment1, fragment2, fragment3) }

    init {
        supportFragmentManager.rxFragments()
            .subscribe { fragmentsFromRx = it }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fragment_holder)

        currentSituation!!.subscribe(Consumer {
            supportFragmentManager.beginTransaction().replace(R.id.activity_fragment_holder_fragment_1, fragment1).commit()
            supportFragmentManager.beginTransaction().replace(R.id.activity_fragment_holder_fragment_2, fragment2).commit()
            supportFragmentManager.beginTransaction().replace(R.id.activity_fragment_holder_fragment_3, fragment3).commit()
        }).disposeOnState(ActivityState.DESTROY, this)
    }

    fun dropFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().remove(fragment).commit()
    }
}