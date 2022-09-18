package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
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
            Contenido: Any,
            Tipo: String,
            Ubicacion: String,
            Correo: String,
            NameSolicitante: String,
            CargoSolicitante: String,
            TelefonoSolicitante: String,
            LoginName: String,
            TicketEstado: String,
            TicketCategoria: String,
            TicketOrigen: String,
            TicketUrgencia: String
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
    override fun onBindViewHolder(holder: RecycleView_Adapter_Tickets.MyViewHolder, position: Int) {
        //-----------
        holder.txt_numero_ticket.text = "#"+dataModelArrayList[position].getGlpiID()

        //-----------
        if (dataModelArrayList[position].getGlpiEstado() == "EN CURSO (Asignada)"){
            holder.txt_estado_ticket.setBackgroundResource(R.drawable.ic_circulo_verde)
        }else{
            holder.txt_estado_ticket.setBackgroundResource(R.drawable.ic_circulo)
        }

        //-----------
        holder.txt_tipo.text = dataModelArrayList[position].getGlpiTipo()
        if (dataModelArrayList[position].getGlpiTipo() == "SOLICITUD"){
            holder.txt_tipo.setTextColor(Color.parseColor("#3FC3FF"))
            holder.txt_numero_ticket.setBackgroundColor((Color.parseColor("#3FC3FF")))
        }else{
            holder.txt_tipo.setTextColor(Color.parseColor("#C5A83F"))
            holder.txt_numero_ticket.setBackgroundColor((Color.parseColor("#C5A83F")))
        }

        //-----------
        holder.txt_descripcionTicket.text = dataModelArrayList[position].getGlpiDescripcion()+"..."

        //-----------
        holder.txt_Requester_Name.text = "Solicitante: "+dataModelArrayList[position].getGlpiRequesterName()

        //-----------
        holder.txt_Requester_Cargo.text = "Cargo: "+dataModelArrayList[position].getGlpiRequesterCargo()

        //-----------
        holder.txt_glpi_currenttime.text = dataModelArrayList[position].getCurrentTime()

        //-----------
        if(dataModelArrayList[position].getGlpiEstado() == "EN CURSO (Asignada)"){
            holder.txt_EstadoColor.setBackgroundResource(R.drawable.esq_redondeada_amarillo)
        }
        else if(dataModelArrayList[position].getGlpiEstado() == "URGENTE"){
            holder.txt_EstadoColor.setBackgroundResource(R.drawable.esq_redondeada_rojo)
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
        var tickets : LinearLayout //para evento click
        var txt_numero_ticket : TextView
        var txt_estado_ticket : TextView
        var txt_tipo : TextView
        var txt_descripcionTicket: TextView
        var txt_Requester_Name: TextView
        var txt_Requester_Cargo: TextView
        var txt_glpi_currenttime: TextView
        var txt_EstadoColor: TextView

        init {
            tickets = itemView.findViewById(R.id.tickets) as LinearLayout
            txt_numero_ticket = itemView.findViewById(R.id.txt_numero_ticket) as TextView
            txt_estado_ticket = itemView.findViewById(R.id.txt_estado_ticket) as TextView
            txt_tipo = itemView.findViewById(R.id.txt_tipo) as TextView
            txt_descripcionTicket = itemView.findViewById(R.id.txt_descripcionTicket) as TextView
            txt_Requester_Name = itemView.findViewById(R.id.txt_requester_name) as TextView
            txt_Requester_Cargo = itemView.findViewById(R.id.txt_requester_cargo) as TextView
            txt_glpi_currenttime = itemView.findViewById(R.id.txt_fecha_apertura) as TextView
            txt_EstadoColor = itemView.findViewById(R.id.txt_estadoColor) as TextView


            itemView.setOnClickListener {
                itemClickListener_.onTicketClick(
                    dataModelArrayList[position].getGlpiID(),
                    dataModelArrayList[position].getGlpiOperadorName(),
                    dataModelArrayList[position].getCurrentTime(),
                    dataModelArrayList[position].getGlpiContenido(),
                    dataModelArrayList[position].getGlpiTipo(),
                    dataModelArrayList[position].getGlpiUbicacionSolicitante(),
                    dataModelArrayList[position].getGlpiCorreoSolicitante(),
                    dataModelArrayList[position].getGlpiRequesterName(),
                    dataModelArrayList[position].getGlpiRequesterCargo(),
                    dataModelArrayList[position].getGlpiTelefonoSolicitante(),
                    dataModelArrayList[position].getGlpiLoginName(),
                    dataModelArrayList[position].getGlpiEstado(),
                    dataModelArrayList[position].getGlpiCategoria(),
                    dataModelArrayList[position].getGlpiOrigen(),
                    dataModelArrayList[position].getGlpiUrgencia()
                )
            }
        }
    }
}