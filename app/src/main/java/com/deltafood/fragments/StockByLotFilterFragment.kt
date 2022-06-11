package com.deltafood.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import com.deltafood.R
import com.deltafood.data.model.response.PurchaseOrder
import com.deltafood.data.model.response.StockByLot
import com.deltafood.databinding.FragmentStockByLotFilterBinding
import com.deltafood.ui.stock_inventory.purchase_receipt.PurchaseViewModel
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
 * Use the [StockByLotFilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StockByLotFilterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentStockByLotFilterBinding
    var date = ""
    val viewModel by activityViewModels<PurchaseViewModel>()
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
        binding = FragmentStockByLotFilterBinding.inflate(layoutInflater)
        var stockByLot = viewModel?.filteredStockByLot
        binding?.editExpire.setText(currentDate())
        date = formattedCurrentDate()
        if (stockByLot!=null){
            binding?.etLot.setText(stockByLot?.lot!!)
            binding?.etSlot.setText(stockByLot?.slot!!)

            binding?.editExpire.setText(formatDate(stockByLot?.expDate!!))
            date = stockByLot?.expDate!!
        }
        binding?.layoutClose.setOnClickListener {
            activity?.onBackPressed()
        }
        binding?.btnViewResult?.setOnClickListener {
            activity?.onBackPressed()
        }
        binding?.rlExpiry?.setOnClickListener {
            calendarShow(binding?.editExpire)
        }
        binding?.editExpire?.setOnClickListener {
            calendarShow(binding?.editExpire)
        }
        binding?.rlUseBy?.setOnClickListener {
            calendarShow(binding?.editUseBy)
        }
        binding?.editUseBy?.setOnClickListener {
            calendarShow(binding?.editUseBy)
        }
        binding?.btnViewResult?.setOnClickListener {
            var stockByLot = StockByLot()
            stockByLot.lot = binding?.etLot.text.toString()
            stockByLot.slot = binding?.etSlot.text.toString()
            stockByLot.expDate = date

            var isFilterCleared = stockByLot.lot.isEmpty() && stockByLot.lot.isEmpty()
                    && stockByLot.expDate!!.isEmpty()

            viewModel?.filterStockLot(stockByLot,isFilterCleared)
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
         * @return A new instance of fragment StockByLotFilterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StockByLotFilterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun calendarShow(editText: EditText){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(requireActivity(), { view, year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
            binding?.editExpire.setText("$dayOfMonth/${String.format("%02d", monthOfYear+1)}/$year")
            date = "$year${String.format("%02d", monthOfYear+1)}$dayOfMonth"

        }, year, month, day)

        dpd.show()
    }
}