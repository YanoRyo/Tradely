package com.example.tradely.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tradely.R
import com.example.tradely.models.Product
import com.example.tradely.ui.activities.ProductDetailsActivity
import com.example.tradely.utils.Constants
import com.example.tradely.utils.GlideLoader

class DashboardItemListAdapter (
    private val context: Context,
    private val list: ArrayList<Product>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener:OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_layout,
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
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.findViewById(R.id.iv_dashboard_item_image))
            holder.itemView.findViewById<TextView>(R.id.tv_dashboard_item_title).text = model.title
            holder.itemView.findViewById<TextView>(R.id.tv_dashboard_item_price).text = "$${model.price}"

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
                context.startActivity(intent)
            }
        }
    }

    fun setOnClickLister(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

    interface OnClickListener{
        fun onClick(position: Int, product: Product)
    }
}