package com.deltafood.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.deltafood.R
import com.deltafood.data.model.response.Products
import com.deltafood.databinding.FragmentOrderLineSelectionFilterBinding
import com.deltafood.databinding.FragmentProductSelectioinFilterBinding
import com.deltafood.interfaces.ProductSelectionFilterListener
import com.deltafood.ui.inquiries.product_site_stock.ProductSiteStockViewModel
import com.deltafood.utils.currentDate
import com.deltafood.utils.formatDate
import com.deltafood.utils.formattedCurrentDate
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductSelectioinFilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductSelectioinFilterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var date = ""
    private lateinit var binding : FragmentProductSelectioinFilterBinding
    private lateinit var productSelectionFilterListener: ProductSelectionFilterListener
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
        binding = FragmentProductSelectioinFilterBinding.inflate(layoutInflater)

        binding?.layoutClose.setOnClickListener {
            activity?.onBackPressed()
        }
        binding?.editExpire.setOnClickListener {
            calendarShow()
        }
        binding?.rlExpiry.setOnClickListener {
            calendarShow()
        }
        binding?.editExpire.setText(currentDate())
        date = formattedCurrentDate()
        var products = viewModel?.filteredProducts
        if (products!=null){
            date = products?.expectedDate!!
            binding?.etProductName.setText(products?.productName!!)
            binding?.etOrderLine.setText(products?.orderLine!!)
            binding?.etOrderNo.setText(products?.orderNo!!)
            binding?.editExpire.setText(formatDate(products?.expectedDate!!))
            binding?.etVendorId.setText(products?.supplierId!!)
            binding?.editQty.setText(products?.remainingQTY!!)
        }
        binding?.btnViewResult?.setOnClickListener {
            var products = Products()
            products.productName = binding?.etProductName.text.toString()
            products.orderLine = binding?.etOrderLine.text.toString()
            products.orderNo = binding?.etOrderNo.text.toString()
            products.expectedDate = date
            products.supplierId = binding?.etVendorId.text.toString()
            products.remainingQTY = binding?.editQty.text.toString()
            var isFilterCleared = products.productName.isEmpty() && products.orderLine.isEmpty()
                    && products.orderNo.isEmpty() && products.expectedDate.isEmpty() && products.supplierId.isEmpty() && products.remainingQTY.isEmpty()

            viewModel?.filterProducts(products,isFilterCleared)
            activity?.onBackPressed()
        }
        // Inflate the layout for this fragment
        return binding?.root
    }

    private fun calendarShow(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
            binding?.editExpire.setText("$dayOfMonth/${String.format("%02d", monthOfYear+1)}/$year")
            date = "$year${String.format("%02d", monthOfYear+1)}$dayOfMonth"

        }, year, month, day)

        dpd.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductSelectioinFilterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductSelectioinFilterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}