package com.netbet.taras2dyshko.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.netbet.taras2dyshko.R
import com.netbet.taras2dyshko.game.GameActivity


class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewLoading: TextView
    private lateinit var webViewModel: WebViewModel
    private val TAG = "WebViewActivity1"

    private var fileChooserResultLauncher = createFileChooserResultLauncher()
    private var fileChooserValueCallback: ValueCallback<Array<Uri>>? = null


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        hideStatusBar()
        initView()

        webViewModel = ViewModelProvider(this).get(WebViewModel::class.java)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            allowFileAccess = true
            allowContentAccess = true
            displayZoomControls = false
            builtInZoomControls = true
            loadsImagesAutomatically = true
            allowUniversalAccessFromFileURLs = true
            allowFileAccessFromFileURLs = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            databaseEnabled = true
        }

        initWebView()

        webViewModel.getUrl()
        webViewModel.urlLiveData.observe(this, Observer {
            if (it.isEmpty()) {
                loadGameActivity(this@WebViewActivity)
            } else {
                webView.loadUrl(it)
            }
        })

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

    private fun initWebView() {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
                textViewLoading.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (webView.progress == 100) {
                    progressBar.visibility = View.GONE
                    textViewLoading.visibility = View.GONE
                }
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

        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                try {
                    fileChooserValueCallback = filePathCallback;
                    fileChooserResultLauncher.launch(fileChooserParams?.createIntent())
                } catch (e: ActivityNotFoundException) {
                    // You may handle "No activity found to handle intent" error
                }
                return true
            }
        }

    }

    private fun createFileChooserResultLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                fileChooserValueCallback?.onReceiveValue(arrayOf(Uri.parse(it?.data?.dataString)));
            } else {
                fileChooserValueCallback?.onReceiveValue(null)
            }
        }
    }

    private fun loadGameActivity(context: Context) {
        val intent = GameActivity.start(context)
    }

    private fun hideStatusBar() {
        // Hide the status bar.
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        // Hide the action bar.
        actionBar?.hide()
    }
}