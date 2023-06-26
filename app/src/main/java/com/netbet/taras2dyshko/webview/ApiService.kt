package com.netbet.taras2dyshko.webview

import retrofit2.http.GET

interface ApiService {
    @GET("3453453/")
    suspend fun getUrl(): ResultJSON

}