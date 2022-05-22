package com.deltafood.adapter

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView
import com.deltafood.R
import com.deltafood.data.model.response.Locations
import com.deltafood.data.model.response.PurchaseOrder
import com.deltafood.databinding.ItemLocationSelectionBinding
import com.deltafood.databinding.ItemPoOrderSelectionBinding
import com.deltafood.interfaces.LocationSelectListener
import com.deltafood.interfaces.PurchaseOrderSelectListener
import com.deltafood.utils.formatDate

/**
 * Created by Noushad N on 28-04-2022.
 */
class POOrderSelectionAdapter(
    private var mContext: Context,
    private var items: MutableList<PurchaseOrder>?,
    var purchaseOrderSelectListener: PurchaseOrderSelectListener
) :
    RecyclerView.Adapter<POOrderSelectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)

    fun submitList(items: MutableList<PurchaseOrder>?){
        this.items?.clear()
        this.items?.addAll(items!!)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position)!!
        holder?.binding?.tvPoNo.text = "Order No: ${item.poNo}"
        holder?.binding?.tvDate.text = "Expected Receipt Date:  ${formatDate(item.expectedDate)}"
        holder?.binding?.tvDetails.text= "Order Site: ${item.siteId}\nInternal or order reference: ${item.ref}"
        holder?.binding?.tvVendor.text = "Supplier: ${item.vendorId}"
        holder?.binding?.llMain?.setOnClickListener {
            purchaseOrderSelectListener.onOrderClick(item)
        }
    }

    override fun getItemCount(): Int {
        return items?.size!!
    }

    class ViewHolder(val binding : ItemPoOrderSelectionBinding) : RecyclerView.ViewHolder(binding?.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPoOrderSelectionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

}
