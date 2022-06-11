package com.deltafood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.deltafood.R
import com.deltafood.data.model.response.Sites
import com.deltafood.data.model.response.Status
import com.deltafood.data.model.response.Transactions
import com.deltafood.databinding.FragmentOrderLineSelectionFilterBinding
import com.deltafood.databinding.FragmentSiteFilterBinding
import com.deltafood.databinding.FragmentStockFilterBinding
import com.deltafood.databinding.FragmentTransactionFilterBinding
import com.deltafood.ui.inquiries.location_wise.LocationWiseViewModel
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StockFilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SiteFilterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val viewModel by activityViewModels<PurchaseViewModel>()
    private lateinit var binding : FragmentSiteFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSiteFilterBinding.inflate(layoutInflater)
        var sites = viewModel?.filteredSites
        if (sites!=null){
            binding?.etName?.setText(sites.name)
            binding?.etCompanyName?.setText(sites.companyName)
            binding?.etSite?.setText(sites.site)
        }
        binding?.layoutClose.setOnClickListener {

            activity?.onBackPressed()
        }
        binding?.btnViewResult?.setOnClickListener {
            var sites = Sites()
            sites?.name = binding?.etName?.text.toString()
            sites?.companyName = binding?.etCompanyName?.text.toString()
            sites?.site = binding?.etSite?.text.toString()

            var isFilterCleared = sites?.name!!.isEmpty() && sites?.companyName!!.isEmpty() && sites?.site!!.isEmpty()
            viewModel?.filterSites(sites,isFilterCleared)
            activity?.onBackPressed()
        }
        return binding?.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StockFilterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SiteFilterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}