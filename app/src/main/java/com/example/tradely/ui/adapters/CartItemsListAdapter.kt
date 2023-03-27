package com.example.tradely.ui.adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tradely.R
import com.example.tradely.firestore.FirestoreClass
import com.example.tradely.models.CartItem
import com.example.tradely.ui.activities.CartListActivity
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

            if (model.cart_quantity == "0") {
                holder.itemView.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility = View.GONE
                holder.itemView.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility = View.GONE

                holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).text =
                    context.resources.getString(R.string.lbl_out_of_stock)

                holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSnackBarError
                    )
                )
            } else {
                holder.itemView.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility = View.VISIBLE
                holder.itemView.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility = View.VISIBLE

                holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSecondaryText
                    )
                )
            }
            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_cart_item).setOnClickListener {
                when(context) {
                    is CartListActivity -> {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }
                }
                FirestoreClass().removeItemFromCart(context, model.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }



    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}