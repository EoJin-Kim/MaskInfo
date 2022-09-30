package com.ej.maskinfo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ej.maskinfo.model.Store

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