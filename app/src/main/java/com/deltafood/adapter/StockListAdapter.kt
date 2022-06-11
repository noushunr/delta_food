package com.deltafood.adapter

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView
import com.deltafood.R
import com.deltafood.data.model.response.Locations
import com.deltafood.data.model.response.Products
import com.deltafood.data.model.response.Stock
import com.deltafood.data.model.response.Transactions
import com.deltafood.databinding.ItemLocationSelectionBinding
import com.deltafood.databinding.ItemProductsSelectionBinding
import com.deltafood.databinding.ItemStockBinding
import com.deltafood.databinding.ItemTransactionsBinding
import com.deltafood.interfaces.LocationSelectListener
import com.deltafood.interfaces.StockSelectListener
import com.deltafood.interfaces.TransactionSelectListener

/**
 * Created by Noushad N on 28-04-2022.
 */
class StockListAdapter(
    private var items: MutableList<Stock>?,
    var stockSelectListener: StockSelectListener
) :
    RecyclerView.Adapter<StockListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)
    
    fun submitList(items: MutableList<Stock>?){
        this.items?.clear()
        this.items?.addAll(items!!)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items?.get(position)
        holder?.binding?.llMain?.setOnClickListener {
            stockSelectListener?.onStockClick(item!!)
        }
        holder?.binding?.tvSlNo.text = "Serial Number: ${item?.serial}"
        holder?.binding?.tvLot.text = "Lot: ${item?.lot}"

    }

    override fun getItemCount(): Int {
        return items?.size!!
    }

    class ViewHolder(val binding : ItemStockBinding) : RecyclerView.ViewHolder(binding?.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemStockBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

}
