package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecycleView_Adapter_TicketsHistorico(contexto:Context,private val dataModelArrayList:ArrayList<Data_Tickets>):
    RecyclerView.Adapter<RecycleView_Adapter_Tickets.MyViewHolder>(){

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(contexto)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleView_Adapter_Tickets.MyViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecycleView_Adapter_Tickets.MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}