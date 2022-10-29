package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R

class RecycleView_Adapter_Entities(context: Context,
    private val dataModelArrayListEntities:ArrayList<Data_Tickets>
    ):RecyclerView.Adapter<RecycleView_Adapter_Entities.MyViewHolderEntities>(){
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleView_Adapter_Entities.MyViewHolderEntities {
        val viewEntities = inflater.inflate(R.layout.recicleview_entities,parent,false)
        return MyViewHolderEntities(viewEntities)
    }

    override fun onBindViewHolder(
        holder: RecycleView_Adapter_Entities.MyViewHolderEntities,
        position: Int
    ) {
        holder.btn_entities_.text = "dataModelArrarListEntities[position].getTicketSortsDescription()"
    }

    override fun getItemCount(): Int {
        return dataModelArrayListEntities.size
    }

    inner class MyViewHolderEntities(itemEntities: View):RecyclerView.ViewHolder(itemEntities){
        var btn_entities_: Button

        init {
            btn_entities_ = itemEntities.findViewById(R.id.btn_entities)
        }
    }

}