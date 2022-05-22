package com.deltafood.adapter

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView
import com.deltafood.R
import com.deltafood.data.model.response.POInquiry
import com.deltafood.databinding.ItemPoInquiryBinding
import com.deltafood.databinding.ItemPoOrderSelectionBinding
import com.deltafood.interfaces.PoInquiryProductSelectListener
import com.deltafood.utils.formatDate

/**
 * Created by Noushad N on 28-04-2022.
 */
class POInquiryAdapter(
    private var mContext: Context,
    private var alPoInquiries : List<POInquiry>,
    private var poInquiryProductSelectListener: PoInquiryProductSelectListener
) :
    RecyclerView.Adapter<POInquiryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = alPoInquiries[position]
        holder?.binding?.tvPoNo.text = "Order no: ${item.poNo}"
        holder?.binding?.tvDate.text = "Expected Receipt Date:  ${formatDate(item.expectedDate)}"
        holder?.binding?.tvDetails.text= "UOM: ${item.uom}\nDescription: ${item.description}"
        holder?.binding?.tvRemainingQty.text = "Remaining quantity: ${item.remQty}"
        holder?.binding?.tvLineNo.text = "Line: ${item.lineNo}"
        holder?.binding?.tvProductId.text = "${item.productId}"
        holder?.binding?.llMain?.setOnClickListener {
            poInquiryProductSelectListener.onProductClick(item)
        }
    }

    override fun getItemCount(): Int {
        return alPoInquiries.size
    }

    class ViewHolder(val binding : ItemPoInquiryBinding) : RecyclerView.ViewHolder(binding?.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPoInquiryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


    }

}
