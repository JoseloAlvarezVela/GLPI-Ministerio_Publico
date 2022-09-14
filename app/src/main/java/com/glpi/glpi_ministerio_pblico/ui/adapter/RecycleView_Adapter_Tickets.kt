package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
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
        //val viewFooter = inflater.inflate(R.layout.activity_tickets_historico, parent, false)
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_tickets,
        //parent,false)


        //return MyViewHolder(view,viewFooter)
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

        holder.txt_glpi_currenttime.text = dataModelArrayList[position].getCurrentTime()

        holder.txt_descripcionTicket.text = HtmlCompat.fromHtml(dataModelArrayList[position].getGlpiDescripcion(), HtmlCompat.FROM_HTML_MODE_LEGACY)

        if(dataModelArrayList[position].getGlpiEstado() == "EN CURSO (Asignada)"){
            holder.txt_EstadoColor.setBackgroundResource(R.drawable.esq_redondeada_amarillo)
        }
        else if(dataModelArrayList[position].getGlpiEstado() == "URGENTE"){
            holder.txt_EstadoColor.setBackgroundResource(R.drawable.esq_redondeada_rojo)
        }
        else{
            holder.txt_EstadoColor.setBackgroundResource(R.drawable.esq_redondeada_gris)
        }

        holder.txt_Requester_Name.setText("Solicitante: "+dataModelArrayList[position].getGlpiRequesterName())
        holder.txt_Requester_Cargo.setText(dataModelArrayList[position].getGlpiRequesterCargo())

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
        var txt_Requester_Cargo: TextView
        //var txt_currentTime: TextView

        var txt_descripcionTicket: TextView
        var txt_EstadoColor: TextView


        init {
            tickets = itemView.findViewById(R.id.tickets) as LinearLayout
            txt_numero_ticket = itemView.findViewById(R.id.txt_numero_ticket) as TextView
            txt_tipo = itemView.findViewById(R.id.txt_tipo) as TextView
            txt_glpi_currenttime = itemView.findViewById(R.id.txt_fecha_apertura) as TextView
            txt_Requester_Name = itemView.findViewById(R.id.txt_requester_name) as TextView
            txt_Requester_Cargo = itemView.findViewById(R.id.txt_requester_cargo) as TextView
            txt_descripcionTicket = itemView.findViewById(R.id.txt_descripcionTicket) as TextView
            txt_EstadoColor = itemView.findViewById(R.id.txt_estadoColor) as TextView

            //enviamos los datos a la activity
            tickets.setOnClickListener {
                val intent = Intent(itemView.context, NavFooterTicketsActivity::class.java)
                intent.putExtra("fechaApertura", txt_glpi_currenttime.text)
                intent.putExtra("descripción", txt_descripcionTicket.text)

                itemView.context.startActivity(intent)
            }
        }
    }


}