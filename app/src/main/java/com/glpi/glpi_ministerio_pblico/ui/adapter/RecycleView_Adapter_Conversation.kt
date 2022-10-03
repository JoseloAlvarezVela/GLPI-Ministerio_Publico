package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.ui.view.BaseViewHolder

class RecyclerAdapter(
    val context: Context,
    private var dataModelArrayListConverdation:ArrayList<Data_Tickets>,
    private val itemclickListener:onConversationClickListener):RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val TICKET_INFO = 0
    private val TICKET_ASSIGNED = 1

    interface onConversationClickListener{
        fun onEditClick(glpiTasksDescripcion: String, glpiTasksTipo: String)
        fun onFabClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return if(viewType == TICKET_INFO) {
            TicketInfo(LayoutInflater.from(context).inflate(
                R.layout.activity_tickets_historico,parent,false))
        } else if (viewType == TICKET_ASSIGNED) {
            AssignedViewHolder(LayoutInflater.from(context).inflate(
                R.layout.recycleview_ticket_conversation,parent,false))
        }else {
            TicketInfo(LayoutInflater.from(context).inflate(
                R.layout.recycleview_ticket_conversation,parent,false))
        }

    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is TicketInfo -> holder.bind(dataModelArrayListConverdation[position],position)
            is AssignedViewHolder -> holder.bind(dataModelArrayListConverdation[position],position)
            else -> throw IllegalArgumentException("se olvidó de pasar  el viewholder en el bind")
        }
    }

    override fun getItemCount(): Int {
        return dataModelArrayListConverdation.size
    }

    override fun getItemViewType(position: Int): Int {
        //return if (position == 0) TICKET_INFO   else TICKET_ASSIGNED
        return TICKET_ASSIGNED
    }

    inner class TicketInfo(itemTicketInfo: View): BaseViewHolder<Data_Tickets>(itemTicketInfo){
        override fun bind(item: Data_Tickets, position: Int) {


            //itemView.findViewById<LinearLayout>(R.id.linearLayout10).isVisible = false

            itemView.findViewById<ConstraintLayout>(R.id.layout_historico).isVisible = true

            itemView.findViewById<TextView>(R.id.txt_nameOperador).text =
                item.getTaskUserName()
            //itemView.findViewById<ImageButton>(R.id.btn_edit).isVisible = false
            itemView.findViewById<TextView>(R.id.txt_descripcionTicketHistorico).text =
                item.getTicketSortsContents()

            /*itemView.findViewById<TextView>(R.id.txt_nameOperador).text =
                item.getGlpiOperadorName()*/

            val stringCreationDate = "Fecha Cración ${item.getTicketSortsCreationDate()}"
            itemView.findViewById<TextView>(R.id.txt_currentTime).text =
                stringCreationDate
            Log.i("mensajemodi",""+item.getTicketSortsCreationDate())
            if (item.getTicketSortsCreationDate() != item.getTicketSortsModificationDate()){
                itemView.findViewById<TextView>(R.id.txt_modificationDate).isVisible = true
                itemView.findViewById<TextView>(R.id.txt_modificationDate).text =
                    "Ult. Modificación "+item.getTicketSortsModificationDate()
            }



        }

    }

    inner class AssignedViewHolder(itemAssigned: View): BaseViewHolder<Data_Tickets>(itemAssigned){
        override fun bind(item: Data_Tickets, position: Int) {
            //Log.i("mensajeAdapter",""+item.getGlpiTasksTipo())
            if(item.getGlpiTasksTipo() == "TASK"){
                itemView.findViewById<TextView>(R.id.tv_conversation_estado).isVisible = false
                val param = itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(0,10,100,10)
                itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).layoutParams = param

                itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).setBackgroundResource(R.drawable.esq_redondeada_tasks)
                /*itemView.findViewById<TextView>(R.id.txt_descripcionTicketHistorico_conversation).text =
                    item.getGlpiTasksDescripcion()*/
            }else if(item.getGlpiTasksTipo() == "FOLLOWUP"){
                itemView.findViewById<TextView>(R.id.tv_conversation_estado).isVisible = false
                val param = itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(100,10,0,10)
                itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).layoutParams = param
                itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).setBackgroundResource(R.drawable.esq_redondeada_followup)
            }else if(item.getGlpiTasksTipo() == "SOLUTION"){
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