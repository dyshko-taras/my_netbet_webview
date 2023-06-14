package com.test.netbet.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.test.netbet.R

class WebViewActivity : AppCompatActivity() {

    private var URL = ""
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewLoading: TextView
    private lateinit var webViewModel: WebViewModel
    private val TAG = "WebViewActivity1"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        Log.d(TAG, "1")
        initView()
        webViewModel = ViewModelProvider(this).get(WebViewModel::class.java)
        webView.settings.apply {
            javaScriptEnabled = true
        }
        initWebView()
        Log.d(TAG, "2")
        webViewModel.getUrl()
        webViewModel.urlLiveData.observe(this, Observer {
            URL = it
            Log.d(TAG, "URL: $URL")
        })
//        webView.loadUrl("https://ctrlab.site/mBdvxz/")
        if (savedInstanceState == null) {
//            webView.loadUrl(URL)
            webView.loadUrl("https://ctrlab.site/mBdvxz/")

            Log.d(TAG, "3")
        }




        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        webView.keepScreenOn = true
    }

    // add Intent
    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, WebViewActivity::class.java))
        }
    }

    //init view
    private fun initView() {
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        textViewLoading = findViewById(R.id.textViewLoading)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
        else finishAffinity()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webView.restoreState(savedInstanceState)
    }

    private fun initWebView() {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
                textViewLoading.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
                textViewLoading.visibility = View.GONE
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                if (error?.primaryError == SslError.SSL_UNTRUSTED) {
                    // Не довіряють сертифікату SSL, скасувати завантаження
                    handler?.cancel()
                } else {
                    // Інші помилки SSL, продовжити завантаження
                    handler?.proceed()
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }

        }
    }

}