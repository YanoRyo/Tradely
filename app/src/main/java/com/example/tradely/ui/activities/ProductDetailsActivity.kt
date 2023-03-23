package com.example.tradely.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tradely.R
import com.example.tradely.databinding.ActivityProductDetailsBinding
import com.example.tradely.utils.Constants

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private var mProductId:String = ""

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
}