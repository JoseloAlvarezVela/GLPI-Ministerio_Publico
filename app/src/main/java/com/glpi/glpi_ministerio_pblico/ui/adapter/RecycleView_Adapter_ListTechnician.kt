package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R

class RecycleView_Adapter_ListTechnician(
    context: Context,
    private val dataModelArrayListTechnician: ArrayList<Data_ListTechnician>,
    private val itemClickListener: onListTechnicianClickListener
):RecyclerView.Adapter<RecycleView_Adapter_ListTechnician.MyViewListTechnicianHolder>() {

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    interface onListTechnicianClickListener{
        fun onSelectTechnicianClick(
            listStatusAllowedId: String,
            listStatusAllowedName: String,
            listTechnicianLastName: String
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleView_Adapter_ListTechnician.MyViewListTechnicianHolder {
        val view = inflater.inflate(R.layout.recycleview_list_status,parent,false)
        return MyViewListTechnicianHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecycleView_Adapter_ListTechnician.MyViewListTechnicianHolder,
        position: Int
    ) {
        holder.listTechnicianName.text = "${dataModelArrayListTechnician[position].listTechnicianName} ${dataModelArrayListTechnician[position].listTechnicianLastName}"
    }

    override fun getItemCount(): Int {
        return dataModelArrayListTechnician.size
    }

    inner class MyViewListTechnicianHolder(itemListTechnician: View):RecyclerView.ViewHolder(itemListTechnician){

        var listTechnicianName: TextView

        init {

            listTechnicianName = itemListTechnician.findViewById<TextView>(R.id.nameStatusAllowed) as TextView

            itemListTechnician.setOnClickListener {
                itemClickListener.onSelectTechnicianClick(
                    dataModelArrayListTechnician[position].listTechnicianId.toString(),
                    dataModelArrayListTechnician[position].listTechnicianName.toString(),
                    dataModelArrayListTechnician[position].listTechnicianLastName.toString()
                )
            }
        }
    }
}