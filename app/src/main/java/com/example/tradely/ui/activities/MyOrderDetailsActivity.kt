package com.example.tradely.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradely.R
import com.example.tradely.databinding.ActivityMyOrderDetailsBinding
import com.example.tradely.models.Order
import com.example.tradely.ui.adapters.CartItemsListAdapter
import com.example.tradely.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MyOrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyOrderDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrderDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()

        var myOrderDetails: Order = Order()
        if (intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAILS)) {
            myOrderDetails = intent.getParcelableExtra<Order>(Constants.EXTRA_MY_ORDER_DETAILS)!!
        }
        setUpUI(myOrderDetails)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarMyOrderDetailsActivity)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        binding.toolbarMyOrderDetailsActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setUpUI(orderDetails: Order) {
        binding.tvOrderDetailsId.text = orderDetails.title
        val dateFormat = "dd MMM yyyy HH:MM"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_datetime
        val orderDateTime = formatter.format(calendar.time)
        binding.tvOrderDetailsDate.text = orderDateTime

        // Get the difference between the order date time and current date time in hours.
        // If the difference in hours is 1 or less then the order status will be PENDING.
        // If the difference in hours is 2 or greater then 1 then the order status will be PROCESSING.
        // And, if the difference in hours is 3 or greater then the order status will be DELIVERED.

        val diffInMilliSeconds: Long = System.currentTimeMillis() - orderDetails.order_datetime
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
        Log.e("Difference in Hours", "$diffInHours")

        when {
            diffInHours < 1 -> {
                binding.tvOrderStatus.text = resources.getString(R.string.order_status_pending)
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this@MyOrderDetailsActivity,
                        R.color.colorAccent
                    )
                )
            }
            diffInHours < 2 -> {
                binding.tvOrderStatus.text = resources.getString(R.string.order_status_in_process)
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this@MyOrderDetailsActivity,
                        R.color.colorOrderStatusInProcess
                    )
                )
            }
            else -> {
                binding.tvOrderStatus.text = resources.getString(R.string.order_status_delivered)
                binding.tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this@MyOrderDetailsActivity,
                        R.color.colorOrderStatusDelivered
                    )
                )
            }
        }

        binding.rvMyOrderItemsList.layoutManager = LinearLayoutManager(this@MyOrderDetailsActivity)
        binding.rvMyOrderItemsList.setHasFixedSize(true)

        val cartListAdapter =
            CartItemsListAdapter(this@MyOrderDetailsActivity, orderDetails.items, false)
        binding.rvMyOrderItemsList.adapter = cartListAdapter

        binding.tvMyOrderDetailsAddressType.text = orderDetails.address.type
        binding.tvMyOrderDetailsFullName.text = orderDetails.address.name
        binding.tvMyOrderDetailsAddress.text =
            "${orderDetails.address.address}, ${orderDetails.address.zipCode}"
        binding.tvMyOrderDetailsAdditionalNote.text = orderDetails.address.additionalNote

        if (orderDetails.address.otherDetails.isNotEmpty()) {
            binding.tvMyOrderDetailsOtherDetails.visibility = View.VISIBLE
            binding.tvMyOrderDetailsOtherDetails.text = orderDetails.address.otherDetails
        } else {
            binding.tvMyOrderDetailsOtherDetails.visibility = View.GONE
        }
        binding.tvMyOrderDetailsMobileNumber.text = orderDetails.address.mobileNumber

        binding.tvOrderDetailsSubTotal.text = orderDetails.sub_total_amount
        binding.tvOrderDetailsShippingCharge.text = orderDetails.shipping_charge
        binding.tvOrderDetailsTotalAmount.text = orderDetails.total_amount
    }
}