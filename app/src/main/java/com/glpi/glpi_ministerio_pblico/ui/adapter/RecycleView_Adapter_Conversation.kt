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
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.data.database.TicketInfoDB
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

        holder.conversationContent.text = dataModelArrayListConversation[position].ticketInfoContent

        /*Log.i("mensaje compare","${dataModelArrayListConversation[position].ticketInfoIdUser} = ${dataModelArrayListConversation[position].ticketInfoIdTechnician}")
        when(dataModelArrayListConversation[position].ticketInfoIdUser == dataModelArrayListConversation[position].ticketInfoIdTechnician){
            true -> {
                holder.param.setMargins(100,10,0,10)
                holder.conversationTicketStatus.layoutParams = holder.param
            }
            false -> {
                holder.param.setMargins(0, 10, 100, 10)
                holder.conversationTicketStatus.layoutParams = holder.param
            }
        }*/
        /*when(dataModelArrayListConversation[position].ticketInfoType!!.contains("SOLUTION")){
            true -> Log.i("mensaje","si existe solución")
            false -> Log.i("mensaje","si existe solución")
        }*/
        when(prefer.getTicketSortsStatus()){
            "CERRADO" -> {
                when(dataModelArrayListConversation[position].ticketInfoType){
                    "TASK" -> {
                        val time = dataModelArrayListConversation[position].ticketInfoCreationDate.toString().split(" ") //hora de inicio de tarea
                        val minutesToAdd: Long = dataModelArrayListConversation[position].ticketInfoTimeToSolve.toString().toLong()  //tiempo para resolver tarea en minutos
                        val minutesToSecondsAdd = minutesToAdd*60

                        val convertHour = time[1].toString().split(":")
                        //convertimos horas a segundos
                        //Log.i("mensaje hora","${time[1]}")
                        //Log.i("mensaje hora","${convertHour}")
                        val minutesToSeconds = convertHour[1].toInt()*60
                        val hourToSecond = convertHour[0].toInt()*3600
                        val secondsTotal = hourToSecond+minutesToSeconds+convertHour[2].toInt()+minutesToSecondsAdd
                        //Log.i("mensaje hour to seconds","$secondsTotal")

                        val hora = (secondsTotal / 3600)
                        val minutos = ((secondsTotal%3600) /60)
                        val segundos = (secondsTotal%60)
                        //Log.i("mensaje seconds to Hour","$hora:$minutos:$segundos")


                        holder.tasksCreationDate.text =
                            "${dataModelArrayListConversation[position].ticketInfoCreationDate} -> $hora:$minutos:$segundos" //sumar a cration el time to solve


                        //convertir de minutos a horas
                        val minToHour: Double = (minutesToAdd / 60.0) //obtengo 0.5
                        val resto: Double = minToHour - minToHour.toString().split(".")[0].toLong()
                        val minuts: Long = (resto * 60).toLong()
                        /*Log.i("mensaje minTohour1","${dataModelArrayListConversation[position].ticketInfoTimeToSolve}")
                        Log.i("mensaje minTohour2","$minToHour")
                        Log.i("mensaje minTohour3","$resto")
                        Log.i("mensaje minTohour4","$minuts")*/
                        val min = minToHour.toString().split(".")
                        when(dataModelArrayListConversation[position].ticketInfoTimeToSolve!!.toInt() < 60){
                            true -> holder.tasksTimeToSolve.text = dataModelArrayListConversation[position].ticketInfoTimeToSolve+" minutos"
                            false -> holder.tasksTimeToSolve.text = "${minToHour.toString().split(".")[0]} horas y $minuts minutos "
                        }



                        holder.linearLayoutTasksCarrierName.isVisible = true
                        holder.solutionImageButtonEdit.isVisible = false
                        holder.imgBtnPrivateTask.isVisible = true
                        holder.imgBtnTaskStatus.isVisible = true
                        holder.followUpLinearLayoutTimeToSolve.isVisible = true

                        when(dataModelArrayListConversation[position].ticketInfoPrivate){
                            "SI" -> holder.imgBtnPrivateTask.setImageResource(R.drawable.ic_candado_cerrado)
                            "NO" -> holder.imgBtnPrivateTask.setImageResource(R.drawable.ic_candado_abierto)
                        }

                        when(dataModelArrayListConversation[position].ticketInfoStatus){
                            "PENDIENTE" -> holder.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_to_do)
                            "TERMINADO" -> holder.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_done)
                            "INFORMACION" -> holder.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_information)
                        }

                        //holder.ticketInfoNameTechnician.text = dataModelArrayListConversation[position].ticketInfoIdTechnician
                        holder.ticketInfoNameTechnician.text = prefer.getNameUser().toString()
                        holder.param.setMargins(0, 10, 100, 10)
                        holder.conversationTicketStatus.layoutParams = holder.param
                        holder.conversationTicketStatus.setBackgroundResource(R.drawable.esq_redondeada_tasks)
                    }
                    "FOLLOWUP" -> {
                        holder.solutionImageButtonEdit.isVisible = false
                        holder.linearLayoutTasksCarrierName.isVisible = false
                        holder.followUpLinearLayoutTimeToSolve.isVisible = false
                        holder.imgBtnPrivateTask.isVisible = true
                        holder.imgBtnTaskStatus.isVisible = false
                        holder.param.setMargins(100,10,0,10)
                        holder.conversationTicketStatus.layoutParams = holder.param
                        holder.conversationTicketStatus.setBackgroundResource(R.drawable.esq_redondeada_followup)
                        when(dataModelArrayListConversation[position].ticketInfoPrivate){
                            "SI" -> holder.imgBtnPrivateTask.setImageResource(R.drawable.ic_candado_cerrado)
                            "NO" -> holder.imgBtnPrivateTask.setImageResource(R.drawable.ic_candado_abierto)
                        }
                    }
                    "SOLUTION" -> {
                        holder.conversationStatus.isVisible = true
                        holder.solutionImageButtonEdit.isVisible = false
                        holder.followUpLinearLayoutTimeToSolve.isVisible = false
                        holder.linearLayoutTasksCarrierName.isVisible = false
                        holder.imgBtnPrivateTask.isVisible = false
                        holder.imgBtnTaskStatus.isVisible = false
                        holder.param.setMargins(100,10,0,10)
                        holder.conversationTicketStatus.layoutParams = holder.param
                        holder.conversationTicketStatus.setBackgroundResource(R.drawable.esq_redondeada_solution)
                    }
                }
            }
            else -> {
                when(dataModelArrayListConversation[position].ticketInfoType){
                    "TASK" -> {
                        val time = dataModelArrayListConversation[position].ticketInfoCreationDate.toString().split(" ") //hora de inicio de tarea
                        val minutesToAdd: Long = dataModelArrayListConversation[position].ticketInfoTimeToSolve.toString().toLong()  //tiempo para resolver tarea en minutos
                        val minutesToSecondsAdd = minutesToAdd*60 //de minutos a segundos

                        val convertHour = time[1].toString().split(":")
                        //convertimos horas a segundos
                        //Log.i("mensaje hora","${time[1]}")
                        //Log.i("mensaje hora","${convertHour}")
                        val minutesToSeconds = convertHour[1].toInt()*60
                        val hourToSecond = convertHour[0].toInt()*3600
                        val secondsTotal = hourToSecond+minutesToSeconds+convertHour[2].toInt()+minutesToSecondsAdd
                        //Log.i("mensaje hour to seconds","$secondsTotal")

                        val hora = (secondsTotal / 3600)
                        //Log.i("mensaje timeH",hora.toString())
                        val minutos = ((secondsTotal%3600) /60)
                        //Log.i("mensaje timeM",minutos.toString())
                        val segundos = (secondsTotal%60)
                        //Log.i("mensaje timeS",segundos.toString())
                        //Log.i("mensaje seconds to Hour","$hora:$minutos:$segundos")


                        holder.tasksCreationDate.text =
                            "${dataModelArrayListConversation[position].ticketInfoCreationDate} -> $hora:$minutos:$segundos" //sumar a cration el time to solve


                        //convertir de minutos a horas
                        val minToHour: Double = (minutesToAdd / 60.0) //obtengo 0.5
                        val resto: Double = minToHour - minToHour.toString().split(".")[0].toLong()
                        val minuts: Long = (resto * 60).toLong()
                        /*Log.i("mensaje minTohour1","${dataModelArrayListConversation[position].ticketInfoTimeToSolve}")
                        Log.i("mensaje minTohour2","$minToHour")
                        Log.i("mensaje minTohour3","$resto")
                        Log.i("mensaje minTohour4","$minuts")*/
                        val min = minToHour.toString().split(".")
                        when(dataModelArrayListConversation[position].ticketInfoTimeToSolve!!.toInt() < 60){
                            true -> holder.tasksTimeToSolve.text = dataModelArrayListConversation[position].ticketInfoTimeToSolve+" minutos"
                            false -> holder.tasksTimeToSolve.text = "${minToHour.toString().split(".")[0]} horas y $minuts minutos "
                        }



                        holder.conversationStatus.isVisible = false
                        holder.linearLayoutTasksCarrierName.isVisible = true
                        holder.solutionImageButtonEdit.isVisible = true
                        holder.imgBtnPrivateTask.isVisible = true
                        holder.imgBtnTaskStatus.isVisible = true
                        holder.followUpLinearLayoutTimeToSolve.isVisible = true

                        when(dataModelArrayListConversation[position].ticketInfoPrivate){
                            "SI" -> holder.imgBtnPrivateTask.setImageResource(R.drawable.ic_candado_cerrado)
                            "NO" -> holder.imgBtnPrivateTask.setImageResource(R.drawable.ic_candado_abierto)
                        }

                        when(dataModelArrayListConversation[position].ticketInfoStatus){
                            "PENDIENTE" -> holder.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_to_do)
                            "TERMINADO" -> holder.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_done)
                            "INFORMACION" -> holder.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_information)
                        }

                        //holder.ticketInfoNameTechnician.text = dataModelArrayListConversation[position].ticketInfoIdTechnician
                        holder.ticketInfoNameTechnician.text = prefer.getNameUser().toString()
                        holder.param.setMargins(0, 10, 100, 10)
                        holder.conversationTicketStatus.layoutParams = holder.param
                        holder.conversationTicketStatus.setBackgroundResource(R.drawable.esq_redondeada_tasks)
                    }
                    "FOLLOWUP" -> {
                        holder.conversationStatus.isVisible = false
                        holder.solutionImageButtonEdit.isVisible = true
                        holder.linearLayoutTasksCarrierName.isVisible = false
                        holder.followUpLinearLayoutTimeToSolve.isVisible = false
                        holder.imgBtnPrivateTask.isVisible = true
                        holder.imgBtnTaskStatus.isVisible = false
                        holder.param.setMargins(100,10,0,10)
                        holder.conversationTicketStatus.layoutParams = holder.param
                        holder.conversationTicketStatus.setBackgroundResource(R.drawable.esq_redondeada_followup)
                        when(dataModelArrayListConversation[position].ticketInfoPrivate){
                            "SI" -> holder.imgBtnPrivateTask.setImageResource(R.drawable.ic_candado_cerrado)
                            "NO" -> holder.imgBtnPrivateTask.setImageResource(R.drawable.ic_candado_abierto)
                        }
                    }
                    "SOLUTION" -> {
                        holder.conversationStatus.isVisible = true
                        holder.solutionImageButtonEdit.isVisible = false
                        holder.followUpLinearLayoutTimeToSolve.isVisible = false
                        holder.linearLayoutTasksCarrierName.isVisible = false
                        holder.imgBtnPrivateTask.isVisible = false
                        holder.imgBtnTaskStatus.isVisible = false
                        holder.param.setMargins(100,10,0,10)
                        holder.conversationTicketStatus.layoutParams = holder.param
                        holder.conversationTicketStatus.setBackgroundResource(R.drawable.esq_redondeada_solution)
                    }
                }
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
        var imgBtnTaskStatus: ImageButton

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
            imgBtnTaskStatus = itemConversation.findViewById<ImageButton>(R.id.imgBtnTaskStatus) as ImageButton


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