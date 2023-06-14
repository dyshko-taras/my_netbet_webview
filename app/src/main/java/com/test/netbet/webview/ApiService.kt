package com.test.netbet.webview

import retrofit2.http.GET

interface ApiService {
    @GET("3453453/")
    suspend fun getUrl(): ApiResult
}