package com.deltafood.adapter

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView
import com.deltafood.R
import com.deltafood.data.model.response.Locations
import com.deltafood.data.model.response.Status
import com.deltafood.databinding.ItemLocationSelectionBinding
import com.deltafood.databinding.ItemStockStatusBinding
import com.deltafood.interfaces.StatusSelectListener

/**
 * Created by Noushad N on 28-04-2022.
 */
class StockStatusAdapter(
    private var mContext: Context,
    private var items : MutableList<Status>,
    private var statusSelectListener: StatusSelectListener
) :
    RecyclerView.Adapter<StockStatusAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)
    fun submitList(items: MutableList<Status>?){
        this.items?.clear()
        this.items?.addAll(items!!)
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items[position]
        holder?.binding?.tvStatus.text = "Status : ${item.status}"
        holder?.binding?.tvDescription.text = "Status : ${item.description}"
        holder?.binding?.llStatus?.setOnClickListener {
            statusSelectListener.onStatusClick(item)
        }
    }

    override fun getItemCount(): Int {
        return items?.size!!
    }

    class ViewHolder(val binding : ItemStockStatusBinding) : RecyclerView.ViewHolder(binding?.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemStockStatusBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

}
