package com.vincentmasselis.devapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class BlankFragment : Fragment() {

    companion object {
        private const val PARAM_NAME = "PARAM_NAME"
        fun builder(name: String) = BlankFragment().apply { arguments = Bundle().apply { putString(PARAM_NAME, name) } }
    }

    private val name by lazy { arguments!!.getString(PARAM_NAME)!! }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = View(context)
    override fun toString(): String {
        return "${super.toString()}(name='$name')"
    }
}