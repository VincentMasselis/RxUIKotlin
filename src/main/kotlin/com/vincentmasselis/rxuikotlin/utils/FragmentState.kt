package com.vincentmasselis.rxuikotlin.utils

enum class FragmentState {
    PRE_ATTACH,
    ATTACH,
    CREATE,
    ACTIVITY_CREATED,
    VIEW_CREATED,
    START,
    RESUME,
    PAUSE,
    STOP,
    SAVE_INSTANCE_STATE,
    DESTROY_VIEW,
    DESTROY,
    DETACH;

    @Throws(NoOpposite.FragmentStateException::class)
    fun opposite(): FragmentState =
        when (this) {
            PRE_ATTACH -> throw NoOpposite.FragmentStateException(this)
            ATTACH -> DETACH
            CREATE -> DESTROY
            ACTIVITY_CREATED -> throw NoOpposite.FragmentStateException(this)
            VIEW_CREATED -> DESTROY_VIEW
            START -> STOP
            RESUME -> PAUSE
            PAUSE -> RESUME
            STOP -> START
            SAVE_INSTANCE_STATE -> throw NoOpposite.FragmentStateException(this)
            DESTROY_VIEW -> VIEW_CREATED
            DESTROY -> CREATE
            DETACH -> ATTACH
        }
}