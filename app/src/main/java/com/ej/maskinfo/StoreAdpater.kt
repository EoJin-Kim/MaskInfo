package com.ej.maskinfo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ej.maskinfo.databinding.ItemStoreBinding
import com.ej.maskinfo.model.Store

class StoreAdapter : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>(){

    private var mItems : List <Store> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store,parent,false)
        return StoreViewHolder(view);
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.binding.store = mItems.get(position)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun updateItem(items: List<Store>?){
        mItems = items!!
        notifyDataSetChanged()
    }

    inner class StoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemStoreBinding.bind(view)
    }

}

@BindingAdapter("remainState")
fun setRemainStat(textView: TextView,store: Store){
    when (store.remainStat) {
        "plenty"-> textView.text = "충분"
        "some" -> textView.text = "여유"
        "few" -> textView.text = "매진임박"
        "empty" -> textView.text = "재고 없음"
    }
}
@BindingAdapter("count")
fun setCount(textView: TextView,store: Store){
    when (store.remainStat) {
        "plenty"-> textView.text = "100개 이상"
        "some" -> textView.text = "30개 이상"
        "few" -> textView.text = "2개 이상"
        "empty" -> textView.text = "1개 이하"
    }
}
@BindingAdapter("color")
fun setColor(textView: TextView,store: Store){
    when (store.remainStat) {
        "plenty"-> textView.setTextColor(Color.GREEN)
        "some" -> textView.setTextColor(Color.YELLOW)
        "few" -> textView.setTextColor(Color.RED)
        "empty" -> textView.setTextColor(Color.GRAY)
    }
}