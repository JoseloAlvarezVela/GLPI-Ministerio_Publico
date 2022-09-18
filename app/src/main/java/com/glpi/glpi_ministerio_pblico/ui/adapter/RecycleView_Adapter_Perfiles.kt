package com.glpi.glpi_ministerio_pblico.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R

/*adaptador que obtendrá los datos del archivo de actividad principal
y llenará la vista del reciclador*/
class RecycleView_Adapter_Perfiles(
    context: Context,
    private val dataModelArrayListPerfiles:ArrayList<Data_Perfiles>,
    val mItemClickListener:ItemClickListener
    ):RecyclerView.Adapter<RecycleView_Adapter_Perfiles.MyViewHolderPerfil>() {

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    //interfaz que manejara los eventos click
    interface ItemClickListener{
        fun onItemClick(position: Data_Perfiles)
        fun onLongClick(position: Data_Perfiles)
    }

    /*Dentro del método onCreateViewHolder, el compilador inflará el archivo
    "fragment_mis_peticiones.xml" para que pueda desarrollar el diseño de cada fila
    de la vista del reciclador*/
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleView_Adapter_Perfiles.MyViewHolderPerfil {
        val viewPerfiles = inflater.inflate(R.layout.recyclerview_perfiles, parent,false)

        return MyViewHolderPerfil(viewPerfiles)
        //return MyViewHolderPerfil(LayoutInflater.from(context).inflate(R.layout.modal_perfiles,parent,false))
    }

    /*el método onBindViewHolder() asiganará los datos a los componentes*/
    override fun onBindViewHolder(
        holder: RecycleView_Adapter_Perfiles.MyViewHolderPerfil,
        position: Int
    ) {
        holder.btn_perfiles_.text = dataModelArrayListPerfiles[position].getGlpiPerfilLogin()
    }
    //retornamos el tamaño de los datos obtenidos para mostrar en el recycleview
    override fun getItemCount(): Int {
        return dataModelArrayListPerfiles.size
    }

    //inicializamos los componetes de nuestro Perfil
    inner class MyViewHolderPerfil(itemViewPerfil: View): RecyclerView.ViewHolder(itemViewPerfil){
        var btn_perfiles_ : TextView

        init {
            btn_perfiles_ = itemViewPerfil.findViewById(R.id.btn_perfil) as TextView

            itemViewPerfil.setOnClickListener{
                mItemClickListener.onItemClick(dataModelArrayListPerfiles[position])
            }
            itemViewPerfil.setOnLongClickListener{
                mItemClickListener.onLongClick(
                    dataModelArrayListPerfiles[position]
                )
                return@setOnLongClickListener true
            }

        }
    }

}