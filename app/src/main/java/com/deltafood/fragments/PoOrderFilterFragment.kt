package com.deltafood.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.deltafood.R
import com.deltafood.data.model.response.Locations
import com.deltafood.data.model.response.PurchaseOrder
import com.deltafood.databinding.FragmentFilterBinding
import com.deltafood.databinding.FragmentPoOrderFilterBinding
import com.deltafood.ui.inquiries.location_wise.LocationWiseViewModel
import com.deltafood.ui.inquiries.po_inquiry.POInquiryViewModel
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
 * Use the [FilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PoOrderFilterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null
    private var date = ""
    val viewModel by activityViewModels<POInquiryViewModel>()
    private lateinit var binding : FragmentPoOrderFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPoOrderFilterBinding.inflate(layoutInflater)

        binding?.layoutClose.setOnClickListener {

            activity?.onBackPressed()
        }
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
        var filteredOrders = viewModel?.filteredOrders
        if (filteredOrders!=null){
            binding?.etOrder.setText(filteredOrders?.poNo!!)
            binding?.etSite.setText(filteredOrders?.siteId!!)
            binding?.etVendor.setText(filteredOrders?.vendorId!!)
            binding?.etRef.setText(filteredOrders?.ref!!)
            binding?.editExpire.setText(formatDate(filteredOrders?.expectedDate!!))
            date = filteredOrders?.expectedDate!!
        }
        binding?.btnViewResult?.setOnClickListener {
            var purchaseOrder = PurchaseOrder()
            purchaseOrder.poNo = binding?.etOrder.text.toString()
            purchaseOrder.siteId = binding?.etSite.text.toString()
            purchaseOrder.vendorId = binding?.etVendor.text.toString()
            purchaseOrder.expectedDate = date
            purchaseOrder.ref = binding?.etRef.text.toString()
            var isFilterCleared = purchaseOrder.poNo.isEmpty() && purchaseOrder.siteId.isEmpty()
                    && purchaseOrder.vendorId.isEmpty() && purchaseOrder.expectedDate.isEmpty()
                    && purchaseOrder.ref.isEmpty()

            viewModel?.filterOrders(purchaseOrder,isFilterCleared)
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
         * @return A new instance of fragment FilterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            PoOrderFilterFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}