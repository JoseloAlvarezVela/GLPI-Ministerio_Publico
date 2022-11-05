package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R

class RecycleView_Adapter_SolutionTemplate(
    context: Context,
    private val dataModalArrayListSolutionTemplate: ArrayList<Data_SolutionTemplate>,
    private val itemSolutionTemplateClickListener: RecycleView_Adapter_SolutionTemplate.onSolutionTemplateClickListener
): RecyclerView.Adapter<RecycleView_Adapter_SolutionTemplate.MyViewSolutionTemplateHolder>()  {

    private val inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(context)
    }

    interface onSolutionTemplateClickListener{
        fun onSolutionTemplateClick(
            solutionTemplateId: String,
            solutionTemplateName: String,
            solutionTemplateContent: String
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleView_Adapter_SolutionTemplate.MyViewSolutionTemplateHolder {
        val view = inflater.inflate(R.layout.recycleview_solution_template, parent, false)

        return MyViewSolutionTemplateHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecycleView_Adapter_SolutionTemplate.MyViewSolutionTemplateHolder,
        position: Int
    ) {
        holder.nameSolutionTemplate.text =
            dataModalArrayListSolutionTemplate[position].solutionTemplateName.toString()
    }

    override fun getItemCount(): Int {
        return dataModalArrayListSolutionTemplate.size
    }

    inner class MyViewSolutionTemplateHolder(itemTasksTemplate: View): RecyclerView.ViewHolder(itemTasksTemplate){
        var nameSolutionTemplate: TextView = itemView.findViewById(R.id.nameSolutionTemplate) as TextView


        init {
            itemView.setOnClickListener {
                itemSolutionTemplateClickListener.onSolutionTemplateClick(
                    dataModalArrayListSolutionTemplate[position].solutionTemplateId.toString(),
                    dataModalArrayListSolutionTemplate[position].solutionTemplateName.toString(),
                    dataModalArrayListSolutionTemplate[position].solutionTemplateContent.toString()
                )
            }
        }
    }
}