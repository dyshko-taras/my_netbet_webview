package com.test.netbet.game

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.netbet.R

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
    }

    //add Intent
    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, GameActivity::class.java))
        }
    }
}