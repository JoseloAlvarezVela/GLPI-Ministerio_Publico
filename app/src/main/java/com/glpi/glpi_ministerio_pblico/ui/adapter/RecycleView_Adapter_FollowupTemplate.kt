package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R

class RecycleView_Adapter_FollowupTemplate(
    context: Context,
    private val dataModalArrayListFollowupTemplate: ArrayList<Data_FollowupTemplate>,
    private val itemFollowupTemplateClickListener: onFollowTemplateClickListener
):RecyclerView.Adapter<RecycleView_Adapter_FollowupTemplate.MyViewFollowupTemplateHolder>() {

    private val inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(context)
    }

    interface onFollowTemplateClickListener{
        fun onFollowupTemplateClick(
            nameFollowupTemplate: String,
            contentFollowupTemplate: String
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleView_Adapter_FollowupTemplate.MyViewFollowupTemplateHolder {
        val view = inflater.inflate(R.layout.recycleview_followup_template,parent,false)
        return MyViewFollowupTemplateHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecycleView_Adapter_FollowupTemplate.MyViewFollowupTemplateHolder,
        position: Int
    ) {
        holder.nameFollowupTemplate.text =
            dataModalArrayListFollowupTemplate[position].getnameFollowupTemplates()
    }

    override fun getItemCount(): Int {
        return dataModalArrayListFollowupTemplate.size
    }

    inner class MyViewFollowupTemplateHolder(itemFollowupTemplate: View):RecyclerView.ViewHolder(itemFollowupTemplate){
        var nameFollowupTemplate: TextView = itemView.findViewById(R.id.nombreFollowupTemplate)

        init {
            itemView.setOnClickListener {
                itemFollowupTemplateClickListener.onFollowupTemplateClick(
                    dataModalArrayListFollowupTemplate[position].getnameFollowupTemplates(),
                    dataModalArrayListFollowupTemplate[position].getcontentFollowupTemplates()
                )
            }

        }
    }
}