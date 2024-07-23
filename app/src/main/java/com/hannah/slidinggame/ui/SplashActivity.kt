package com.hannah.slidinggame.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.AlertDialog
import com.hannah.slidinggame.R
import com.hannah.slidinggame.logic.GameMode

class SplashActivity : AppCompatActivity() {
    private lateinit var singleplayerBtn: ImageView
    private lateinit var twoPlayerBtn: ImageView
    private lateinit var questionBtn: ImageView
    private lateinit var settingBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shlash_activity)

        singleplayerBtn = findViewById(R.id.imageView1)
        twoPlayerBtn = findViewById(R.id.imageView2)
        questionBtn = findViewById(R.id.question_mark)
        settingBtn = findViewById(R.id.setting)

        singleplayerBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("game_mode", GameMode.ONE_PLAYER)
            startActivity(intent)
        }

        twoPlayerBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("game_mode", GameMode.TWO_PLAYER)
            startActivity(intent)
        }

        questionBtn.setOnClickListener {
           val ab: AlertDialog.Builder = AlertDialog.Builder(this)
            ab.setTitle(R.string.about_game)
            ab.setMessage(R.string.about_description)
            ab.setCancelable(false)
            ab.setPositiveButton(R.string.okay_btn) {_, _ ->
                println("done.")
            }
            ab.create().show()

        }
    }
}