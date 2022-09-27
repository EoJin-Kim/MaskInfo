package com.ej.maskinfo.model

import com.squareup.moshi.Json

data class Store (
    @Json(name = "addr")
    var addr : String,
    @Json(name = "code")
    var code : String,
    @Json(name = "created_at")
    val createdAt :String,
    @Json(name = "lat")
    val lat:Double,
    @Json(name = "lng")
    val lng:Double,
    @Json(name = "name")
    val name:String,
    @Json(name = "remain_stat")
    val remainStat : String,
    @Json(name = "stock_at")
    val stockAt : String,
    @Json(name = "type")
    var type : String,
)
