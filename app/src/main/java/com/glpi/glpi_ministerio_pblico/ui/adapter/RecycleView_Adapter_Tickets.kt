package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.ui.tickets.NavFooterTicketsActivity
import java.util.ArrayList

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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleView_Adapter_Tickets.MyViewHolder {
        val view = inflater.inflate(R.layout.recycleview_tickets, parent, false)

        return MyViewHolder(view)
    }

    /*el método onBindViewHolder() asiganará los datos a los componentes*/
    override fun onBindViewHolder(holder: RecycleView_Adapter_Tickets.MyViewHolder, position: Int) {
        holder.txt_glpi_currenttime.setText(dataModelArrayList[position].getCurrentTime())
        holder.txt_numero_ticket.setText(dataModelArrayList[position].getGlpiID())
        holder.txt_distritosFiscales.setText(dataModelArrayList[position].getGlpiName())
        holder.txt_descripcionTicket.setText(dataModelArrayList[position].getGlpiDescripcion())

        // onClick Listener para los elementos
        holder.tickets.setOnClickListener {
            val context=holder.tickets.context
            val intent = Intent( context, NavFooterTicketsActivity::class.java)
            context.startActivity(intent)
        }
    }

    //retornamos el tamaño de los datos obtenidos para mostrar en el recycleview
    override fun getItemCount(): Int {
        return dataModelArrayList.size
    }

    //inicializamos los componetes de nuestro ticket de la forma tradicional
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tickets : LinearLayout //para evento click
        var txt_glpi_currenttime: TextView
        var txt_numero_ticket : TextView
        var txt_distritosFiscales: TextView
        var txt_descripcionTicket: TextView

        init {
            tickets = itemView.findViewById(R.id.tickets) as LinearLayout
            txt_glpi_currenttime = itemView.findViewById(R.id.txt_fecha_apertura) as TextView
            txt_numero_ticket = itemView.findViewById(R.id.txt_numero_ticket) as TextView
            txt_distritosFiscales = itemView.findViewById(R.id.txt_distritosFiscales) as TextView
            txt_descripcionTicket = itemView.findViewById(R.id.txt_descripcionTicket) as TextView
        }
    }


}