package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.decodeHtml
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsAgregarSeguimientoBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_FollowupTemplate
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_TasksTemplate
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_FollowupTemplate
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_TasksTemplate
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import org.json.JSONArray
import java.util.HashMap

class TicketsAgregarSeguimientoActivity : AppCompatActivity(), RecycleView_Adapter_FollowupTemplate.onFollowTemplateClickListener {
    private var recyclerView: RecyclerView? = null
    /*creamos la lista de arreglos que tendrá los objetos,
   esta lista de arreglos (dataModelArrayList) funcionará como fuente de datos*/
    internal lateinit var dataModelArrayListFollowupTemplate: ArrayList<Data_FollowupTemplate>
    private var recyclerView_Adapter_FollowupTemplate: RecycleView_Adapter_FollowupTemplate? = null
    private lateinit var binding: ActivityTicketsAgregarSeguimientoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsAgregarSeguimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ticketInfo()

        var flagTasks = MainActivity.flag
        recyclerView = binding.includeModalFollowupTemplate.recyclerFollowupTemplate

        volleyRequestFollowupTemplates(MainActivity.urlApi_FollowupTemplates)

        if (flagTasks){
            val intent = intent.extras
            val ticketId = intent!!.getString("ticketId")
            val tasksDescription = intent!!.getString("tasks_description","")
            binding.tvIdTicket.text = "Petición #$ticketId"
            binding.edtFollowupDescription.setText(tasksDescription)
            MainActivity.flag = false
        }

        btn_fabs_taddsegact()
        btn_header_taddsegact()
    }

    private fun ticketInfo(){
        val bundle = intent.extras
        binding.tvIdTicket.text = "Petición #${bundle!!.getString("TicketID")}"
    }
    //INICIO - funcion de maneja los botones del header
    private fun btn_header_taddsegact() {
        //boton atras
        binding.btnAtrasActtaddseg.setOnClickListener {
            onBackPressed()
        }
        //boton agregar seguimiento
        binding.btnAddsegActtaddseg.setOnClickListener {
            Toast.makeText(this, "seguiemiento añadido", Toast.LENGTH_LONG).show()
            val intent_agregarSeguimiento = Intent(this, MainActivity::class.java)
            intent_agregarSeguimiento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_agregarSeguimiento)
        }
    }
    //FIN - funcion de maneja los botones del header

    //INICIO - funcion de fabs que abre camara del celular y archivos del celular
    private fun btn_fabs_taddsegact() {
        var click_desplegar = false
        //boton para desplegar y plegar los fabs
        binding.fabDesplegarAddseg.setOnClickListener {
            if (click_desplegar == false){
                binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = true
                binding.lyFabsActtaddseg.isVisible = true
                click_desplegar = true
            }else{
                binding.lyFabsActtaddseg.isVisible = false
                binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = false
                click_desplegar = false
            }
        }

        //archivo del celular
        binding.fabArchivoActtaddseg.setOnClickListener {
            Toast.makeText(this, "abrir archivos del celular", Toast.LENGTH_SHORT).show()
        }

        //camara del celular
        binding.fabFotoActtaddseg.setOnClickListener {
            Toast.makeText(this, "abrir camara del celular", Toast.LENGTH_SHORT).show()
        }

        //platilla
        binding.fabFollowupTemplates.setOnClickListener {
            binding.includeModalFollowupTemplate.modalPlantillaAddFollowup.isVisible = true
            binding.lyFabsActtaddseg.isVisible = false
        }
        //fondo gris para cerra modal
        binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.setOnClickListener {
            binding.lyFabsActtaddseg.isVisible = false
            binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = false
            binding.includeModalFollowupTemplate.modalPlantillaAddFollowup.isVisible = false
            click_desplegar = false
        }
        //FIN - funcion de fabs que abre camara del celular y archivos del celular
    }

    private fun volleyRequestFollowupTemplates(urlapiFollowuptemplates: String) {
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlapiFollowuptemplates, Response.Listener { response ->
                try {
                    dataModelArrayListFollowupTemplate = ArrayList()

                    val jsonObjectResponse = JSONArray(response)
                    var iterador = 0
                    for (i in  0 until jsonObjectResponse.length()){
                        val nTemplate = jsonObjectResponse.getJSONObject(i)
                        val player = Data_FollowupTemplate()
                        player.setnameFollowupTemplates(nTemplate.getString("NOMBRE"))
                        player.setcontentFollowupTemplates(nTemplate.getString("CONTENIDO"))
                        //iterador++
                        Log.i("mensaje posicion",""+nTemplate.getString("NOMBRE"))
                        Log.i("mensaje posicion",""+jsonObjectResponse.length())
                        dataModelArrayListFollowupTemplate.add(player)
                    }
                    setupRecycler()
                } catch (e: Exception) {
                    e.printStackTrace()
                    //Toast.makeText(this, "token expirado: $e", Toast.LENGTH_LONG).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                return params
            }
        }
        this?.let {
            VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets)
        }
    }

    private fun setupRecycler() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true,)
        layoutManager.stackFromEnd = true

        recyclerView!!.layoutManager = layoutManager

        recyclerView_Adapter_FollowupTemplate =
            RecycleView_Adapter_FollowupTemplate(this,dataModelArrayListFollowupTemplate,this)
        recyclerView!!.adapter = recyclerView_Adapter_FollowupTemplate
    }

    override fun onFollowupTemplateClick(nameFollowupTemplate: String, contentFollowupTemplate: String) {
        Toast.makeText(this, ""+nameFollowupTemplate, Toast.LENGTH_LONG).show()

        binding.edtFollowupDescription.setText(decodeHtml(contentFollowupTemplate))
    }

}