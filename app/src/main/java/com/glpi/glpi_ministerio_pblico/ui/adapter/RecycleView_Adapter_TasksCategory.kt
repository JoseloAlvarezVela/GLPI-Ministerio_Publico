package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R

class RecycleView_Adapter_TasksCategory(
    context: Context,
    private val dataModalArrayListTasksCategory: ArrayList<Data_TasksCategory>,
    private val itemTasksCategoryClickListener: onTasksCategoryClickListener
):RecyclerView.Adapter<RecycleView_Adapter_TasksCategory.MyViewTasksCategoryHolder>() {

    private val inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(context)
    }

    interface onTasksCategoryClickListener{
        fun onTasksCategoryClick(
            nameTasksCategory: String,
            contentTasksCategory: String
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleView_Adapter_TasksCategory.MyViewTasksCategoryHolder {
        val view = inflater.inflate(R.layout.recycleview_tasks_category, parent, false)

        return MyViewTasksCategoryHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecycleView_Adapter_TasksCategory.MyViewTasksCategoryHolder,
        position: Int
    ) {
        holder.nameTasksCategory.text =
            dataModalArrayListTasksCategory[position].getNameTasksCategories()
        /*if (position%2==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#00aaff"))
        }*/
    }

    override fun getItemCount(): Int {
        return dataModalArrayListTasksCategory.size
    }

    inner class MyViewTasksCategoryHolder(itemTasksCategory: View): RecyclerView.ViewHolder(itemTasksCategory){
        var nameTasksCategory: TextView = itemView.findViewById(R.id.nombreTasksCategory) as TextView
        var backgroundTasksCategory: LinearLayout = itemView.findViewById(R.id.backgroundTasksCategory) as LinearLayout

        init {
            itemView.setOnClickListener {
                itemTasksCategoryClickListener.onTasksCategoryClick(
                    dataModalArrayListTasksCategory[position].getNameTasksCategories(),
                    dataModalArrayListTasksCategory[position].getContentTasksCategories()
                )
            }
        }
    }
}