package com.vincentmasselis.rxuikotlin.utils

enum class ActivityState {
    CREATE,
    START,
    RESUME,
    PAUSE,
    STOP,
    SAVE_INSTANCE_STATE,
    DESTROY;

    @Throws(NoOpposite.ActivityStateException::class) fun opposite(): ActivityState =
            when (this) {
                CREATE -> DESTROY
                START -> STOP
                RESUME -> PAUSE
                PAUSE -> RESUME
                STOP -> START
                SAVE_INSTANCE_STATE -> throw NoOpposite.ActivityStateException(this)
                DESTROY -> CREATE
            }
}