package com.example.tradely.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tradely.R
import com.example.tradely.databinding.FragmentDashboardBinding
import com.example.tradely.firestore.FirestoreClass
import com.example.tradely.models.Product
import com.example.tradely.ui.activities.LoginActivity
import com.example.tradely.ui.activities.ProductDetailsActivity
import com.example.tradely.ui.activities.SettingsActivity
import com.example.tradely.ui.adapters.DashboardItemListAdapter
import com.example.tradely.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class DashboardFragment : BaseFragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        getDashboardItemList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
//        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_settings -> {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null){
                    startActivity(Intent(activity, SettingsActivity::class.java))
                } else {
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successDashboardItemList(dashboardItemList: ArrayList<Product>){
        hideProgressDialog()
        if (dashboardItemList.size > 0){
            binding.rvDashboardItems.visibility = View.VISIBLE
            binding.tvNoDashboardItemsFound.visibility = View.GONE

            binding.rvDashboardItems.layoutManager = GridLayoutManager(activity,2)
            binding.rvDashboardItems.setHasFixedSize(true)
            val adapter = DashboardItemListAdapter(requireActivity(),dashboardItemList)
            binding.rvDashboardItems.adapter = adapter

            adapter.setOnClickLister(object: DashboardItemListAdapter.OnClickListener{
                override fun onClick(position: Int, product: Product) {
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.product_id)
                    startActivity(intent)
                }
            })
        } else {
            binding.rvDashboardItems.visibility = View.GONE
            binding.tvNoDashboardItemsFound.visibility = View.VISIBLE
        }
    }

    private fun getDashboardItemList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getDashboardItemList(this@DashboardFragment)
    }
}