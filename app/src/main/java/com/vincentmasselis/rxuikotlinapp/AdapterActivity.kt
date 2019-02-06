package com.vincentmasselis.rxuikotlinapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.vincentmasselis.rxuikotlin.disposeOnState
import com.vincentmasselis.rxuikotlin.subscribe
import com.vincentmasselis.rxuikotlin.utils.ActivityState
import kotlinx.android.synthetic.main.activity_adapter.*

class AdapterActivity : AppCompatActivity() {

    val adapter = Adapter()
    val recyclerView by lazy { recycler_view }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adapter)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.subscribe(adapter).disposeOnState(ActivityState.DESTROY, this)
    }
}