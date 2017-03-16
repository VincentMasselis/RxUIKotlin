package com.vincentmasselis.rxuikotlin.utils

enum class ActivityState {
    CREATE,
    START,
    RESUME,
    PAUSE,
    STOP,
    DESTROY;

    fun opposite(): ActivityState =
            when (this) {
                ActivityState.CREATE -> DESTROY
                ActivityState.START -> STOP
                ActivityState.RESUME -> PAUSE
                ActivityState.PAUSE -> RESUME
                ActivityState.STOP -> START
                ActivityState.DESTROY -> CREATE
            }
}