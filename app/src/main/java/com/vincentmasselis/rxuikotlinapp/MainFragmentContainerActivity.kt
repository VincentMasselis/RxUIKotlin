package com.vincentmasselis.rxuikotlinapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainFragmentContainerActivity : AppCompatActivity() {

    lateinit var fragment: MainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fragment_container)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commitNow()
        fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as MainFragment
    }
}