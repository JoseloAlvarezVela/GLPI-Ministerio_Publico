package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R

class RecycleView_Adapter_SolutionType(
    context: Context,
    private val dataModalArrayListSolutionType: ArrayList<Data_SolutionType>,
    private val itemSolutionTypeClickListener: RecycleView_Adapter_SolutionType.onSolutionTypeClickListener
): RecyclerView.Adapter<RecycleView_Adapter_SolutionType.MyViewSolutionTypeHolder>()  {

    private val inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(context)
    }

    interface onSolutionTypeClickListener{
        fun onSolutionTypeClick(
            solutionTemplateId: String,
            solutionTemplateName: String
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleView_Adapter_SolutionType.MyViewSolutionTypeHolder {
        val view = inflater.inflate(R.layout.recycleview_solution_type, parent, false)

        return MyViewSolutionTypeHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecycleView_Adapter_SolutionType.MyViewSolutionTypeHolder,
        position: Int
    ) {
        holder.nameSolutionTemplate.text =
            dataModalArrayListSolutionType[position].solutionTypeName.toString()
    }

    override fun getItemCount(): Int {
        return dataModalArrayListSolutionType.size
    }

    inner class MyViewSolutionTypeHolder(itemTasksTemplate: View): RecyclerView.ViewHolder(itemTasksTemplate){
        var nameSolutionTemplate: TextView = itemView.findViewById(R.id.nameSolutionType) as TextView


        init {
            itemView.setOnClickListener {
                itemSolutionTypeClickListener.onSolutionTypeClick(
                    dataModalArrayListSolutionType[position].solutionTypeId.toString(),
                    dataModalArrayListSolutionType[position].solutionTypeName.toString()
                )
            }
        }
    }
}