package com.ej.maskinfo.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ej.maskinfo.LocationDistance
import com.ej.maskinfo.MainActivity
import com.ej.maskinfo.model.Store
import com.ej.maskinfo.model.StoreInfo
import com.ej.maskinfo.repository.MaskService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Inject
import kotlin.Comparator
import kotlin.streams.toList


@HiltViewModel
class MainViewModel @Inject constructor(
    private val service: MaskService,
    private val fusedLocationClient: FusedLocationProviderClient
) : ViewModel() {
    private val TAG = MainActivity::class.simpleName

    var itemLiveData = MutableLiveData<List<Store>>()
    val loadingLiveData = MutableLiveData<Boolean>()

    lateinit var customLocation : Location

    init {


        customLocation = Location("test")
        customLocation.latitude = 37.188078
        customLocation.longitude = 127.043002
        fechStoreInfo()
    }

    @SuppressLint("MissingPermission")
    fun fechStoreInfo() {
        loadingLiveData.value = true


        fusedLocationClient.lastLocation.addOnSuccessListener { location ->

            viewModelScope.launch {

                val storeInfo = service.fechStoreInfo(customLocation.latitude,customLocation.longitude)
                itemLiveData.value = storeInfo.stores.filter { store ->
                    store.remainStat!=null
                }
                loadingLiveData.value = false
            }
        }.addOnFailureListener { exception ->
            loadingLiveData.value = false
        }



//        service.fechStoreInfo(location.latitude,location.longitude)
//            .enqueue(object : Callback<StoreInfo> {
//
//            @RequiresApi(Build.VERSION_CODES.N)
//            override fun onResponse(call: Call<StoreInfo>, response: Response<StoreInfo>) {
//                val items = response.body()?.stores!!.stream().filter{ item ->item.remainStat!=null && item.remainStat!="empty"}.toList() as MutableList<Store>?
//
//
//                for (store in items!!) {
//                    val distance = LocationDistance.distance(location.latitude,location.longitude,store.lat,store.lng,"k")
//                    store.distance = distance
//                }
//                items.sortWith(Comparator { store, store2 -> store.distance.compareTo(store2.distance) })
//                itemLiveData.postValue(items!!)
//
//                loadingLiveData.postValue(false)
//
////                adapter.updateItem(items!!.stream().filter{item ->item.remainStat!=null}.toList())
////                supportActionBar?.title = "마스크 재고 있는 곳: ${items?.size}"
//            }
//
//            override fun onFailure(call: Call<StoreInfo>, t: Throwable) {
//                Log.e(TAG,"onFailure: ${t.message}")
//                itemLiveData.postValue(Collections.emptyList())
//                loadingLiveData.postValue(false)
//            }
//        })
    }
}