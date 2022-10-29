package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.decodeHtml
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TasksCategory
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TasksTemplate
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsAgregarTareaBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.*
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class TicketsAgregarTareaActivity : AppCompatActivity(),
    RecycleView_Adapter_TasksTemplate.onTasksTemplateClickListener,
    RecycleView_Adapter_TasksCategory.onTasksCategoryClickListener,
    RecycleView_Adapter_ListTechnician.onListTechnicianClickListener{

    lateinit var binding: ActivityTicketsAgregarTareaBinding //declaramos binding para acceder variables
    //creamos el objeto de la clase recyclerView
    private var recyclerView: RecyclerView? = null
    private var recyclerViewCategory: RecyclerView? = null
    private var recyclerViewListTechnician: RecyclerView? = null
    /*creamos la lista de arreglos que tendrá los objetos de la clase Data_Tickets
   esta lista de arreglos (dataModelArrayList) funcionará como fuente de datos*/
    internal lateinit var dataModelArrayListTasksTemplate: ArrayList<Data_TasksTemplate>
    internal lateinit var dataModelArrayListTasksCategory: ArrayList<Data_TasksCategory>
    internal lateinit var dataModelArrayListTasksTechnician: ArrayList<Data_ListTechnician>
    private var recyclerView_Adapter_TasksTemplate: RecycleView_Adapter_TasksTemplate? = null
    private var recyclerView_Adapter_TasksCategory: RecycleView_Adapter_TasksCategory? = null
    private var recyclerView_Adapter_ListTechnician: RecycleView_Adapter_ListTechnician? = null

    //para obetener id despues de cargar
    /*companion object{
        lateinit var idTemplate: String
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsAgregarTareaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.includeModalPlantillaTarea.recyclerTasksTemplate
        recyclerViewCategory = binding.includeModalPlantillaAgregarCategoria.recyclerTasksCategory
        recyclerViewListTechnician = binding.includeModalListTechnician.recyclerListTechnician

        volleyRequestDataTasksTemplate()
        volleyRequestDataTasksCategory()
        volleyRequestListTechnician()


        val bundle = intent.extras
        Log.i("mensaje flag","${bundle!!.getBoolean("flagOnEditClick")}")
        when(bundle!!.getBoolean("flagOnEditClick")){
            true -> updateTask()
            false -> insertTask()
        }
        btn_fabs()
        btn_atras()
        btn_agregarCat()
        btnAssignTechnician()
        btnTimeToSolveTask()
        btnDocuments()
        imgBtnPadLockTask()
    }

    private fun volleyRequestListTechnician() {
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_ListTechnician, Response.Listener { response ->
                try {
                    dataModelArrayListTasksTechnician = ArrayList()

                    val jsonObjectResponse = JSONArray(response)
                    for (i in  0 until jsonObjectResponse.length()){
                        val listTechnicianJson = jsonObjectResponse.getJSONObject(i)
                        val dataListTechnician = Data_ListTechnician()
                        dataListTechnician.listTechnicianId = listTechnicianJson.getString("ID")
                        dataListTechnician.listTechnicianLastName = listTechnicianJson.getString("APELLIDO")
                        dataListTechnician.listTechnicianName = listTechnicianJson.getString("NOMBRE")

                        dataModelArrayListTasksTechnician.add(dataListTechnician)
                    }
                    setupRecyclerListTechnician()
                } catch (e: Exception) {
                    e.printStackTrace()
                    //Toast.makeText(this, "token expirado: $e", Toast.LENGTH_LONG).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "ERROR CON EL SERVIDOR _", Toast.LENGTH_SHORT).show()
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
            val hourToMinutes = (getHour.toInt()*60)
            val minutesToSeconds = (hourToMinutes+getMinutes.toInt())*60
            Toast.makeText(this, "$minutesToSeconds para resolver tarea", Toast.LENGTH_SHORT).show()
            binding.btnTimeToSolveTask.text = "$getHour : $getMinutes minutos"
            binding.btnTimeToSolveTask.tag = minutesToSeconds
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

    private fun imgBtnPadLockTask(){
        val bundle = intent.extras
        var ticketPrivate = bundle!!.getString("ticketInfoPrivate")
        var newTicketPrivate = "NO"
        var flagImgViewPadLock = false
        Log.i("mensaje type",ticketPrivate.toString())
        if (ticketPrivate == "SI"){
            //Toast.makeText(this, "Segumiento Privado", Toast.LENGTH_SHORT).show()
            flagImgViewPadLock = false
            binding.imgBtnPadLockTask.setOnClickListener {
                if(flagImgViewPadLock){
                    binding.imgBtnPadLockTask.setImageResource(R.drawable.ic_candado_cerrado)

                    binding.imgBtnPadLockTask.tag = "1"
                    flagImgViewPadLock = false
                }else{
                    binding.imgBtnPadLockTask.setImageResource(R.drawable.ic_candado_abierto)
                    binding.imgBtnPadLockTask.tag = "0"
                    flagImgViewPadLock = true
                }
            }
        }else{
            flagImgViewPadLock = true

            binding.imgBtnPadLockTask.setOnClickListener {
                if(flagImgViewPadLock){
                    binding.imgBtnPadLockTask.setImageResource(R.drawable.ic_candado_cerrado)
                    Toast.makeText(this, "Segumiento Privado", Toast.LENGTH_SHORT).show()
                    binding.imgBtnPadLockTask.tag = "1"
                    newTicketPrivate = "NO"
                    flagImgViewPadLock = false
                }else{
                    binding.imgBtnPadLockTask.setImageResource(R.drawable.ic_candado_abierto)
                    binding.imgBtnPadLockTask.tag = "0"
                    newTicketPrivate = "SI"
                    flagImgViewPadLock = true
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

    private fun setupRecyclerListTechnician() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true,)
        layoutManager.stackFromEnd = true

        recyclerViewListTechnician!!.layoutManager = layoutManager
        recyclerView_Adapter_ListTechnician =
            RecycleView_Adapter_ListTechnician(this,dataModelArrayListTasksTechnician,this)
        recyclerViewListTechnician!!.adapter = recyclerView_Adapter_ListTechnician
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

    private fun insertTask(){
        val flagInsertTask = false
        val bundle = intent.extras
        val ticketSortsId = bundle!!.getString("ticketSortsId").toString()
        val ticketType = bundle!!.getString("ticketSortsType") //solicitud o incidente
        var ticketSortsStatus = bundle!!.getString("ticketSortsStatus").toString() //en curso(asignado), en espera ...
        val ticketSortsIdTechnician = bundle!!.getString("ticketSortsIdTechnician").toString()
        //val ticketInfoId = bundle!!.getString("ticketInfoId").toString()
        val ticketInfoId = "tarea será agregada"

        binding.tvIdTicket.text = "Petición $ticketSortsId"

        if(ticketType == "SOLICITUD"){
            binding.imgBtnTypeTasks.setImageResource(R.drawable.ic_interrogacion)
        }else{
            binding.imgBtnTypeTasks.setImageResource(R.drawable.ic_incidencia)
        }

        Log.i("mensaje status","${ticketSortsStatus}")
        if(ticketSortsStatus == "EN CURSO (Asignada)"){
            binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo_verde)
            binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_verde)
            binding.imgBtnStatusTasks.tag = "2"
            ticketSortsStatus = "2"
        }else if(ticketSortsStatus == "EN ESPERA"){
            binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo)
            binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo)
            binding.imgBtnStatusTasks.tag = "4"
            ticketSortsStatus = "4"
        }else if (ticketSortsStatus == "CERRADO"){
            binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo_negro)
            //binding.imgBtnStatusTasks.tag = 2
            binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_negro)
        }

        binding.btnTimeToSolveTask.text = "30 minutos"
        binding.btnTimeToSolveTask.tag = "1800"

        binding.chkBoxPadLockTask.tag = "0" // estado de la tarea: hecho, por hacer, informativo
        binding.imgBtnPadLockTask.tag = "0" // tarea privado = 1, público 0
        binding.edtTasksDescription.tag = "0"
        binding.btnAddCategory.tag = false
        //Log.i("mensaje categoryId","${binding.btnAddCategory.tag}")

        Log.i("mensaje flag insert","${flagInsertTask}")
        imgBtnTicketStatus(ticketSortsStatus)
        btnAddTasks(flagInsertTask, ticketSortsId,ticketInfoId,ticketSortsIdTechnician,ticketSortsStatus)

    }

    private fun updateTask(){
        val flagUpdateTask = true
        Toast.makeText(this, "updateTask", Toast.LENGTH_SHORT).show()
        val bundle = intent.extras
        val ticketSortsId = bundle!!.getString("ticketSortsId").toString()
        val ticketInfoId = bundle!!.getString("ticketInfoId").toString()
        val ticketInfoIdTechnician = bundle!!.getString("ticketInfoIdTechnician").toString()
        //val ticketType = bundle!!.getString("ticketType")

        val ticketSortsType = bundle!!.getString("ticketSortsType")//solicitud o incidente
        val ticketSortsStatus = bundle!!.getString("ticketSortsStatus").toString()
        val ticketInfoCategory = bundle!!.getString("ticketInfoCategory")
        val ticketInfoIdCategory = bundle!!.getString("ticketInfoIdCategory")
        val ticketInfoStatus = bundle!!.getString("ticketInfoStatus")
        val ticketInfoTimeToSolve = bundle!!.getString("ticketInfoTimeToSolve")

        val ticketInfoPrivate = bundle!!.getString("ticketInfoPrivate")
        val ticketInfoContent = bundle!!.getString("ticketInfoContent")

        binding.tvIdTicket.text = "Petición #$ticketSortsId"
        binding.edtTasksDescription.setText(ticketInfoContent)
        binding.edtTasksDescription.tag = "0"
        binding.btnAddCategory.text = ticketInfoCategory
        binding.btnAddCategory.tag = ticketInfoIdCategory
        binding.btnTimeToSolveTask.text = "$ticketInfoTimeToSolve minutos"
        val minutesToSeconds = (ticketInfoTimeToSolve?.toInt()?.times(60))
        binding.btnTimeToSolveTask.tag = minutesToSeconds.toString()
        when(ticketSortsType){
            "SOLICITUD" -> binding.imgBtnTypeTasks.setImageResource(R.drawable.ic_interrogacion)
            "INCIDENCIA" -> binding.imgBtnTypeTasks.setImageResource(R.drawable.ic_incidencia)
        }

        when(ticketInfoStatus){
            "TERMINADO" -> {
                binding.chkBoxPadLockTask.isChecked = true
                binding.chkBoxPadLockTask.tag = "1"
            }
            "PENDIENTE" -> {
                binding.chkBoxPadLockTask.isChecked = false
                binding.chkBoxPadLockTask.tag = "0"
            }
            "INFORMACION" -> {
                binding.chkBoxPadLockTask.isChecked = false
                binding.chkBoxPadLockTask.tag = "2"
            }
        }

        binding.chkBoxPadLockTask.setOnClickListener {
            when(binding.chkBoxPadLockTask.isChecked){
                true -> binding.chkBoxPadLockTask.tag = "1"
                false -> binding.chkBoxPadLockTask.tag = "0"
            }
        }

        when(ticketSortsStatus){
            "EN CURSO (Asignada)" -> {
                binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo_verde)
                binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_verde)
                binding.imgBtnStatusTasks.tag = "2"
            }
            "EN CURSO (Planificación)" -> {binding.imgBtnStatusTasks.tag = "3"}
            "EN ESPERA" -> {
                binding.imgBtnStatusTasks.setImageResource(R.drawable.ic_circulo)
                binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo)
                binding.imgBtnStatusTasks.tag = "4"
            }
            "SOLUCIONADO" -> {binding.imgBtnStatusTasks.tag = "5"}
        }

        when(ticketInfoPrivate){
            "SI" -> {
                binding.imgBtnPadLockTask.setImageResource(R.drawable.ic_candado_cerrado)
                binding.imgBtnPadLockTask.tag = "1"
            }
            "NO" -> {
                binding.imgBtnPadLockTask.setImageResource(R.drawable.ic_candado_abierto)
                binding.imgBtnPadLockTask.tag = "0"
            }
        }
        Log.i("mensaje flag update","${flagUpdateTask}")
        btnAddTasks(flagUpdateTask, ticketSortsId,ticketInfoId,ticketInfoIdTechnician,ticketSortsStatus)
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

    private fun btnAssignTechnician() {
        //INICO - boton que abre modal_agregar_cat_tickets.xml
        binding.btnAddTechnician.setOnClickListener {
            binding.LayoutBackgroudAgregarTarea.isVisible = true //fondo gris casi transparente
            //binding.includeModalPlantillaAddcat.modalPlantillaAgregarCategoria.isVisible = true //modal
            binding.includeModalListTechnician.modalListTechnician.isVisible = true
        }

        //INICIO - boton que cierra modal_agregar_cat_tickets.xml
        binding.includeModalListTechnician.btnCloseModalListTechnician.setOnClickListener {
            binding.includeModalPlantillaAgregarCategoria.modalPlantillaAgregarTarea.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
        }
    }

    //INICIO - función que añade la tarea y te devuelve al menu principal
    private fun btnAddTasks(flagUpdateTask: Boolean,ticketSortsId: String, ticketInfoId: String, ticketSortsIdTechnician: String, ticketSortsStatus: String) {
        Log.i("mensaje flag taskOut","${flagUpdateTask}")
        binding.btnAddTasks.setOnClickListener {
            Toast.makeText(this, "Tarea añadida", Toast.LENGTH_LONG).show()


            val taskState = binding.chkBoxPadLockTask.tag.toString()
            val taskPrivate = binding.imgBtnPadLockTask.tag.toString()
            val ticketStatus = binding.imgBtnStatusTasks.tag.toString()

            val taskType = "1" // 1 no planificada, 2 planificada

            val categoryId = binding.btnAddCategory.tag.toString()

            val templateId = binding.edtTasksDescription.tag.toString() // id de plantillas
            val taskDescription = binding.edtTasksDescription.text.toString()

            val dateEnd = ""
            val dateBegin = ""

            Log.i("mensaje flag task","${flagUpdateTask}")
            when(flagUpdateTask){
                true -> {
                    requestVolleyUpdateTask(
                        ticketInfoId,
                        taskDescription,
                        taskPrivate,
                        ticketSortsIdTechnician,
                        taskState,
                        templateId,
                        categoryId,
                        dateEnd,
                        dateBegin,
                        ticketStatus,
                        ticketSortsId)

                    onBackPressed()
                }
                false -> {
                    val technicianId = binding.btnAddTechnician.tag.toString()
                    val actionTime = binding.btnTimeToSolveTask.tag.toString()
                    val ticketSortsStatusTag = binding.imgBtnStatusTasks.tag.toString()
                    requestVolleyInsertTask(
                        taskType,
                        taskDescription,
                        taskPrivate,
                        technicianId,
                        taskState,
                        templateId,
                        categoryId,
                        actionTime,
                        ticketSortsStatusTag,
                        ticketSortsId
                    )

                    onBackPressed()
                }
            }


            /*when(categoryId == null){
                true -> Toast.makeText(this, "Agregar Categoria", Toast.LENGTH_SHORT).show()
                else -> {
                    Log.i("mensaje flag task","${flagUpdateTask}")
                    when(flagUpdateTask){
                        true -> {
                            requestVolleyUpdateTask(
                                ticketInfoId,
                                taskDescription,
                                taskPrivate,
                                ticketSortsIdTechnician,
                                taskState,
                                templateId,
                                categoryId,
                                dateEnd,
                                dateBegin,
                                ticketStatus,
                                ticketSortsId)

                            onBackPressed()
                        }
                        false -> {
                            requestVolleyInsertTask(
                                taskType,
                                taskDescription,
                                taskPrivate,
                                technicianId,
                                taskState,
                                templateId,
                                categoryId,
                                actionTime,
                                ticketSortsStatusTag,
                                ticketSortsId
                            )

                            onBackPressed()
                        }
                    }
                }
            }*/





        }
    }

    private fun requestVolleyUpdateTask(
        ticketInfoId: String,
        taskDescription: String,
        taskPrivate: String,
        taskIdTech: String,
        taskState: String,
        templateId: String,
        categoryId: String,
        dateEnd: String,
        dateBegin: String,
        ticketStatus: String,
        ticketSortsId: String
    ) {
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_UpdateTasks+ticketInfoId, Response.Listener { response ->
                try {
                    val dataAddFollowup = JSONObject(response) //obtenemos el objeto json

                    Log.i("mensaje","$dataAddFollowup")

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "token expirado_: $e", Toast.LENGTH_LONG).show()
                    //Log.i("mensaje recycler e: ", "recycler ERROR: $e")
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                params["content"] = taskDescription
                params["private"] = taskPrivate
                params["user_tech"] = taskIdTech
                params["task_state"] = taskState
                params["template_id"] = templateId
                params["categories_id"] = categoryId
                params["date_end"] = dateEnd
                params["date_begin"] = dateBegin
                params["ticket_state"] = ticketStatus
                params["ticket_id"] = ticketSortsId
                Log.i("mensaje params","$params")
                return params
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequestDataTickets)
        //FIN obtenemos perfil de usuario
    }

    private fun requestVolleyInsertTask(
        taskType: String,
        taskDescription: String,
        taskPrivate: String,
        ticketSortsIdTechnician: String,
        taskState: String,
        templateId: String,
        categoryId: String,
        actionTime: String,
        ticketSortsStatus: String,
        ticketSortsId: String
    ) {
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_InsertTasks+ticketSortsId, Response.Listener { response ->
                try {
                    val dataAddFollowup = JSONObject(response) //obtenemos el objeto json

                    Log.i("mensaje","$dataAddFollowup")

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "token expirado_: $e", Toast.LENGTH_LONG).show()
                    //Log.i("mensaje recycler e: ", "recycler ERROR: $e")
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                params["type"] = taskType
                params["content"] = taskDescription
                params["private"] = taskPrivate
                params["user_tech"] = ticketSortsIdTechnician
                params["task_state"] = taskState
                params["templates_id"] = templateId
                params["categories_id"] = categoryId
                params["action_time"] = actionTime
                params["ticket_state"] = ticketSortsStatus
                Log.i("mensaje params","$params")
                return params
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequestDataTickets)
        //FIN obtenemos perfil de usuario
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
            binding.includeModalListTechnician.modalListTechnician.isVisible = false
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
        binding.edtTasksDescription.tag = idTasksTemplates
        binding.btnAddCategory.text = categoryTasksTemplates
        binding.btnTimeToSolveTask.text = timeTasksTemplates.split(".")[0] +" minutos"
        //binding.idTemplate.text = idTasksTemplates
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

        //Toast.makeText(this, "$nameTasksCategory, Id: $idTasksCategories", Toast.LENGTH_SHORT).show()

        binding.btnAddCategory.text = decodeHtml(contentTasksCategory)
        binding.btnAddCategory.tag = idTasksCategories
        //Toast.makeText(this, "${binding.idCategory.text} con nombre ${binding.btnAddCategory.text}", Toast.LENGTH_SHORT).show()
    }

    override fun onSelectTechnicianClick(
        listStatusAllowedId: String,
        listStatusAllowedName: String,
        listTechnicianLastName: String
    ) {
        binding.btnAddTechnician.text = "$listStatusAllowedName $listTechnicianLastName"
        binding.btnAddTechnician.tag = "$listStatusAllowedId"

        binding.includeModalListTechnician.modalListTechnician.isVisible = false
        binding.LayoutBackgroudAgregarTarea.isVisible = false
        binding.fabPlantilla.isVisible = false
        binding.fabFoto.isVisible = false
        binding.fabArchivo.isVisible = false
        click = false
        binding.LayoutFabAgregarTarea.isVisible = true
    }

}