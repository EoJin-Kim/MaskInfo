package com.ej.maskinfo.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class Store (
    @Json(name = "addr")
    var addr : String,
    @Json(name = "code")
    var code : String,
    @field:Json(name = "created_at")
    var createdAt :String,
    @Json(name = "lat")
    var lat:Double,
    @Json(name = "lng")
    var lng:Double,
    @Json(name = "name")
    var name:String,
    @field:Json(name = "remain_stat")
    var remainStat : String,
    @field:Json(name = "stock_at")
    var stockAt : String,
    @Json(name = "type")
    var type : String,

    var distance : Double
)
