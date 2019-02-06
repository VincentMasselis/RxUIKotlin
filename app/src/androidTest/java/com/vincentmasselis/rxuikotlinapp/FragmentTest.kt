package com.vincentmasselis.rxuikotlinapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FragmentTest {

    @get:Rule
    val containerActivityRule = ActivityTestRule(MainFragmentContainerActivity::class.java, true, false)

    @get:Rule
    val secondActivityRule = ActivityTestRule(PlaceholderActivity::class.java, true, false)

    @Test
    fun immediate() {
        fun checkActivity() {
            val activity = containerActivityRule.launchActivity(null)
            activity.fragment.createDisposable.checkDisposed()
            activity.fragment.viewCreatedDisposable.checkDisposed()
            activity.fragment.resumeDisposable.checkDisposed()
            containerActivityRule.finishActivity()
        }

        listOf(Situations.Singles.Immediate, Situations.Maybes.Immediate, Situations.Observables.Immediate).forEach {
            currentSituation = it
            checkActivity()
        }
    }

    @Test
    fun fast() {
        fun checkActivity() {
            val activity = containerActivityRule.launchActivity(null)
            containerActivityRule.finishActivity()
            activity.fragment.createDisposable.checkDisposed()
            activity.fragment.viewCreatedDisposable.checkDisposed()
            activity.fragment.resumeDisposable.checkDisposed()
        }

        listOf(Situations.Singles.Fast, Situations.Maybes.Fast, Situations.Observables.Fast).forEach {
            currentSituation = it
            checkActivity()
        }
    }

    @Test
    fun regular() {
        fun checkActivity() {
            val activity = containerActivityRule.launchActivity(null)
            activity.fragment.createDisposable.checkNotDisposed()
            activity.fragment.viewCreatedDisposable.checkNotDisposed()
            activity.fragment.resumeDisposable.checkNotDisposed()
            containerActivityRule.finishActivity()
            activity.fragment.createDisposable.checkDisposed()
            activity.fragment.viewCreatedDisposable.checkDisposed()
            activity.fragment.resumeDisposable.checkDisposed()
        }

        listOf(Situations.Singles.Regular, Situations.Maybes.Regular, Situations.Observables.Regular).forEach {
            currentSituation = it
            checkActivity()
        }
    }

    @Test
    fun slow() {
        fun checkActivity() {
            val activity = containerActivityRule.launchActivity(null)
            activity.fragment.createDisposable.checkNotDisposed()
            activity.fragment.viewCreatedDisposable.checkNotDisposed()
            activity.fragment.resumeDisposable.checkNotDisposed()
            containerActivityRule.finishActivity()
            activity.fragment.createDisposable.checkDisposed()
            activity.fragment.viewCreatedDisposable.checkDisposed()
            activity.fragment.resumeDisposable.checkDisposed()
        }

        listOf(Situations.Singles.Slow, Situations.Maybes.Slow, Situations.Observables.Slow).forEach {
            currentSituation = it
            checkActivity()
        }
    }

    @Test
    fun resumePause() {
        fun checkActivity() {
            val activity = containerActivityRule.launchActivity(null)
            activity.fragment.resumeDisposable.checkNotDisposed()
            secondActivityRule.launchActivity(null)
            activity.fragment.resumeDisposable.checkDisposed()
            secondActivityRule.finishActivity()
            activity.fragment.resumeDisposable.checkNotDisposed()
            containerActivityRule.finishActivity()
            activity.fragment.resumeDisposable.checkDisposed()
        }

        listOf(Situations.Singles.Slow, Situations.Maybes.Slow, Situations.Observables.Slow).forEach {
            currentSituation = it
            checkActivity()
        }
    }

}
