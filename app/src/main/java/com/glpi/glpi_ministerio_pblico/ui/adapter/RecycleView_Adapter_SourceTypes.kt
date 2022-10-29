package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R

class RecycleView_Adapter_SourceTypes(
    context: Context,
    private val dataModelArrayListSourceTypes: ArrayList<Data_SourceTypes>,
    private val itemClickListener: onSourceTypesClickListener
):RecyclerView.Adapter<RecycleView_Adapter_SourceTypes.MyViewSourceTypesHolder>() {

    private val inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(context)
    }

    interface onSourceTypesClickListener{
        fun onSelectSourceTypesClick(sourceTypesId: String, sourceTypesName: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleView_Adapter_SourceTypes.MyViewSourceTypesHolder {
        val view = inflater.inflate(R.layout.recycleview_list_status , parent,false)
        return MyViewSourceTypesHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecycleView_Adapter_SourceTypes.MyViewSourceTypesHolder,
        position: Int
    ) {
        holder.sourceTypesName.text = dataModelArrayListSourceTypes[position].sourceTypesName
    }

    override fun getItemCount(): Int {
        return dataModelArrayListSourceTypes.size
    }

    inner class MyViewSourceTypesHolder(itemSourceTypes: View): RecyclerView.ViewHolder(itemSourceTypes){
        var sourceTypesName: TextView

        init {
            sourceTypesName = itemSourceTypes.findViewById(R.id.nameStatusAllowed) as TextView

            itemSourceTypes.setOnClickListener {
                itemClickListener.onSelectSourceTypesClick(
                    dataModelArrayListSourceTypes[position].sourceTypesId.toString(),
                    dataModelArrayListSourceTypes[position].sourceTypesName.toString()
                )
            }
        }
    }
}