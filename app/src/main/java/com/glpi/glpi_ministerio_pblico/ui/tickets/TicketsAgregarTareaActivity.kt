package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.decodeHtml
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.flag
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TasksTemplate
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsAgregarTareaBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_TasksTemplate
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_TasksTemplate
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class TicketsAgregarTareaActivity : AppCompatActivity(), RecycleView_Adapter_TasksTemplate.onTasksTemplateClickListener {
    lateinit var binding: ActivityTicketsAgregarTareaBinding //declaramos binding para acceder variables
    //creamos el objeto de la clase recyclerView
    private var recyclerView: RecyclerView? = null
    /*creamos la lista de arreglos que tendrá los objetos de la clase Data_Tickets
   esta lista de arreglos (dataModelArrayList) funcionará como fuente de datos*/
    internal lateinit var dataModelArrayListTasksTemplate: ArrayList<Data_TasksTemplate>
    private var recyclerView_Adapter_TasksTemplate: RecycleView_Adapter_TasksTemplate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsAgregarTareaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var flagTasks = flag
        recyclerView = binding.includeModalPlantillaTarea.recyclerTasksTemplate
        //recyclerView = binding.recyclerTaskTemplate

        ticketInfo()
        //setupRecycler()
        volleyRequestDataTasksTemplate()

        if (flagTasks){
            val intent = intent.extras
            val ticketId = intent!!.getString("ticketId")
            val tasksDescription = intent!!.getString("tasks_description","")
            binding.tvIdTicket.text = "Petición #$ticketId"
            binding.edtTasksDescription.setText(tasksDescription)
            flag = false
        }

        btn_fabs()
        btn_atras()
        btn_agregarTarea()
        btn_agregarCat()

    }

    private fun ticketInfo(){
        val bundle = intent.extras
        binding.tvIdTicket.text = "Petición #${bundle!!.getString("TicketID")}"
    }

    private fun volleyRequestDataTasksTemplate() {
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlApi_TasksTemplate, Response.Listener { response ->
                try {
                    dataModelArrayListTasksTemplate = ArrayList()

                    val jsonObjectResponse = JSONArray(response)
                    var iterador = 0
                    for (i in  0 until jsonObjectResponse.length()){
                        val nTemplate = jsonObjectResponse.getJSONObject(iterador)
                        val player = Data_TasksTemplate()
                        player.setNombreTasksTemplate(nTemplate.getString("NOMBRE"))
                        player.setContentTasksTemplate(nTemplate.getString("CONTENIDO"))
                        iterador++
                        Log.i("mensaje posicion",""+nTemplate.getString("NOMBRE"))
                        dataModelArrayListTasksTemplate.add(player)
                    }
                    setupRecycler()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "token expirado: $e", Toast.LENGTH_LONG).show()
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

        recyclerView_Adapter_TasksTemplate =
            RecycleView_Adapter_TasksTemplate(this,dataModelArrayListTasksTemplate,this)
        recyclerView!!.adapter = recyclerView_Adapter_TasksTemplate
    }

    //INICIO - funcion que abre modal para escoger una tarea
    private fun btn_agregarCat() {
        //INICO - boton que abre modal_agregar_cat_tickets.xml
        binding.btnAddcatActtaddt.setOnClickListener {
            binding.LayoutBackgroudAgregarTarea.isVisible = true //fondo gris casi transparente
            binding.includeModalPlantillaAddcat.modalPlantillaAgregarCategoria.isVisible = true //modal
        }

        //INICIO - boton que cierra modal_agregar_cat_tickets.xml
        binding.includeModalPlantillaAddcat.btnSalirModalAddcat.setOnClickListener {
            binding.includeModalPlantillaAddcat.modalPlantillaAgregarCategoria.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
        }
    }

    //INICIO - función que añade la tarea y te devuelve al menu principal
    private fun btn_agregarTarea() {
        binding.btnAddtActtaddt.setOnClickListener {
            Toast.makeText(this, "tarea añadida", Toast.LENGTH_LONG).show()
            val intent_agregarTarea = Intent(this, MainActivity::class.java)
            intent_agregarTarea.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_agregarTarea)
        }
    }

    //INICIO - funcion que regresa a la vista anterior: activity_nav_footer_tickets.xml
    private fun btn_atras() {
       binding.btnAtrasActtaddt.setOnClickListener {
           onBackPressed()
           /*val intent_atras = Intent(this, NavFooterTicketsActivity::class.java)
           intent_atras.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
           startActivity(intent_atras)*/
       }
    }

    //INICIO - funcion que despliega los FAB's
    var click: Boolean = false
    private fun btn_fabs(){


        //INICIO DE inicialización de variables
        val desplegar_fabs = binding.fabDesplegarAddTarea
        val abrir_plantilla = binding.btnFabPlantilla
            val layout_plantilla = binding.fabPlantilla
        val abrir_camara = binding.btnFabFoto
            val layout_camara = binding.fabFoto
        val abrir_archivo = binding.btnFabArchivo
            val layout_archivo = binding.fabArchivo
        val hide_fab = binding.LayoutFabAgregarTarea
        val backGround = binding.LayoutBackgroudAgregarTarea //layout gris casitransparente para modals
        val cerrar_plantilla = binding.includeModalPlantillaTarea.btnCerrarMdplaaddt

        //INICIO - eventos de click de los FAB's
        desplegar_fabs.setOnClickListener {
            if (click == false){
                layout_plantilla.isVisible = true
                layout_camara.isVisible = true
                layout_archivo.isVisible = true
                backGround.isVisible = true
                click = true
            }else{
                layout_plantilla.isVisible = false
                layout_camara.isVisible = false
                layout_archivo.isVisible = false
                backGround.isVisible = false
                click = false
            }

        }

        //INICIO - evento de click que abre modal_plantilla_agreegar_tarea.xml
        abrir_plantilla.setOnClickListener {
            binding.includeModalPlantillaTarea.modalPlantillaAgregarTarea.isVisible = true
            hide_fab.isVisible = false
        }

        //INICIO - evento de click que cierra modal_plantilla_agreegar_tarea.xml
        cerrar_plantilla.setOnClickListener {
            binding.includeModalPlantillaTarea.modalPlantillaAgregarTarea.isVisible = false
            backGround.isVisible = false
            layout_plantilla.isVisible = false
            layout_camara.isVisible = false
            layout_archivo.isVisible = false
            backGround.isVisible = false
            binding.LayoutFabAgregarTarea.isVisible = true
            click = false
        }

        //INICIO - evento de click que abre camara del celular
        abrir_camara.setOnClickListener {
            Toast.makeText(this, "introduce código que abre la camara del celular", Toast.LENGTH_SHORT).show()
        }

        //INICIO - evento de click que abre archivos del celular
        abrir_archivo.setOnClickListener {
            Toast.makeText(this, "Abrir Archivos", Toast.LENGTH_SHORT).show()
        }

        //INICIO - evento click que cierra modals
        backGround.setOnClickListener {
            binding.includeModalPlantillaTarea.modalPlantillaAgregarTarea.isVisible = false
            binding.includeModalPlantillaAddcat.modalPlantillaAgregarCategoria.isVisible = false
            layout_plantilla.isVisible = false
            layout_camara.isVisible = false
            layout_archivo.isVisible = false
            backGround.isVisible = false
            binding.LayoutFabAgregarTarea.isVisible = true
            click = false
        }
        //FIN de eventos de click de los FAB's
    }
    //FIN - funcion que despliega los FAB's


    override fun onTasksTemplateClick(nameTasksTemplate: String, contentTasksTemplate: String) {
        binding.includeModalPlantillaTarea.modalPlantillaAgregarTarea.isVisible = false
        binding.LayoutBackgroudAgregarTarea.isVisible = false
        binding.fabPlantilla.isVisible = false
        binding.fabFoto.isVisible = false
        binding.fabArchivo.isVisible = false
        click = false
        binding.LayoutFabAgregarTarea.isVisible = true
        binding.edtTasksDescription.setText(decodeHtml(contentTasksTemplate))
        binding.edtTasksDescription.setTextColor(Color.parseColor("#1D20DD"))
        /*val fullHtml = "<h2 style=\"text-align: center;\"><span style=\"color: #0000ff;\">ACOMPAÑAMIENTO</span></h2><h4><span style=\"color: #0000ff;\"><span style=\"color: #ff0000;\">Evento:</span> </span></h4><h4><span style=\"color: #0000ff;\"><span style=\"color: #ff0000;\">Modalidad (pres./rem.):</span> </span></h4>"
        val decodeFullhtml = fullHtml.split("</span>") as ArrayList
        binding.edtTasksDescription.setText(decodeFullhtml.toString())
        binding.edtTasksDescription.setTextColor(Color.parseColor("#1D20DD"))
        for(i in 0 until decodeFullhtml.size){
            Log.i("mensaje html",""+ decodeHtml(decodeFullhtml[i]))
            binding.edtTasksDescription.setText(decodeHtml(decodeFullhtml[i]))
        }*/
        //Log.i("mensaje html",""+ decodeHtml(decodeFullhtml[0]))
        //binding.edtTasksDescription.setText(decodeFullhtml.toString())
    }
}