package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.annotation.SuppressLint
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
    private var dataModelArrayListConversation: ArrayList<Data_TicketInfo>,
    private val itemClickListener:onConversationClickListener
    ):RecyclerView.Adapter<RecyclerAdapter.MyViewConversationHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    interface onConversationClickListener{
        fun onEditClick(
            ticketInfoType: String,
            ticketInfoContent: String,
            ticketInfoPrivate: String,
            ticketInfoIdTechnician: String,
            ticketInfoStatus: String,
            ticketInfoIdTemplate: String,
            ticketInfoIdCategory: String,
            ticketInfoCategory: String,
            ticketInfoId: String,
            ticketInfoTimeToSolve: String)
        fun onFabClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.MyViewConversationHolder {
        val view = inflater.inflate(R.layout.recycleview_ticket_conversation,parent,false)
        return MyViewConversationHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewConversationHolder, position: Int) {
        holder.nameCarrier.text =
            "${dataModelArrayListConversation[position].ticketInfoNameUser} ${dataModelArrayListConversation[position].ticketInfoLastNameUser}"

        holder.tasksDate.text = dataModelArrayListConversation[position].ticketInfoDate
        holder.tasksCreationDate.text =
            "${dataModelArrayListConversation[position].ticketInfoCreationDate} -> ${dataModelArrayListConversation[position].ticketInfoTimeToSolve}" //sumar a cration el time to solve

        holder.tasksTimeToSolve.text = dataModelArrayListConversation[position].ticketInfoTimeToSolve+" minutos"

        holder.conversationContent.text = dataModelArrayListConversation[position].ticketInfoContent

        when(dataModelArrayListConversation[position].ticketInfoType){
            "TASK" -> {
                holder.linearLayoutTasksCarrierName.isVisible = true
                holder.ticketInfoNameTechnician.text =
                    dataModelArrayListConversation[position].ticketInfoNameTechnician
                holder.param.setMargins(0, 10, 100, 10)
                holder.conversationTicketStatus.layoutParams = holder.param
                holder.conversationTicketStatus.setBackgroundResource(R.drawable.esq_redondeada_tasks)
            }
            "FOLLOWUP" -> {
                holder.linearLayoutTasksCarrierName.isVisible = false
                holder.followUpLinearLayoutTimeToSolve.isVisible = false
                holder.param.setMargins(100,10,0,10)
                holder.conversationTicketStatus.layoutParams = holder.param
                holder.conversationTicketStatus.setBackgroundResource(R.drawable.esq_redondeada_followup)
            }
            "SOLUTION" -> {
                holder.conversationStatus.isVisible = true
                holder.solutionImageButtonEdit.isVisible = false
                holder.followUpLinearLayoutTimeToSolve.isVisible = false
                holder.linearLayoutTasksCarrierName.isVisible = false
                holder.param.setMargins(100,10,0,10)
                holder.conversationTicketStatus.layoutParams = holder.param
                holder.conversationTicketStatus.setBackgroundResource(R.drawable.esq_redondeada_solution)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataModelArrayListConversation.size
    }

    inner class MyViewConversationHolder(itemConversation: View):RecyclerView.ViewHolder(itemConversation){

        var nameCarrier: TextView
        var linearLayoutTasksCarrierName: LinearLayout
        var ticketInfoNameTechnician: TextView
        var tasksDate: TextView
        var tasksCreationDate: TextView
        var tasksTimeToSolve: TextView
        var followUpLinearLayoutTimeToSolve: LinearLayout
        var solutionLinearLayout: LinearLayout
        var solutionImageButtonEdit: ImageButton
        var conversationTicketStatus: LinearLayout
        var conversationStatus: TextView
        var conversationContent: TextView

        var param: ViewGroup.MarginLayoutParams


        init {

            nameCarrier = itemConversation.findViewById<TextView>(R.id.txt_nameOperador_conversation) as TextView
            linearLayoutTasksCarrierName = itemConversation.findViewById(R.id.linearLayout_tasksOperadorName) as LinearLayout
            ticketInfoNameTechnician = itemConversation.findViewById<TextView>(R.id.txt_tasksOperadorName) as TextView
            tasksDate = itemConversation.findViewById<TextView>(R.id.txt_currentTime_conversation) as TextView
            tasksCreationDate = itemConversation.findViewById<TextView>(R.id.txt_creationTicketDate) as TextView
            tasksTimeToSolve = itemConversation.findViewById<TextView>(R.id.txt_estimatedHour) as TextView
            followUpLinearLayoutTimeToSolve = itemConversation.findViewById<LinearLayout>(R.id.linearLayout_timeToResolve) as LinearLayout
            solutionLinearLayout = itemConversation.findViewById<LinearLayout>(R.id.linearLayout_Solution) as LinearLayout
            solutionImageButtonEdit = itemConversation.findViewById<ImageButton>(R.id.btn_edit) as ImageButton
            conversationTicketStatus = itemConversation.findViewById<LinearLayout>(R.id.ticket_estado_conversation) as LinearLayout
            conversationStatus = itemConversation.findViewById<TextView>(R.id.tv_conversation_estado) as TextView
            conversationContent = itemConversation.findViewById<TextView>(R.id.txt_descripcionTicketHistorico_conversation) as TextView


            param = itemView.findViewById<LinearLayout>(R.id.ticket_estado_conversation).layoutParams as ViewGroup.MarginLayoutParams

            itemConversation.findViewById<ImageButton>(R.id.btn_edit).setOnClickListener {
                itemClickListener.onEditClick(

                    dataModelArrayListConversation[position].ticketInfoType.toString(),
                    dataModelArrayListConversation[position].ticketInfoContent.toString(),
                    dataModelArrayListConversation[position].ticketInfoPrivate.toString(),
                    dataModelArrayListConversation[position].ticketInfoIdTechnician.toString(),
                    dataModelArrayListConversation[position].ticketInfoStatus.toString(),
                    dataModelArrayListConversation[position].ticketInfoIdTemplate.toString(),
                    dataModelArrayListConversation[position].ticketInfoIdCategory.toString(),
                    dataModelArrayListConversation[position].ticketInfoCategory.toString(),
                    dataModelArrayListConversation[position].ticketInfoId.toString(),

                    dataModelArrayListConversation[position].ticketInfoTimeToSolve.toString(),
                )
            }
        }
    }
}