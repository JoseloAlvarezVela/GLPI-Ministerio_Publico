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
    private val itemClickListener_: ontickteClickListener
    ):RecyclerView.Adapter<RecycleView_Adapter_Tickets.MyViewHolder>(){

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    //interfaz que manejara los eventos click
    interface ontickteClickListener{
        fun onTicketClick(
            TicketID: String,
            NameOperador: Any,
            CurrentTime: Any,
            ModificationDate: String,
            IdRecipient: String,
            IdTechnician: String,
            IdRequester: String,
            Contenido: Any,
            Tipo: String,
            creationDateTicket: String,
            Ubicacion: String,
            Correo: String,
            NameSolicitante: String,
            CargoSolicitante: String,
            TelefonoSolicitante: String,
            LoginName: String,
            TicketEstado: String,
            TicketCategoria: String,
            TicketOrigen: String,
            TicketUrgencia: String,
            glpiTasksName: String,
            glpiTasksDescripcion: String
            //TelefonoTecnico: String, PEDIR ESTOS DATOS
            /*GrupoTecnico: String,
            NameObservador: String*/
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
        //-----------


        //-----------
        if (dataModelArrayList[position].getTicketSortsState() == "EN CURSO (Asignada)"){
            holder.txt_estado_ticket.setBackgroundResource(R.drawable.ic_circulo_verde)
        }else if (dataModelArrayList[position].getTicketSortsState() == "CERRADO"){
            holder.txt_estado_ticket.setBackgroundResource(R.drawable.ic_circulo_negro)
        }else{
            holder.txt_estado_ticket.setBackgroundResource(R.drawable.ic_circulo)
        }

        //-----------
        holder.txt_tipo.text = dataModelArrayList[position].getTicketSortsType()
        if (dataModelArrayList[position].getTicketSortsType() == "SOLICITUD"){
            holder.txt_numero_ticket.text = "?  #"+dataModelArrayList[position].getTicketSortsID()
            holder.txt_tipo.setTextColor(Color.parseColor("#3DC7FF"))
            holder.txt_numero_ticket.setBackgroundColor((Color.parseColor("#3DC7FF")))
        }else{
            holder.txt_numero_ticket.text = "!  #"+dataModelArrayList[position].getTicketSortsID()
            holder.txt_tipo.setTextColor(Color.parseColor("#FEB300"))
            holder.txt_numero_ticket.setBackgroundColor((Color.parseColor("#FEB300")))
        }

        //-----------
        holder.txt_descripcionTicket.text = dataModelArrayList[position].getTicketSortsDescription()+"..."

        //-----------
        holder.txt_Requester_Name.text = "Solicitante: "+ dataModelArrayList[position].getTicketSortsNameRequester()

        //-----------
        holder.txt_Requester_Cargo.text = "Cargo: ${dataModelArrayList[position].getTicketSortsPositionRequester()}"

        //-----------
        holder.txt_glpi_currenttime.text = dataModelArrayList[position].getTicketSortsCreationDate()

        //urgencia del ticket
        if(dataModelArrayList[position].getTicketSortsUrgency() == "ALTA"){
            holder.txt_EstadoColor.setBackgroundResource(R.drawable.esq_redondeada_rojo)
        }
        else if(dataModelArrayList[position].getTicketSortsUrgency() == "MEDIA"){
            holder.txt_EstadoColor.setBackgroundResource(R.drawable.esq_redondeada_amarillo)
        }
        else{
            holder.txt_EstadoColor.setBackgroundResource(R.drawable.esq_redondeada_gris)
        }
    }

    //retornamos el tamaño de los datos obtenidos para mostrar en el recycleview
    override fun getItemCount(): Int {
        return dataModelArrayList.size
    }

    //inicializamos los componetes de nuestro ticket de la forma tradicional
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tickets : ConstraintLayout //para evento click
        var txt_numero_ticket : TextView
        var txt_estado_ticket : TextView
        var txt_tipo : TextView
        var txt_descripcionTicket: TextView
        var txt_Requester_Name: TextView
        var txt_Requester_Cargo: TextView
        var txt_glpi_currenttime: TextView
        var txt_EstadoColor: TextView
        //SECTION TASKS
        var txt_TaskNameLogin: String
        var txt_TaskDescription: String
        /*var txt_TaskNameLogin: TextView
        var txt_TaskDescription: TextView*/

        init {
            tickets = itemView.findViewById(R.id.tickets) as ConstraintLayout
            txt_numero_ticket = itemView.findViewById(R.id.txt_numero_ticket) as TextView
            txt_estado_ticket = itemView.findViewById(R.id.txt_estado_ticket) as TextView
            txt_tipo = itemView.findViewById(R.id.txt_tipo) as TextView
            txt_descripcionTicket = itemView.findViewById(R.id.txt_descripcionTicket) as TextView
            txt_Requester_Name = itemView.findViewById(R.id.txt_requester_name) as TextView
            txt_Requester_Cargo = itemView.findViewById(R.id.txt_requester_cargo) as TextView
            txt_glpi_currenttime = itemView.findViewById(R.id.txt_fecha_apertura) as TextView
            txt_EstadoColor = itemView.findViewById(R.id.txt_estadoColor) as TextView
            /*txt_TaskNameLogin = itemView.findViewById(R.id.txt_TaskNameLogin) as TextView
            txt_TaskDescription = itemView.findViewById(R.id.txt_TaskDescription) as TextView*/
            txt_TaskNameLogin = "itemView.findViewById(R.id.txt_TaskNameLogin) as TextView"
            txt_TaskDescription = "itemView.findViewById(R.id.txt_TaskDescription) as TextView"


            itemView.setOnClickListener {
                itemClickListener_.onTicketClick(
                    dataModelArrayList[position].getTicketSortsID(),
                    dataModelArrayList[position].getGlpiOperadorName(),
                    dataModelArrayList[position].getTicketSortsCreationDate(),
                    dataModelArrayList[position].getTicketSortsModificationDate(),
                    dataModelArrayList[position].getTicketSortsIdRecipient(),
                    dataModelArrayList[position].getTicketSortsIdTechnician(),
                    dataModelArrayList[position].getTicketSortsIdRequester(),
                    dataModelArrayList[position].getTicketSortsContents(),
                    dataModelArrayList[position].getTicketSortsType(),
                    dataModelArrayList[position].getTicketSortsCreationDate(),
                    dataModelArrayList[position].getGlpiUbicacionSolicitante(),
                    dataModelArrayList[position].getGlpiCorreoSolicitante(),
                    dataModelArrayList[position].getTechnicianName(),
                    dataModelArrayList[position].getGlpiRequesterCargo(),
                    dataModelArrayList[position].getGlpiTelefonoSolicitante(),
                    dataModelArrayList[position].getGlpiLoginName(),
                    dataModelArrayList[position].getTicketSortsState(),
                    dataModelArrayList[position].getTicketSortsCategory(),
                    dataModelArrayList[position].getTicketSortsSource(),
                    dataModelArrayList[position].getTicketSortsUrgency(),
                    "dataModelArrayList[position].getGlpiTasksName()",
                    "dataModelArrayList[position].getGlpiTasksDescripcion()"
                )
            }
        }
    }
}