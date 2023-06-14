package com.test.netbet.webview

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WebViewModel(application: Application) : AndroidViewModel(application) {

    val apiFactory = ApiFactory()
    val TAG = "WebViewActivity1"


    private val _url = MutableLiveData<String>()
    val urlLiveData: LiveData<String>
        get() = _url

    fun getUrl() {
        viewModelScope.launch {
            try {
                val response = apiFactory.apiService.getUrl()
                val url = response.url
                val conn = response.conn
                Log.d(TAG, url)
                Log.d(TAG, conn)

            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
            }
        }
    }
}