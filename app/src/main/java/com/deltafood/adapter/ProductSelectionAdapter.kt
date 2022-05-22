package com.deltafood.adapter

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.deltafood.R
import com.deltafood.data.model.response.Products
import com.deltafood.databinding.ItemProductsSelectionBinding
import com.deltafood.interfaces.ProductSelectListener
import com.deltafood.interfaces.SiteSelectListener
import com.deltafood.utils.formatDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Noushad N on 28-04-2022.
 */
class ProductSelectionAdapter(
    private var mContext: Context,
    private var items: MutableList<Products>?,
    var productSelectListener: ProductSelectListener
) :
    RecyclerView.Adapter<ProductSelectionAdapter.ViewHolder>(), Filterable {
    private var filteredItems: MutableList<Products>? = items
    private val adapterScope = CoroutineScope(Dispatchers.Default)
    private val diffCallBack = object : DiffUtil.ItemCallback<Products>() {
        override fun areItemsTheSame(
            oldItem: Products,
            newItem: Products
        ): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(
            oldItem: Products,
            newItem: Products
        ): Boolean {
            return oldItem.productName == newItem.productName
        }

    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    fun submitList(list: List<Products>) {
        filteredItems?.clear()
        filteredItems?.addAll(list)
        notifyDataSetChanged()
//        adapterScope.launch {
//            withContext(Dispatchers.Main) {
//                differ.submitList(list.toList())
//            }
//        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = filteredItems?.get(position)
        holder?.binding?.tvProductName.setText(item?.productName)
        holder?.binding?.tvProductId.setText(item?.productId)
        holder?.binding?.tvQty.setText("Supplier Id: ${item?.supplierId}\nRemaining Quantity: ${item?.remainingQTY}")
        holder?.binding?.tvOrders.setText("Order No: ${item?.orderNo}\nOrder Line: ${item?.orderLine}\n" +
                "Expected Receipt Date: ${formatDate(item?.expectedDate!!)}")
        holder?.binding?.llMain?.setOnClickListener {
            productSelectListener?.onProductClick(item!!)
        }
    }

    override fun getItemCount(): Int {
        return filteredItems?.size!!
    }

    class ViewHolder(val binding : ItemProductsSelectionBinding) : RecyclerView.ViewHolder(binding?.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductsSelectionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) filteredItems = items else {
                    val filteredList = ArrayList<Products>()
                    items?.filter {
                        (it.productName?.contains(constraint!!,ignoreCase = true)!!)

                    }?.forEach { filteredList.add(it) }
                    filteredItems = filteredList

                }
                return FilterResults().apply { values = filteredItems }
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                filteredItems = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Products>
                notifyDataSetChanged()
            }

        }
    }

}
