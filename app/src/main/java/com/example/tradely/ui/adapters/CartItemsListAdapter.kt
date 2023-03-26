package com.example.tradely.ui.adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tradely.R
import com.example.tradely.models.CartItem
import com.example.tradely.utils.GlideLoader

class CartItemsListAdapter(
    private val context: Context,
    private val list: ArrayList<CartItem>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_cart_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.findViewById(R.id.iv_cart_item_image))
            holder.itemView.findViewById<TextView>(R.id.tv_cart_item_title).text = model.title
            holder.itemView.findViewById<TextView>(R.id.tv_cart_item_price).text = "$${model.price}"
            holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).text = model.cart_quantity
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }



    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}