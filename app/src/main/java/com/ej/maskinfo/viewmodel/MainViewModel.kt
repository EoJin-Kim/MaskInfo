package com.ej.maskinfo.viewmodel

import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ej.maskinfo.LocationDistance
import com.ej.maskinfo.MainActivity
import com.ej.maskinfo.model.Store
import com.ej.maskinfo.model.StoreInfo
import com.ej.maskinfo.repository.MaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import kotlin.Comparator
import kotlin.streams.toList

class MainViewModel : ViewModel() {
    private val TAG = MainActivity::class.simpleName

    var itemLiveData = MutableLiveData<MutableList<Store>>()

    lateinit var location : Location
    private val retrofit = Retrofit.Builder()
        .baseUrl(MaskService.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service : MaskService = retrofit.create(MaskService::class.java)


    init {
        location = Location("test")
        location.latitude = 37.188078
        location.longitude = 127.043002
        fechStoreInfo()
    }

    fun fechStoreInfo() {

        val call = service.fechStoreInfo(location.latitude,location.longitude)
            .enqueue(object : Callback<StoreInfo> {

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<StoreInfo>, response: Response<StoreInfo>) {
                val items = response.body()?.stores!!.stream().filter{ item ->item.remainStat!=null && item.remainStat!="empty"}.toList() as MutableList<Store>?


                for (store in items!!) {
                    val distance = LocationDistance.distance(location.latitude,location.longitude,store.lat,store.lng,"k")
                    store.distance = distance
                }
                items.sortWith(Comparator { store, store2 -> store.distance.compareTo(store2.distance) })
                itemLiveData.postValue(items!!)

//                adapter.updateItem(items!!.stream().filter{item ->item.remainStat!=null}.toList())
//                supportActionBar?.title = "마스크 재고 있는 곳: ${items?.size}"
            }

            override fun onFailure(call: Call<StoreInfo>, t: Throwable) {
                Log.e(TAG,"onFailure: ${t.message}")
                itemLiveData.postValue(Collections.emptyList())
            }
        })
    }
}