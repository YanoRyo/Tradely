package com.example.tradely.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradely.R
import com.example.tradely.databinding.ActivityCartListBinding
import com.example.tradely.firestore.FirestoreClass
import com.example.tradely.models.CartItem
import com.example.tradely.ui.adapters.CartItemsListAdapter

class CartListActivity : BaseActivity() {

    private lateinit var binding: ActivityCartListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCartListActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarCartListActivity.setNavigationOnClickListener { onBackPressed() }
    }

    fun successCarItemList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()

        if (cartList.size > 0) {
            binding.rvCartItemsList.visibility = View.VISIBLE
            binding.llCheckout.visibility = View.VISIBLE
            binding.tvNoCartItemFound.visibility = View.GONE

            binding.rvCartItemsList.layoutManager = LinearLayoutManager(this@CartListActivity)
            binding.rvCartItemsList.setHasFixedSize(true)

            val cartListAdapter = CartItemsListAdapter(this@CartListActivity, cartList)
            binding.rvCartItemsList.adapter = cartListAdapter
            var subTotal:Double = 0.0
            for (item in cartList) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                subTotal += (price * quantity)
            }
            binding.tvSubTotal.text = "$${subTotal}"
            binding.tvShippingCharge.text = "$10.0"

            if (subTotal > 0){
                binding.llCheckout.visibility = View.VISIBLE

                val total = subTotal + 10
                binding.tvTotalAmount.text = "$${total}"
            }else{
                binding.llCheckout.visibility = View.GONE
            }
        }else{
            binding.rvCartItemsList.visibility = View.GONE
            binding.llCheckout.visibility = View.GONE
            binding.tvNoCartItemFound.visibility = View.VISIBLE
        }
    }

    private fun getCartItemsList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCartList(this@CartListActivity)
    }

    override fun onResume() {
        super.onResume()
        getCartItemsList()
    }
}