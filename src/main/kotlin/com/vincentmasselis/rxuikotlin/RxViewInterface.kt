package com.vincentmasselis.rxuikotlin

import android.support.v4.view.ViewCompat
import android.view.View
import com.vincentmasselis.rxuikotlin.utils.RxViewAttachListener
import com.vincentmasselis.rxuikotlin.utils.ViewLifecycleProvider
import com.vincentmasselis.rxuikotlin.utils.ViewState
import io.reactivex.subjects.BehaviorSubject

@Suppress("UNCHECKED_CAST")
private var View.tagLifecycleSubject: BehaviorSubject<ViewState>?
    get() = getTag(R.integer.tag_view_lifecycle_subject) as BehaviorSubject<ViewState>?
    set(value) = setTag(R.integer.tag_view_lifecycle_subject, value)

/**
 * Useful interface to easily create custom views that have [onAttach] and [onDetach] method to
 * simulate a lifecycle inside the view. This interface also provide a way to use the method
 * [io.reactivex.disposables.Disposable.disposeOnState(ViewState, ViewLifecycleProvider)] for your
 * custom views.
 *
 * To use, your must call `initLifecycle()` in the `init{}` code block and override the property
 * [view] in your custom view. Optionally, you can override [onAttach] and [onDetach] to have a
 * similar lifecycle to activities or fragments.
 * You must not override the method `initLifecycle()` and the property `lifecycleObs`, if you change
 * them, you will face to strange behaviors and bugs.
 */
interface RxViewInterface : ViewLifecycleProvider {

    val view: View

    /** Don't override this property ! */
    override val lifecycleObs get() = view.tagLifecycleSubject.let { it ?: throw IllegalStateException("You must call initLifecycle() before calling disposeOnState()") }

    /**
     * Don't override this method !
     *
     * Call me in the `init{}`
     */
    fun initLifecycle() {
        if (view.tagLifecycleSubject == null) view.tagLifecycleSubject = BehaviorSubject.create()
        else throw IllegalStateException("initLifecycle() is called twice, it must be called only once")

        view.addOnAttachStateChangeListener(RxViewAttachListener(this, lifecycleObs))

        if (ViewCompat.isAttachedToWindow(view)) //If the view is attached before the listener is added, I manually call the [onAttach] method.
            onAttach()
    }

    fun onAttach() {}

    fun onDetach() {}
}