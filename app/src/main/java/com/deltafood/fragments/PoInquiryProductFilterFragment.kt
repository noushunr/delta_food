package com.deltafood.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.deltafood.R
import com.deltafood.data.model.response.POInquiry
import com.deltafood.databinding.FragmentPoInquiryProductFilterBinding
import com.deltafood.databinding.FragmentPoOrderFilterBinding
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
 * Use the [PoInquiryProductFilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PoInquiryProductFilterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var date = ""
    val viewModel by activityViewModels<POInquiryViewModel>()
    private lateinit var binding : FragmentPoInquiryProductFilterBinding
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
        binding = FragmentPoInquiryProductFilterBinding.inflate(layoutInflater)

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
        var filteredPoInquiry = viewModel?.filteredPoInquiry
        if (filteredPoInquiry!=null){
            binding?.etPoNo.setText(filteredPoInquiry?.poNo!!)
            binding?.etProduct.setText(filteredPoInquiry?.productId!!)
            binding?.etUom.setText(filteredPoInquiry?.uom!!)
            binding?.etLine.setText(filteredPoInquiry?.lineNo!!)
            binding?.etQty.setText(filteredPoInquiry?.remQty!!)
            binding?.editExpire.setText(formatDate(filteredPoInquiry?.expectedDate!!))
            date = filteredPoInquiry?.expectedDate!!
        }
        binding?.btnViewResult?.setOnClickListener {
            var poInquiry = POInquiry()
            poInquiry.remQty = binding?.etQty.text.toString()
            poInquiry.poNo = binding?.etPoNo.text.toString()
            poInquiry.uom = binding?.etUom.text.toString()
            poInquiry.lineNo = binding?.etLine.text.toString()
            poInquiry.productId = binding?.etProduct.text.toString()
            poInquiry.expectedDate = date
            var isFilterCleared = poInquiry.remQty.isEmpty() && poInquiry.poNo.isEmpty()
                    && poInquiry.uom.isEmpty() && poInquiry.lineNo.isEmpty()
                    && poInquiry.productId.isEmpty() && poInquiry.expectedDate.isEmpty()

            viewModel?.filterPOInquiries(poInquiry,isFilterCleared)
            activity?.onBackPressed()
        }
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
         * @return A new instance of fragment PoInquiryProductFilterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PoInquiryProductFilterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}