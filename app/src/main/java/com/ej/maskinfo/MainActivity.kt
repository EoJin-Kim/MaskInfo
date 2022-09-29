package com.ej.maskinfo

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ej.maskinfo.databinding.ActivityMainBinding
import com.ej.maskinfo.model.Store
import com.ej.maskinfo.viewmodel.MainViewModel
import com.google.android.gms.location.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.simpleName

    lateinit var activityMainBinding: ActivityMainBinding

    val viewModel : MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java)}
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
//                Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
                performAction()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    this@MainActivity,
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            .check();




    }

    @SuppressLint("MissingPermission")
    private fun performAction() {

        fusedLocationClient.lastLocation
            .addOnFailureListener(this){ e ->
                Log.e(TAG,"performaAction : ${e.cause}")

            }
            .addOnSuccessListener { location: Location? ->
                Thread.sleep(1000)
                // Got last known location. In some rare situations this can be null.
                Log.d(TAG,"${location} aa")
                if (location != null) {
                    Log.d(TAG,"getLatitude: ${location.latitude}")
                    Log.d(TAG,"getLatitude: ${location.latitude}")


                    location.latitude = 37.188078
                    location.longitude = 127.043002
                    viewModel.location = location
                    viewModel.fechStoreInfo()
                }


            }

        val recyclerView = activityMainBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val adapter = StoreAdapter()
        recyclerView.adapter = adapter

        viewModel.itemLiveData.observe(this) { stores ->
            adapter.updateItem(stores)
            supportActionBar?.title = "마스크 재고 있는 곳: ${stores.size}"
        }
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
                viewModel.fechStoreInfo()
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
        val dist = String.format("%.2fkm",store.distance)
        holder.distanceTextView.text = "${dist}km"


        var count = "100개 이상"
        var remainState = "충분"
        var color = Color.GREEN

        when (store.remainStat) {
            null->{
                remainState = "재고 없음"
                count = "재고 없음"
                color = Color.GRAY
            }
            "plenty"->{
                remainState = "충분"
                count = "100개 이상"
                color = Color.GREEN
            }
            "some" -> {
                remainState = "여유"
                count = "30개 이상"
                color = Color.YELLOW
            }
            "few" -> {
                remainState = "매진임박"
                count = "2개 이상"
                color = Color.RED
            }
            "empty" -> {
                remainState = "재고 없음"
                count = "재고 없음"
                color = Color.GRAY
            }

        }


        holder.remainTextView.text = remainState
        holder.countTextView.text = count

        holder.remainTextView.setTextColor(color)
        holder.countTextView.setTextColor(color)

               

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