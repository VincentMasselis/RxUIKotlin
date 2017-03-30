package com.vincentmasselis.rxuikotlin.utils

import android.view.View
import com.vincentmasselis.rxuikotlin.RxViewInterface
import io.reactivex.subjects.Subject
import java.lang.ref.WeakReference

internal class RxViewAttachListener(viewWithLifecycle: RxViewInterface, subject: Subject<ViewState>) : View.OnAttachStateChangeListener {

    private val viewWithLifecycle = WeakReference(viewWithLifecycle)
    private val subject = WeakReference(subject)

    override fun onViewAttachedToWindow(v: View?) {
        subject.get()?.onNext(ViewState.ATTACH)
        viewWithLifecycle.get()?.onAttach()
    }

    override fun onViewDetachedFromWindow(v: View?) {
        viewWithLifecycle.get()?.onDetach()
        subject.get()?.onNext(ViewState.DETACH)
    }
}