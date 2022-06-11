package com.deltafood.adapter

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.deltafood.data.model.response.StockLocations
import com.deltafood.data.model.response.Unit
import com.deltafood.databinding.ItemUnitBinding
import com.deltafood.interfaces.StockLocationSelectListener
import com.deltafood.interfaces.UnitSelectListener

/**
 * Created by Noushad N on 28-04-2022.
 */
class StockLocationAdapter(
    private var items: MutableList<StockLocations>?,
    private var stockLocationSelectListener: StockLocationSelectListener

) :
    RecyclerView.Adapter<StockLocationAdapter.ViewHolder>(), Filterable {
    private var filteredItems: MutableList<StockLocations>? = items

    fun submitList(list: List<StockLocations>) {
        filteredItems?.clear()
        filteredItems?.addAll(list)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
       ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = filteredItems?.get(position)

        holder?.binding?.tvUnits.text = "${item?.location} ${item?.locType} ${item?.locCategory}"
        holder?.binding?.tvUnits.setOnClickListener {
            stockLocationSelectListener?.onLocationClick(item!!)
        }
    }

    override fun getItemCount(): Int {
        return filteredItems?.size!!
    }

    class ViewHolder(val binding : ItemUnitBinding) : RecyclerView.ViewHolder(binding?.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemUnitBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) filteredItems = items else {
                    val filteredList = ArrayList<StockLocations>()
                    items?.filter {
                        (it.location?.contains(constraint!!,ignoreCase = true)!! || it.locCategory?.contains(constraint!!,ignoreCase = true)!!)

                    }?.forEach { filteredList.add(it) }
                    filteredItems = filteredList

                }
                return FilterResults().apply { values = filteredItems }
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                filteredItems = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<StockLocations>
                notifyDataSetChanged()
            }

        }
    }
}
