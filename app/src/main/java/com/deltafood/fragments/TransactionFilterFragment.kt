package com.deltafood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.deltafood.R
import com.deltafood.data.model.response.Status
import com.deltafood.data.model.response.Transactions
import com.deltafood.databinding.FragmentOrderLineSelectionFilterBinding
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
class TransactionFilterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val viewModel by activityViewModels<PurchaseViewModel>()
    private lateinit var binding : FragmentTransactionFilterBinding
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
        binding = FragmentTransactionFilterBinding.inflate(layoutInflater)
        var transactions = viewModel?.filteredTransactions
        if (transactions!=null){
            binding?.etName?.setText(transactions.name)
        }
        binding?.layoutClose.setOnClickListener {

            activity?.onBackPressed()
        }
        binding?.btnViewResult?.setOnClickListener {
            var transactions = Transactions()
            transactions?.name = binding?.etName?.text.toString()

            var isFilterCleared = transactions?.name!!.isEmpty()
            viewModel?.filterTransactions(transactions,isFilterCleared)
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
            TransactionFilterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}