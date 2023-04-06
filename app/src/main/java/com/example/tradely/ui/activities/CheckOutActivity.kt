package com.example.tradely.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tradely.R
import com.example.tradely.databinding.ActivityCheckOutBinding
import com.example.tradely.firestore.FirestoreClass
import com.example.tradely.models.Address
import com.example.tradely.models.CartItem
import com.example.tradely.models.Order
import com.example.tradely.models.Product
import com.example.tradely.ui.adapters.CartItemsListAdapter
import com.example.tradely.utils.Constants

class CheckOutActivity : BaseActivity() {

    private lateinit var binding: ActivityCheckOutBinding
    private var mAddressDetails: Address? = null
    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartListItems: ArrayList<CartItem>
    private var mSubTotal: Double = 0.0
    private var mTotalAmount: Double = 0.0
    private lateinit var mOrderDetails: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)) {
            mAddressDetails = intent.getParcelableExtra<Address>(Constants.EXTRA_SELECTED_ADDRESS)
        }

        if (mAddressDetails != null) {
            binding.tvCheckoutAddressType.text = mAddressDetails?.type
            binding.tvCheckoutFullName.text = mAddressDetails?.name
            binding.tvCheckoutAddress.text = "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            binding.tvCheckoutAdditionalNote.text = mAddressDetails?.additionalNote

            if (mAddressDetails?.otherDetails!!.isNotEmpty()) {
                binding.tvCheckoutOtherDetails.text = mAddressDetails?.otherDetails
            }
            binding.tvMobileNumber.text = mAddressDetails?.mobileNumber
        }

        binding.btnPlaceOrder.setOnClickListener {
            placeAnOrder()
        }

        getProductList()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCheckoutActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarCheckoutActivity.setNavigationOnClickListener { onBackPressed() }
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {
        hideProgressDialog()
        mProductsList = productsList
        getCartItemsList()
    }

    private fun getCartItemsList() {
        FirestoreClass().getCartList(this@CheckOutActivity)
    }
    private fun getProductList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAllProductsList(this@CheckOutActivity)
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()
        for (product in mProductsList){
            for (cartItem in cartList){
                if (product.product_id == cartItem.product_id) {
                    cartItem.stock_quantity = product.stock_quantity
                }
            }
        }
        mCartListItems = cartList
        binding.rvCartListItems.layoutManager = LinearLayoutManager(this@CheckOutActivity)
        binding.rvCartListItems.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@CheckOutActivity, mCartListItems, false)
        binding.rvCartListItems.adapter = cartListAdapter

        for (item in mCartListItems) {
            val availableQuantity = item.stock_quantity.toInt()
            if (availableQuantity > 0) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                mSubTotal += (price * quantity)
            }
        }
        binding.tvCheckoutSubTotal.text = "$${mSubTotal}"
        binding.tvCheckoutShippingCharge.text = "$10.0"

        if (mSubTotal > 0){
            binding.llCheckoutPlaceOrder.visibility = View.VISIBLE
            mTotalAmount = mSubTotal * 10.0
            binding.tvCheckoutTotalAmount.text = "$${mTotalAmount}"
        }else{
            binding.llCheckoutPlaceOrder.visibility = View.GONE
        }
    }

    fun allDetailsUpdatedSuccessfully() {
        hideProgressDialog()
        Toast.makeText(this@CheckOutActivity, "Your order was placed successfully.", Toast.LENGTH_LONG).show()
        val intent = Intent(this@CheckOutActivity, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    fun orderPlacedSuccess() {
        FirestoreClass().updateAllDetails(this, mCartListItems, mOrderDetails)
    }
    private fun placeAnOrder() {
        showProgressDialog(resources.getString(R.string.please_wait))
        if (mAddressDetails != null){
            mOrderDetails = Order(
                FirestoreClass().getCurrentUserID(),
                mCartListItems,
                mAddressDetails!!,
                "My order ${System.currentTimeMillis()}",
                mCartListItems[0].image,
                mSubTotal.toString(),
                "10.0",
                mTotalAmount.toString(),
                System.currentTimeMillis()
            )
            FirestoreClass().placeOrder(this, mOrderDetails)
        }
    }
}