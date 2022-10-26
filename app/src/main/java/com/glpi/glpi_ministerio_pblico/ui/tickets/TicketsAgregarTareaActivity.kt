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

    //para obetener id despues de cargar
    /*companion object{
        lateinit var idTemplate: String
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsAgregarTareaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var flagTasks = flagEdit
        recyclerView = binding.includeModalPlantillaTarea.recyclerTasksTemplate
        recyclerViewCategory = binding.includeModalPlantillaAgregarCategoria.recyclerTasksCategory
        //recyclerView = binding.recyclerTaskTemplate

        volleyRequestDataTasksTemplate()
        volleyRequestDataTasksCategory()
        //ticketInfo()


        if (flagTasks){
            val intent = intent.extras
            val ticketSortsId = intent!!.getString("ticketSortsId")
            val ticketSortsStatus = intent!!.getString("ticketSortsStatus")
            val ticketInfoContent = intent!!.getString("ticketInfoContent")
            val ticketInfoCategory = intent!!.getString("ticketInfoCategory")
            val ticketInfoTimeToSolve = intent!!.getString("ticketInfoTimeToSolve")
            val ticketInfoPrivate = intent!!.getString("ticketInfoPrivate")
            val ticketInfoStatus = intent!!.getString("ticketInfoStatus")
            //val ticketInfoIdTechnician = intent!!.getString("ticketInfoIdTechnician")
            binding.tvIdTicket.text = "Petición #$ticketSortsId"
            binding.edtTasksDescription.setText(ticketInfoContent)
            binding.btnAddCategory.text = ticketInfoCategory
            binding.btnTimeToSolveTask.text = "$ticketInfoTimeToSolve minutos"

            when(ticketSortsStatus){
                "EN CURSO (Asignada)" -> binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo_verde)
                "EN ESPERA" -> binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo)
            }

            when(ticketInfoPrivate){
                "SI" -> binding.imgBtnPadLockTask.setImageResource(R.drawable.ic_candado_cerrado)
                "NO" -> binding.imgBtnPadLockTask.setImageResource(R.drawable.ic_candado_abierto)
            }

            when(ticketInfoStatus){
                "PENDIENTE" -> binding.chkboxPadLock.isChecked = true
                "TERMINADO" -> binding.chkboxPadLock.isChecked = false
            }

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
        val arrayHour = arrayListOf<String>("00","01","02","03","04","05","06","07","08","09","10","11","12")
        val arrayMinutes = arrayListOf<String>("05","10","15","20","25","30","35","40","45","50","55")
        var iHour = 0
        var iMinutes = 0
        binding.btnTimeToSolveTask.setOnClickListener {
            binding.LayoutBackgroudAgregarTarea.isVisible = true
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = true
            val timer = 5
            binding.btnTimeToSolveTask.text = "$timer minutos"

            binding.includeTimeToSolveTask.hours.text = arrayHour[0]

            binding.includeTimeToSolveTask.imgBtnUpHour.setOnClickListener {
                if (iHour < arrayHour.size-1){
                    iHour++
                    binding.includeTimeToSolveTask.hours.text = arrayHour[iHour]
                }
            }
            binding.includeTimeToSolveTask.imgBtnDownHour.setOnClickListener {
                if (iHour>0){
                    iHour--
                    binding.includeTimeToSolveTask.hours.text = arrayHour[iHour]
                }
            }


            binding.includeTimeToSolveTask.imgBtnUpMinutes.setOnClickListener {
                if (iMinutes < arrayMinutes.size-1){
                    iMinutes++
                    binding.includeTimeToSolveTask.minutes.text = arrayMinutes[iMinutes]
                }
            }
            binding.includeTimeToSolveTask.imgBtnDownMinutes.setOnClickListener {
                if (iMinutes>0){
                    iMinutes--
                    binding.includeTimeToSolveTask.minutes.text = arrayMinutes[iMinutes]
                }
            }
        }

        var getHour = ""
        var getMinutes = ""
        binding.includeTimeToSolveTask.btnOkUnderModalTimeToSolveTask.setOnClickListener {
            getHour = binding.includeTimeToSolveTask.hours.text.toString()
            getMinutes = binding.includeTimeToSolveTask.minutes.text.toString()
            Toast.makeText(this, "$getHour : $getMinutes para resolver tarea", Toast.LENGTH_SHORT).show()
            binding.btnTimeToSolveTask.text = "$getHour : $getMinutes minutos"
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
            binding.includeTimeToSolveTask.hours.text = arrayHour[0]
            binding.includeTimeToSolveTask.minutes.text = arrayMinutes[0]
        }

        binding.includeTimeToSolveTask.btnCloseUnderModalTimeToSolveTask.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
        }
        binding.includeTimeToSolveTask.btnCloseModalTimeToSolveTask.setOnClickListener {
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
        }
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
                    binding.imgBtnStatusTasks.tag = 0
                    newTicketStatus = "EN ESPERA"
                    flagTicketStatus = false
                }else{
                    binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo_verde)
                    binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_verde)
                    binding.imgBtnStatusTasks.tag = 1
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
                    binding.imgBtnStatusTasks.tag = 0
                    newTicketStatus = "EN ESPERA"
                    flagTicketStatus = false
                }else{
                    binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo_verde)
                    binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_verde)
                    binding.imgBtnStatusTasks.tag = 1
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
                        player.setIdTasksTemplates(nTemplate.getString("ID"))
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
                        player.setIdTasksCategories(nTemplate.getString("ID"))
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

    private fun ticketInfo(){
        val bundle = intent.extras
        val ticketSortsId = bundle!!.getString("ticketSortsId")
        val ticketType = bundle!!.getString("ticketType") //solicitud o incidente
        val ticketStatus = bundle!!.getString("ticketStatus")//en curso ,cerrado ...
        val getTaskUsersEstimateDuration = bundle!!.getString("getTaskUsersEstimateDuration")
        val tasksStatus = bundle!!.getString("tasksStatus")
        val tasksCategory = bundle!!.getString("tasksCategory")
        binding.tvIdTicket.text = "Petición $ticketSortsId"

        val idTemplate = binding.idTemplate.text.toString()

        if (tasksCategory != null){
            binding.btnAddCategory.text = tasksCategory
        }

        if (getTaskUsersEstimateDuration != null){
            binding.btnTimeToSolveTask.text = getTaskUsersEstimateDuration
        }


        if(ticketType == "SOLICITUD"){
            binding.imgBtnTypeTasks.setImageResource(R.drawable.ic_interrogacion)
        }else{
            binding.imgBtnTypeTasks.setImageResource(R.drawable.ic_incidencia)
        }

        if (tasksStatus == "TERMINADO"){
            binding.chkboxPadLock.isChecked = true

        }

        Log.i("mensaje","$ticketStatus")
        if(ticketStatus == "EN CURSO (Asignada)"){
            binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo_verde)
            binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_verde)
            binding.imgBtnStatusTasks.tag = 1
        }else if(ticketStatus == "EN ESPERA"){
            Toast.makeText(this, "$ticketStatus", Toast.LENGTH_SHORT).show()
            binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo)
            binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo)
            binding.imgBtnStatusTasks.tag = 0
        }else if (ticketStatus == "CERRADO"){
            binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo_negro)
            binding.imgBtnStatusTasks.tag = 2
            binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_negro)
        }

        imgBtnTicketStatus(ticketStatus)
        btnAddTasks()

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
            val ticketInfoContent = binding.edtTasksDescription.text
            var ticketInfoPrivate = binding.chkboxPadLock.tag.toString()

            val bundle = intent.extras
            val flagOnEditClick = bundle!!.getString("flagOnEditClick")
            var ticketInfoIdTechnician = bundle!!.getString("ticketInfoIdTechnician")

            val getTaskUsersEstimateDuration = bundle!!.getString("getTaskUsersEstimateDuration")
            var ticketDate = bundle!!.getString("ticketDate")

            //var ticketPrivate = binding.chkboxPadLock.tag.toString()
            val ticketId = binding.tvIdTicket.text.split(" ")[1].replace("#","")
            val tasksDescription = binding.edtTasksDescription.text
            val durationToSolveTasks = binding.btnTimeToSolveTask.text
            val category = binding.btnAddCategory.text
            var ticketStatus = binding.imgBtnStatusTasks.tag.toString()
            val idTemplates = binding.idTemplate.text
            val idCategory = binding.idCategory.text

            if (ticketStatus == "0"){
                ticketStatus = "EN ESPERA"
            }else{
                ticketStatus = "EN CURSO (Asignada)"
            }

            var ticketTaSksStatus: String = if (binding.chkboxPadLock.isChecked){
                "1"
            }else{
                "0"
            }
            //fecha y hora actual
            val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale("es", "PE"))//obtenemos fecha actual
            val currentdate = sdf.format(Date())

            if (flagOnEditClick == "true"){
                ticketInfoIdTechnician = "0"
            }else{
                ticketDate = currentdate
            }

            ticketInfoPrivate = if (ticketInfoPrivate == "PRIVADO"){
                "1"
            }else{
                "0"
            }

            Log.i("mensaje addTask",
                "content: $ticketInfoContent\n"+
                "private: $ticketInfoPrivate\n" +
                "user_tech: $ticketInfoIdTechnician\n"+
                "task_state: $ticketTaSksStatus\n"+
                "templates_id: $idTemplates\n"+
                "categories_id: $idCategory\n"+
                "date_end: $currentdate\n"+
                "date_begin: $ticketDate\n"+
                "ticket_state: $ticketStatus\n"+ //incidente/solicitud
                "id de ticket: $ticketId")


            /*Log.i("mensaje",
                "id de ticket: $ticketId \n" +
                        "TIPO: TASKS\n"+
                        "PRIVADO: $ticketPrivate\n" +
                        "ID_USUARIO: ${MainActivity.idUserTechnician}\n" +
                        "USUARIO: ${MainActivity.userTechnician}\n"+
                        "NOMBRE: ${MainActivity.nameTechnician}\n"+
                        "APELLIDO: ${MainActivity.lastNameTechnician}\n"+
                        "FECHA: $ticketDate\n"+
                        "FECHA_CREACION: $ticketDate\n"+
                        "FECHA_MODIFICACION: $currentdate\n"+
                        "CONTENIDO: $tasksDescription\n"+
                        "FECHA_INICIO: null\n"+
                        "FECHA_FIN: null\n"+
                        "DURACION: $durationToSolveTasks\n"+
                        "ESTADO: $ticketTaSksStatus\n"+
                        "CATEGORIA: $category\n"+
                        "EDITOR: $idTechnician\n"+ //aca debe ir el id del que edita el ticket
                        "TECNICO: $idTechnician\n")*/
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
        timeTasksTemplates: String,
        idTasksTemplates: String
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
        binding.btnTimeToSolveTask.text = timeTasksTemplates.split(".")[0] +" minutos"
        binding.idTemplate.text = idTasksTemplates
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

    override fun onTasksCategoryClick(nameTasksCategory: String, contentTasksCategory: String,idTasksCategories: String) {
        binding.includeModalPlantillaAgregarCategoria.modalPlantillaAgregarTarea.isVisible = false
        binding.LayoutBackgroudAgregarTarea.isVisible = false
        binding.fabPlantilla.isVisible = false
        binding.fabFoto.isVisible = false
        binding.fabArchivo.isVisible = false
        click = false
        binding.LayoutFabAgregarTarea.isVisible = true

        Toast.makeText(this, "$nameTasksCategory", Toast.LENGTH_SHORT).show()

        binding.btnAddCategory.text = decodeHtml(contentTasksCategory)
        binding.idCategory.text = idTasksCategories
    }

}