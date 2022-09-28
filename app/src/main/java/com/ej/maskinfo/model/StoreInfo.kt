package com.ej.maskinfo.model





import com.squareup.moshi.Json
import java.util.List;

public data class StoreInfo(

    @Json(name = "count")
    var count : Int,
    @Json(name = "stores")
    var stores  : MutableList<Store>


)

