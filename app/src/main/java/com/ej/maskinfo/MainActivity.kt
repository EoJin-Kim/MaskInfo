package com.ej.maskinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ej.maskinfo.databinding.ActivityMainBinding
import com.ej.maskinfo.model.Store
import com.ej.maskinfo.model.StoreInfo
import com.ej.maskinfo.repository.MaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.simpleName

    lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val recyclerView = activityMainBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)

        val adapter = StoreAdapter()
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl(MaskService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val service : MaskService = retrofit.create(MaskService::class.java)

        val storeInfoCall : Call<StoreInfo> = service.fechStoreInfo()

        storeInfoCall.enqueue(object : Callback<StoreInfo>{
            override fun onResponse(call: Call<StoreInfo>, response: Response<StoreInfo>) {
                val items = response.body()?.stores
                adapter.updateItem(items)
                supportActionBar?.title = "마스크 재고 있는 곳: ${items?.size}"
            }

            override fun onFailure(call: Call<StoreInfo>, t: Throwable) {
                Log.e(TAG,"onFailure: ${t.message}")
            }
        })



    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_refresh -> {

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }



}

class StoreAdapter : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>(){
    private var mItems : List <Store> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store,parent,false)
        return StoreViewHolder(view);
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = mItems.get(position)

        holder.nameTextView.text = store.name
        holder.addressTextView.text = store.addr
        holder.distanceTextView.text = "1.0km"
        holder.remainTextView.text = store.remainStat
        holder.countTextView.text = "100ro 이상"
               

    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun updateItem(items: List<Store>?){
        mItems = items!!
        notifyDataSetChanged()
    }

    inner class StoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nameTextView : TextView = view.findViewById(R.id.name_text_view)
        val addressTextView : TextView = view.findViewById(R.id.addr_text_view)
        val distanceTextView : TextView = view.findViewById(R.id.distance_text_view)
        val remainTextView : TextView = view.findViewById(R.id.remain_text_view)
        val countTextView : TextView = view.findViewById(R.id.count_text_view)
    }

}