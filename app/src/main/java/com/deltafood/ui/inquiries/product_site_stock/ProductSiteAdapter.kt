package com.deltafood.ui.inquiries.product_site_stock

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.deltafood.R
import com.deltafood.adapter.ProductSelectionAdapter
import com.deltafood.data.model.response.Products
import com.deltafood.databinding.ItemProductsSelectionBinding
import com.deltafood.databinding.ItemProductsSiteBinding
import com.deltafood.interfaces.ProductSelectListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Noushad N on 28-04-2022.
 */
class ProductSiteAdapter(
    private var mContext: Context,
    private var items: MutableList<Products>?,
    var productSelectListener: ProductSelectListener
) :
    RecyclerView.Adapter<ProductSiteAdapter.ViewHolder>() {
    private var filteredItems: MutableList<Products>? = items
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
        holder?.binding?.tvProductName.text = item?.productName
        holder?.binding?.tvProductId.text = item?.productId
        holder?.binding?.tvCategory.text = "Category: ${item?.category}"
        holder?.binding?.tvDetails.text= "Stock Management: ${item?.stockManagement}\nLot Management: ${item?.locManagement}\nSerial No Management: ${item?.serialNo}\nBuyer: ${item?.buyer}"
        holder?.binding?.tvStatus.text = "Product status: ${item?.status}\nCount Mode: ${item?.countMode}"
        holder?.binding?.llMain?.setOnClickListener {
            productSelectListener?.onProductClick(item!!)
        }
    }

    override fun getItemCount(): Int {
        return filteredItems?.size!!
    }

    class ViewHolder(val binding : ItemProductsSiteBinding) : RecyclerView.ViewHolder(binding?.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductsSiteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

}
