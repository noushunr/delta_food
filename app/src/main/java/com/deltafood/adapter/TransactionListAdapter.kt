package com.deltafood.adapter

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView
import com.deltafood.R
import com.deltafood.data.model.response.Locations
import com.deltafood.data.model.response.Products
import com.deltafood.data.model.response.Transactions
import com.deltafood.databinding.ItemLocationSelectionBinding
import com.deltafood.databinding.ItemProductsSelectionBinding
import com.deltafood.databinding.ItemTransactionsBinding
import com.deltafood.interfaces.LocationSelectListener
import com.deltafood.interfaces.TransactionSelectListener

/**
 * Created by Noushad N on 28-04-2022.
 */
class TransactionListAdapter(
    private var items: MutableList<Transactions>?,
    var transactionSelectListener: TransactionSelectListener
) :
    RecyclerView.Adapter<TransactionListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)
    
    fun submitList(items: MutableList<Transactions>?){
        this.items?.clear()
        this.items?.addAll(items!!)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items?.get(position)
        holder?.binding?.llMain?.setOnClickListener {
            transactionSelectListener?.onTransactionClick(item!!)
        }
        holder?.binding?.tvCode.text = "${item?.code}"
        holder?.binding?.tvName.text = "${item?.name}"

    }

    override fun getItemCount(): Int {
        return items?.size!!
    }

    class ViewHolder(val binding : ItemTransactionsBinding) : RecyclerView.ViewHolder(binding?.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTransactionsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

}
