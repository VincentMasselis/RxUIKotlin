package com.vincentmasselis.devapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityTest {

    @get:Rule
    val mainActivityRule = ActivityTestRule(MainActivity::class.java, true, false)

    @get:Rule
    val secondActivityRule = ActivityTestRule(PlaceholderActivity::class.java, true, false)

    @Test
    fun immediate() {
        fun checkActivity() {
            val activity = mainActivityRule.launchActivity(null)
            activity.createDisposable.checkDisposed()
            activity.resumeDisposable.checkDisposed()
            mainActivityRule.finishActivity()
        }

        listOf(Situations.Singles.Immediate, Situations.Maybes.Immediate, Situations.Observables.Immediate).forEach {
            currentSituation = it
            checkActivity()
        }
    }

    @Test
    fun fast() {
        fun checkActivity() {
            val activity = mainActivityRule.launchActivity(null)
            mainActivityRule.finishActivity()
            activity.createDisposable.checkDisposed()
            activity.resumeDisposable.checkDisposed()
        }

        listOf(Situations.Singles.Fast, Situations.Maybes.Fast, Situations.Observables.Fast).forEach {
            currentSituation = it
            checkActivity()
        }
    }

    @Test
    fun regular() {
        fun checkActivity() {
            val activity = mainActivityRule.launchActivity(null)
            activity.createDisposable.checkNotDisposed()
            activity.resumeDisposable.checkNotDisposed()
            mainActivityRule.finishActivity()
            Thread.sleep(500)
            activity.createDisposable.checkDisposed()
            activity.resumeDisposable.checkDisposed()
        }

        listOf(Situations.Singles.Regular, Situations.Maybes.Regular, Situations.Observables.Regular).forEach {
            currentSituation = it
            checkActivity()
        }
    }

    @Test
    fun slow() {
        fun checkActivity() {
            val activity = mainActivityRule.launchActivity(null)
            activity.createDisposable.checkNotDisposed()
            activity.resumeDisposable.checkNotDisposed()
            mainActivityRule.finishActivity()
            Thread.sleep(500)
            activity.createDisposable.checkDisposed()
            activity.resumeDisposable.checkDisposed()
        }

        listOf(Situations.Singles.Slow, Situations.Maybes.Slow, Situations.Observables.Slow).forEach {
            currentSituation = it
            checkActivity()
        }
    }

    @Test
    fun resumePause() {
        fun checkActivity() {
            val activity = mainActivityRule.launchActivity(null)
            activity.resumeDisposable.checkNotDisposed()
            secondActivityRule.launchActivity(null)
            activity.resumeDisposable.checkDisposed()
            secondActivityRule.finishActivity()
            activity.resumeDisposable.checkNotDisposed()
            mainActivityRule.finishActivity()
            activity.resumeDisposable.checkDisposed()
        }

        listOf(Situations.Singles.Slow, Situations.Maybes.Slow, Situations.Observables.Slow).forEach {
            currentSituation = it
            checkActivity()
        }
    }
}
