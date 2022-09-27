package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.ui.view.BaseViewHolder
import java.lang.IllegalArgumentException

class RecyclerAdapter(
    val context: Context,
    private val dataModelArrayListConverdation:ArrayList<Data_Tickets>,
    private val itemclickListener:onConversationClickListener
):RecyclerView.Adapter<BaseViewHolder<*>>() {

    interface onConversationClickListener{
        fun onEditClick(glpiTasksDescripcion: String, glpiTasksTipo: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {

        return AssignedViewHolder(LayoutInflater.from(context).inflate(R.layout.recycleview_ticket_conversation,parent,false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is AssignedViewHolder -> holder.bind(dataModelArrayListConverdation[position],position)
            else -> throw IllegalArgumentException("se olvidó de pasar  el viewholder en el bind")
        }
    }

    override fun getItemCount(): Int {
        return dataModelArrayListConverdation.size
    }


    inner class AssignedViewHolder(itemAssigned: View): BaseViewHolder<Data_Tickets>(itemAssigned){
        override fun bind(item: Data_Tickets, position: Int) {
            Log.i("mensajeAdapter",""+item.getGlpiTasksTipo())
            if(item.getGlpiTasksTipo() == "TASK"){
                val param = itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(0,10,100,10)
                itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).layoutParams = param

                itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).setBackgroundResource(R.drawable.esq_redondeada_tasks)
                /*itemView.findViewById<TextView>(R.id.txt_descripcionTicketHistorico_conversation).text =
                    item.getGlpiTasksDescripcion()*/
            }else if(item.getGlpiTasksTipo() == "FOLLOWUP"){
                itemView.findViewById<LinearLayout>(R.id.tv_conversation_estado).isVisible = false
                val param = itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(100,10,0,10)
                itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).layoutParams = param
                itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).setBackgroundResource(R.drawable.esq_redondeada_followup)
            }else if(item.getGlpiTasksTipo() == "SOLUTION"){
                itemView.findViewById<LinearLayout>(R.id.tv_conversation_estado).isVisible = false
                val param = itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(100,10,0,10)
                itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).layoutParams = param
                itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).setBackgroundResource(R.drawable.esq_redondeada_solution)
            }

            itemView.findViewById<TextView>(R.id.txt_nameOperador_conversation).text =
                item.getGlpiTasksName()

            itemView.findViewById<TextView>(R.id.txt_descripcionTicketHistorico_conversation).text =
                item.getGlpiTasksDescripcion()

            itemView.findViewById<TextView>(R.id.txt_currentTime_conversation).text =
                item.getConversationCreation().toString()


            if(item.getGlpiTasksTipo() == "SOLUTION"){
                itemView.findViewById<ImageButton>(R.id.btn_edit).isVisible = false
            }else{
                itemView.findViewById<ImageButton>(R.id.btn_edit).setOnClickListener {
                    itemclickListener.onEditClick(
                        item.getGlpiTasksDescripcion(),
                        item.getGlpiTasksTipo()
                    )
                }
            }

        }
    }
}