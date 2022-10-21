package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.graphics.Color
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
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.flagEdit
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TasksCategory
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TasksTemplate
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsAgregarTareaBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_TasksCategory
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_TasksTemplate
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_TasksCategory
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_TasksTemplate
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TicketsAgregarTareaActivity : AppCompatActivity(),
    RecycleView_Adapter_TasksTemplate.onTasksTemplateClickListener,
    RecycleView_Adapter_TasksCategory.onTasksCategoryClickListener {

    lateinit var binding: ActivityTicketsAgregarTareaBinding //declaramos binding para acceder variables
    //creamos el objeto de la clase recyclerView
    private var recyclerView: RecyclerView? = null
    private var recyclerViewCategory: RecyclerView? = null
    /*creamos la lista de arreglos que tendrá los objetos de la clase Data_Tickets
   esta lista de arreglos (dataModelArrayList) funcionará como fuente de datos*/
    internal lateinit var dataModelArrayListTasksTemplate: ArrayList<Data_TasksTemplate>
    internal lateinit var dataModelArrayListTasksCategory: ArrayList<Data_TasksCategory>
    private var recyclerView_Adapter_TasksTemplate: RecycleView_Adapter_TasksTemplate? = null
    private var recyclerView_Adapter_TasksCategory: RecycleView_Adapter_TasksCategory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsAgregarTareaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var flagTasks = flagEdit
        recyclerView = binding.includeModalPlantillaTarea.recyclerTasksTemplate
        recyclerViewCategory = binding.includeModalPlantillaAgregarCategoria.recyclerTasksCategory
        //recyclerView = binding.recyclerTaskTemplate

        ticketInfo()
        volleyRequestDataTasksTemplate()
        volleyRequestDataTasksCategory()

        if (flagTasks){
            val intent = intent.extras
            val ticketId = intent!!.getString("ticketId")
            val tasksDescription = intent!!.getString("tasks_description")
            binding.tvIdTicket.text = "Petición #$ticketId"
            binding.edtTasksDescription.setText(tasksDescription)
            flagEdit = false
        }

        btn_fabs()
        btn_atras()

        btn_agregarCat()
        btnTimeToSolveTask()
        btnDocuments()

    }

    private fun btnDocuments(){
        var flagBtnShowDocuments = false
        binding.includeDocumentsTaks.btnShowDocuments.setOnClickListener {
            if (!flagBtnShowDocuments){
                Toast.makeText(this, "$flagBtnShowDocuments", Toast.LENGTH_SHORT).show()
                binding.includeDocumentsTaks.contentDocuments.isVisible = true
                flagBtnShowDocuments = true
            }else{
                binding.includeDocumentsTaks.contentDocuments.isVisible = false
                flagBtnShowDocuments = false
            }

        }
    }

    private fun btnTimeToSolveTask(){
        binding.btnTimeToSolveTask.setOnClickListener {
            binding.LayoutBackgroudAgregarTarea.isVisible = true
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = true
            val timer = 5
            binding.btnTimeToSolveTask.text = "$timer minutos"
            //Log.i("mensaje TimeToSolveTask","${ binding.btnTimeToSolveTask.text.split(" ")}")
        }
        binding.includeTimeToSolveTask.btnCloseUnderModalTimeToSolveTask.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
        }
        binding.includeTimeToSolveTask.btnCloseModalTimeToSolveTask.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
        }
        binding.includeTimeToSolveTask.btn5min.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
            binding.btnTimeToSolveTask.text = binding.includeTimeToSolveTask.btn5min.text
            val timer = binding.btnTimeToSolveTask.text.split(" ")
            Log.i("mensaje newTimer", timer[0])
        }
        binding.includeTimeToSolveTask.btn10min.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
            binding.btnTimeToSolveTask.text = binding.includeTimeToSolveTask.btn10min.text
            val timer = binding.btnTimeToSolveTask.text.split(" ")
            Log.i("mensaje newTimer", timer[0])
        }
        binding.includeTimeToSolveTask.btn15min.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
            binding.btnTimeToSolveTask.text = binding.includeTimeToSolveTask.btn15min.text
            val timer = binding.btnTimeToSolveTask.text.split(" ")
            Log.i("mensaje newTimer", timer[0])
        }
        binding.includeTimeToSolveTask.btn20min.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
            binding.btnTimeToSolveTask.text = binding.includeTimeToSolveTask.btn20min.text
            val timer = binding.btnTimeToSolveTask.text.split(" ")
            Log.i("mensaje newTimer", timer[0])
        }
        binding.includeTimeToSolveTask.btn25min.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
            binding.btnTimeToSolveTask.text = binding.includeTimeToSolveTask.btn25min.text
            val timer = binding.btnTimeToSolveTask.text.split(" ")
            Log.i("mensaje newTimer", timer[0])
        }
        binding.includeTimeToSolveTask.btn30min.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
            binding.btnTimeToSolveTask.text = binding.includeTimeToSolveTask.btn30min.text
            val timer = binding.btnTimeToSolveTask.text.split(" ")
            Log.i("mensaje newTimer", timer[0])
        }
        binding.includeTimeToSolveTask.btn35min.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
            binding.btnTimeToSolveTask.text = binding.includeTimeToSolveTask.btn35min.text
            val timer = binding.btnTimeToSolveTask.text.split(" ")
            Log.i("mensaje newTimer", timer[0])
        }
        binding.includeTimeToSolveTask.btn40min.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
            binding.btnTimeToSolveTask.text = binding.includeTimeToSolveTask.btn40min.text
            val timer = binding.btnTimeToSolveTask.text.split(" ")
            Log.i("mensaje newTimer", timer[0])
        }
        binding.includeTimeToSolveTask.btn45min.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
            binding.btnTimeToSolveTask.text = binding.includeTimeToSolveTask.btn45min.text
            val timer = binding.btnTimeToSolveTask.text.split(" ")
            Log.i("mensaje newTimer", timer[0])
        }
        binding.includeTimeToSolveTask.btn50min.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
            binding.btnTimeToSolveTask.text = binding.includeTimeToSolveTask.btn50min.text
            val timer = binding.btnTimeToSolveTask.text.split(" ")
            Log.i("mensaje newTimer", timer[0])
        }
        binding.includeTimeToSolveTask.btn55min.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
            binding.btnTimeToSolveTask.text = binding.includeTimeToSolveTask.btn55min.text
            val timer = binding.btnTimeToSolveTask.text.split(" ")
            Log.i("mensaje newTimer", timer[0])
        }
        binding.includeTimeToSolveTask.btn60min.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
            binding.btnTimeToSolveTask.text = binding.includeTimeToSolveTask.btn60min.text
            val timer = binding.btnTimeToSolveTask.text.split(" ")
            Log.i("mensaje newTimer", timer[0])
        }
    }

    private fun ticketInfo(){
        val bundle = intent.extras
        val idTicket = bundle!!.getString("TicketID")
        val ticketType = bundle!!.getString("ticketType") //solicitud o incidente
        val ticketStatus = bundle!!.getString("ticketStatus")//en curso ,cerrado ...
        val getTaskUsersEstimateDuration = bundle!!.getString("getTaskUsersEstimateDuration")
        binding.tvIdTicket.text = "Petición $idTicket"

        if (getTaskUsersEstimateDuration != null){
            binding.btnTimeToSolveTask.text = getTaskUsersEstimateDuration
        }


        if(ticketType == "SOLICITUD"){
            binding.imgBtnTypeTasks.setImageResource(R.drawable.ic_interrogacion)
        }else{
            binding.imgBtnTypeTasks.setImageResource(R.drawable.ic_incidencia)
        }

        Log.i("mensaje","$ticketStatus")
        if(ticketStatus == "EN CURSO (Asignada)"){
            binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo_verde)
            binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_verde)
        }else if(ticketStatus == "EN ESPERA"){
            Toast.makeText(this, "$ticketStatus", Toast.LENGTH_SHORT).show()
            binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo)
            binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo)
        }else if (ticketStatus == "CERRADO"){
            binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo_negro)
            binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_negro)
        }

        imgBtnTicketStatus(ticketStatus)
        btnAddTasks()

    }

    private fun imgBtnTicketStatus(ticketStatus: String?){
        var flagTicketStatus: Boolean
        var newTicketStatus = "EN CURSO (Asignada)"
        if (ticketStatus == "EN CURSO (Asignada)"){
            flagTicketStatus = true
            binding.imgBtnStatusTasks.setOnClickListener {
                if(flagTicketStatus){
                    binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo)
                    binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo)
                    newTicketStatus = "EN ESPERA"
                    flagTicketStatus = false
                }else{
                    binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo_verde)
                    binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_verde)
                    newTicketStatus = "EN CURSO (Asignada)"
                    flagTicketStatus = true
                }
            }
        }else{
            flagTicketStatus = false
            binding.imgBtnStatusTasks.setOnClickListener {
                if(flagTicketStatus){
                    binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo)
                    binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo)
                    newTicketStatus = "EN ESPERA"
                    flagTicketStatus = false
                }else{
                    binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo_verde)
                    binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_verde)
                    newTicketStatus = "EN CURSO (Asignada)"
                    flagTicketStatus = true
                }
            }
        }
    }

    private fun volleyRequestDataTasksTemplate() {
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlApi_TasksTemplate, Response.Listener { response ->
                try {
                    dataModelArrayListTasksTemplate = ArrayList()
                    val jsonObjectResponse = JSONArray(response)
                    //var iterador = 0
                    for(i in  0 until jsonObjectResponse.length()){
                        val nTemplate = jsonObjectResponse.getJSONObject(i)
                        val player = Data_TasksTemplate()
                        player.setNameTasksTemplates(nTemplate.getString("NOMBRE"))
                        player.setContentTasksTemplates(nTemplate.getString("CONTENIDO"))
                        player.setCategoryTasksTemplates(nTemplate.getString("CATEGORIA"))
                        player.setTimeTasksTemplates(nTemplate.getString("TIEMPO"))
                        //iterador++
                        //Log.i("mensaje posicion",""+nTemplate.getString("NOMBRE"))
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

    private fun volleyRequestDataTasksCategory() {
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlApi_TasksCategory, Response.Listener { response ->
                try {
                    dataModelArrayListTasksCategory = ArrayList()

                    val jsonObjectResponse = JSONArray(response)
                    var iterador = 0
                    for (i in  0 until jsonObjectResponse.length()){
                        val nTemplate = jsonObjectResponse.getJSONObject(iterador)
                        val player = Data_TasksCategory()
                        player.setNameTasksCategories(nTemplate.getString("NOMBRE"))
                        player.setContentTasksCategories(nTemplate.getString("COMPLETO"))
                        iterador++
                        Log.i("mensaje posicion",""+nTemplate.getString("NOMBRE"))
                        dataModelArrayListTasksCategory.add(player)
                    }
                    setupRecyclerCategory()
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

    private fun setupRecyclerCategory() {
            val layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true,)
            layoutManager.stackFromEnd = true

        recyclerViewCategory!!.layoutManager = layoutManager

            recyclerView_Adapter_TasksCategory =
                RecycleView_Adapter_TasksCategory(this,dataModelArrayListTasksCategory,this)
        recyclerViewCategory!!.adapter = recyclerView_Adapter_TasksCategory
        }

    //INICIO - funcion que abre modal para escoger una tarea
    private fun btn_agregarCat() {
        //INICO - boton que abre modal_agregar_cat_tickets.xml
        binding.btnAddCategory.setOnClickListener {

            binding.LayoutBackgroudAgregarTarea.isVisible = true //fondo gris casi transparente
            //binding.includeModalPlantillaAddcat.modalPlantillaAgregarCategoria.isVisible = true //modal
            binding.includeModalPlantillaAgregarCategoria.modalPlantillaAgregarTarea.isVisible = true
        }

        //INICIO - boton que cierra modal_agregar_cat_tickets.xml
        binding.includeModalPlantillaAddcat.btnSalirModalAddcat.setOnClickListener {
            binding.includeModalPlantillaAgregarCategoria.modalPlantillaAgregarTarea.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
        }
    }

    //INICIO - función que añade la tarea y te devuelve al menu principal
    private fun btnAddTasks() {
        binding.btnAddTasks.setOnClickListener {
            Toast.makeText(this, "tarea añadido", Toast.LENGTH_LONG).show()
            val bundle = intent.extras
            var idTechnician = bundle!!.getString("IdTechnician")
            val flagOnEditClick = bundle!!.getString("flagOnEditClick")

            var ticketPrivate = binding.chkboxPadLock.tag.toString()
            val ticketId = binding.tvIdTicket.text.split(" ")[1].replace("#","")
            val tasksDescription = binding.edtTasksDescription.text
            val durationToSolveTasks = binding.btnTimeToSolveTask.text
            val category = binding.btnAddCategory.text


            if (flagOnEditClick == "true"){
                idTechnician = "0"
            }

            if (ticketPrivate == "PRIVADO"){
                ticketPrivate = "NO PRIVADO"
            }

            //fecha y hora actual
            val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale("es", "PE"))//obtenemos fecha actual
            val currentdate = sdf.format(Date())
            Log.i("mensaje",
                "id de ticket: $ticketId \n" +
                        "TIPO: TASKS\n"+
                        "PRIVADO: $ticketPrivate\n" +
                        "ID_USUARIO: ${MainActivity.idUserTechnician}\n" +
                        "USUARIO: ${MainActivity.userTechnician}\n"+
                        "NOMBRE: ${MainActivity.nameTechnician}\n"+
                        "APELLIDO: ${MainActivity.lastNameTechnician}\n"+
                        "FECHA: $currentdate\n"+
                        "FECHA_CREACION: $currentdate\n"+
                        "FECHA_MODIFICACION: $currentdate\n"+
                        "CONTENIDO: $tasksDescription\n"+
                        "FECHA_INICIO: null\n"+
                        "FECHA_FIN: null\n"+
                        "DURACION: $durationToSolveTasks\n"+
                        "ESTADO: terminado/pendiente\n"+
                        "CATEGORIA: $category\n"+
                        "EDITOR: $idTechnician\n"+ //aca debe ir el id del que edita el ticket
                        "TECNICO: $idTechnician\n")
            onBackPressed()
            /*val intent_agregarTarea = Intent(this, MainActivity::class.java)
            intent_agregarTarea.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_agregarTarea)*/
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
            //binding.includeModalPlantillaAddcat.modalPlantillaAgregarCategoria.isVisible = false
            binding.includeModalPlantillaAgregarCategoria.modalPlantillaAgregarTarea.isVisible = false
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


    override fun onTasksTemplateClick(
        nameTasksTemplate: String,
        contentTasksTemplate: String,
        categoryTasksTemplates: String,
        timeTasksTemplates: String
    ) {
        binding.includeModalPlantillaTarea.modalPlantillaAgregarTarea.isVisible = false
        binding.LayoutBackgroudAgregarTarea.isVisible = false
        binding.fabPlantilla.isVisible = false
        binding.fabFoto.isVisible = false
        binding.fabArchivo.isVisible = false
        click = false
        binding.LayoutFabAgregarTarea.isVisible = true
        binding.edtTasksDescription.setText(decodeHtml(contentTasksTemplate))
        binding.edtTasksDescription.setTextColor(Color.parseColor("#1D20DD"))
        binding.btnAddCategory.text = categoryTasksTemplates
        binding.btnTimeToSolveTask.text = timeTasksTemplates
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

    override fun onTasksCategoryClick(nameTasksCategory: String, contentTasksCategory: String) {
        binding.includeModalPlantillaAgregarCategoria.modalPlantillaAgregarTarea.isVisible = false
        binding.LayoutBackgroudAgregarTarea.isVisible = false
        binding.fabPlantilla.isVisible = false
        binding.fabFoto.isVisible = false
        binding.fabArchivo.isVisible = false
        click = false
        binding.LayoutFabAgregarTarea.isVisible = true

        Toast.makeText(this, "$nameTasksCategory", Toast.LENGTH_SHORT).show()

        binding.btnAddCategory.text = decodeHtml(contentTasksCategory)
    }

}