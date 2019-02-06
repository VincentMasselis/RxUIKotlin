package com.vincentmasselis.rxuikotlin

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Completable
import io.reactivex.annotations.CheckReturnValue
import java.util.*

internal val protectedId = Random().nextInt()

@CheckReturnValue
fun <T : LifecycleViewHolder> RecyclerView.subscribe(adapter: LifecycleAdapter<T>) = Completable
    .create { downStream ->
        setTag(protectedId, Unit)
        downStream.setCancellable { this.adapter = null }
        this.adapter = adapter
    }
    .subscribe()