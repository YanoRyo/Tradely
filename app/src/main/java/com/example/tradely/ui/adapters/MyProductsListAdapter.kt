package com.example.tradely.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tradely.R
import com.example.tradely.models.Product
import com.example.tradely.ui.activities.ProductDetailsActivity
import com.example.tradely.ui.fragments.ProductsFragment
import com.example.tradely.utils.Constants
import com.example.tradely.utils.GlideLoader

open class MyProductsListAdapter(
    private val context: Context,
    private val list: ArrayList<Product>,
    private val fragment: ProductsFragment
    ):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.findViewById(R.id.iv_item_image))
            holder.itemView.findViewById<TextView>(R.id.tv_item_name).text = model.title
            holder.itemView.findViewById<TextView>(R.id.tv_item_price).text = "$${model.price}"

            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_product).setOnClickListener {
                fragment.deleteProduct(model.product_id)
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, model.user_id)
                context.startActivities(arrayOf(intent))
            }
        }
    }


    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}