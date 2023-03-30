package com.example.tradely.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tradely.R
import com.example.tradely.databinding.ActivityAddressListBinding
import com.example.tradely.firestore.FirestoreClass
import com.example.tradely.models.Address
import com.example.tradely.ui.adapters.AddressListAdapter
import com.example.tradely.utils.SwipeToDeleteCallback
import com.example.tradely.utils.SwipeToEditCallback

class AddressListActivity : BaseActivity() {

    private lateinit var binding: ActivityAddressListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupActionBar()

        binding.tvAddAddress.setOnClickListener {
            val intent = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getAddressList()
    }

    private fun getAddressList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAddressesList(this@AddressListActivity)
    }

    fun successAddressListFromFireStore(addressList: ArrayList<Address>) {
        hideProgressDialog()
        if (addressList.size > 0) {
            binding.rvAddressList.visibility = View.VISIBLE
            binding.tvNoAddressFound.visibility = View.GONE
            binding.rvAddressList.layoutManager = LinearLayoutManager(this@AddressListActivity)
            binding.rvAddressList.setHasFixedSize(true)

            val addressAdapter = AddressListAdapter(this, addressList)
            binding.rvAddressList.adapter = addressAdapter

            val editSwipeHandler = object: SwipeToEditCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = binding.rvAddressList.adapter as AddressListAdapter
                    adapter.notifyEditItem(
                        this@AddressListActivity,
                        viewHolder.adapterPosition
                    )
                }
            }
            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(binding.rvAddressList)

            val deleteSwipeHandler = object: SwipeToDeleteCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    showProgressDialog(resources.getString(R.string.please_wait))
                    FirestoreClass().deleteAddress(this@AddressListActivity, addressList[viewHolder.adapterPosition].id)
                }
            }
            val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
            deleteItemTouchHelper.attachToRecyclerView(binding.rvAddressList)


        }else{
            binding.rvAddressList.visibility = View.GONE
            binding.tvNoAddressFound.visibility = View.VISIBLE
        }
    }

    fun deleteAddressSuccess() {
        hideProgressDialog()
        Toast.makeText(this@AddressListActivity, resources.getString(R.string.err_your_address_deleted_successfully),Toast.LENGTH_LONG).show()
        getAddressList()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarAddressListActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarAddressListActivity.setNavigationOnClickListener { onBackPressed() }
    }
}