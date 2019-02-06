package com.vincentmasselis.rxuikotlinapp

import android.view.LayoutInflater
import android.view.ViewGroup
import com.vincentmasselis.rxuikotlin.LifecycleAdapter

class Adapter : LifecycleAdapter<ViewHolder>() {

    val viewHolders = mutableListOf<ViewHolder>()

    private val viewHolderCount = 50

    override fun getItemCount(): Int = viewHolderCount

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_view_holder, parent, false)).also { viewHolders += it }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}
}