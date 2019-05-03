package com.vincentmasselis.devapp

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

    @Test
    fun scroll() {
        val activity = adapterActivityRule.launchActivity(null)
        val viewHolders = activity.adapter.viewHolders.toList() // Create a list of the original viewHolders
        viewHolders.forEach { it.disposable()!!.checkNotDisposed() }
        Thread.sleep(500)
        activity.runOnUiThread { activity.recyclerView.smoothScrollToPosition(49) }
        Thread.sleep(1500)
        viewHolders
            // Some of the VHs are recycled, that mean the disposable is not disposed at this moment. I'm looking for VHs which are created but not recycled yet and verify that
            // they are correctly disposed. To do this, I only selected the VHs with a low position index.
            .filter { it.adapterPosition < viewHolders.size }
            .forEach { it.disposable()?.checkDisposed() }
        adapterActivityRule.finishActivity()
    }
}