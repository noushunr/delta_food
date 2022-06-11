package com.deltafood.ui.stock_inventory.stock_change

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.deltafood.R
import com.deltafood.adapter.StockLocationAdapter
import com.deltafood.adapter.UnitAdapter
import com.deltafood.data.model.response.StockLocations
import com.deltafood.data.model.response.Unit
import com.deltafood.databinding.FragmentStockLocationBinding
import com.deltafood.databinding.FragmentUnitSelectionBinding
import com.deltafood.interfaces.StockLocationSelectListener
import com.deltafood.interfaces.UnitSelectListener
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StockLocationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StockLocationsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var productId: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentStockLocationBinding
    private var alStockLocations : ArrayList<StockLocations>?=null
    private var unit = ""
    val viewModel by activityViewModels<PurchaseViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            alStockLocations = it.getParcelableArrayList(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock_location,container,false)
        // Inflate the layout for this fragment

        alStockLocations?.let {
            if (it?.size == 0){
                binding?.tvEmpty.visibility = View.VISIBLE
            }else{
                binding?.tvEmpty.visibility = View.GONE
            }
        }
        var adapter = StockLocationAdapter(alStockLocations,object : StockLocationSelectListener{

            override fun onLocationClick(stockLocations: StockLocations) {
                viewModel?.loc.postValue(stockLocations?.location)
                viewModel?.stockLocations.postValue("${stockLocations?.location} ${stockLocations?.locType} ${stockLocations?.locCategory}")
                activity?.onBackPressed()
            }

        })
        binding?.rvLocation.adapter = adapter
//        viewModel?.liveUnits?.observe(viewLifecycleOwner){
//            adapter?.submitList(it)
//        }


        binding?.editUnit?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                
            }

            override fun afterTextChanged(p0: Editable?) {
                adapter?.filter.filter(p0)
            }

        })
        binding?.layoutBackClick.setOnClickListener {
            activity?.onBackPressed()
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UnitSelectionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: ArrayList<StockLocations>, param2: String) =
            StockLocationsFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}