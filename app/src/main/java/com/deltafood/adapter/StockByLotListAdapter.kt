package com.deltafood.adapter

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView
import com.deltafood.R
import com.deltafood.data.model.response.*
import com.deltafood.databinding.*
import com.deltafood.interfaces.LocationSelectListener
import com.deltafood.interfaces.StockByLotSelectListener
import com.deltafood.interfaces.StockSelectListener
import com.deltafood.interfaces.TransactionSelectListener
import com.deltafood.utils.formatDate

/**
 * Created by Noushad N on 28-04-2022.
 */
class StockByLotListAdapter(
    private var items: MutableList<StockByLot>?,
    var stockSelectListener: StockByLotSelectListener
) :
    RecyclerView.Adapter<StockByLotListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)
    
    fun submitList(items: MutableList<StockByLot>?){
        this.items?.clear()
        this.items?.addAll(items!!)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items?.get(position)
        holder?.binding?.root?.setOnClickListener {
            stockSelectListener?.onStockClick(item!!)
        }
        holder?.binding?.tvSublot.text = "Sublot: ${item?.slot}"
        holder?.binding?.tvLot.text = "Lot: ${item?.lot}"
        holder?.binding?.tvExpDate.text = "Expiration Date:  ${formatDate(item?.expDate!!)}"

    }

    override fun getItemCount(): Int {
        return items?.size!!
    }

    class ViewHolder(val binding : ItemStockByLotBinding) : RecyclerView.ViewHolder(binding?.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemStockByLotBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

}
