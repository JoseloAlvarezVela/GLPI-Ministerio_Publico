package com.glpi.glpi_ministerio_pblico.ui.modals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_Entities
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_Perfiles
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_Entities
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_Perfiles

class Modal_Perfiles_Activity : AppCompatActivity(), RecycleView_Adapter_Perfiles.ItemClickListener {

    /*creamos la lista de arreglos que tendrá los objetos de la clase Data_Perfiles
    esta lista de arreglos (dataModelArrayListPerfiles) funcionará como fuente de datos*/
    internal lateinit var dataModelArrayListPerfil: ArrayList<Data_Perfiles>
    internal lateinit var dataModelArrayListEntities: ArrayList<Data_Entities>
    //creamos el objeto de la clase RecycleView_Adapter_Tickets
    private var RecycleView_Adapter_Perfiles: RecycleView_Adapter_Perfiles? = null
    private var RecycleView_Adapter_Entities: RecycleView_Adapter_Entities? = null
    //creamos el objeto de la clase recyclerView
    private var recyclerViewPerfiles: RecyclerView? = null
    private var recyclerViewEntities: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modal_perfiles)

        val btn = findViewById<Button>(R.id.btn_perfil)

        val recyclerPerfiles = findViewById<RecyclerView>(R.id.recycler_perfiles)
        recyclerPerfiles.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        RecycleView_Adapter_Perfiles = RecycleView_Adapter_Perfiles(this,dataModelArrayListPerfil,this)
        recyclerPerfiles.adapter = RecycleView_Adapter_Perfiles

        Toast.makeText(this, "aca se puede escribir codigo $btn", Toast.LENGTH_SHORT).show()

    }

    override fun onItemClick(position: Data_Perfiles) {
        Toast.makeText(this, "click 1", Toast.LENGTH_SHORT).show()
    }

    override fun onLongClick(position: Data_Perfiles) {
        Toast.makeText(this, "click 2", Toast.LENGTH_SHORT).show()
    }
}