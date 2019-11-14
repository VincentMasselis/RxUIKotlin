package com.vincentmasselis.devapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vincentmasselis.rxuikotlin.disposeOnState
import com.vincentmasselis.rxuikotlin.fragmentmanager.rxFragmentList
import com.vincentmasselis.rxuikotlin.utils.ActivityState
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

@SuppressLint("CheckResult")
class FragmentHolderActivity : AppCompatActivity() {

    var fragmentsFromRx = listOf<Fragment>()
        set(value) {
            field = value
            fragmentsFromRxSubject.onNext(value)
        }

    val fragmentsFromRxSubject = PublishSubject.create<List<Fragment>>()

    val fragment1 by lazy { BlankFragment.builder("fragment1") }
    val fragment2 by lazy { BlankFragment.builder("fragment2") }
    val fragment3 by lazy { BlankFragment.builder("fragment3") }

    val fragments by lazy { listOf(fragment1, fragment2, fragment3) }

    init {
        supportFragmentManager.rxFragmentList()
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