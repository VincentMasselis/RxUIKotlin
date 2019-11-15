package com.vincentmasselis.devapp

import android.content.pm.ActivityInfo
import androidx.fragment.app.enableFragmentManagerDebugLogs
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.vincentmasselis.rxuikotlin.utils.ActivityState
import io.reactivex.Notification
import io.reactivex.Observable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RxViewCreatedFragmentsTest {

    init {
        enableFragmentManagerDebugLogs()
    }

    @get:Rule
    val activityRule = ActivityTestRule(RxViewCreatedFragmentActivity::class.java, true, false)

    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun subOnCreatePutFragmentOnCreate() {
        val activity = activityRule.launchActivity(RxViewCreatedFragmentActivity.intent(context, ActivityState.CREATE, ActivityState.CREATE))
        Thread.sleep(500)
        val fragment1 = activity.fragment1
        activityRule.finishActivity()
        Thread.sleep(500)
        activity.events
            .test()
            .assertValues(
                Notification.createOnNext(listOf()),
                Notification.createOnNext(listOf(fragment1))
            )
    }

    @Test
    fun subOnStartPutFragmentOnCreate() {
        val activity = activityRule.launchActivity(RxViewCreatedFragmentActivity.intent(context, ActivityState.START, ActivityState.CREATE))
        Thread.sleep(500)
        val fragment1 = activity.fragment1
        activityRule.finishActivity()
        Thread.sleep(500)
        activity.events
            .test()
            .assertValues(
                Notification.createOnNext(listOf(fragment1))
            )
    }

    @Test
    fun subOnResumePutFragmentOnCreate() {
        val activity = activityRule.launchActivity(RxViewCreatedFragmentActivity.intent(context, ActivityState.RESUME, ActivityState.CREATE))
        Thread.sleep(500)
        val fragment1 = activity.fragment1
        activityRule.finishActivity()
        Thread.sleep(500)
        activity.events
            .test()
            .assertValues(
                Notification.createOnNext(listOf(fragment1))
            )
    }

    @Test
    fun subOnCreatePutFragmentOnStart() {
        val activity = activityRule.launchActivity(RxViewCreatedFragmentActivity.intent(context, ActivityState.CREATE, ActivityState.START))
        Thread.sleep(500)
        val fragment1 = activity.fragment1
        activityRule.finishActivity()
        Thread.sleep(500)
        activity.events
            .test()
            .assertValues(
                Notification.createOnNext(listOf()),
                Notification.createOnNext(listOf(fragment1))
            )
    }

    @Test
    fun subOnCreatePutFragmentOnResume() {
        val activity = activityRule.launchActivity(RxViewCreatedFragmentActivity.intent(context, ActivityState.CREATE, ActivityState.RESUME))
        Thread.sleep(500)
        val fragment1 = activity.fragment1
        activityRule.finishActivity()
        Thread.sleep(500)
        activity.events
            .test()
            .assertValues(
                Notification.createOnNext(listOf()),
                Notification.createOnNext(listOf(fragment1))
            )
    }

    @Test
    fun subOnCreatePutFragmentOnCreateAndReplace() {
        val activity = activityRule.launchActivity(RxViewCreatedFragmentActivity.intent(context, ActivityState.CREATE, ActivityState.CREATE))
        Thread.sleep(500)
        val fragment1 = activity.fragment1
        activity.replaceByFragment2()
        Thread.sleep(500)
        val fragment2 = activity.fragment2
        activityRule.finishActivity()
        Thread.sleep(500)
        activity.events
            .test()
            .assertValues(
                Notification.createOnNext(listOf()),
                Notification.createOnNext(listOf(fragment1)),
                Notification.createOnNext(listOf()),
                Notification.createOnNext(listOf(fragment2))
            )
    }

    @Test
    fun subOnCreatePutFragmentOnCreatePlusPutBackStack() {
        val activity = activityRule.launchActivity(RxViewCreatedFragmentActivity.intent(context, ActivityState.CREATE, ActivityState.CREATE))
        Thread.sleep(500)
        val fragment1 = activity.fragment1
        activity.putIntoBackStackFragment2()
        Thread.sleep(500)
        val fragment2 = activity.fragment2
        activityRule.finishActivity()
        Thread.sleep(500)
        activity.events
            .test()
            .assertValues(
                Notification.createOnNext(listOf()),
                Notification.createOnNext(listOf(fragment1)),
                Notification.createOnNext(listOf()),
                Notification.createOnNext(listOf(fragment2))
            )
    }

    @Test
    fun subOnCreatePlusPutFragmentOnCreatePlusPutBackStackPlusOrientation() {
        val activityBeforeOrientation = activityRule.launchActivity(RxViewCreatedFragmentActivity.intent(context, ActivityState.CREATE, ActivityState.CREATE))
        Thread.sleep(500)
        val fragment1BeforeOrientation = activityBeforeOrientation.fragment1
        activityBeforeOrientation.putIntoBackStackFragment2()
        Thread.sleep(500)
        val fragment2BeforeOrientation = activityBeforeOrientation.fragment2
        activityBeforeOrientation.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        Thread.sleep(500)
        val activityAfterOrientation = activityRule.activity
        val fragment1AfterOrientation = activityAfterOrientation.fragment1
        val fragment2AfterOrientation = activityAfterOrientation.fragment2
        activityAfterOrientation.runOnUiThread { activityAfterOrientation.popBackStack() }
        Thread.sleep(500)
        activityRule.finishActivity()
        Thread.sleep(500)
        Observable.mergeArray(activityBeforeOrientation.events, activityAfterOrientation.events)
            .test()
            .assertValues(
                Notification.createOnNext(listOf()),
                Notification.createOnNext(listOf(fragment1BeforeOrientation)),
                Notification.createOnNext(listOf()),
                Notification.createOnNext(listOf(fragment2BeforeOrientation)),
                Notification.createOnNext(listOf()),
                Notification.createOnNext(listOf(fragment2AfterOrientation)),
                Notification.createOnNext(listOf()),
                Notification.createOnNext(listOf(fragment1AfterOrientation))
            )
    }

    @Test
    fun subOnStartPlusPutFragmentOnCreatePlusPutBackStackPlusOrientation() {
        val activityBeforeOrientation = activityRule.launchActivity(RxViewCreatedFragmentActivity.intent(context, ActivityState.START, ActivityState.CREATE))
        Thread.sleep(500)
        val fragment1BeforeOrientation = activityBeforeOrientation.fragment1
        activityBeforeOrientation.putIntoBackStackFragment2()
        Thread.sleep(500)
        val fragment2BeforeOrientation = activityBeforeOrientation.fragment2
        activityBeforeOrientation.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        Thread.sleep(500)
        val activityAfterOrientation = activityRule.activity
        val fragment1AfterOrientation = activityAfterOrientation.fragment1
        val fragment2AfterOrientation = activityAfterOrientation.fragment2
        activityAfterOrientation.runOnUiThread { activityAfterOrientation.popBackStack() }
        Thread.sleep(500)
        activityRule.finishActivity()
        Thread.sleep(500)
        Observable.mergeArray(activityBeforeOrientation.events, activityAfterOrientation.events)
            .test()
            .assertValues(
                Notification.createOnNext(listOf(fragment1BeforeOrientation)),
                Notification.createOnNext(listOf()),
                Notification.createOnNext(listOf(fragment2BeforeOrientation)),
                Notification.createOnNext(listOf(fragment2AfterOrientation)),
                Notification.createOnNext(listOf()),
                Notification.createOnNext(listOf(fragment1AfterOrientation))
            )
    }
}
