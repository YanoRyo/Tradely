package com.example.tradely.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.tradely.R
import com.example.tradely.databinding.ActivityProductDetailsBinding
import com.example.tradely.firestore.FirestoreClass
import com.example.tradely.models.CartItem
import com.example.tradely.models.Product
import com.example.tradely.utils.Constants
import com.example.tradely.utils.GlideLoader

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityProductDetailsBinding
    private var mProductId:String = ""
    private lateinit var mProductDetails: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("product id", mProductId)
        }
        var productOwnerId:String = ""

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            productOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        if (FirestoreClass().getCurrentUserID() == productOwnerId) {
            binding.btnAddToCart.visibility = View.GONE
            binding.btnGoToCart.visibility = View.GONE
        }else {
            binding.btnAddToCart.visibility = View.VISIBLE
        }
        binding.btnAddToCart.setOnClickListener(this@ProductDetailsActivity)
        binding.btnGoToCart.setOnClickListener(this@ProductDetailsActivity)
        getProductDetails()
    }

    private fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductDetails(this@ProductDetailsActivity, mProductId)
    }

    fun productExistsInCart() {
        hideProgressDialog()
        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }
    fun productDetailsSuccess(product: Product) {
        mProductDetails = product
        hideProgressDialog()
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            binding.ivProductDetailImage
        )
        binding.tvProductDetailsTitle.text = product.title
        binding.tvProductDetailsPrice.text = "$${product.price}"
        binding.tvProductDetailsDescription.text = product.description
        binding.tvProductDetailsAvailableQuantity.text = product.stock_quantity

        if (FirestoreClass().getCurrentUserID() == product.user_id) {
            hideProgressDialog()
        }else{
            FirestoreClass().checkIfItemExistInCart(this@ProductDetailsActivity, mProductId)
        }
    }


    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarProductDetailsActivity)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        binding.toolbarProductDetailsActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun addToCart() {
        val cartItem = CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addCartItems(this, cartItem)
    }

    fun addToCartSuccess() {
        hideProgressDialog()
        Toast.makeText(this, resources.getString(R.string.success_message_item_added_to_cart),Toast.LENGTH_LONG).show()

        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }
    override fun onClick(view: View?) {
        if (view != null){
            when(view.id){
                R.id.btn_add_to_cart -> {
                    addToCart()
                }
                R.id.btn_go_to_cart -> {
                    startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
                }
            }
        }
    }
}