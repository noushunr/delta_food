package com.deltafood.adapter

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.deltafood.R
import com.deltafood.data.preferences.PrefManager
import com.deltafood.interfaces.SiteSelectListener

/**
 * Created by Noushad N on 28-04-2022.
 */
class SitesAdapter(
    private var mContext: Context,
    private var alSites : List<String>,
    var lastPosition: Int,
    var siteSelectListener: SiteSelectListener
) :
    RecyclerView.Adapter<SitesAdapter.SiteViewHolder>() {
    var lastCheckedPosition = -1
    init {
        lastCheckedPosition = lastPosition
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiteViewHolder {
        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_sites, parent, false)
        return SiteViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: SiteViewHolder, position: Int) {
        holder.tvSiteName.setText(alSites.get(position))
        holder.checkBox.isChecked = position == lastCheckedPosition
        holder?.rlMain?.setOnClickListener {
            lastCheckedPosition = position
            notifyDataSetChanged()
            siteSelectListener.onSiteClick(alSites[position])
        }
//        holder?.checkBox?.setOnCheckedChangeListener { compoundButton, b ->
//            if (b){
//                lastCheckedPosition = position
//                notifyDataSetChanged()
//            }
//        }

    }

    override fun getItemCount(): Int {
        return alSites.size
    }

    class SiteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvSiteName: TextView = itemView.findViewById<TextView>(R.id.site_name)
        var checkBox = itemView.findViewById<AppCompatCheckBox>(R.id.checkbox)
        var rlMain = itemView.findViewById<RelativeLayout>(R.id.rl_main)

    }

}
