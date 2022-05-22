package com.deltafood.adapter

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.deltafood.R
import com.deltafood.data.model.response.Orders
import com.deltafood.data.model.response.Products
import com.deltafood.databinding.ItemProductsOrdersBinding
import com.deltafood.databinding.ItemProductsSelectionBinding
import com.deltafood.utils.formatDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Noushad N on 28-04-2022.
 */
class OrdersReceiveAdapter(
    private var mContext: Context,
    private var list: List<Orders>
) :
    RecyclerView.Adapter<OrdersReceiveAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = list[position]
        holder?.binding?.tvProductId.setText(item.productId)
        holder?.binding?.tvPoNo.setText("PO No: ${item.poNo}")
        holder?.binding?.tvRemainingQty.setText("Remaining :  ${item.remainingQTY}")
        holder?.binding?.tvDate.setText(formatDate(item.expectedDate))
        holder?.binding?.tvQty.setText("Quantity: ${item.poQTY}\nBP: ${item.bp}")
        holder?.binding?.tvUom.setText("UOM: ${item.uom}\nLine: ${item.poLine}")

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding : ItemProductsOrdersBinding) : RecyclerView.ViewHolder(binding?.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductsOrdersBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

}
