package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
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
import com.glpi.glpi_ministerio_pblico.ui.adapter.*
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TicketsAgregarSeguimientoActivity : AppCompatActivity(),
    RecycleView_Adapter_FollowupTemplate.onFollowTemplateClickListener,
    RecycleViw_Adapter_ListStatusAllowed.onStatusAllowedClickListener{
    private var recyclerView: RecyclerView? = null
    private var recyclerViewListStatusAllowed: RecyclerView? = null

    internal lateinit var dataModelArrayListStatusAllowed: ArrayList<Data_ListStatusAllowed>
    internal lateinit var dataModelArrayListFollowupTemplate: ArrayList<Data_FollowupTemplate>
    private var recyclerView_Adapter_FollowupTemplate: RecycleView_Adapter_FollowupTemplate? = null
    private var recyclerView_Adapter_ListStatusAllowed: RecycleViw_Adapter_ListStatusAllowed? = null
    private lateinit var binding: ActivityTicketsAgregarSeguimientoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsAgregarSeguimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //ticketInfo()
        //var followupStatus = "0"

        recyclerView = binding.includeModalFollowupTemplate.recyclerFollowupTemplate
        recyclerViewListStatusAllowed = binding.includeListStatusAllowed.recyclerFollowupListAllowed



        volleyRequestListStatusAllowed()
        volleyRequestFollowupTemplates(MainActivity.urlApi_FollowupTemplates)

        val bundle = intent.extras
        when(bundle!!.getBoolean("flagOnEditClick")){
            true -> updateFollowup()
            false -> getTicketInfo()
        }
        modalListStatusAllowed()
        appBarHeaderFollowup()
        imgBtnPadLock()
        btn_fabs_taddsegact()
        /*if (MainActivity.updateFollowup){
            appBarHeaderFollowup()
            getTicketInfo()
            imgBtnPadLock()
            btn_fabs_taddsegact()
            MainActivity.updateFollowup = false
        }else{
            appBarHeaderFollowup()
            imgBtnPadLock()
            updateFollowup()
            btn_fabs_taddsegact()
        }*/
    }

    private fun btnAddFollowup(ticketId: String){
        binding.btnAddFollowup.setOnClickListener {
            Toast.makeText(this, "Tarea añadida", Toast.LENGTH_LONG).show()
            val followupDescription = binding.edtFollowupDescription.text
            val followupPrivate = binding.imgViewPadLock.tag.toString()
            val listStatusAllowedId = binding.btnStatusFollowup.tag.toString()

            onBackPressed()

            requestVolleyAddFollowup(ticketId, listStatusAllowedId,followupPrivate,followupDescription)
        }
    }

    private fun getTicketInfo(){
        Toast.makeText(this, "getTicketInfo", Toast.LENGTH_LONG).show()
        val bundle = intent.extras
        val ticketSortsId = bundle!!.getString("ticketSortsId").toString()
        val ticketSortsStatus = bundle!!.getString("ticketSortsStatus")
        val ticketPrivate = bundle!!.getString("ticketPrivate")

        Log.i("mensaje type",ticketSortsStatus.toString())
        if (ticketSortsStatus == "EN CURSO (Asignada)"){
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo_verde)
            binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_verde)
        }else if (ticketSortsStatus == "EN ESPERA"){
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo)
            binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo)
        }else if (ticketSortsStatus == "CERRADO"){
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo_negro)
            binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_negro)
        }

        if(ticketPrivate == "SI"){
            binding.imgViewPadLock.tag = "1"
        }else{
            binding.imgViewPadLock.tag = "0"
        }

        binding.tvIdTicket.text = "Petición #$ticketSortsId"

        imgBtnTicketStatus(ticketSortsStatus)
        btnAddFollowup(ticketSortsId)
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
        Toast.makeText(this, "updateFollowup", Toast.LENGTH_SHORT).show()
        val bundle = intent.extras
        val ticketSortsId = bundle!!.getString("ticketSortsId").toString()
        val ticketSortsStatus = bundle!!.getString("ticketSortsStatus")
        val ticketPrivate = bundle!!.getString("ticketPrivate")
        val ticketInfoContent = bundle!!.getString("ticketInfoContent")

        imgBtnTicketStatus(ticketSortsStatus)
        imgBtnPadLock()

        binding.tvIdTicket.text = "Petición #$ticketSortsId"
        if (ticketSortsStatus == "EN CURSO (Asignada)"){
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo_verde)
            binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_verde)
        }else{
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo)
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

        btnAddFollowup(ticketSortsId)
        MainActivity.flagEdit = false
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

    private fun btnUpdateFollowup(followupStatus: String, ticketPrivate: String){
        //boton agregar seguimiento
        binding.btnAddFollowup.setOnClickListener {
            Toast.makeText(this, "seguiemiento añadido", Toast.LENGTH_LONG).show()
            Log.i("mensaje",
                "id de ticket: " +
                        "estado de ticket: $followupStatus\n" +
                        "Segumiento Privado: $ticketPrivate\n"+
                        "descripción ticket: ${binding.edtFollowupDescription.text}")
            val intent_agregarSeguimiento = Intent(this, MainActivity::class.java)
            intent_agregarSeguimiento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_agregarSeguimiento)
        }
    }

    //INICIO - funcion de maneja los botones del header
    private fun appBarHeaderFollowup() {
        //boton atras
        binding.btnAtrasActtaddseg.setOnClickListener {
            onBackPressed()
        }
        //btnAddFollowup()
    }





    private fun requestVolleyAddFollowup(
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
                params["request_type"] = "4"
                params["ticket_state"] = listStatusAllowedId
                params["ticket_id"] = ticketId

                return params
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequestDataTickets)
        //FIN obtenemos perfil de usuario
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
            binding.includeListStatusAllowed.modalListStatusAllowed.isVisible = false
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

    override fun onFollowupTemplateClick(nameFollowupTemplate: String, contentFollowupTemplate: String) {
        Toast.makeText(this, ""+nameFollowupTemplate, Toast.LENGTH_LONG).show()
        binding.edtFollowupDescription.setText(decodeHtml(contentFollowupTemplate))
        Log.i("mensaje clikc","${binding.edtFollowupDescription.text}")
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
        Log.i("mensaje tag","${binding.btnStatusFollowup.tag}")
        binding.includeListStatusAllowed.modalListStatusAllowed.isVisible = false
        binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = false
    }

}