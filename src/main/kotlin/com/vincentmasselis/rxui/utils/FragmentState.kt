package com.vincentmasselis.rxui.utils

enum class FragmentState {
    ATTACH,
    CREATE,
    VIEW_CREATED,
    START,
    RESUME,
    PAUSE,
    STOP,
    DESTROY_VIEW,
    DESTROY,
    DETACH;

    fun opposite(): FragmentState =
            when (this) {
                FragmentState.ATTACH -> DETACH
                FragmentState.CREATE -> DESTROY
                FragmentState.VIEW_CREATED -> DESTROY_VIEW
                FragmentState.START -> STOP
                FragmentState.RESUME -> PAUSE
                FragmentState.PAUSE -> RESUME
                FragmentState.STOP -> START
                FragmentState.DESTROY_VIEW -> VIEW_CREATED
                FragmentState.DESTROY -> CREATE
                FragmentState.DETACH -> ATTACH
            }
}