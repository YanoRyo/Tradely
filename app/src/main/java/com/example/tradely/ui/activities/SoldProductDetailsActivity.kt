package com.example.tradely.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tradely.databinding.ActivitySoldProductDetailsBinding
import com.example.tradely.models.SoldProduct
import com.example.tradely.utils.Constants

class SoldProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySoldProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoldProductDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var productDetails: SoldProduct = SoldProduct()
        if (intent.hasExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)) {
            productDetails = intent.getParcelableExtra<SoldProduct>(Constants.EXTRA_SOLD_PRODUCT_DETAILS)!!
        }
    }
}