package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R

class RecycleViw_Adapter_ListStatusAllowed(
    context: Context,
    private val dataModelArrayListStatusAllowed:ArrayList<Data_ListStatusAllowed>,
    private val itemClickListener: onStatusAllowedClickListener
    ):RecyclerView.Adapter<RecycleViw_Adapter_ListStatusAllowed.MyViewStatusAllowedHolder>(){

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    interface onStatusAllowedClickListener{
        fun onSelectStatusClick(listStatusAllowedId: String, listStatusAllowedName: String)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleViw_Adapter_ListStatusAllowed.MyViewStatusAllowedHolder {
        val view = inflater.inflate(R.layout.recycleview_list_status,parent,false)
        return MyViewStatusAllowedHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecycleViw_Adapter_ListStatusAllowed.MyViewStatusAllowedHolder,
        position: Int
    ) {
        holder.listStatusAllowedName.text = dataModelArrayListStatusAllowed[position].listStatusAllowedName
    }

    override fun getItemCount(): Int {
        return dataModelArrayListStatusAllowed.size
    }

    inner class MyViewStatusAllowedHolder(itemStatusAllowed: View):RecyclerView.ViewHolder(itemStatusAllowed){

        var listStatusAllowedName: TextView
        init {

            listStatusAllowedName = itemStatusAllowed.findViewById<TextView>(R.id.nameStatusAllowed) as TextView

            itemStatusAllowed.setOnClickListener {
                itemClickListener.onSelectStatusClick(
                    dataModelArrayListStatusAllowed[position].listStatusAllowedId.toString(),
                    dataModelArrayListStatusAllowed[position].listStatusAllowedName.toString()
                )
            }
        }
    }
}