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
import com.deltafood.interfaces.SiteSelectListener
import com.deltafood.interfaces.StockSelectListener
import com.deltafood.interfaces.TransactionSelectListener

/**
 * Created by Noushad N on 28-04-2022.
 */
class SiteListAdapter(
    private var items: MutableList<Sites>?,
    var siteSelectListener: SiteSelectListener
) :
    RecyclerView.Adapter<SiteListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)
    
    fun submitList(items: MutableList<Sites>?){
        this.items?.clear()
        this.items?.addAll(items!!)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items?.get(position)
        holder?.binding?.llMain?.setOnClickListener {
            siteSelectListener?.onSitesClick(item!!)
        }
        holder?.binding?.tvCompany.text = "Company Name: ${item?.companyName}"
        holder?.binding?.tvSite.text = "Site: ${item?.site}"
        holder?.binding?.tvDetails.text = "Name: ${item?.name}\nDescription: ${item?.description}"

    }

    override fun getItemCount(): Int {
        return items?.size!!
    }

    class ViewHolder(val binding : ItemSiteBinding) : RecyclerView.ViewHolder(binding?.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSiteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

}
