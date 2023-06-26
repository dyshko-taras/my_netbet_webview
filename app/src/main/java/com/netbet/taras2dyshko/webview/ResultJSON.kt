package com.netbet.taras2dyshko.webview

import com.google.gson.annotations.SerializedName

class ResultJSON {

    @SerializedName("kolbaska")
    var url: String = ""

    @SerializedName("conn")
    var conn: String = "Good"

    override fun toString(): String {
        return "ApiResult(site='$url', conn='$conn')"
    }


}