package com.ej.maskinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ej.maskinfo.databinding.ActivityMainBinding
import com.ej.maskinfo.model.Store

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)

        val recyclerView = activityMainBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)

        setContentView(activityMainBinding.root)
    }
}

class StoreAdapter : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>(){
    private val mItems = ArrayList<Store>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store,parent,false)
        return StoreViewHolder(view);
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class StoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

}