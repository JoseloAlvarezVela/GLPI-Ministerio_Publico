package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.decodeHtml
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsAgregarSeguimientoBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.*
import com.glpi.glpi_ministerio_pblico.ui.misPeticiones.MisPeticionesFragment
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TicketsAgregarSeguimientoActivity : AppCompatActivity(),
    RecycleView_Adapter_FollowupTemplate.onFollowTemplateClickListener,
    RecycleViw_Adapter_ListStatusAllowed.onStatusAllowedClickListener,
    RecycleView_Adapter_SourceTypes.onSourceTypesClickListener{
    private var recyclerView: RecyclerView? = null
    private var recyclerViewListStatusAllowed: RecyclerView? = null
    private var recyclerViewListSourceTypes: RecyclerView? = null

    internal lateinit var dataModelArrayListStatusAllowed: ArrayList<Data_ListStatusAllowed>
    internal lateinit var dataModelArrayListFollowupTemplate: ArrayList<Data_FollowupTemplate>
    internal lateinit var dataModelArrayListSourceTypes: ArrayList<Data_SourceTypes>

    private var recyclerView_Adapter_FollowupTemplate: RecycleView_Adapter_FollowupTemplate? = null
    private var recyclerView_Adapter_ListStatusAllowed: RecycleViw_Adapter_ListStatusAllowed? = null
    private var recyclerView_Adapter_ListSourceTypes: RecycleView_Adapter_SourceTypes? = null



    private lateinit var binding: ActivityTicketsAgregarSeguimientoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsAgregarSeguimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.includeModalFollowupTemplate.recyclerFollowupTemplate
        recyclerViewListStatusAllowed = binding.includeListStatusAllowed.recyclerFollowupListAllowed
        recyclerViewListSourceTypes = binding.includeModalListSourceTypes.recyclerListSourceTypes

        volleyRequestListStatusAllowed()
        volleyRequestFollowupTemplates(MainActivity.urlApi_FollowupTemplates)
        volleyRequestListSourceTypes()

        val bundle = intent.extras
        when(bundle!!.getBoolean("flagOnEditClick")){
            true -> updateFollowup()
            false -> getInfoFollowup()
        }
        modalListStatusAllowed()
        modalListSourceTypes()
        appBarHeaderFollowup()
        imgBtnPadLock()
        btnDeployFab()
    }

    private fun btnAddFollowup(flagUpdateFollow: Boolean, ticketId: String, ticketInfoId: String){
        binding.btnAddFollowup.setOnClickListener {
            Toast.makeText(this, "Tarea añadida", Toast.LENGTH_LONG).show()
            val followupDescription = binding.edtFollowupDescription.text
            val followupPrivate = binding.imgViewPadLock.tag.toString()
            val listStatusAllowedId = binding.btnStatusFollowup.tag.toString()
            val requestType = binding.btnSourceTypes.tag.toString()

            when{
                followupDescription.isNullOrBlank() -> Toast.makeText(this, "sin descripción", Toast.LENGTH_SHORT).show()
                requestType == "null" -> Toast.makeText(this, "Seleccionar origen del seguimiento", Toast.LENGTH_SHORT).show()
                flagUpdateFollow -> {
                    requestVolleyUpdateFollowup(
                        requestType,
                        ticketInfoId,
                        ticketId,
                        listStatusAllowedId,
                        followupPrivate,
                        followupDescription
                    )
                    onBackPressed()
                }
                !flagUpdateFollow -> {
                    requestVolleyInsertFollowup(
                        requestType,
                        ticketId,
                        listStatusAllowedId,
                        followupPrivate,
                        followupDescription
                    )

                    /*val intentOnBack = Intent(this, NavFooterTicketsActivity::class.java)
                    intentOnBack.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intentOnBack)*/
                    onBackPressed()

                }
            }
        }
    }

    private fun getInfoFollowup(){
        val flagGetTicketInfo = false
        Toast.makeText(this, "getTicketInfo", Toast.LENGTH_LONG).show()
        val bundle = intent.extras
        val ticketSortsId = bundle!!.getString("ticketSortsId").toString()
        val ticketSortsStatus = bundle!!.getString("ticketSortsStatus")
        val ticketPrivate = bundle!!.getString("ticketPrivate")
        val ticketInfoId = bundle!!.getString("ticketInfoId").toString()

        Log.i("mensaje type",ticketSortsStatus.toString())
        if (ticketSortsStatus == "EN CURSO (Asignada)"){
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo_verde)
            binding.btnStatusFollowup.text = "EN CURSO (Asignada)"
            binding.btnStatusFollowup.tag = "2"
            binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_verde)
        }else if (ticketSortsStatus == "EN ESPERA"){
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo)
            binding.btnStatusFollowup.text = "EN ESPERA"
            binding.btnStatusFollowup.tag = "4"
            binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo)
        }else if (ticketSortsStatus == "CERRADO"){
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo_negro)
            binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_negro)
        }

        binding.btnSourceTypes.tag = "null"

        if(ticketPrivate == "SI"){
            binding.imgViewPadLock.tag = "1"
        }else{
            binding.imgViewPadLock.tag = "0"
        }

        binding.tvIdTicket.text = "Petición #$ticketSortsId"

        //imgBtnTicketStatus(ticketSortsStatus)
        btnAddFollowup(flagGetTicketInfo,ticketSortsId,ticketInfoId)
    }

    private fun modalListStatusAllowed(){
        binding.btnStatusFollowup.setOnClickListener {
            binding.includeListStatusAllowed.modalListStatusAllowed.isVisible = true
            binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = true
        }

        binding.includeListStatusAllowed.btnCloseModalListStatusAllowed.setOnClickListener {
            binding.includeListStatusAllowed.modalListStatusAllowed.isVisible = false
            binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = false
        }
    }

    private fun modalListSourceTypes(){
        binding.btnSourceTypes.setOnClickListener {
            binding.includeModalListSourceTypes.modalListSourceTypes.isVisible = true
            binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = true
        }

        binding.includeModalListSourceTypes.btnCloseModalSourceTypes.setOnClickListener {
            binding.includeModalListSourceTypes.modalListSourceTypes.isVisible = false
            binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = false
        }
    }

    private fun imgBtnPadLock(){
        val bundle = intent.extras
        var ticketPrivate = bundle!!.getString("ticketPrivate")
        var newTicketPrivate = "NO"
        var flagImgViewPadLock = false
        Log.i("mensaje type",ticketPrivate.toString())
        if (ticketPrivate == "SI"){
            //Toast.makeText(this, "Segumiento Privado", Toast.LENGTH_SHORT).show()
            flagImgViewPadLock = false
            binding.imgViewPadLock.setOnClickListener {
                if(flagImgViewPadLock){
                    binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_cerrado)

                    binding.imgViewPadLock.tag = "1"
                    flagImgViewPadLock = false
                }else{
                    binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_abierto)
                    binding.imgViewPadLock.tag = "0"
                    flagImgViewPadLock = true
                }
            }
        }else{
            flagImgViewPadLock = true

            binding.imgViewPadLock.setOnClickListener {
                if(flagImgViewPadLock){
                    binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_cerrado)
                    Toast.makeText(this, "Segumiento Privado", Toast.LENGTH_SHORT).show()
                    binding.imgViewPadLock.tag = "1"
                    newTicketPrivate = "NO"
                    flagImgViewPadLock = false
                }else{
                    binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_abierto)
                    binding.imgViewPadLock.tag = "0"
                    newTicketPrivate = "SI"
                    flagImgViewPadLock = true
                }
            }
        }
    }

    private fun updateFollowup(){
        val flagUpdateFollow = true
        Toast.makeText(this, "updateFollowup", Toast.LENGTH_SHORT).show()
        val bundle = intent.extras
        val ticketSortsId = bundle!!.getString("ticketSortsId").toString()
        val ticketSortsStatus = bundle!!.getString("ticketSortsStatus")
        val ticketPrivate = bundle!!.getString("ticketPrivate")
        val ticketInfoContent = bundle!!.getString("ticketInfoContent")
        val ticketInfoId = bundle!!.getString("ticketInfoId").toString()
        val ticketSortsSource = bundle!!.getString("ticketSortsSource").toString()
        val ticketInfoSource = bundle!!.getString("ticketInfoSource").toString()

        //imgBtnTicketStatus(ticketSortsStatus)
        imgBtnPadLock()

        binding.btnSourceTypes.text = ticketInfoSource
        when(ticketInfoSource){
            "Correo electrónico" -> binding.btnSourceTypes.tag = "2"
            "Documento" -> binding.btnSourceTypes.tag = "5"
            "Formcreator" -> binding.btnSourceTypes.tag = "8"
            "Teléfono" -> binding.btnSourceTypes.tag = "3"
            "Whatsapp" -> binding.btnSourceTypes.tag = "4"
        }

        binding.tvIdTicket.text = "Petición #$ticketSortsId"
        if (ticketSortsStatus == "EN CURSO (Asignada)"){
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo_verde)
            binding.btnStatusFollowup.text = "EN CURSO (Asignada)"
            binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_verde)
        }else{
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo)
            binding.btnStatusFollowup.text = "EN ESPERA"
            binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo)
        }

        if(ticketPrivate == "SI"){
            binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_cerrado)
            binding.imgViewPadLock.tag = "1"
        }else{
            binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_abierto)
            binding.imgViewPadLock.tag = "0"

        }

        binding.edtFollowupDescription.setText(ticketInfoContent)
        binding.btnStatusFollowup.text = ticketSortsStatus
        when(ticketSortsStatus){
            "EN CURSO (Asignada)" -> binding.btnStatusFollowup.tag = "2"
            "EN CURSO (Planificación)" -> binding.btnStatusFollowup.tag = "3"
            "EN ESPERA" -> binding.btnStatusFollowup.tag = "4"
            "SOLUCIONADO" -> binding.btnStatusFollowup.tag = "5"
        }

        btnAddFollowup(flagUpdateFollow,ticketSortsId,ticketInfoId)
        //MainActivity.flagEdit = false
    }

    private fun imgBtnTicketStatus(ticketSortsStatus: String?): String {
        var flagTicketStatus: Boolean
        var newTicketStatus = "EN CURSO (Asignada)"
        if (ticketSortsStatus == "EN CURSO (Asignada)"){
            flagTicketStatus = true
            binding.imgBtnStatus.setOnClickListener {
                if(flagTicketStatus){
                    binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo)
                    binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo)
                    newTicketStatus = "EN ESPERA"
                    flagTicketStatus = false
                }else{
                    binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo_verde)
                    binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_verde)
                    newTicketStatus = "EN CURSO (Asignada)"
                    flagTicketStatus = true
                }
            }
        }else{
            flagTicketStatus = false
            binding.imgBtnStatus.setOnClickListener {
                if(flagTicketStatus){
                    binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo)
                    binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo)
                    newTicketStatus = "EN ESPERA"
                    flagTicketStatus = false
                }else{
                    binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo_verde)
                    binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_verde)
                    newTicketStatus = "EN CURSO (Asignada)"
                    flagTicketStatus = true
                }
            }
        }
        return newTicketStatus
    }


    //INICIO - funcion de maneja los botones del header
    private fun appBarHeaderFollowup() {
        //boton atras
        binding.btnAtrasActtaddseg.setOnClickListener {
            onBackPressed()
        }
    }


    private fun requestVolleyUpdateFollowup(
        requestType: String,
        ticketInfoId: String,
        ticketId: String,
        listStatusAllowedId: String,
        followupPrivate: String,
        followupDescription: Editable
    ) {
        //metodo que nos devuelve los datos para los tickets
        Log.i("mensaje link","${MainActivity.urlApi_UpdateFollowup+ticketInfoId}")
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_UpdateFollowup+ticketInfoId, Response.Listener { response ->
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
                params["content"] = followupDescription.toString()
                params["private"] = followupPrivate
                params["request_type"] = requestType
                params["ticket_state"] = listStatusAllowedId
                params["ticket_id"] = ticketId

                Log.i("mensaje paramsupfoll", " $params")
                return params
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequestDataTickets)
        //FIN obtenemos perfil de usuario
    }

    private fun requestVolleyInsertFollowup(
        requestType: String,
        ticketId: String,
        listStatusAllowedId: String,
        followupPrivate: String,
        followupDescription: Editable
    ) {
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_InsertFollowup, Response.Listener { response ->
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
                params["content"] = followupDescription.toString()
                params["private"] = followupPrivate
                params["request_type"] = requestType
                params["ticket_state"] = listStatusAllowedId
                params["ticket_id"] = ticketId

                return params
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequestDataTickets)
        //FIN obtenemos perfil de usuario
    }

    //INICIO - funcion de fabs que abre camara del celular y archivos del celular
    private fun btnDeployFab() {
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
            binding.includeListStatusAllowed.modalListStatusAllowed.isVisible = false
            binding.includeModalListSourceTypes.modalListSourceTypes.isVisible = false
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

    private fun volleyRequestListSourceTypes() {
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_GetRequestTypes, Response.Listener { response ->
                try {
                    dataModelArrayListSourceTypes = ArrayList()

                    val jsonObjectResponse = JSONArray(response)
                    Log.i("mensaje allowed",""+jsonObjectResponse)
                    for (i in  0 until jsonObjectResponse.length()){

                        val dataListSourceTypes = jsonObjectResponse.getJSONObject(i)
                        val listSourceTypes = Data_SourceTypes()

                        listSourceTypes.sourceTypesId = dataListSourceTypes.getString("ID")
                        listSourceTypes.sourceTypesName = dataListSourceTypes.getString("NAME")

                        dataModelArrayListSourceTypes.add(listSourceTypes)
                    }
                    setupRecyclerListSourceTypes()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "ListSourceTypes: $e", Toast.LENGTH_LONG).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "ERROR ListSourceTypes", Toast.LENGTH_SHORT).show()
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

    private fun volleyRequestListStatusAllowed() {
        val bundle = intent.extras
        val ticketSortsId = bundle!!.getString("ticketSortsId")
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_ListStatusAllowed, Response.Listener { response ->
                try {
                    dataModelArrayListStatusAllowed = ArrayList()

                    val jsonObjectResponse = JSONArray(response)
                    Log.i("mensaje allowed",""+jsonObjectResponse)
                    for (i in  0 until jsonObjectResponse.length()){
                        val dataListStatusAllowed = jsonObjectResponse.getJSONObject(i)
                        val listStatusAllowed = Data_ListStatusAllowed()

                        listStatusAllowed.listStatusAllowedId = dataListStatusAllowed.getString("ID")
                        listStatusAllowed.listStatusAllowedName = dataListStatusAllowed.getString("NAME")

                        dataModelArrayListStatusAllowed.add(listStatusAllowed)
                    }
                    setupRecyclerListStatusAllowed()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "ListStatusAllowed: $e", Toast.LENGTH_LONG).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "ERROR ListStatusAllowed", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                params["ticket_id"] = ticketSortsId.toString()
                params["profile_id"] = "6"
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

    private fun setupRecyclerListStatusAllowed() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true,)
        layoutManager.stackFromEnd = true

        recyclerViewListStatusAllowed!!.layoutManager = layoutManager

        recyclerView_Adapter_ListStatusAllowed =
            RecycleViw_Adapter_ListStatusAllowed(this,dataModelArrayListStatusAllowed,this)
        recyclerViewListStatusAllowed!!.adapter = recyclerView_Adapter_ListStatusAllowed
    }

    private fun setupRecyclerListSourceTypes() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true,)
        layoutManager.stackFromEnd = true

        recyclerViewListSourceTypes!!.layoutManager = layoutManager

        recyclerView_Adapter_ListSourceTypes =
            RecycleView_Adapter_SourceTypes(this,dataModelArrayListSourceTypes,this)
        recyclerViewListSourceTypes!!.adapter = recyclerView_Adapter_ListSourceTypes
    }

    //nota:eliminar fragment de fondo
    private fun replaceFragment(misPeticionesFragment: MisPeticionesFragment) {
        val f2 = misPeticionesFragment
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutFragment, f2)
        transaction.addToBackStack(null)
        transaction.commit()

        /*val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayoutFragment,misPeticionesFragment).commit()*/

    }

    override fun onFollowupTemplateClick(nameFollowupTemplate: String, contentFollowupTemplate: String) {
        Toast.makeText(this, ""+nameFollowupTemplate, Toast.LENGTH_LONG).show()
        binding.edtFollowupDescription.setText(decodeHtml(contentFollowupTemplate))
        //Log.i("mensaje clikc","${binding.edtFollowupDescription.text}")
        binding.includeModalFollowupTemplate.modalPlantillaAddFollowup.isVisible = false
        binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = false
    }

    override fun onSelectStatusClick(listStatusAllowedId: String, listStatusAllowedName: String) {
        Toast.makeText(this, "$listStatusAllowedName seleccionado", Toast.LENGTH_SHORT).show()
        binding.btnStatusFollowup.text = listStatusAllowedName
        binding.btnStatusFollowup.tag = listStatusAllowedId

        when(listStatusAllowedName){
            "EN CURSO (Asignada)" -> binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo_verde)
            "EN CURSO (Planificación)" -> binding.imgBtnStatus.setImageResource(R.drawable.ic_calendar_range)
            "EN ESPERA" -> binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo)
            "SOLUCIONADO" -> binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo_negro)
            "CERRADO" -> binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo_negro_cerrado)
        }
        //Log.i("mensaje tag","${binding.btnStatusFollowup.tag}")
        binding.includeListStatusAllowed.modalListStatusAllowed.isVisible = false
        binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = false
    }

    override fun onSelectSourceTypesClick(sourceTypesId: String, sourceTypesName: String) {
        binding.btnSourceTypes.text = sourceTypesName
        binding.btnSourceTypes.tag = sourceTypesId
        binding.includeModalListSourceTypes.modalListSourceTypes.isVisible = false
        binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = false
    }

}