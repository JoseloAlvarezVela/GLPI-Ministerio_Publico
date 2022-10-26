package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.R


/*adaptador que obtendrá los datos del archivo de actividad principal
y llenará la vista del reciclador*/
class RecycleView_Adapter_Tickets(
    context:Context,
    private val dataModelArrayList:ArrayList<Data_Tickets>,
    private val itemClickListener_: onTicketClickListener
    ):RecyclerView.Adapter<RecycleView_Adapter_Tickets.MyViewHolder>(){

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    //interfaz que manejara los eventos click
    interface onTicketClickListener{
        fun onTicketClick(
            ticketSortsId: String,
            ticketSortsType: String,
            ticketSortsContent: String,
            ticketSortsStatus: String,
            ticketSortsCreationDate: String,
            ticketSortsModificationDate: String,
            ticketSortsIdRecipient: String,

            ticketSortsIdTechnician: String,
            ticketSortsNameTechnician: String,
            ticketSortsLastNameTechnician: String,
            ticketSortsPhoneTechnician: String,
            ticketSortsEmailTechnician: String,

            ticketSortsIdRequester: String,
            ticketSortsNameRequester: String,
            ticketSortsLastNameRequester: String,
            ticketSortsPhoneRequester: String,
            ticketSortsPositionRequester: String,
            ticketSortsEmailRequester: String,
            ticketSortsLocationRequester: String,

            ticketSortsCategory: String,
            ticketSortsSource: String,
            ticketSortsUrgency: String
        )
    }

    /*Dentro del método onCreateViewHolder, el compilador inflará el archivo
    fragment_mis_peticiones.xml para que pueda desarrollar el diseño de cada fila
    de la vista del reciclador*/
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):RecycleView_Adapter_Tickets.MyViewHolder {

        val view = inflater.inflate(R.layout.recycleview_tickets, parent, false)

        return MyViewHolder(view)
    }

    /*el método onBindViewHolder() asiganará los datos a los componentes*/
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: RecycleView_Adapter_Tickets.MyViewHolder, position: Int) {

        holder.tvTicketType.text = dataModelArrayList[position].ticketSortsType
        when(dataModelArrayList[position].ticketSortsType){
            "SOLICITUD" -> {
                holder.tvTicketId.text = "?  #${dataModelArrayList[position].ticketSortsId}"
                holder.tvTicketId.setBackgroundColor((Color.parseColor("#3DC7FF")))
                holder.tvTicketType.setTextColor(Color.parseColor("#3DC7FF"))
            }
            "INCIDENCIA" -> {
                holder.tvTicketId.text = "!  #"+dataModelArrayList[position].ticketSortsId
                holder.tvTicketId.setBackgroundColor((Color.parseColor("#FEB300")))
                holder.tvTicketType.setTextColor(Color.parseColor("#FEB300"))
            }
        }

        when(dataModelArrayList[position].ticketSortsStatus){
            "EN CURSO (Asignada)" -> holder.tvTicketStatus.setBackgroundResource(R.drawable.ic_circulo_verde)
            "EN ESPERA" -> holder.tvTicketStatus.setBackgroundResource(R.drawable.ic_circulo)
            "CERRADO" -> holder.tvTicketStatus.setBackgroundResource(R.drawable.ic_circulo_negro)
        }

        holder.tvTicketDescription.text = dataModelArrayList[position].ticketSortsDescription+"..."

        holder.tvRequesterName.text =
            "Solicitante: ${dataModelArrayList[position].ticketSortsNameRequester} ${dataModelArrayList[position].ticketSortsLastNameRequester}"

        holder.tvRequesterPosition.text = "Cargo: ${dataModelArrayList[position].ticketSortsPositionRequester}"

        holder.tvTicketCreationDate.text = dataModelArrayList[position].ticketSortsCreationDate

        when(dataModelArrayList[position].ticketSortsUrgency){
            "ALTA" -> holder.tvTicketUrgency.setBackgroundResource(R.drawable.esq_redondeada_rojo)
            "MEDIA" -> holder.tvTicketUrgency.setBackgroundResource(R.drawable.esq_redondeada_amarillo)
            "BAJA" -> holder.tvTicketUrgency.setBackgroundResource(R.drawable.esq_redondeada_gris)
        }
    }

    //retornamos el tamaño de los datos obtenidos para mostrar en el recycleview
    override fun getItemCount(): Int {
        return dataModelArrayList.size
    }

    //inicializamos los componetes de nuestro ticket de la forma tradicional
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tickets : ConstraintLayout //para evento click
        var tvTicketId : TextView
        var tvTicketStatus : TextView
        var tvTicketType : TextView
        var tvTicketDescription: TextView
        var tvRequesterName: TextView
        var tvRequesterPosition: TextView
        var tvTicketCreationDate: TextView
        var tvTicketUrgency: TextView

        init {
            tickets = itemView.findViewById(R.id.tickets) as ConstraintLayout
            tvTicketId = itemView.findViewById(R.id.txt_numero_ticket) as TextView
            tvTicketStatus = itemView.findViewById(R.id.txt_estado_ticket) as TextView
            tvTicketType = itemView.findViewById(R.id.txt_tipo) as TextView
            tvTicketDescription = itemView.findViewById(R.id.txt_descripcionTicket) as TextView
            tvRequesterName = itemView.findViewById(R.id.txt_requester_name) as TextView
            tvRequesterPosition = itemView.findViewById(R.id.txt_requester_cargo) as TextView
            tvTicketCreationDate = itemView.findViewById(R.id.txt_fecha_apertura) as TextView
            tvTicketUrgency = itemView.findViewById(R.id.txt_estadoColor) as TextView

            itemView.setOnClickListener {
                itemClickListener_.onTicketClick(
                    dataModelArrayList[bindingAdapterPosition].ticketSortsId.toString(),
                    dataModelArrayList[position].ticketSortsType.toString(),
                    dataModelArrayList[position].ticketSortsContent.toString(),
                    dataModelArrayList[position].ticketSortsStatus.toString(),
                    dataModelArrayList[position].ticketSortsCreationDate.toString(),
                    dataModelArrayList[position].ticketSortsModificationDate.toString(),
                    dataModelArrayList[position].ticketSortsIdRecipient.toString(),

                    dataModelArrayList[position].ticketSortsIdTechnician.toString(),
                    dataModelArrayList[position].ticketSortsNameTechnician.toString(),
                    dataModelArrayList[position].ticketSortsLastNameTechnician.toString(),
                    dataModelArrayList[position].ticketSortsPhoneTechnician.toString(),
                    dataModelArrayList[position].ticketSortsEmailTechnician.toString(),

                    dataModelArrayList[position].ticketSortsIdRequester.toString(),
                    dataModelArrayList[position].ticketSortsNameRequester.toString(),
                    dataModelArrayList[position].ticketSortsLastNameRequester.toString(),
                    dataModelArrayList[position].ticketSortsPhoneRequester.toString(),
                    dataModelArrayList[position].ticketSortsPositionRequester.toString(),
                    dataModelArrayList[position].ticketSortsEmailRequester.toString(),
                    dataModelArrayList[position].ticketSortsLocationRequester.toString(),

                    dataModelArrayList[position].ticketSortsCategory.toString(),
                    dataModelArrayList[position].ticketSortsSource.toString(),
                    dataModelArrayList[position].ticketSortsUrgency.toString()
                )
            }
        }
    }
}