package com.vincentmasselis.rxui.utils

enum class ServiceState {
    CREATE,
    DESTROY;

    fun opposite(): ServiceState =
            when (this) {
                ServiceState.CREATE -> DESTROY
                ServiceState.DESTROY -> CREATE
            }
}