package com.deltafood.adapter

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView
import com.deltafood.R

/**
 * Created by Noushad N on 28-04-2022.
 */
class OrderSelectionAdapter(
    private var mContext: Context
) :
    RecyclerView.Adapter<OrderSelectionAdapter.OrderSelection>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderSelection {
        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_order_selection, parent, false)
        return OrderSelection(
            view
        )
    }

    override fun onBindViewHolder(holder: OrderSelection, position: Int) {
    }

    override fun getItemCount(): Int {
        return 10
    }

    class OrderSelection(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

}
