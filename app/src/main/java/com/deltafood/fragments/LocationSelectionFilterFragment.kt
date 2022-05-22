package com.deltafood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.deltafood.R
import com.deltafood.data.model.response.Locations
import com.deltafood.data.model.response.Products
import com.deltafood.databinding.FragmentLocationSelectionFilterBinding
import com.deltafood.databinding.FragmentProductSelectioinFilterBinding
import com.deltafood.databinding.FragmentStockByLotFilterBinding
import com.deltafood.interfaces.ProductSelectionFilterListener
import com.deltafood.ui.inquiries.location_wise.LocationWiseViewModel
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteStockViewModel
import com.deltafood.utils.formatDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LocationSelectionFilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocationSelectionFilterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val viewModel by activityViewModels<LocationWiseViewModel>()
    private lateinit var binding : FragmentLocationSelectionFilterBinding
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
        // Inflate the layout for this fragment
        binding = FragmentLocationSelectionFilterBinding.inflate(layoutInflater)

        binding?.layoutClose.setOnClickListener {

            activity?.onBackPressed()
        }
        var locations = viewModel?.filteredLocations
        if (locations!=null){
            binding?.etLocName.setText(locations?.locationName!!)
            binding?.etWareHouse.setText(locations?.wareHouse!!)
            binding?.etLocType.setText(locations?.locType!!)
            binding?.etStorage.setText(locations?.storageLocation!!)
        }
        binding?.btnViewResult?.setOnClickListener {
            var locations = Locations()
            locations.locationName = binding?.etLocName.text.toString()
            locations.wareHouse = binding?.etWareHouse.text.toString()
            locations.locType = binding?.etLocType.text.toString()
            locations.storageLocation = binding?.etStorage.text.toString()
            var isFilterCleared = locations.locationName.isEmpty() && locations.wareHouse.isEmpty()
                    && locations.locType.isEmpty() && locations.storageLocation.isEmpty()

            viewModel?.filterLocations(locations,isFilterCleared)
            activity?.onBackPressed()
        }

        // Inflate the layout for this fragment
        return binding?.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LocationSelectionFilterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LocationSelectionFilterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}