package com.test.netbet.webview

import com.google.gson.annotations.SerializedName

class ApiResult {

    @SerializedName("kolbaska")
    var url: String = ""

    @SerializedName("conn")
    var conn: String = "0"

    override fun toString(): String {
        return "ApiResult(site='$url', conn='$conn')"
    }


}