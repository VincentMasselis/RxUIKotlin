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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adapter)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.subscribe(adapter).disposeOnState(ActivityState.DESTROY, this)
    }
}