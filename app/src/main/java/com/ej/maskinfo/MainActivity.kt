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
import androidx.activity.viewModels
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

    val viewModel : MainViewModel by viewModels()
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


        val adapter = StoreAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            this.adapter = adapter
        }


        viewModel.apply {
            itemLiveData.observe(this@MainActivity) { stores ->
                adapter.updateItem(stores)
                supportActionBar?.title = "마스크 재고 있는 곳: ${stores.size}"
            }
            loadingLiveData.observe(this@MainActivity){ isLoading ->
                if (isLoading) {
                    activityMainBinding.progressBar.visibility = View.VISIBLE
                } else {
                    activityMainBinding.progressBar.visibility = View.GONE
                }
            }
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

