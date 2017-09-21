package com.vincentmasselis.rxuikotlin.utils

sealed class NoOpposite {

    class ActivityStateException(val state: ActivityState) : Throwable() {
        override fun toString(): String = "NoOpposite.ActivityStateException(state=$state)"
    }

    class FragmentStateException(val state: FragmentState) : Throwable() {
        override fun toString(): String = "NoOpposite.FragmentStateException(state=$state)"
    }
}