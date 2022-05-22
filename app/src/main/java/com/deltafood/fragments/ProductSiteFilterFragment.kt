package com.deltafood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.deltafood.R
import com.deltafood.data.model.response.Products
import com.deltafood.databinding.FragmentProductSelectioinFilterBinding
import com.deltafood.databinding.FragmentProductSiteFilterBinding
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteStockViewModel
import com.deltafood.utils.formatDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductSiteFilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductSiteFilterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentProductSiteFilterBinding
    val viewModel by activityViewModels<ProductSiteStockViewModel>()
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
        binding = FragmentProductSiteFilterBinding.inflate(layoutInflater)

        binding?.layoutClose.setOnClickListener {

            activity?.onBackPressed()
        }
        binding?.btnViewResult?.setOnClickListener {
            activity?.onBackPressed()
        }
        var products = viewModel?.filteredProducts
        if (products!=null){
            binding?.etProductName.setText(products?.productName!!)
            binding?.etCategory.setText(products?.category!!)
            binding?.etBuyer.setText(products?.buyer!!)
            binding?.etStock.setText(products?.stockManagement!!)
            binding?.etLoc.setText(products?.locManagement!!)
            binding?.etSerial.setText(products?.serialNo!!)
            binding?.etStatus.setText(products?.status!!)
        }
        binding?.btnViewResult?.setOnClickListener {
            var products = Products()
            products.productName = binding?.etProductName.text.toString()
            products.category = binding?.etCategory.text.toString()
            products.buyer = binding?.etBuyer.text.toString()
            products.stockManagement = binding?.etStock.text.toString()
            products.locManagement = binding?.etLoc.text.toString()
            products.serialNo = binding?.etSerial.text.toString()
            products.status = binding?.etStatus.text.toString()
            var isFilterCleared = products.productName.isEmpty() && products.category.isEmpty()
                    && products.buyer.isEmpty() && products.stockManagement.isEmpty() && products.locManagement.isEmpty()
                    && products.serialNo.isEmpty() && products.status.isEmpty()
            viewModel?.filterProductsForSites(products,isFilterCleared)
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
         * @return A new instance of fragment ProductSiteFilterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductSiteFilterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}