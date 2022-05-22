package com.deltafood.adapter

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView
import com.deltafood.R
import com.deltafood.data.model.response.Locations
import com.deltafood.data.model.response.Products
import com.deltafood.databinding.ItemLocationSelectionBinding
import com.deltafood.databinding.ItemProductsSelectionBinding
import com.deltafood.interfaces.LocationSelectListener

/**
 * Created by Noushad N on 28-04-2022.
 */
class LocationSelectionAdapter(
    private var mContext: Context,
    private var items: MutableList<Locations>?,
    var locationSelectListener: LocationSelectListener
) :
    RecyclerView.Adapter<LocationSelectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)
    
    fun submitList(items: MutableList<Locations>?){
        this.items?.clear()
        this.items?.addAll(items!!)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items?.get(position)
        holder?.binding?.llMain?.setOnClickListener {
            locationSelectListener?.onLocationClick(item!!)
        }
        holder?.binding?.tvLocationName.setText("Location: ${item?.locationName}")
        holder?.binding?.tvWarehouse.setText("Warehouse: ${item?.wareHouse}")
        holder?.binding?.tvType.setText("Location Type: ${item?.locType}\nLoc Cat:  ${item?.locCategory}\nCount in Progress:  ${item?.countInProgress}\nStorage Location:  ${item?.storageLocation}")
        holder?.binding?.tvLocDetails.setText("Blocked: ${item?.blocked}\nDedicated: ${item?.dedicate}\nSingle Product: ${item?.singleProduct}\nReplenish: ${item?.replenish}\nCap.man: ${item?.capMan}")
    }

    override fun getItemCount(): Int {
        return items?.size!!
    }

    class ViewHolder(val binding : ItemLocationSelectionBinding) : RecyclerView.ViewHolder(binding?.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLocationSelectionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

}
