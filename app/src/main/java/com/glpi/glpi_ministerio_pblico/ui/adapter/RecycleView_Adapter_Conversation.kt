package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.ui.view.BaseViewHolder
import java.util.concurrent.TimeUnit

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
            ticketInfoIdSource: String,
            ticketInfoSource: String,
            ticketInfoTimeToSolve: String,
            adapterPosition: Int)
        fun onFabClick()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerAdapter.MyViewConversationHolder {
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

        /*when(dataModelArrayListConversation[position].ticketInfoIdUser == dataModelArrayListConversation[position].ticketInfoIdTechnician){
            true -> {
                holder.param.setMargins(0, 10, 100, 10)
                holder.conversationTicketStatus.layoutParams = holder.param
            }
            false -> {
                holder.param.setMargins(100,10,0,10)
                holder.conversationTicketStatus.layoutParams = holder.param
            }
        }*/

        when(dataModelArrayListConversation[position].ticketInfoType){
            "TASK" -> {
                val time = dataModelArrayListConversation[position].ticketInfoCreationDate.toString().split(" ")[1]
                val minutesToAdd: Int = dataModelArrayListConversation[position].ticketInfoTimeToSolve.toString().toInt()
                val minutesToMillis = minutesToAdd* 60000
                //CONVERTIR DE HORA A MILISEGUNDOS Y LUEGO VOLVER A HORA
                //val date = ticketInfo.ticketInfoTimeToSolve.toString().split(" ")
                //val hour = time
                val secondsToMs1: Int = time[2].toInt() * 1000
                val minutesToMs1: Int = time[1].toInt() * 60000
                val hoursToMs1: Int = time[0].toInt() * 3600000
                val total1 = secondsToMs1 + minutesToMs1 + hoursToMs1
                val estimatedTime_: Long = (total1 + (minutesToAdd.toInt()*60000)).toLong()
                //Log.i("mensaje split: ", "${date[1]}")
                val milisToHours = String.format(
                    "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(estimatedTime_),
                    TimeUnit.MILLISECONDS.toMinutes(estimatedTime_) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(estimatedTime_) % TimeUnit.MINUTES.toSeconds(1)
                )

                Log.i("mensaje split","$$time en milisegundos es $total1")
                Log.i("mensaje split","$time + $minutesToAdd minutos es $milisToHours")

                holder.linearLayoutTasksCarrierName.isVisible = true
                holder.imgBtnPrivateTask.isVisible = true
                holder.chkBoxStatus.isVisible = true
                holder.followUpLinearLayoutTimeToSolve.isVisible = true

                when(dataModelArrayListConversation[position].ticketInfoPrivate){
                    "SI" -> holder.imgBtnPrivateTask.setImageResource(R.drawable.ic_candado_cerrado)
                    "NO" -> holder.imgBtnPrivateTask.setImageResource(R.drawable.ic_candado_abierto)
                }

                when(dataModelArrayListConversation[position].ticketInfoStatus){
                    "PENDIENTE" -> holder.chkBoxStatus.isChecked = false
                    "TERMINADO" -> holder.chkBoxStatus.isChecked = true
                }

                holder.ticketInfoNameTechnician.text = dataModelArrayListConversation[position].ticketInfoNameTechnician
                holder.param.setMargins(0, 10, 100, 10)
                holder.conversationTicketStatus.layoutParams = holder.param
                holder.conversationTicketStatus.setBackgroundResource(R.drawable.esq_redondeada_tasks)
            }
            "FOLLOWUP" -> {
                holder.linearLayoutTasksCarrierName.isVisible = false
                holder.followUpLinearLayoutTimeToSolve.isVisible = false
                holder.imgBtnPrivateTask.isVisible = false
                holder.chkBoxStatus.isVisible = false
                holder.param.setMargins(100,10,0,10)
                holder.conversationTicketStatus.layoutParams = holder.param
                holder.conversationTicketStatus.setBackgroundResource(R.drawable.esq_redondeada_followup)
            }
            "SOLUTION" -> {
                holder.conversationStatus.isVisible = true
                holder.solutionImageButtonEdit.isVisible = false
                holder.followUpLinearLayoutTimeToSolve.isVisible = false
                holder.linearLayoutTasksCarrierName.isVisible = false
                holder.imgBtnPrivateTask.isVisible = false
                holder.chkBoxStatus.isVisible = false
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

        var imgBtnPrivateTask: ImageButton
        var chkBoxStatus: CheckBox

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

            imgBtnPrivateTask = itemConversation.findViewById<TextView>(R.id.imgBtnPrivateTask) as ImageButton
            chkBoxStatus = itemConversation.findViewById<TextView>(R.id.chkBoxStatus) as CheckBox


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
                    dataModelArrayListConversation[position].ticketInfoIdSource.toString(),
                    dataModelArrayListConversation[position].ticketInfoSource.toString(),

                    dataModelArrayListConversation[position].ticketInfoTimeToSolve.toString(),
                    adapterPosition
                )
            }
        }
    }
}