package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.flag
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.jsonArrayResponse
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TasksUsers
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TicketID
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.databinding.ActivityNavFooterTicketsBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_Tickets
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecyclerAdapter
import com.glpi.glpi_ministerio_pblico.ui.misIncidencias.MisIncidenciasFragment
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import com.glpi.glpi_ministerio_pblico.utilities.Utils_Global
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class NavFooterTicketsActivity : AppCompatActivity(),RecyclerAdapter.onConversationClickListener {
    private lateinit var binding: ActivityNavFooterTicketsBinding
    private lateinit var jsonObjectResponse: JSONObject

    /*creamos la lista de arreglos que tendrá los objetos de la clase Data_Tickets
   esta lista de arreglos (dataModelArrayList) funcionará como fuente de datos*/
    internal lateinit var dataModelArrayListConverdation: ArrayList<Data_Tickets>
    internal lateinit var dataModelArrayListConverzation: ArrayList<Data_Tickets>

    private lateinit var jsonArrayIdRecipient: JSONArray
    private lateinit var jsonArrayIdTechnician: JSONArray
    private lateinit var jsonArrayIdRequester: JSONArray

    lateinit private var progressBarTicketConversation: ProgressBar

    lateinit var TicketID_: String

    //INICIO toogle buton tickets
    var clickTickets: Boolean = false
    var clickConvezaciones: Boolean = false
    var click_fab:Boolean = false //fab_opciones de layout activity_tickets_historico.xml

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavFooterTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        volleyRequestIdRecipient() //datos del operador
        volleyRequestIdTechnician() //datos del tecnico
        volleyRequestIdRequester() //datos del solicitante
        volleyRequestPost(urlApi_TicketID)
        activityHeader()
        btnPetition()
        btnFabs()
        toggleButtonFooter()
    }

    private fun btnFabs(){
        //INICIO fab_opciones de layout activity_tickets_historico.xml
        binding.includeFabs.fabDesplegarOp.setOnClickListener {
            if (click_fab == false){
                binding.includeFabs.fabSolucion.isVisible = true
                binding.includeFabs.btnFabSolucion.isVisible = true
                binding.includeFabs.fabDocumentos.isVisible = true
                binding.includeFabs.btnFabDocumentos.isVisible = true
                binding.includeFabs.fabTareas.isVisible = true
                binding.includeFabs.btnFabTareas.isVisible = true
                binding.includeFabs.fabSeguimiento.isVisible = true
                binding.includeFabs.btnFabSeguimiento.isVisible = true
                binding.fabBackground.isVisible = true

                click_fab = true
            }else{
                hideFabs()
            }
        }
        binding.fabBackground.setOnClickListener {
            binding.includeFabs.fabSolucion.isVisible = false
            binding.includeFabs.btnFabSolucion.isVisible = false
            binding.includeFabs.fabDocumentos.isVisible = false
            binding.includeFabs.btnFabDocumentos.isVisible = false
            binding.includeFabs.fabTareas.isVisible = false
            binding.includeFabs.btnFabTareas.isVisible = false
            binding.includeFabs.fabSeguimiento.isVisible = false
            binding.includeFabs.btnFabSeguimiento.isVisible = false
            binding.fabBackground.isVisible = false
            binding.includeFabs.fabDesplegarOp.isVisible = true

            click_fab = false
        }
        //FIN fab_opciones de layout activity_tickets_historico

        //INICIO eventos click de fab_opciones
        binding.includeFabs.btnFabTareas.setOnClickListener {
            val intentAddTask = Intent(this@NavFooterTicketsActivity, TicketsAgregarTareaActivity::class.java)
            val bundle = Bundle()
            bundle.putString("TicketID", TicketID_)
            intentAddTask.putExtras(bundle)
            intentAddTask.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentAddTask)

            hideFabs()
        }
        binding.includeFabs.btnFabSeguimiento.setOnClickListener {
            val intentAddFollowup = Intent(this@NavFooterTicketsActivity, TicketsAgregarSeguimientoActivity::class.java)
            val bundle = Bundle()
            bundle.putString("TicketID", TicketID_)
            intentAddFollowup.putExtras(bundle)
            intentAddFollowup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentAddFollowup)

            hideFabs()
        }
        binding.includeFabs.btnFabSolucion.setOnClickListener {
            val intentAddSolution = Intent(this@NavFooterTicketsActivity, TicketsAgregarSolucionActivity::class.java)
            val bundle = Bundle()
            bundle.putString("TicketID", TicketID_)
            intentAddSolution.putExtras(bundle)
            intentAddSolution.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentAddSolution)

            hideFabs()
        }
        binding.includeFabs.btnFabDocumentos.setOnClickListener {
            val intentAddDocument = Intent(this@NavFooterTicketsActivity, TicketsAgregarDocumentosActivity::class.java)
            val bundle = Bundle()
            bundle.putString("TicketID", TicketID_)
            intentAddDocument.putExtras(bundle)
            intentAddDocument.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentAddDocument)

            hideFabs()
        }
        //fin eventos click de fab_opciones
    }

    private fun hideFabs(){
        binding.includeFabs.fabSolucion.isVisible = false
        binding.includeFabs.btnFabSolucion.isVisible = false
        binding.includeFabs.fabDocumentos.isVisible = false
        binding.includeFabs.btnFabDocumentos.isVisible = false
        binding.includeFabs.fabTareas.isVisible = false
        binding.includeFabs.btnFabTareas.isVisible = false
        binding.includeFabs.fabSeguimiento.isVisible = false
        binding.includeFabs.btnFabSeguimiento.isVisible = false
        binding.fabBackground.isVisible = false

        click_fab = false
    }

    private fun btnPetition(){
        //INICIO botones para desplegar y plegar descripciones
        var clickG: Boolean = false
        var clickD: Boolean = false
        var clickA: Boolean = false

        binding.includeTickets.btnDesplegarGeneral.setOnClickListener{
            if(clickG == false){
                binding.includeTickets.general.isVisible = false
                clickG = true
            }else{
                binding.includeTickets.general.isVisible = true
                clickG = false
            }
        }

        binding.includeTickets.btnDesplegarDecripcion.setOnClickListener{
            if(clickD == false){
                binding.includeTickets.decripcion.isVisible = false
                clickD = true
            }else{
                binding.includeTickets.decripcion.isVisible = true
                clickD = false
            }
        }

        binding.includeTickets.btnDesplegarActor.setOnClickListener {
            if(clickA == false){
                binding.includeTickets.actor.isVisible = false
                clickA = true
            }else{
                binding.includeTickets.actor.isVisible = true
                clickA = false
            }
        }
        //FIN botones para desplegar y plegar descripciones
    }

    private fun activityHeader(){
        //boton atras - include de nav_header_tickets.xml
        binding.includeNavHeaderTickets.btnAtrasTickets.setOnClickListener {
            onBackPressed()
        }
    }

    private fun toggleButtonFooter(){
        binding.btnConversacionFooter.iconTint = ContextCompat.getColorStateList(this, R.color.textColor)
        binding.btnConversacionFooter.setTextColor(Color.parseColor("#175381"))

        binding.btnTicketsFooter.setOnClickListener {
            if(clickTickets == false){
                binding.btnTicketsFooter.iconTint = ContextCompat.getColorStateList(this, R.color.textColor)
                binding.btnTicketsFooter.setTextColor(Color.parseColor("#175381"))

                binding.btnConversacionFooter.iconTint = ContextCompat.getColorStateList(this, R.color.ticketsGris)
                binding.btnConversacionFooter.setTextColor(Color.parseColor("#676161"))

                binding.includeTickets.includeTicketsLayout.isVisible = true //se muestra
                binding.ticketConversation.isVisible = false
                binding.btnConversacionFooterCOLOR.setBackgroundResource(R.color.ticketsBlanco)

                //************INICIO DE SETEO LOS FAB'S DE LAYOUT activity_tickets_historico.xml************
                binding.includeFabs.fabSolucion.isVisible = false
                binding.includeFabs.btnFabSolucion.isVisible = false
                binding.includeFabs.fabDocumentos.isVisible = false
                binding.includeFabs.btnFabDocumentos.isVisible = false
                binding.includeFabs.fabTareas.isVisible = false
                binding.includeFabs.btnFabTareas.isVisible = false
                binding.includeFabs.fabSeguimiento.isVisible = false
                binding.includeFabs.btnFabSeguimiento.isVisible = false
                binding.fabBackground.isVisible = false
                click_fab = false
                //************FIN DE SETEO DE LOS FAB'S DE LAYOUT activity_tickets_historico.xml************

                clickTickets = true
                clickConvezaciones = false
            }
        }

        binding.btnConversacionFooter.setOnClickListener {
            if(clickConvezaciones == false){
                binding.ticketConversation.isVisible = true
                binding.btnTicketsFooter.iconTint = ContextCompat.getColorStateList(this, R.color.ticketsGris)
                binding.btnTicketsFooter.setTextColor(Color.parseColor("#676161"))

                binding.btnConversacionFooter.iconTint = ContextCompat.getColorStateList(this, R.color.textColor)
                binding.btnConversacionFooter.setTextColor(Color.parseColor("#175381"))

                binding.includeTickets.includeTicketsLayout.isVisible = false
                binding.btnTicketsFooterCOLOR.setBackgroundResource(R.color.ticketsBlanco)
                clickConvezaciones = true
                clickTickets = false
            }
        }
        //FIN toogle buton
    }

    private fun setupRecyclerView(){
        val recyclerViewNews = binding.recyclerViewConversation
        val newsList = dataModelArrayListConverdation
        val newsAdapter = RecyclerAdapter(this,newsList,this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true,)
        layoutManager.stackFromEnd = true

        recyclerViewNews.adapter = newsAdapter
        recyclerViewNews.layoutManager = layoutManager
        if (newsAdapter.itemCount == 0){
            binding.recyclerViewConversation.isVisible = false
            binding.includeTicketsHistorico.layoutHistorico.isVisible = true
        }
        //recyclerViewNews.setHasFixedSize(true)
    }

    private fun volleyRequestPost(urlApi_: String) {
        progressBarTicketConversation = binding.progressBarTicketConversation

        val bundle = intent.extras
        val TicketID_ = bundle!!.getString("TicketID")
        val Contenido_ = bundle!!.getString("Contenido")
        val NameOperador_ = bundle!!.getString("NameOperador")
        val CurrentTime_ = bundle!!.getString("CurrentTime")
        val ModificationDate = bundle.getString("ModificationDate")
        val creationDateTicket = bundle!!.getString("creationDateTicket")



        jsonObjectResponse = JSONObject()

        val stringRequestDataTickets = @RequiresApi(Build.VERSION_CODES.N)
        object : StringRequest(Method.POST,
            urlApi_+TicketID_, Response.Listener { response ->
                try {
                    dataModelArrayListConverdation = ArrayList()
                    jsonObjectResponse = JSONObject(response)
                    var iterador = 0
                    var tasksIterador: JSONObject
                    for (i in  0 until jsonObjectResponse.length()){
                        val playerModel = Data_Tickets()
                        if (jsonObjectResponse.getString(iterador.toString())[2] == 'T'){

                            tasksIterador = jsonObjectResponse.getJSONObject(iterador.toString())

                            val tasksTipo = tasksIterador.getString("TIPO")
                            playerModel.setGlpiTasksTipo(tasksTipo)
                            val tasksNameOperador = tasksIterador.getString("NOMBRE")
                            val tasksApellidoOperador = tasksIterador.getString("APELLIDO")
                            playerModel.setGlpiTasktName("$tasksNameOperador $tasksApellidoOperador")
                            val tasksFechaCreacion = tasksIterador.getString("FECHA_CREACION")
                            val dateTime = tasksFechaCreacion.replace(" ","T")
                            val dateTimeiso1801 = dateTime+"Z"
                            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                            val tasks_dateTime: Date = format.parse(dateTimeiso1801)
                            //Log.i("mensaje creacionString",""+tasksFechaCreacion)

                            playerModel.setConversationCreation(tasks_dateTime)
                            val decoded: String = Html.fromHtml(tasksIterador.getString("CONTENIDO")).toString()
                            val decoded2: Spanned = HtmlCompat.fromHtml(decoded, HtmlCompat.FROM_HTML_MODE_COMPACT)
                            //Log.i("mensaje taskiterador",""+decoded2)
                            playerModel.setGlpiTasksDescripcion(decoded2.toString())

                            //----------
                            val dataId = jsonArrayIdRecipient.getJSONObject(0)
                            val nameId = dataId.getString("NOMBRE")
                            val lastNameId = dataId.getString("APELLIDO")
                            playerModel.setTaskUserName("$nameId $lastNameId")

                            val dataTechnicianId = jsonArrayIdTechnician.getJSONObject(0)
                            val nameTechnicianId = dataTechnicianId.getString("NOMBRE")
                            val lastNameTechnicianId = dataTechnicianId.getString("APELLIDO")
                            playerModel.setTechnicianName("$nameTechnicianId $lastNameTechnicianId")
                            //binding.includeTickets.txtTasksUserName.text = "$nameId $lastNameId"

                            if (tasksTipo == "TASK"){
                                val estimatedTime = tasksIterador.getString("DURACION")
                                val estimatedTimeConvert = estimatedTime.replace(".","").toLong()
                                val milisToHours = String.format(
                                    "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(estimatedTimeConvert),
                                    TimeUnit.MILLISECONDS.toMinutes(estimatedTimeConvert) % TimeUnit.HOURS.toMinutes(1),
                                    TimeUnit.MILLISECONDS.toSeconds(estimatedTimeConvert) % TimeUnit.MINUTES.toSeconds(1)
                                )
                                playerModel.setTaskUsersMillisToHours(milisToHours)

                                val date = creationDateTicket.toString().split(" ")
                                //val day = date[0]
                                val hour = date[1].split(":")
                                val secondsToMs1: Int = hour[2].toInt() * 1000
                                val minutesToMs1: Int = hour[1].toInt() * 60000
                                val hoursToMs1: Int = hour[0].toInt() * 3600000
                                val total1 = secondsToMs1 + minutesToMs1 + hoursToMs1
                                val estimatedTime_: Long = (total1 + estimatedTimeConvert.toInt()).toLong()
                                //Log.i("mensaje split: ", "${date[1]}")
                                val hms = String.format(
                                    "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(estimatedTime_),
                                    TimeUnit.MILLISECONDS.toMinutes(estimatedTime_) % TimeUnit.HOURS.toMinutes(1),
                                    TimeUnit.MILLISECONDS.toSeconds(estimatedTime_) % TimeUnit.MINUTES.toSeconds(1)
                                )
                                //Log.i("mensaje trans: ", "$hms")
                                playerModel.setTaskUsersEstimateDuration(hms)
                            }

                            //----------

                            playerModel.setTicketSortsContents(Contenido_.toString())
                            playerModel.setGlpiOperadorName(NameOperador_.toString())
                            playerModel.setTicketSortsCreationDate(CurrentTime_.toString())
                            playerModel.setTicketSortsModificationDate(ModificationDate.toString())
                            playerModel.setTicketSortsModificationDate(ModificationDate.toString())

                            iterador++


                            dataModelArrayListConverdation.sortBy { it.getConversationCreation() }
                            dataModelArrayListConverdation.add(playerModel)
                        }else{
                            /*Log.i("mensaje creacionString",""+jsonObjectResponse.getJSONObject(iterador.toString()))
                            tasksIterador = jsonObjectResponse.getJSONObject(iterador.toString())
                            playerModel.setGlpiDescripcion(tasksIterador.getString("scalar"))

                            iterador++*/
                            //----------------------------------------------------------------------------
                        }
                    }
                    setupRecyclerView()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "token expirado_: $e", Toast.LENGTH_LONG).show()
                    //Log.i("mensaje entitis dentroE",""+response.get(0))
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
        Handler().postDelayed({
            progressBarTicketConversation!!.isVisible = false
            binding.ticketConversation.isVisible = true
        },2000/* 5 second */)
        this?.let {
            VolleySingleton.getInstance(this).addToRequestQueue(stringRequestDataTickets)
        }
        DataToTicketsHistoricoActivity()
    }

    private fun createTextView(taskDescriptions: String): TextView {
        val newTextView = TextView(this)
        //*****************INICIO DISEÑO DEL TEXVIEW****************************
        newTextView.text = taskDescriptions
        newTextView.setTextColor(Color.parseColor("#000000"))
        //--
        val fontFamily = Typeface.createFromAsset(
            assets,
            "font/averia_sans_libre_light.ttf"
        )
        newTextView.setTypeface(fontFamily)
        //--
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            newTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        }
        newTextView.setPadding(30,32,0,32)
        //--
        //*****************FIN DISEÑO DEL TEXVIEW*******************************
        return newTextView
    }

    @SuppressLint("SetTextI18n")
    private fun DataToTicketsHistoricoActivity() {
        val bundle = intent.extras

        TicketID_ = bundle!!.getString("TicketID").toString()
        val NameOperador_ = bundle!!.getString("NameOperador")//aun no hay
        val CurrentTime_ = bundle!!.getString("CurrentTime")
        val ModificationDate = bundle!!.getString("ModificationDate")

        val Contenido_ = bundle!!.getString("Contenido")
        //-----
        val Tipo = bundle!!.getString("Tipo")


        val Ubicacion_ = bundle!!.getString("Ubicacion")
        val Correo_ = bundle!!.getString("Correo")
        val NameSolicitante_ = bundle!!.getString("NameSolicitante")
        val CargoSolicitante_ = bundle!!.getString("CargoSolicitante")
        val TelefonoSolicitante_ = bundle!!.getString("TelefonoSolicitante")
        val LoginName_ = bundle!!.getString("LoginName")
        val TicketEstado_ = bundle!!.getString("TicketEstado")
        val TicketCategoria_ = bundle!!.getString("TicketCategoria")
        val TicketOrigen_ = bundle!!.getString("TicketOrigen")
        val TicketUrgencia_ = bundle!!.getString("TicketUrgencia")
        //SECTION TASKS
        val taskName = bundle!!.getString("taskName")
        val taskDescription = bundle.getString("taskDescription")

        //jsonObjetcIdResponse = JSONObject()
        //volleyRequestID(idRecipient.toString())
        //val id = jsonObjetcIdResponse.getJSONObject("0")


        //val nameOperador = id.getString("NOMBRE")//OBTENEMOS EL NOMBRE DEL OPERADOR QUE ASIGNÓ EL TICKET

        //--------------

        //--------------

        binding.includeNavHeaderTickets.txtTicketID.text = "Petición #$TicketID_"
        binding.includeNavHeaderTickets.txtUbicacion.text = Ubicacion_
        if (TicketEstado_ == "EN CURSO (Asignada)"){
            binding.includeNavHeaderTickets.txtTicketEstado.setBackgroundResource(R.drawable.ic_circulo_verde)
        }else{
            binding.includeNavHeaderTickets.txtTicketEstado.setBackgroundResource(R.drawable.ic_circulo)
        }

        binding.includeTicketsHistorico.txtCurrentTime.text = "Fecha Creación $CurrentTime_"
        if (CurrentTime_ != ModificationDate){
            binding.includeTicketsHistorico.txtModificationDate.text = "Ult. Modificación $ModificationDate"
            binding.includeTicketsHistorico.txtModificationDate.isVisible = true
        }

        binding.includeTicketsHistorico.txtDescripcionTicketHistorico.text = Contenido_
        binding.includeTickets.labelFechaOperadorApertura.text = "$CurrentTime_ - "
        binding.includeTickets.labelCategoria.text = TicketCategoria_

        //CAMBIAR COLOR DE ESTADO DE URGENCIA-------------------------------------------------------
        if (TicketUrgencia_ == "BAJA"){
            val compoundDrawables: Array<Drawable> = binding.includeTickets.labelPrioridad.compoundDrawables
            val drawableLeft = compoundDrawables[0].mutate()
            drawableLeft.colorFilter =
                PorterDuffColorFilter(Color.parseColor("#06B711"), PorterDuff.Mode.SRC_IN)
        }else if(TicketUrgencia_ == "MEDIA"){
            val compoundDrawables: Array<Drawable> = binding.includeTickets.labelPrioridad.compoundDrawables
            val drawableLeft = compoundDrawables[0].mutate()
            drawableLeft.colorFilter =
                PorterDuffColorFilter(Color.parseColor("#FBFF0F"), PorterDuff.Mode.SRC_IN)
        }else{
            val compoundDrawables: Array<Drawable> = binding.includeTickets.labelPrioridad.compoundDrawables
            val drawableLeft = compoundDrawables[0].mutate()
            drawableLeft.colorFilter =
                PorterDuffColorFilter(Color.parseColor("#FF8800"), PorterDuff.Mode.SRC_IN)
        }
        //------------------------------------------------------------------------------------------
        binding.includeTickets.labelPrioridad.text = TicketUrgencia_
        binding.includeTickets.lableFechaMAXCierre.text = "fecha y hora de cierre estimado - $TicketUrgencia_"
        binding.includeTickets.labelSolicitudIncidencia.text = Tipo
        if (Tipo == "SOLICITUD"){
            binding.includeTickets.txtSolicitud.text ="?"
            binding.includeTickets.tvIncidencia.isVisible = false
            binding.includeTickets.tvSolicitud.isVisible = true
        }
        binding.includeTickets.labelUbicacion.text = Ubicacion_
        //binding.includeTickets.labelEmail.text = Correo_
        binding.includeTickets.labelOrigen.text = TicketOrigen_
        //binding.includeTickets.labelSolicitanteNombre.text = NameSolicitante_
        binding.includeTickets.labelSolicitanteCargo.text = CargoSolicitante_
        binding.includeTickets.labelSolicitanteCelular.text = TelefonoSolicitante_
        binding.includeTickets.labelAsignadoNombre.text = NameSolicitante_
        binding.includeTickets.labelDescrTicket.text = Contenido_

        //SECTION TASKS
        binding.includeTicketsHistorico.txtTaskNameLogin.text = taskName
        binding.includeTicketsHistorico.txtTaskDescription.text = taskDescription
    }

    private fun volleyRequestIdRecipient(){
        val bundle = intent.extras
        val idRecipient = bundle!!.getString("IdRecipient")
        Log.i("mensaje cel","$idRecipient")
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlApi_TasksUsers+idRecipient, Response.Listener { response ->
            try {
                jsonArrayIdRecipient = JSONArray()
                jsonArrayIdRecipient = JSONArray(response)
                val dataId = jsonArrayIdRecipient.getJSONObject(0)
                val nameId = dataId.getString("NOMBRE")
                val lastNameId = dataId.getString("APELLIDO")
                val cellPhone = dataId.getString("TELEFONO")

                binding.includeTicketsHistorico.txtNameOperador.text = "$nameId $lastNameId"
                binding.includeTickets.txtTasksUserName.text = "$nameId $lastNameId"
                if (cellPhone != " " && cellPhone != "null"){
                    binding.includeTickets.labelAsignadoCelular.text = cellPhone
                    binding.includeTickets.labelAsignadoCelular.isVisible = true
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }, Response.ErrorListener {
            Toast.makeText(this, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
        }){
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                return params
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequestDataTickets)
        //FIN volley ------------------------------------------------------------
    }

    private fun volleyRequestIdTechnician(){
        val bundle = intent.extras
        val idRecipient = bundle!!.getString("IdTechnician")
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlApi_TasksUsers+idRecipient, Response.Listener { response ->
                try {
                    jsonArrayIdTechnician = JSONArray()
                    jsonArrayIdTechnician = JSONArray(response)
                    /*val dataId = jsonArrayIdTechnician.getJSONObject(0)
                    val nameId = dataId.getString("NOMBRE")
                    val lastNameId = dataId.getString("APELLIDO")
                    binding.includeTicketsHistorico.txtNameOperador.text = "$nameId $lastNameId"
                    binding.includeTickets.txtTasksUserName.text = "$nameId $lastNameId"*/
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                return params
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequestDataTickets)
    }
    private fun volleyRequestIdRequester(){
        val bundle = intent.extras
        val idRecipient = bundle!!.getString("IdRequester")
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlApi_TasksUsers+idRecipient, Response.Listener { response ->
                try {
                    jsonArrayIdRequester = JSONArray()
                    jsonArrayIdRequester = JSONArray(response)
                    val dataId = jsonArrayIdRequester.getJSONObject(0)
                    val nameId = dataId.getString("NOMBRE")
                    val lastNameId = dataId.getString("APELLIDO")
                    binding.includeTickets.labelSolicitanteNombre.text = "$nameId $lastNameId"

                    val placeId = dataId.getString("UBICACION")
                    binding.includeTickets.labelUbicacion.text = placeId
                    binding.includeNavHeaderTickets.txtUbicacion.text = placeId

                    val emailId = dataId.getString("CORREO")
                    if (emailId != "null" && emailId != "null"){
                        binding.includeTickets.labelEmail.isVisible = true
                        binding.includeTickets.labelEmail.text = emailId
                    }


                    val positionId = dataId.getString("CARGO")
                    binding.includeTickets.labelSolicitanteCargo.text = positionId

                    val cellphoneId = dataId.getString("TELEFONO")
                    if (cellphoneId != "null"){
                        binding.includeTickets.labelSolicitanteCelular.isVisible = true
                        binding.includeTickets.labelSolicitanteCelular.text = cellphoneId
                    }

                }catch (e:Exception){
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                return params
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequestDataTickets)
    }


    override fun onEditClick(
        glpiTasksDescripcion: String,
        glpiTasksTipo: String
    ) {
        //recuperamos el id del ticket
        val bundle = intent.extras
        val ticketId = bundle!!.getString("TicketID")

        val intentTasks = (Intent(this,TicketsAgregarTareaActivity::class.java))
        intentTasks.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val intentFollowUp = (Intent(this,TicketsAgregarSeguimientoActivity::class.java))
        intentFollowUp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        flag = true
        //Log.i("mensaje edit: ",""+glpiConversationTipo)
        if (glpiTasksTipo == "TASK"){
            intentTasks.putExtra("ticketId",ticketId)
            intentTasks.putExtra("tasks_description",glpiTasksDescripcion)
            startActivity(intentTasks)
        }else{
            intentFollowUp.putExtra("ticketId",ticketId)
            intentFollowUp.putExtra("tasks_description",glpiTasksDescripcion)
            startActivity(intentFollowUp)
        }
    }

    override fun onFabClick() {
        val bundle = intent.extras
        Toast.makeText(this, bundle!!.getString("Contenido"), Toast.LENGTH_SHORT).show()
        val Contenido_ = bundle!!.getString("Contenido")
    }
}