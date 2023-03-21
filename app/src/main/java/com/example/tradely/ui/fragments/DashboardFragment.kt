package com.example.tradely.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import com.example.tradely.R
import com.example.tradely.databinding.FragmentDashboardBinding
import com.example.tradely.firestore.FirestoreClass
import com.example.tradely.models.Product
import com.example.tradely.ui.activities.LoginActivity
import com.example.tradely.ui.activities.SettingsActivity
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
        for (i in dashboardItemList){
            Log.i("Item Title", i.title)
        }
    }

    private fun getDashboardItemList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getDashboardItemList(this@DashboardFragment)
    }
}