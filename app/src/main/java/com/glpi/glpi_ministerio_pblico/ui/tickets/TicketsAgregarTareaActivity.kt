package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.decodeHtml
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TasksCategory
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TasksTemplate
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.data.database.TicketInfoDB
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsAgregarTareaBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.*
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import com.glpi.glpi_ministerio_pblico.utilities.Utils_Global
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class TicketsAgregarTareaActivity : AppCompatActivity(),
    RecycleView_Adapter_TasksTemplate.onTasksTemplateClickListener,
    RecycleView_Adapter_TasksCategory.onTasksCategoryClickListener,
    RecycleView_Adapter_ListTechnician.onListTechnicianClickListener,
    RecycleViw_Adapter_ListStatusAllowed.onStatusAllowedClickListener{

    lateinit var binding: ActivityTicketsAgregarTareaBinding //declaramos binding para acceder variables
    //creamos el objeto de la clase recyclerView
    private var recyclerView: RecyclerView? = null
    private var recyclerViewCategory: RecyclerView? = null
    private var recyclerViewListTechnician: RecyclerView? = null
    private var recyclerViewListStatusAllowed: RecyclerView? = null
    /*creamos la lista de arreglos que tendrá los objetos de la clase Data_Tickets
   esta lista de arreglos (dataModelArrayList) funcionará como fuente de datos*/
    internal lateinit var dataModelArrayListTasksTemplate: ArrayList<Data_TasksTemplate>
    internal lateinit var dataModelArrayListTasksCategory: ArrayList<Data_TasksCategory>
    internal lateinit var dataModelArrayListTasksTechnician: ArrayList<Data_ListTechnician>
    internal lateinit var dataModelArrayListStatusAllowed: ArrayList<Data_ListStatusAllowed>
    private var recyclerView_Adapter_TasksTemplate: RecycleView_Adapter_TasksTemplate? = null
    private var recyclerView_Adapter_TasksCategory: RecycleView_Adapter_TasksCategory? = null
    private var recyclerView_Adapter_ListTechnician: RecycleView_Adapter_ListTechnician? = null
    private var recyclerView_Adapter_ListStatusAllowed: RecycleViw_Adapter_ListStatusAllowed? = null

    private var onSelectTemplateTask = false

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
        recyclerViewListStatusAllowed = binding.includeListStatusAllowed.recyclerFollowupListAllowed

        volleyRequestListStatusAllowed()
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
        imgBtnTaskStatus()
        imgBtnTicketStatus()
    }

    private fun imgBtnTaskStatus(){
        val bundle = intent.extras

        Log.i("mensaje infoSta",bundle!!.getString("ticketInfoStatus").toString())
        when(bundle!!.getString("ticketInfoStatus").toString()){
            "TERMINADO" -> {
                binding.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_done)
                binding.imgBtnTaskStatus.tag = "2" //hecho
            }
            "INFORMACION" -> {
                binding.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_information)
                binding.imgBtnTaskStatus.tag = "0" //informativo
            }
            "PENDIENTE" -> {
                binding.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_to_do)
                binding.imgBtnTaskStatus.tag = "1" //por hacer
            }
        }


        binding.imgBtnTaskStatus.setOnClickListener {
            Log.i("mensaje infoSta",binding.imgBtnTaskStatus.tag.toString())
            when(binding.imgBtnTaskStatus.tag){
                "1" -> {
                    binding.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_done)
                    binding.imgBtnTaskStatus.tag = "2" //por hacer
                    //Toast.makeText(this, binding.imgBtnTaskStatus.tag.toString(), Toast.LENGTH_SHORT).show()
                }
                "0" -> {
                    binding.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_to_do)
                    binding.imgBtnTaskStatus.tag = "1" //hecho
                    //Toast.makeText(this, binding.imgBtnTaskStatus.tag.toString(), Toast.LENGTH_SHORT).show()
                }
                "2" -> {
                    binding.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_information)
                    binding.imgBtnTaskStatus.tag = "0" //informativo
                    //Toast.makeText(this, binding.imgBtnTaskStatus.tag.toString(), Toast.LENGTH_SHORT).show()
                }

            }
        }
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
                //Toast.makeText(this, "$flagBtnShowDocuments", Toast.LENGTH_SHORT).show()
                binding.includeDocumentsTaks.contentDocuments.isVisible = true
                flagBtnShowDocuments = true
            }else{
                binding.includeDocumentsTaks.contentDocuments.isVisible = false
                flagBtnShowDocuments = false
            }

        }
    }


    @SuppressLint("SetTextI18n")
    private fun btnTimeToSolveTask(){
        val bundle = intent.extras
        val ticketInfoTimeToSolve = bundle!!.getString("ticketInfoTimeToSolve")
        val flagOnEditClick = bundle!!.getBoolean("flagOnEditClick")
        val arrayHour = arrayListOf<String>("00","01","02","03","04","05","06","07","08","09","10","11","12")
        val arrayMinutes = arrayListOf<String>("00","05","10","15","20","25","30","35","40","45","50","55")
        var iHour = 0
        var iMinutes = 5

        val minutesToSolve = Utils_Global.convertMinutesToTimeFormat(ticketInfoTimeToSolve.toString()).split(":")[1]
        val hoursToSolve = Utils_Global.convertMinutesToTimeFormat(ticketInfoTimeToSolve.toString()).split(":")[0]
        Log.i("mensaje index",arrayHour.indexOf(hoursToSolve).toString())
        var positionIndex: Int
        var positionIndexHour = arrayHour.indexOf(hoursToSolve)
        when(arrayMinutes.indexOf(minutesToSolve)){
            -1 -> {
                positionIndex = 0
            }
            else -> {
                positionIndex = arrayMinutes.indexOf(minutesToSolve)
            }
        }

        when (flagOnEditClick){
            true -> {
                binding.btnTimeToSolveTask.text = Utils_Global.convertMinutesToTimeFormat(ticketInfoTimeToSolve.toString()) + "horas"


                binding.btnTimeToSolveTask.setOnClickListener {
                    binding.LayoutBackgroudAgregarTarea.isVisible = true
                    binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = true

                    binding.includeTimeToSolveTask.hours.text = arrayHour[positionIndexHour]
                    binding.includeTimeToSolveTask.minutes.text = arrayMinutes[positionIndex]

                    binding.includeTimeToSolveTask.imgBtnUpHour.setOnClickListener {
                        if (positionIndexHour < arrayHour.size-1){
                            positionIndexHour++
                            binding.includeTimeToSolveTask.hours.text = arrayHour[positionIndexHour]
                        }
                    }
                    binding.includeTimeToSolveTask.imgBtnDownHour.setOnClickListener {
                        if (positionIndexHour>0){
                            positionIndexHour--
                            binding.includeTimeToSolveTask.hours.text = arrayHour[positionIndexHour]
                        }
                    }


                    binding.includeTimeToSolveTask.imgBtnUpMinutes.setOnClickListener {
                        positionIndex++
                        if (positionIndex < arrayMinutes.size-1){
                            binding.includeTimeToSolveTask.minutes.text = arrayMinutes[positionIndex]
                        }else{
                            positionIndex = 0
                            binding.includeTimeToSolveTask.minutes.text = arrayMinutes[positionIndex]
                        }
                    }
                    binding.includeTimeToSolveTask.imgBtnDownMinutes.setOnClickListener {
                        positionIndex--
                        if (positionIndex>0){

                            binding.includeTimeToSolveTask.minutes.text = arrayMinutes[positionIndex]
                        }else{
                            positionIndex = arrayMinutes.size-1
                            binding.includeTimeToSolveTask.minutes.text = arrayMinutes[positionIndex]
                        }
                    }
                }
            }
            false -> {

                binding.btnTimeToSolveTask.setOnClickListener {
                    when(onSelectTemplateTask)
                    {
                        true -> {
                            binding.includeTimeToSolveTask.minutes.text = //arrayMinutes[iMinutes]
                                binding.btnTimeToSolveTask.text.split(" ")[0]
                            iMinutes = arrayMinutes.indexOf(binding.btnTimeToSolveTask.text.split(" ")[0])
                            onSelectTemplateTask = false
                        }
                        false -> {
                            binding.includeTimeToSolveTask.minutes.text = arrayMinutes[iMinutes]
                        }
                    }
                    //binding.includeTimeToSolveTask.minutes.text = arrayMinutes[iMinutes]

                    binding.LayoutBackgroudAgregarTarea.isVisible = true
                    binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = true


                    binding.includeTimeToSolveTask.hours.text = arrayHour[0]


                    binding.includeTimeToSolveTask.imgBtnUpHour.setOnClickListener {
                        if (positionIndexHour < arrayHour.size-1){
                            positionIndexHour++
                            binding.includeTimeToSolveTask.hours.text = arrayHour[positionIndexHour]
                        }
                    }
                    binding.includeTimeToSolveTask.imgBtnDownHour.setOnClickListener {
                        if (positionIndexHour>0){
                            positionIndexHour--
                            binding.includeTimeToSolveTask.hours.text = arrayHour[positionIndexHour]
                        }
                    }


                    binding.includeTimeToSolveTask.imgBtnUpMinutes.setOnClickListener {
                        iMinutes++
                        if (iMinutes < arrayMinutes.size-1){
                            binding.includeTimeToSolveTask.minutes.text = arrayMinutes[iMinutes]
                        }else{
                            iMinutes = 0
                            binding.includeTimeToSolveTask.minutes.text = arrayMinutes[iMinutes]
                        }
                    }
                    binding.includeTimeToSolveTask.imgBtnDownMinutes.setOnClickListener {
                        iMinutes--
                        if (iMinutes>0){

                            binding.includeTimeToSolveTask.minutes.text = arrayMinutes[iMinutes]
                        }else{
                            iMinutes = arrayMinutes.size-1
                            binding.includeTimeToSolveTask.minutes.text = arrayMinutes[iMinutes]
                        }
                    }
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
            Log.i("mensaje minutesToSec",minutesToSeconds.toString())
            //Toast.makeText(this, "$minutesToSeconds para resolver tarea", Toast.LENGTH_SHORT).show()

            when(getHour){
                "0" -> binding.btnTimeToSolveTask.text = "$getMinutes minutos"
                else -> binding.btnTimeToSolveTask.text = "$getHour : $getMinutes minutos"
            }

            binding.btnTimeToSolveTask.tag = minutesToSeconds
            binding.includeTimeToSolveTask.modalTimeToSolveTask.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
            binding.includeTimeToSolveTask.hours.text = arrayHour[0]
            //Log.i("mensaje","${binding.btnTimeToSolveTask.tag}")
            //binding.includeTimeToSolveTask.minutes.text = arrayMinutes[positionIndex]
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



    private fun imgBtnTicketStatus(){
        binding.btnStatusTask.setOnClickListener {
            binding.LayoutBackgroudAgregarTarea.isVisible = true
            binding.includeListStatusAllowed.modalListStatusAllowed.isVisible = true
        }
        /*val ticketStatus = binding.btnStatusTask.text
        if (ticketStatus == "EN CURSO (Asignada)"){
            flagTicketStatus = true

            binding.btnStatusTask.setOnClickListener {
                binding.LayoutBackgroudAgregarTarea.isVisible = true
                binding.includeListStatusAllowed.modalListStatusAllowed.isVisible = true
                if(flagTicketStatus){
                    binding.imgBtnTaskStatus.setImageResource(R.drawable.ic_circulo)
                    binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo)
                    binding.imgBtnTaskStatus.tag = 0
                    newTicketStatus = "EN ESPERA"
                    flagTicketStatus = false
                }else{
                    binding.imgBtnTaskStatus.setImageResource(R.drawable.ic_circulo_verde)
                    binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_verde)
                    binding.imgBtnTaskStatus.tag = 1
                    newTicketStatus = "EN CURSO (Asignada)"
                    flagTicketStatus = true
                }
            }
        }else{
            flagTicketStatus = false
            binding.imgBtnTaskStatus.setOnClickListener {
                if(flagTicketStatus){
                    binding.imgBtnTaskStatus.setImageResource(R.drawable.ic_circulo)
                    binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo)
                    binding.imgBtnTaskStatus.tag = 0
                    newTicketStatus = "EN ESPERA"
                    flagTicketStatus = false
                }else{
                    binding.imgBtnTaskStatus.setImageResource(R.drawable.ic_circulo_verde)
                    binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_verde)
                    binding.imgBtnTaskStatus.tag = 1
                    newTicketStatus = "EN CURSO (Asignada)"
                    flagTicketStatus = true
                }
            }
        }*/
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
                    //Toast.makeText(this, "Segumiento Privado", Toast.LENGTH_SHORT).show()
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
                        player.setIdCategoryTasksTemplates(nTemplate.getString("ID_CATEGORIA"))
                        player.setCategoryTasksTemplates(nTemplate.getString("CATEGORIA"))
                        player.setTimeTasksTemplates(nTemplate.getString("TIEMPO"))
                        player.setStatusTasksTemplates(nTemplate.getString("ESTADO"))
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
        var ticketSortsId = ""
        var ticketType = "" //solicitud o incidente
        var ticketSortsStatus = "" //en curso(asignado), en espera ...
        var ticketSortsIdTechnician = ""
        binding.btnAddTechnician.tag = ""
        binding.btnAddCategory.tag = ""

        val room =
            Room.databaseBuilder(this, TicketInfoDB::class.java, "ticketInfoBD").build()
        lifecycleScope.launch {
            val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
            for (item in getTicketInfoDB){
                ticketSortsId = item.ticketSortsID
                ticketType = item.ticketSortsType
                ticketSortsStatus = item.ticketSortsStatus
                ticketSortsIdTechnician = item.ticketSortsIdTechnician

                binding.tvIdTicket.text = "Petición $ticketSortsId"



                when(ticketType){
                    "SOLICITUD" -> binding.imgBtnTypeTasks.setImageResource(R.drawable.ic_interrogacion)
                    "INCIDENCIA" -> binding.imgBtnTypeTasks.setImageResource(R.drawable.ic_incidencia)
                }

                //-------------
                val imagen = ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_circulo_verde
                )
                val drawables: Array<Drawable> = binding.btnStatusTask.compoundDrawables
                binding.btnStatusTask.setCompoundDrawablesWithIntrinsicBounds(
                    drawables[0],
                    drawables[0],
                    imagen,
                    drawables[0]
                )
                binding.btnStatusTask.text = "EN CURSO (Asignada)"
                binding.btnStatusTask.tag = "2"
                //-------------

                //-------------
                binding.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_to_do)
                binding.imgBtnTaskStatus.tag = "1" // estado de la tarea: hecho, por hacer, informativo
                //-------------

                binding.btnTimeToSolveTask.text = "30 minutos"
                binding.btnTimeToSolveTask.tag = "1800"


                binding.imgBtnPadLockTask.tag = "0" // tarea privado = 1, público 0
                binding.edtTasksDescription.tag = "0"

                binding.imgBtnPadLockTask.setImageResource(R.drawable.ic_candado_abierto)
                binding.imgBtnPadLockTask.tag = "0"

                when(ticketSortsStatus){
                    "EN CURSO (Asignada)" -> {
                        val imagen = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_circulo_verde
                        )
                        val drawables: Array<Drawable> = binding.btnStatusTask.compoundDrawables
                        binding.btnStatusTask.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusTask.text = "EN CURSO (Asignada)"
                        binding.btnStatusTask.tag = "2"
                        binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_verde)
                    }
                    "2" -> {
                        val imagen = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_circulo_verde
                        )
                        val drawables: Array<Drawable> = binding.btnStatusTask.compoundDrawables
                        binding.btnStatusTask.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusTask.text = "EN CURSO (Asignada)"
                        binding.btnStatusTask.tag = "2"
                        binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_verde)
                    }
                    "EN ESPERA" -> {
                        val imagen =
                            ContextCompat.getDrawable(applicationContext, R.drawable.ic_circulo)
                        val drawables: Array<Drawable> = binding.btnStatusTask.compoundDrawables
                        binding.btnStatusTask.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusTask.text = "EN ESPERA"
                        binding.btnStatusTask.tag = "4"
                        binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo)
                    }
                    "4" -> {
                        val imagen =
                            ContextCompat.getDrawable(applicationContext, R.drawable.ic_circulo)
                        val drawables: Array<Drawable> = binding.btnStatusTask.compoundDrawables
                        binding.btnStatusTask.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusTask.text = "EN ESPERA"
                        binding.btnStatusTask.tag = "4"
                        binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo)
                    }
                    "CERRADO" -> {
                        val imagen = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_circulo_negro
                        )
                        val drawables: Array<Drawable> = binding.btnStatusTask.compoundDrawables
                        binding.btnStatusTask.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_negro)
                    }
                }

            }
        }

        val ticketInfoId = "tarea será agregada"


        //Log.i("mensaje flag insert","${flagInsertTask}")
        //imgBtnTicketStatus(ticketSortsStatus)
        btnAddTasks(flagInsertTask, ticketSortsId,ticketInfoId,ticketSortsIdTechnician,ticketSortsStatus)

    }

    private fun updateTask(){
        val flagUpdateTask = true

        val room =
            Room.databaseBuilder(this, TicketInfoDB::class.java, "ticketInfoBD").build()
        lifecycleScope.launch {
            val bundle = intent.extras
            val ticketInfoId = bundle!!.getString("ticketInfoId").toString()
            val ticketInfoIdTechnician = bundle!!.getString("ticketInfoIdTechnician").toString()
            val ticketInfoCategory = bundle!!.getString("ticketInfoCategory")
            val ticketInfoIdCategory = bundle!!.getString("ticketInfoIdCategory")
            val ticketInfoStatus = bundle!!.getString("ticketInfoStatus")
            val ticketInfoTimeToSolve = bundle!!.getString("ticketInfoTimeToSolve")
            val ticketInfoPrivate = bundle!!.getString("ticketInfoPrivate")
            val ticketInfoContent = bundle!!.getString("ticketInfoContent")

            val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
            for (item in getTicketInfoDB){
                val ticketSortsId = item.ticketSortsID
                val ticketSortsType = item.ticketSortsType
                val ticketSortsStatus = item.ticketSortsStatus


                binding.tvIdTicket.text = "Petición #$ticketSortsId"
                binding.edtTasksDescription.setText(ticketInfoContent)
                binding.edtTasksDescription.tag = "0"
                binding.btnAddCategory.text = ticketInfoCategory
                binding.btnAddCategory.tag = ticketInfoIdCategory

                //binding.btnTimeToSolveTask.text = "00: $ticketInfoTimeToSolve minutos"
                val splitTimeToSolve = Utils_Global.convertMinutesToTimeFormat(ticketInfoTimeToSolve.toString())
                binding.btnTimeToSolveTask.text = Utils_Global.convertMinutesToTimeFormat(ticketInfoTimeToSolve.toString()) + " horas"
                Log.i("mensaje horas",splitTimeToSolve.split(":")[0])
                binding.includeTimeToSolveTask.minutes.text = splitTimeToSolve.split(":")[1]
                binding.includeTimeToSolveTask.hours.text = splitTimeToSolve.split(":")[0]

                binding.btnAddTechnician.text = prefer.getNameTechnicianTask()
                binding.btnAddTechnician.tag = ticketInfoIdTechnician

                val minutesToSeconds = (ticketInfoTimeToSolve?.toInt()?.times(60))
                binding.btnTimeToSolveTask.tag = minutesToSeconds.toString()
                when(ticketSortsType){
                    "SOLICITUD" -> binding.imgBtnTypeTasks.setImageResource(R.drawable.ic_interrogacion)
                    "INCIDENCIA" -> binding.imgBtnTypeTasks.setImageResource(R.drawable.ic_incidencia)
                }

                when(ticketInfoStatus){
                    "TERMINADO" -> {
                        binding.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_done)
                        binding.imgBtnTaskStatus.tag = "2" //hecho
                    }
                    "PENDIENTE" -> {
                        binding.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_to_do)
                        binding.imgBtnTaskStatus.tag = "1" //informativo
                    }
                    "INFORMACION" -> {
                        binding.imgBtnTaskStatus.setImageResource(R.drawable.ic_task_information)
                        binding.imgBtnTaskStatus.tag = "0" //informativo
                    }
                }

                when(ticketSortsStatus){
                    "EN CURSO (Asignada)" -> {
                        val imagen = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_circulo_verde
                        )
                        val drawables: Array<Drawable> = binding.btnStatusTask.compoundDrawables
                        binding.btnStatusTask.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusTask.text = "EN CURSO (Asignada)"
                        binding.btnStatusTask.tag = "2"
                        binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_verde)
                    }
                    "2" -> {
                        val imagen = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_circulo_verde
                        )
                        val drawables: Array<Drawable> = binding.btnStatusTask.compoundDrawables
                        binding.btnStatusTask.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusTask.text = "EN CURSO (Asignada)"
                        binding.btnStatusTask.tag = "2"
                        binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_verde)
                    }
                    "EN ESPERA" -> {
                        val imagen =
                            ContextCompat.getDrawable(applicationContext, R.drawable.ic_circulo)
                        val drawables: Array<Drawable> = binding.btnStatusTask.compoundDrawables
                        binding.btnStatusTask.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusTask.text = "EN ESPERA"
                        binding.btnStatusTask.tag = "4"
                        binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo)
                    }
                    "4" -> {
                        val imagen =
                            ContextCompat.getDrawable(applicationContext, R.drawable.ic_circulo)
                        val drawables: Array<Drawable> = binding.btnStatusTask.compoundDrawables
                        binding.btnStatusTask.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusTask.text = "EN ESPERA"
                        binding.btnStatusTask.tag = "4"
                        binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo)
                    }
                    "CERRADO" -> {
                        val imagen = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_circulo_negro
                        )
                        val drawables: Array<Drawable> = binding.btnStatusTask.compoundDrawables
                        binding.btnStatusTask.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.imgBtnStatusTasksHeader.setImageResource(R.drawable.ic_circulo_negro)
                    }
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
                //Log.i("mensaje flag update","${flagUpdateTask}")
                btnAddTasks(flagUpdateTask, ticketSortsId,ticketInfoId,ticketInfoIdTechnician,ticketSortsStatus)
            }

        }
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


            val taskState = binding.imgBtnTaskStatus.tag.toString()
            val taskPrivate = binding.imgBtnPadLockTask.tag.toString()
            val ticketStatus = binding.btnStatusTask.tag.toString()

            val taskType = "1" // 1 no planificada, 2 planificada

            val categoryId = binding.btnAddCategory.tag.toString()

            val templateId = binding.edtTasksDescription.tag.toString() // id de plantillas
            val taskDescription = binding.edtTasksDescription.text.toString()

            val dateEnd = ""
            val dateBegin = ""
            val date = "2022-11-14 11:49:00"
            val technicianId = binding.btnAddTechnician.tag.toString()
            val actionTime = binding.btnTimeToSolveTask.tag.toString()
            Log.i("mensaje action",actionTime)
            val ticketSortsStatusTag = binding.btnStatusTask.tag.toString()

            Log.i("mensaje insert","\ntaskType: $taskType\ntaskDescription: $taskDescription\n" +
                    "taskPrivate: $taskPrivate\n" +
                    "technicianId: $technicianId\ntaskState: $taskState\ntemplateId: $templateId\n" +
                    "categoryId: $categoryId\nactionTime: $actionTime\n" +
                    "ticketSortsStatusTag: $ticketSortsStatusTag")
            when{
                taskDescription.isNullOrBlank() -> Toast.makeText(
                    this,
                    "sin descripción",
                    Toast.LENGTH_SHORT
                ).show()

                categoryId.isNullOrBlank() -> {
                    Toast.makeText(
                        this,
                        "debe agregar categoria de la tarea",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                technicianId.isNullOrBlank() -> {
                    Toast.makeText(
                        this,
                        "falta asiganar técnico",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                flagUpdateTask -> {
                    requestVolleyUpdateTask(
                        ticketInfoId,
                        taskDescription,
                        taskPrivate,
                        ticketSortsIdTechnician,
                        taskState,
                        templateId,
                        categoryId,
                        date,
                        actionTime,
                        ticketStatus,
                        ticketSortsId)

                    val ticketSortsStatus = binding.btnStatusTask.tag.toString()
                    val room =
                        Room.databaseBuilder(this, TicketInfoDB::class.java, "ticketInfoBD").build()
                    /*Toast.makeText(
                        this,
                        "${prefer.getTicketSortsId()} : $ticketSortsStatus",
                        Toast.LENGTH_SHORT
                    ).show()*/
                    lifecycleScope.launch {
                        room.daoTicketInfo()
                            .updateTicketInfo(token.prefer.getTicketSortsId(), ticketSortsStatus)
                        val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
                        for (element in getTicketInfoDB) {
                            Log.i(
                                "mensaje dbTicketFollow",
                                "$element\n"
                            )
                        }
                    }
                    MainActivity.updateFragmentFlag = true
                    val ticketSortsStatusString = "SIN_CAMBIO_DE_ESTADO"
                    MainActivity.updateFragmentFlag = true
                    val intentOnBack = Intent(this, NavFooterTicketsActivity::class.java)
                    intentOnBack.putExtra("ticketSortsStatus",ticketSortsStatusString)
                    intentOnBack.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intentOnBack)
                }

                !flagUpdateTask -> {
                    requestVolleyInsertTask(
                        taskType,
                        taskDescription,
                        taskPrivate,
                        technicianId,
                        taskState,
                        templateId,
                        categoryId,
                        actionTime,
                        ticketSortsStatusTag
                    )
                    val ticketSortsStatus = binding.btnStatusTask.tag.toString()
                    val room =
                        Room.databaseBuilder(this, TicketInfoDB::class.java, "ticketInfoBD").build()
                    /*Toast.makeText(
                        this,
                        "${prefer.getTicketSortsId()} : $ticketSortsStatus",
                        Toast.LENGTH_SHORT
                    ).show()*/
                    lifecycleScope.launch {
                        room.daoTicketInfo()
                            .updateTicketInfo(token.prefer.getTicketSortsId(), ticketSortsStatus)
                        val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
                        for (element in getTicketInfoDB) {
                            Log.i(
                                "mensaje dbTicketFollow",
                                "$element\n"
                            )
                        }
                    }
                    val ticketSortsStatusString = "SIN_CAMBIO_DE_ESTADO"
                    MainActivity.updateFragmentFlag = true
                    val intentOnBack = Intent(this, NavFooterTicketsActivity::class.java)
                    intentOnBack.putExtra("ticketSortsStatus",ticketSortsStatusString)
                    intentOnBack.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intentOnBack)
                }
            }
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
        date: String,
        action_Time: String,
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
                params["type"] = "1"
                params["private"] = taskPrivate
                params["user_tech"] = taskIdTech
                params["task_state"] = taskState
                params["template_id"] = templateId
                params["categories_id"] = categoryId
                params["date"] = date
                params["action_time"] = action_Time
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
        ticketSortsStatus: String
    ) {
        val room =
            Room.databaseBuilder(this, TicketInfoDB::class.java, "ticketInfoBD").build()
        lifecycleScope.launch {
            val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
            for (item in getTicketInfoDB){
                val ticketSortsId = item.ticketSortsID
                //metodo que nos devuelve los datos para los tickets
                val stringRequestDataTickets = object : StringRequest(Method.POST,
                    MainActivity.urlApi_InsertTasks+ticketSortsId, Response.Listener { response ->
                        try {
                            val dataAddFollowup = JSONObject(response) //obtenemos el objeto json

                            Log.i("mensaje","$dataAddFollowup")

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(applicationContext, "token expirado_: $e", Toast.LENGTH_LONG).show()
                            //Log.i("mensaje recycler e: ", "recycler ERROR: $e")
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(applicationContext, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
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
                VolleySingleton.getInstance(applicationContext).addToRequestQueue(stringRequestDataTickets)
                //FIN obtenemos perfil de usuario
            }
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

    private fun volleyRequestListStatusAllowed() {
        val room = Room.databaseBuilder(this, TicketInfoDB::class.java, "ticketInfoBD").build()
        lifecycleScope.launch {
            val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
            for (item in getTicketInfoDB) {
                val stringRequestDataTickets = object : StringRequest(Method.POST,
                    MainActivity.urlApi_ListStatusAllowed, Response.Listener { response ->
                        try {
                            dataModelArrayListStatusAllowed = ArrayList()

                            val jsonObjectResponse = JSONArray(response)
                            Log.i("mensaje allowed", "" + jsonObjectResponse)
                            for (i in 0 until jsonObjectResponse.length()) {
                                val dataListStatusAllowed = jsonObjectResponse.getJSONObject(i)
                                val listStatusAllowed = Data_ListStatusAllowed()

                                listStatusAllowed.listStatusAllowedId =
                                    dataListStatusAllowed.getString("ID")
                                listStatusAllowed.listStatusAllowedName =
                                    dataListStatusAllowed.getString("NAME")

                                dataModelArrayListStatusAllowed.add(listStatusAllowed)
                            }
                            setupRecyclerListStatusAllowed()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(
                                applicationContext,
                                "ListStatusAllowed: $e",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(
                            applicationContext,
                            "ERROR ListStatusAllowed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                    override fun getParams(): Map<String, String>? {
                        val params: MutableMap<String, String> = HashMap()
                        params["session_token"] = token.prefer.getToken()
                        params["ticket_id"] = item.ticketSortsID
                        params["profile_id"] = "6"
                        return params
                    }
                }
                this?.let {
                    VolleySingleton.getInstance(applicationContext)
                        .addToRequestQueue(stringRequestDataTickets)
                }
            }

        }

    }

    private fun setupRecyclerListStatusAllowed() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true

        recyclerViewListStatusAllowed!!.layoutManager = layoutManager

        recyclerView_Adapter_ListStatusAllowed =
            RecycleViw_Adapter_ListStatusAllowed(this, dataModelArrayListStatusAllowed, this)
        recyclerViewListStatusAllowed!!.adapter = recyclerView_Adapter_ListStatusAllowed
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val ticketSortsStatusString = "SIN_CAMBIO_DE_ESTADO"
        val intentOnBackPressed = Intent(this, NavFooterTicketsActivity::class.java)
        intentOnBackPressed.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intentOnBackPressed.putExtra("ticketSortsStatus",ticketSortsStatusString)
        startActivity(intentOnBackPressed)
    }

    override fun onTasksTemplateClick(
        nameTasksTemplate: String,
        contentTasksTemplate: String,
        idCategoryTasksTemplates: String,
        categoryTasksTemplates: String,
        timeTasksTemplates: String,
        idTasksTemplates: String,
        statusTasksTemplates: String
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
        binding.btnAddCategory.tag = idCategoryTasksTemplates
        //binding.btnTimeToSolveTask.text = timeTasksTemplates.split(".")[0] +" minutos"

        val minutesToSeconds = timeTasksTemplates.split(".")[0]
        Log.i("mensaje minutos",minutesToSeconds)
        binding.btnTimeToSolveTask.text = Utils_Global.convertMinutesToTimeFormat(minutesToSeconds) +"horas"
        //Toast.makeText(this, "$minutesToSeconds para resolver tarea", Toast.LENGTH_SHORT).show()
        binding.btnTimeToSolveTask.tag = minutesToSeconds.toInt()*60
        onSelectTemplateTask = true
        //binding.includeTimeToSolveTask.minutes.text = timeTasksTemplates.split(".")[0]
        //Log.i("mensaje minTemplate","${ timeTasksTemplates.split(".")[0] +" minutos"} -> $minutesToSeconds")
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

    override fun onSelectStatusClick(listStatusAllowedId: String, listStatusAllowedName: String) {
        //Toast.makeText(this, "$listStatusAllowedName seleccionado", Toast.LENGTH_SHORT).show()
        binding.btnStatusTask.text = listStatusAllowedName
        binding.btnStatusTask.tag = listStatusAllowedId

        when (binding.btnStatusTask.text) {
            "EN CURSO (Asignada)" -> {
                val imagen =
                    ContextCompat.getDrawable(applicationContext, R.drawable.ic_circulo_verde)
                var drawables: Array<Drawable> = binding.btnStatusTask.compoundDrawables
                binding.btnStatusTask.setCompoundDrawablesWithIntrinsicBounds(
                    drawables[0],
                    drawables[0],
                    imagen,
                    drawables[0]
                )
                binding.btnStatusTask.tag = "2"
            }
            "EN ESPERA" -> {
                val imagen = ContextCompat.getDrawable(applicationContext, R.drawable.ic_circulo)
                var drawables: Array<Drawable> = binding.btnStatusTask.compoundDrawables
                binding.btnStatusTask.setCompoundDrawablesWithIntrinsicBounds(
                    drawables[0],
                    drawables[0],
                    imagen,
                    drawables[0]
                )
                binding.btnStatusTask.tag = "4"
            }
        }
        binding.includeListStatusAllowed.modalListStatusAllowed.isVisible = false
        binding.LayoutBackgroudAgregarTarea.isVisible = false
    }

}