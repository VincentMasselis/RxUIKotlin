package com.vincentmasselis.rxui.utils

import android.view.View
import java.lang.ref.WeakReference

class AttachListenerForViewWithLifecycle(viewWithLifecycle: ViewWithLifecycle) : View.OnAttachStateChangeListener {

    private val viewWithLifecycle: WeakReference<ViewWithLifecycle> = WeakReference(viewWithLifecycle)

    override fun onViewAttachedToWindow(v: View?) {
        viewWithLifecycle.get()?.onAttach()
    }

    override fun onViewDetachedFromWindow(v: View?) {
        viewWithLifecycle.get()?.onDetach()
    }

}