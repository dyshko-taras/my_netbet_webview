package com.test.netbet

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.test.netbet.game.GameActivity
import com.test.netbet.webview.WebViewActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel : MainViewModel
    private lateinit var textViewLoading : TextView
    private lateinit var progressBar: ProgressBar
    val TAG = "MainActivity1"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initView()
        mainViewModel.isInternetAvailable(this@MainActivity)
        mainViewModel.isConnectedLiveData.observe(this, Observer {
            Log.d(TAG, "TUT")
            if (it) loadWebViewActivity(this@MainActivity)
            else loadGameActivity(this@MainActivity)
        })

    }

    private fun initView() {
        textViewLoading = findViewById(R.id.textViewLoading)
        progressBar = findViewById(R.id.progressBar)
    }

    // fun load WebViewActivity
    private fun loadWebViewActivity(context: Context) {
        val intent = WebViewActivity.start(context)
    }

    private fun loadGameActivity(context: Context) {
        val intent = GameActivity.start(context)
    }

}