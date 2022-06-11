package com.deltafood.adapter

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView
import com.deltafood.R
import com.deltafood.data.model.response.Orders
import com.deltafood.data.model.response.SearchedLocations
import com.deltafood.databinding.ItemLocationWiseBinding
import com.deltafood.databinding.ItemProductsOrdersBinding
import com.deltafood.utils.formatDate

/**
 * Created by Noushad N on 28-04-2022.
 */
class LocationWiseAdapter(
    private var mContext: Context,
    private var list: List<SearchedLocations>

) :
    RecyclerView.Adapter<LocationWiseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = list[position]
        holder?.binding?.tvProduct.text = item.productId
        holder?.binding?.tvLocationName.text = "Lot: ${item.locationName}"
        holder?.binding?.tvSubLoc.text = "Sublot:  ${item.subLoc}"
        holder?.binding?.tvSerial.text = "Serial No:  ${item.serialNo}"
        var quantityDetails = "PAC Quantity: ${item.pacQuantity}\nPacking Unit: ${item.packingUnit}\nAllocated Quantity: ${item.allowedQuantity}\nSTK Quantity: ${item.stkQuantity}"
        if (item?.location?.isNotEmpty()){
            quantityDetails += "\nLocation: ${item.location}"
        }
        holder?.binding?.tvQuantity.text = quantityDetails

        holder?.binding?.tvDetails.text = "Owner: ${item.owner}\nStatus: ${item.status}"
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding : ItemLocationWiseBinding) : RecyclerView.ViewHolder(binding?.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLocationWiseBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

}
