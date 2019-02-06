package com.vincentmasselis.rxuikotlinapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewHolderTest {

    @get:Rule
    val adapterActivityRule = ActivityTestRule(AdapterActivity::class.java, true, false)

    @get:Rule
    val secondActivityRule = ActivityTestRule(PlaceholderActivity::class.java, true, false)

    @Test
    fun createDestroy() {
        val activity = adapterActivityRule.launchActivity(null)
        val vhs = activity.adapter.viewHolders
        vhs.forEach { it.disposable()!!.checkNotDisposed() }
        adapterActivityRule.finishActivity()
        Thread.sleep(500)
        vhs.forEach { it.disposable()?.checkDisposed() }
    }
}