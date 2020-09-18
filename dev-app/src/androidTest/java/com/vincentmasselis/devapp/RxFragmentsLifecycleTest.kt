package com.vincentmasselis.devapp

import android.content.pm.ActivityInfo
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.enableFragmentManagerDebugLogs
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.vincentmasselis.rxuikotlin.utils.FragmentState
import io.reactivex.rxjava3.core.Notification
import io.reactivex.rxjava3.core.Observable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RxFragmentsLifecycleTest {

    init {
        enableFragmentManagerDebugLogs()
    }

    private fun FragmentActivity.fragmentCount() = supportFragmentManager.fragments.size

    @get:Rule
    val activityRule = ActivityTestRule(RxFragmentsLifecycleActivity::class.java, true, false)

    @Test
    fun createAndDestroy() {
        val activity = activityRule.launchActivity(null)
        Thread.sleep(1000)
        check(activity.fragmentCount() == 1)
        val fragment1 = activity.fragment1
        activity.finish()
        Thread.sleep(1000)
        check(activity.fragmentCount() == 1)
        activity.events
                .test()
                .assertValues(
                        // Creating activity
                        Notification.createOnNext(FragmentState.PRE_ATTACH to fragment1),
                        Notification.createOnNext(FragmentState.ATTACH to fragment1),
                        Notification.createOnNext(FragmentState.PRE_CREATE to fragment1),
                        Notification.createOnNext(FragmentState.CREATE to fragment1),
                        Notification.createOnNext(FragmentState.VIEW_CREATED to fragment1),
                        Notification.createOnNext(FragmentState.ACTIVITY_CREATED to fragment1),
                        Notification.createOnNext(FragmentState.START to fragment1),
                        Notification.createOnNext(FragmentState.RESUME to fragment1),
                        // 1000 ms pause
                        // Destroying activity
                        Notification.createOnNext(FragmentState.PAUSE to fragment1),
                        Notification.createOnNext(FragmentState.STOP to fragment1),
                        Notification.createOnNext(FragmentState.DESTROY_VIEW to fragment1),
                        Notification.createOnNext(FragmentState.DESTROY to fragment1),
                        Notification.createOnNext(FragmentState.DETACH to fragment1)
                )
    }

    @Test
    fun createAndDestroyPlusBackStack() {
        val activity = activityRule.launchActivity(null)
        Thread.sleep(1000)
        check(activity.fragmentCount() == 1)
        val fragment1 = activity.fragment1
        activity.putIntoBackStackFragment2()
        Thread.sleep(1000)
        check(activity.fragmentCount() == 1)
        val fragment2 = activity.fragment2
        activity.finish()
        Thread.sleep(1000)
        check(activity.fragmentCount() == 1)
        activity.events
                .test()
                .assertValues(
                        // Creating activity
                        Notification.createOnNext(FragmentState.PRE_ATTACH to fragment1),
                        Notification.createOnNext(FragmentState.ATTACH to fragment1),
                        Notification.createOnNext(FragmentState.PRE_CREATE to fragment1),
                        Notification.createOnNext(FragmentState.CREATE to fragment1),
                        Notification.createOnNext(FragmentState.VIEW_CREATED to fragment1),
                        Notification.createOnNext(FragmentState.ACTIVITY_CREATED to fragment1),
                        Notification.createOnNext(FragmentState.START to fragment1),
                        Notification.createOnNext(FragmentState.RESUME to fragment1),
                        // 1000 ms pause
                        // Add to backstack fragment2
                        Notification.createOnNext(FragmentState.PRE_ATTACH to fragment2),
                        Notification.createOnNext(FragmentState.ATTACH to fragment2),
                        Notification.createOnNext(FragmentState.PRE_CREATE to fragment2),
                        Notification.createOnNext(FragmentState.CREATE to fragment2),
                        // Hiding fragment1
                        Notification.createOnNext(FragmentState.PAUSE to fragment1),
                        Notification.createOnNext(FragmentState.STOP to fragment1),
                        Notification.createOnNext(FragmentState.DESTROY_VIEW to fragment1),
                        // Finishing creating of fragment2
                        Notification.createOnNext(FragmentState.VIEW_CREATED to fragment2),
                        Notification.createOnNext(FragmentState.ACTIVITY_CREATED to fragment2),
                        Notification.createOnNext(FragmentState.START to fragment2),
                        Notification.createOnNext(FragmentState.RESUME to fragment2),
                        // 1000 ms pause
                        // Destroying activity starting from fragment2
                        Notification.createOnNext(FragmentState.PAUSE to fragment2),
                        Notification.createOnNext(FragmentState.STOP to fragment2),
                        Notification.createOnNext(FragmentState.DESTROY_VIEW to fragment2),
                        Notification.createOnNext(FragmentState.DESTROY to fragment2),
                        Notification.createOnNext(FragmentState.DETACH to fragment2),
                        // destroying fragment1
                        Notification.createOnNext(FragmentState.DESTROY to fragment1),
                        Notification.createOnNext(FragmentState.DETACH to fragment1)
                )
    }

    @Test
    fun createAndDestroyPlusBackStackPlusPop() {
        val activity = activityRule.launchActivity(null)
        Thread.sleep(1000)
        check(activity.fragmentCount() == 1)
        val fragment1 = activity.fragment1
        activity.putIntoBackStackFragment2()
        Thread.sleep(1000)
        check(activity.fragmentCount() == 1)
        val fragment2 = activity.fragment2
        activity.runOnUiThread { activity.popBackStack() }
        Thread.sleep(1000)
        check(activity.fragmentCount() == 1)
        activity.finish()
        Thread.sleep(1000)
        check(activity.fragmentCount() == 1)
        activity.events
                .test()
                .assertValues(
                        // Base activity with 1 fragment
                        Notification.createOnNext(FragmentState.PRE_ATTACH to fragment1),
                        Notification.createOnNext(FragmentState.ATTACH to fragment1),
                        Notification.createOnNext(FragmentState.PRE_CREATE to fragment1),
                        Notification.createOnNext(FragmentState.CREATE to fragment1),
                        Notification.createOnNext(FragmentState.VIEW_CREATED to fragment1),
                        Notification.createOnNext(FragmentState.ACTIVITY_CREATED to fragment1),
                        Notification.createOnNext(FragmentState.START to fragment1),
                        Notification.createOnNext(FragmentState.RESUME to fragment1),
                        // 1000 ms pause
                        // Push to back stack fragment2
                        Notification.createOnNext(FragmentState.PRE_ATTACH to fragment2),
                        Notification.createOnNext(FragmentState.ATTACH to fragment2),
                        Notification.createOnNext(FragmentState.PRE_CREATE to fragment2),
                        Notification.createOnNext(FragmentState.CREATE to fragment2),
                        // Hiding fragment1
                        Notification.createOnNext(FragmentState.PAUSE to fragment1),
                        Notification.createOnNext(FragmentState.STOP to fragment1),
                        Notification.createOnNext(FragmentState.DESTROY_VIEW to fragment1),
                        // Finishing creating of fragment2
                        Notification.createOnNext(FragmentState.VIEW_CREATED to fragment2),
                        Notification.createOnNext(FragmentState.ACTIVITY_CREATED to fragment2),
                        Notification.createOnNext(FragmentState.START to fragment2),
                        Notification.createOnNext(FragmentState.RESUME to fragment2),
                        // 1000 ms pause
                        // Pop fragment2
                        Notification.createOnNext(FragmentState.PAUSE to fragment2),
                        Notification.createOnNext(FragmentState.STOP to fragment2),
                        Notification.createOnNext(FragmentState.DESTROY_VIEW to fragment2),
                        Notification.createOnNext(FragmentState.DESTROY to fragment2),
                        Notification.createOnNext(FragmentState.DETACH to fragment2),
                        // Showing again fragment1
                        Notification.createOnNext(FragmentState.VIEW_CREATED to fragment1),
                        Notification.createOnNext(FragmentState.ACTIVITY_CREATED to fragment1),
                        Notification.createOnNext(FragmentState.START to fragment1),
                        Notification.createOnNext(FragmentState.RESUME to fragment1),
                        // 1000 ms pause
                        // Destroying activity
                        Notification.createOnNext(FragmentState.PAUSE to fragment1),
                        Notification.createOnNext(FragmentState.STOP to fragment1),
                        Notification.createOnNext(FragmentState.DESTROY_VIEW to fragment1),
                        Notification.createOnNext(FragmentState.DESTROY to fragment1),
                        Notification.createOnNext(FragmentState.DETACH to fragment1)
                )
    }

    @Test
    fun createAndDestroyPlusBackStackPlusOrientationPlusPop() {
        val activityBeforeOrientation = activityRule.launchActivity(null)
        Thread.sleep(1000)
        check(activityBeforeOrientation.fragmentCount() == 1)
        val beforeOrientationFragment1 = activityBeforeOrientation.fragment1
        activityBeforeOrientation.putIntoBackStackFragment2()
        Thread.sleep(1000)
        check(activityBeforeOrientation.fragmentCount() == 1)
        val beforeOrientationFragment2 = activityBeforeOrientation.fragment2
        activityBeforeOrientation.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        Thread.sleep(1000)
        check(activityBeforeOrientation.fragmentCount() == 1)
        val activityAfterOrientation = activityRule.activity
        val afterOrientationFragment1 = activityAfterOrientation.fragment1
        val afterOrientationFragment2 = activityAfterOrientation.fragment2
        activityAfterOrientation.runOnUiThread { activityAfterOrientation.popBackStack() }
        Thread.sleep(1000)
        check(activityBeforeOrientation.fragmentCount() == 1)
        activityAfterOrientation.finish()
        Thread.sleep(1000)
        check(activityBeforeOrientation.fragmentCount() == 1)
        Observable.mergeArray(activityBeforeOrientation.events, activityAfterOrientation.events)
                .test()
                .assertValues(
                        // Base activity with 1 fragment
                        Notification.createOnNext(FragmentState.PRE_ATTACH to beforeOrientationFragment1),
                        Notification.createOnNext(FragmentState.ATTACH to beforeOrientationFragment1),
                        Notification.createOnNext(FragmentState.PRE_CREATE to beforeOrientationFragment1),
                        Notification.createOnNext(FragmentState.CREATE to beforeOrientationFragment1),
                        Notification.createOnNext(FragmentState.VIEW_CREATED to beforeOrientationFragment1),
                        Notification.createOnNext(FragmentState.ACTIVITY_CREATED to beforeOrientationFragment1),
                        Notification.createOnNext(FragmentState.START to beforeOrientationFragment1),
                        Notification.createOnNext(FragmentState.RESUME to beforeOrientationFragment1),
                        // 1000 ms pause
                        // Push to back stack fragment2
                        Notification.createOnNext(FragmentState.PRE_ATTACH to beforeOrientationFragment2),
                        Notification.createOnNext(FragmentState.ATTACH to beforeOrientationFragment2),
                        Notification.createOnNext(FragmentState.PRE_CREATE to beforeOrientationFragment2),
                        Notification.createOnNext(FragmentState.CREATE to beforeOrientationFragment2),
                        // Hiding fragment1
                        Notification.createOnNext(FragmentState.PAUSE to beforeOrientationFragment1),
                        Notification.createOnNext(FragmentState.STOP to beforeOrientationFragment1),
                        Notification.createOnNext(FragmentState.DESTROY_VIEW to beforeOrientationFragment1),
                        // Finishing creating of fragment2
                        Notification.createOnNext(FragmentState.VIEW_CREATED to beforeOrientationFragment2),
                        Notification.createOnNext(FragmentState.ACTIVITY_CREATED to beforeOrientationFragment2),
                        Notification.createOnNext(FragmentState.START to beforeOrientationFragment2),
                        Notification.createOnNext(FragmentState.RESUME to beforeOrientationFragment2),
                        // 1000 ms pause
                        // Screen orientation change destroying activity
                        Notification.createOnNext(FragmentState.PAUSE to beforeOrientationFragment2),
                        Notification.createOnNext(FragmentState.STOP to beforeOrientationFragment2),
                        Notification.createOnNext(FragmentState.SAVE_INSTANCE_STATE to beforeOrientationFragment1),
                        Notification.createOnNext(FragmentState.SAVE_INSTANCE_STATE to beforeOrientationFragment2),
                        Notification.createOnNext(FragmentState.DESTROY_VIEW to beforeOrientationFragment2),
                        Notification.createOnNext(FragmentState.DESTROY to beforeOrientationFragment2),
                        Notification.createOnNext(FragmentState.DETACH to beforeOrientationFragment2),
                        Notification.createOnNext(FragmentState.DESTROY to beforeOrientationFragment1),
                        Notification.createOnNext(FragmentState.DETACH to beforeOrientationFragment1),
                        // Screen orientation change re-creating activity
                        Notification.createOnNext(FragmentState.PRE_ATTACH to afterOrientationFragment2),
                        Notification.createOnNext(FragmentState.ATTACH to afterOrientationFragment2),
                        Notification.createOnNext(FragmentState.PRE_CREATE to afterOrientationFragment2),
                        Notification.createOnNext(FragmentState.CREATE to afterOrientationFragment2),
                        Notification.createOnNext(FragmentState.PRE_ATTACH to afterOrientationFragment1),
                        Notification.createOnNext(FragmentState.ATTACH to afterOrientationFragment1),
                        Notification.createOnNext(FragmentState.PRE_CREATE to afterOrientationFragment1),
                        Notification.createOnNext(FragmentState.CREATE to afterOrientationFragment1),
                        Notification.createOnNext(FragmentState.VIEW_CREATED to afterOrientationFragment2),
                        Notification.createOnNext(FragmentState.ACTIVITY_CREATED to afterOrientationFragment2),
                        Notification.createOnNext(FragmentState.START to afterOrientationFragment2),
                        Notification.createOnNext(FragmentState.RESUME to afterOrientationFragment2),
                        // 1000 ms pause
                        // Pop fragment2
                        Notification.createOnNext(FragmentState.PAUSE to afterOrientationFragment2),
                        Notification.createOnNext(FragmentState.STOP to afterOrientationFragment2),
                        Notification.createOnNext(FragmentState.DESTROY_VIEW to afterOrientationFragment2),
                        Notification.createOnNext(FragmentState.DESTROY to afterOrientationFragment2),
                        Notification.createOnNext(FragmentState.DETACH to afterOrientationFragment2),
                        // Showing again fragment1
                        Notification.createOnNext(FragmentState.VIEW_CREATED to afterOrientationFragment1),
                        Notification.createOnNext(FragmentState.ACTIVITY_CREATED to afterOrientationFragment1),
                        Notification.createOnNext(FragmentState.START to afterOrientationFragment1),
                        Notification.createOnNext(FragmentState.RESUME to afterOrientationFragment1),
                        // 1000 ms pause
                        // Destroying activity
                        Notification.createOnNext(FragmentState.PAUSE to afterOrientationFragment1),
                        Notification.createOnNext(FragmentState.STOP to afterOrientationFragment1),
                        Notification.createOnNext(FragmentState.DESTROY_VIEW to afterOrientationFragment1),
                        Notification.createOnNext(FragmentState.DESTROY to afterOrientationFragment1),
                        Notification.createOnNext(FragmentState.DETACH to afterOrientationFragment1)
                )
    }
}
