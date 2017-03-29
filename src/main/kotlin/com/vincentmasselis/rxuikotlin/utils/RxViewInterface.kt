package com.vincentmasselis.rxuikotlin.utils

import android.view.View
import com.vincentmasselis.rxuikotlin.R
import io.reactivex.subjects.BehaviorSubject

@Suppress("UNCHECKED_CAST")
private var View.tagLifecycleSubject: BehaviorSubject<ViewState>?
    get() = getTag(R.integer.tag_view_lifecycle_subject) as BehaviorSubject<ViewState>?
    set(value) = setTag(R.integer.tag_view_lifecycle_subject, value)

interface RxViewInterface : ViewLifecycleProvider {

    val view: View
    /** Don't override this property */
    override val lifecycleObs get() = view.tagLifecycleSubject.let { it ?: throw IllegalStateException("You must call initLifecycle() before calling disposeOnState()") }

    /** Don't override this method */
    fun initLifecycle() {
        if (view.tagLifecycleSubject == null) view.tagLifecycleSubject = BehaviorSubject.create()
        else throw IllegalStateException("initLifecycle() is called twice, it must be called only once")

        view.addOnAttachStateChangeListener(RxViewAttachListener(this, lifecycleObs))
    }

    fun onAttach() {}

    fun onDetach() {}
}