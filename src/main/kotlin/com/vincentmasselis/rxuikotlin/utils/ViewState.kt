package com.vincentmasselis.rxuikotlin.utils

enum class ViewState {
    ATTACH,
    DETACH;

    fun opposite(): ViewState =
            when (this) {
                ViewState.ATTACH -> DETACH
                ViewState.DETACH -> ATTACH
            }
}