package com.netbet.taras2dyshko

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var _isConnectedLiveData : MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val isConnectedLiveData : LiveData<Boolean>
        get() = _isConnectedLiveData


    fun isInternetAvailable(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        _isConnectedLiveData.value = networkInfo != null && networkInfo.isConnected
    }
}