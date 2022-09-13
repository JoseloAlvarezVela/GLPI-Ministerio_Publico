package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.ui.tickets.NavFooterTicketsActivity


/*adaptador que obtendrá los datos del archivo de actividad principal
y llenará la vista del reciclador*/
class RecycleView_Adapter_Tickets(context:Context, private val dataModelArrayList:ArrayList<Data_Tickets>):
    RecyclerView.Adapter<RecycleView_Adapter_Tickets.MyViewHolder>(){

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    /*Dentro del método onCreateViewHolder, el compilador inflará el archivo
    fragment_mis_peticiones.xml para que pueda desarrollar el diseño de cada fila
    de la vista del reciclador*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecycleView_Adapter_Tickets.MyViewHolder {

        //val inflater_ = LayoutInflater.from(parent.getContext())
        val view = inflater.inflate(R.layout.recycleview_tickets, parent, false)
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_tickets,
        //parent,false)


        return MyViewHolder(view)
    }

    /*el método onBindViewHolder() asiganará los datos a los componentes*/
    override fun onBindViewHolder(holder: RecycleView_Adapter_Tickets.MyViewHolder, position: Int) {
        holder.txt_numero_ticket.setText("#"+dataModelArrayList[position].getGlpiID())

        holder.txt_tipo.setText(dataModelArrayList[position].getGlpiTipo())
        if (dataModelArrayList[position].getGlpiTipo() == "SOLICITUD"){
            holder.txt_tipo.setTextColor(Color.parseColor("#3FC3FF"))
            holder.txt_numero_ticket.setBackgroundColor((Color.parseColor("#3FC3FF")))
        }else{
            holder.txt_tipo.setTextColor(Color.parseColor("#C5A83F"))
            holder.txt_numero_ticket.setBackgroundColor((Color.parseColor("#C5A83F")))
        }

        holder.txt_glpi_currenttime.setText(dataModelArrayList[position].getCurrentTime())

        //holder.txt_distritosFiscales.setText(dataModelArrayList[position].getGlpiName())
        holder.txt_descripcionTicket.setText(dataModelArrayList[position].getGlpiDescripcion())

        if(dataModelArrayList[position].getGlpiEstado() == "EN CURSO (Asignada)"){
            holder.txt_EstadoColor.setBackgroundResource(R.drawable.esq_redondeada_amarillo)
        }
        else{
            holder.txt_EstadoColor.setBackgroundResource(R.drawable.esq_redondeada_gris)
        }
        //onClick Listener para los elementos
        holder.tickets.setOnClickListener {
            val context = holder.tickets.context
            val intent = Intent( context, NavFooterTicketsActivity::class.java)
            context.startActivity(intent)
        }

        holder.txt_Requester_Name.setText("Solicitante: "+dataModelArrayList[position].getGlpiRequesterName())
        //holder.txt_Requester_Apellido.setText(dataModelArrayList[position].getGlpiRequesterApellido())
    }

    //retornamos el tamaño de los datos obtenidos para mostrar en el recycleview
    override fun getItemCount(): Int {
        return dataModelArrayList.size
    }

    //inicializamos los componetes de nuestro ticket de la forma tradicional
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tickets : LinearLayout //para evento click
        var txt_numero_ticket : TextView
        var txt_tipo : TextView
        var txt_glpi_currenttime: TextView
        var txt_Requester_Name: TextView
        //var txt_Requester_Apellido: TextView

        //var txt_distritosFiscales: TextView
        var txt_descripcionTicket: TextView
        var txt_EstadoColor: TextView


        init {
            tickets = itemView.findViewById(R.id.tickets) as LinearLayout
            txt_numero_ticket = itemView.findViewById(R.id.txt_numero_ticket) as TextView
            txt_tipo = itemView.findViewById(R.id.txt_tipo) as TextView
            txt_glpi_currenttime = itemView.findViewById(R.id.txt_fecha_apertura) as TextView
            txt_Requester_Name = itemView.findViewById(R.id.txt_requester_name) as TextView
            //txt_Requester_Apellido = itemView.findViewById(R.id.txt_requester_apellido) as TextView

            //txt_distritosFiscales = itemView.findViewById(R.id.txt_distritosFiscales) as TextView
            txt_descripcionTicket = itemView.findViewById(R.id.txt_descripcionTicket) as TextView
            txt_EstadoColor = itemView.findViewById(R.id.txt_estadoColor) as TextView
        }
    }
}