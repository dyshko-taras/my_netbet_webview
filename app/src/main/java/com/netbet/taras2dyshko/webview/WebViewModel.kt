package com.netbet.taras2dyshko.webview

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WebViewModel(application: Application) : AndroidViewModel(application) {

    private val apiFactory = ApiFactory()
    private val TAG = "WebViewActivity1"

    private val _url = MutableLiveData<String>()
    val urlLiveData: LiveData<String>
        get() = _url


    fun getUrl() {
        viewModelScope.launch {
            try {
                val url = apiFactory.apiService.getUrl().url
                _url.value = url
            } catch (e: Exception) {
                Log.d(TAG, "error: ${e.message}")
            }
        }
    }
}