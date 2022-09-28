package com.ej.maskinfo

import com.ej.maskinfo.model.StoreInfo
import com.ej.maskinfo.repository.MaskService
import com.squareup.moshi.Moshi
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun retrofitTest(){
        val retrofit = Retrofit.Builder()
            .baseUrl(MaskService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val service : MaskService= retrofit.create(MaskService::class.java)

        val serverInfo : Call<StoreInfo> = service.fechStoreInfo()

        val storeInfo = serverInfo.execute().body()
        assertEquals(222,storeInfo?.count)
        assertEquals(222,storeInfo?.stores?.size)
    }
}