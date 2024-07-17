package com.hannah.slidinggame.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var myView: MyView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myView= MyView(this)
        setContentView(myView)
    }

    override fun onResume() {
        super.onResume()
        myView.gotForeground()
    }

    override fun onStop() {
        super.onStop()
        myView.gotBackground()
    }

    override fun onDestroy() {
        super.onDestroy()
        myView.clearBeforeShunDown()
    }
}

