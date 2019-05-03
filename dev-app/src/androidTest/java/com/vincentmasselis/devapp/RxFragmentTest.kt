package com.vincentmasselis.devapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.vincentmasselis.rxuikotlin.fragmentmanager.rxFragmentsLifecycle
import com.vincentmasselis.rxuikotlin.utils.FragmentState
import io.reactivex.Completable
import io.reactivex.subjects.PublishSubject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class RxFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(FragmentHolderActivity::class.java, true, false)

    @Test
    fun createAndDestroyImmediateAndFast() {
        fun checkActivity() {
            val activity = activityRule.launchActivity(null)
            check(activity.fragmentsFromRx.size == 3)
            activityRule.finishActivity()
            Thread.sleep(1000)
            check(activity.fragmentsFromRx.isEmpty())
        }
        listOf(
            Situations.Singles.Immediate,
            Situations.Maybes.Immediate,
            Situations.Observables.Immediate,
            Situations.Singles.Fast,
            Situations.Maybes.Fast,
            Situations.Observables.Fast
        ).forEach {
            currentSituation = it
            checkActivity()
        }
    }

    @Test
    fun createAndDestroyRegular() {
        fun checkActivity() {
            val activity = activityRule.launchActivity(null)
            check(activity.fragmentsFromRx.isEmpty())
            Thread.sleep(1500)
            check(activity.fragmentsFromRx.size == 3)
            activityRule.finishActivity()
        }
        listOf(Situations.Singles.Regular, Situations.Maybes.Regular, Situations.Observables.Regular).forEach {
            currentSituation = it
            checkActivity()
        }
    }

    @Test
    fun dropFragment() {
        currentSituation = Situations.Singles.Immediate
        val activity = activityRule.launchActivity(null)
        check(activity.fragmentsFromRx.isNotEmpty())
        check(activity.fragmentsFromRxSubject
            .doOnSubscribe { activity.dropFragment(activity.fragment1) }
            .filter { fragments -> fragments.none { it === activity.fragment1 } }
            .timeout(1, TimeUnit.SECONDS)
            .blockingFirst() != null)
        activityRule.finishActivity()
    }

    @Test
    fun checkAttachment() {
        currentSituation = Situations.Singles.Regular
        val activity = activityRule.launchActivity(null)
        check(activity.fragmentsFromRx.isEmpty())
        val closeSubject = PublishSubject.create<Unit>()
        val fragmentEvents = activity.supportFragmentManager.rxFragmentsLifecycle(false)
            .doOnSubscribe { Completable.timer(2, TimeUnit.SECONDS).subscribe { closeSubject.onNext(Unit) } }
            .takeUntil(closeSubject)
            .blockingIterable()
            .toList()

        check(fragmentEvents.filter { it.first == FragmentState.ATTACH }.count() == 3)
        check(fragmentEvents.filter { it.first == FragmentState.RESUME }.count() == 3)
        check(fragmentEvents.size == 8 * 3) // Each fragment fire 8 events when attached to an activity
        activityRule.finishActivity()
    }

    @Test
    fun checkAttachmentAndDetachment() {
        currentSituation = Situations.Singles.Regular
        val activity = activityRule.launchActivity(null)
        check(activity.fragmentsFromRx.isEmpty())
        val closeSubject = PublishSubject.create<Unit>()
        val fragmentEvents1 = activity.supportFragmentManager.rxFragmentsLifecycle(false)
            .doOnSubscribe { Completable.timer(1, TimeUnit.SECONDS).subscribe { closeSubject.onNext(Unit) } }
            .takeUntil(closeSubject)
            .blockingIterable()
            .toList()

        check(fragmentEvents1.filter { it.first == FragmentState.ATTACH }.count() == activity.fragments.size)
        check(fragmentEvents1.filter { it.first == FragmentState.RESUME }.count() == activity.fragments.size)
        check(fragmentEvents1.size == 8 * activity.fragments.size) // Each fragment fire 8 events when attached to an activity

        val fragmentEvents2 = activity.supportFragmentManager.rxFragmentsLifecycle(false)
            .doOnSubscribe {
                Completable.timer(500, TimeUnit.MILLISECONDS).subscribe {
                    activity.fragments.forEach { activity.dropFragment(it) }
                }
                Completable.timer(1, TimeUnit.SECONDS).subscribe { closeSubject.onNext(Unit) }
            }
            .takeUntil(closeSubject)
            .blockingIterable()
            .toList()

        check(fragmentEvents2.filter { it.first == FragmentState.PAUSE }.count() == activity.fragments.size)
        check(fragmentEvents2.filter { it.first == FragmentState.DETACH }.count() == activity.fragments.size)
        check(fragmentEvents2.size == 5 * activity.fragments.size) // Each fragment fire 5 events when detached to an activity

        activityRule.finishActivity()
    }
}