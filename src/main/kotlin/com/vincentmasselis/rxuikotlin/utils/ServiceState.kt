package com.vincentmasselis.rxuikotlin.utils

enum class ServiceState {
    CREATE,
    DESTROY;

    fun opposite(): ServiceState =
        when (this) {
            ServiceState.CREATE -> DESTROY
            ServiceState.DESTROY -> CREATE
        }
}