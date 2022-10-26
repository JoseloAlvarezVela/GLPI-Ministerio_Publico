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
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_ListStatusAllowed
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_FollowupTemplate
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleViw_Adapter_ListStatusAllowed
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import org.json.JSONArray
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
        //Log.i("mensaje updateFollowup", MainActivity.updateFollowup.toString())
        if (MainActivity.updateFollowup){
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
        }
    }

    private fun imgBtnPadLock(){
        val bundle = intent.extras
        var ticketPrivate = bundle!!.getString("ticketPrivate")
        var newTicketPrivate = "NO"
        var flagImgViewPadLock = false
        Log.i("mensaje type",ticketPrivate.toString())
        if (ticketPrivate == "SI"){
            Toast.makeText(this, "Segumiento Privado", Toast.LENGTH_SHORT).show()
            flagImgViewPadLock = false
            binding.imgViewPadLock.setOnClickListener {
                if(flagImgViewPadLock){
                    binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_cerrado)
                    Toast.makeText(this, "Segumiento Privado", Toast.LENGTH_SHORT).show()
                    binding.imgViewPadLock.tag = "SI"
                    flagImgViewPadLock = false
                }else{
                    binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_abierto)
                    binding.imgViewPadLock.tag = "NO"
                    flagImgViewPadLock = true
                }
            }
        }else{
            flagImgViewPadLock = true

            binding.imgViewPadLock.setOnClickListener {
                if(flagImgViewPadLock){
                    binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_cerrado)
                    Toast.makeText(this, "Segumiento Privado", Toast.LENGTH_SHORT).show()
                    newTicketPrivate = "NO"
                    flagImgViewPadLock = false
                }else{
                    binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_abierto)
                    newTicketPrivate = "SI"
                    flagImgViewPadLock = true
                }
            }
        }
    }

    private fun getTicketInfo(){
        val bundle = intent.extras
        val ticketSortsId = bundle!!.getString("ticketSortsId").toString()
        val ticketOrigin = bundle!!.getString("ticketOrigin")
        //val ticketType = bundle!!.getString("ticketType")
        val ticketStatus = bundle!!.getString("ticketStatus")

        Log.i("mensaje type",ticketStatus.toString())
        if (ticketStatus == "EN CURSO (Asignada)"){
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo_verde)
        }else if (ticketStatus == "EN ESPERA"){
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo)
        }else if (ticketStatus == "CERRADO"){
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo_negro)
        }

        binding.tvIdTicket.text = "Petición #$ticketSortsId"

        btnAddFollowup(ticketSortsId, ticketOrigin)

        imgBtnTicketStatus(ticketStatus)
    }

    private fun updateFollowup(){
        var followupStatus = ""

        val bundle = intent.extras
        val ticketSortsId = bundle!!.getString("ticketSortsId").toString()
        val ticketSortsStatus = bundle!!.getString("ticketSortsStatus")
        val ticketOrigin = bundle!!.getString("ticketOrigin")
        val ticketPrivate = bundle!!.getString("ticketPrivate")
        val tasksDescription = bundle!!.getString("tasks_description")
        var flagTicketPrivate = false

        imgBtnTicketStatus(ticketSortsStatus)
        imgBtnPadLock()
        //btnUpdateFollowup(followupStatus, ticketPrivate.toString())
        Log.i("mensaje ticketOrigin2","$ticketOrigin")
        btnAddFollowup(ticketSortsId, ticketOrigin)

        binding.tvIdTicket.text = "Petición #$ticketSortsId"
        if (ticketSortsStatus == "EN CURSO (Asignada)"){
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo_verde)
            binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_verde)
        }else{
            binding.imgBtnStatus.setImageResource(R.drawable.ic_circulo)
            binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo)
        }

        if(ticketPrivate == "SI"){
            //MainActivity.privateImgViewPadLock = 0.toString()
            binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_cerrado)
            flagTicketPrivate = false
            //Log.i("mensaje padLock","${MainActivity.privateImgViewPadLock}")
        }else{
            //MainActivity.privateImgViewPadLock = 1.toString()
            binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_abierto)
            flagTicketPrivate = true
            //Log.i("mensaje padLock","${MainActivity.privateImgViewPadLock}")
        }

        binding.edtFollowupDescription.setText(tasksDescription)

        MainActivity.flagEdit = false
    }

    private fun imgBtnTicketStatus(ticketStatus: String?): String {
        var flagTicketStatus: Boolean
        var newTicketStatus = "EN CURSO (Asignada)"
        if (ticketStatus == "EN CURSO (Asignada)"){
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

    private fun btnAddFollowup(ticketId: String, ticketOrigin: String?){
        //boton agregar seguimiento
        binding.btnAddFollowup.setOnClickListener {
            Toast.makeText(this, "Tarea añadida", Toast.LENGTH_LONG).show()
            val ticketPrivate = binding.imgViewPadLock.tag.toString()
            val followupDescription = binding.edtFollowupDescription.text

            //fecha y hora actual
            val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale("es", "PE"))//obtenemos fecha actual
            val currentdate = sdf.format(Date())
            Log.i("mensaje sendTaks",
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
                        "CONTENIDO: $followupDescription\n"+
                        "EDITOR: ${MainActivity.idUserTechnician}")
            onBackPressed()
            /*val intent_agregarSeguimiento = Intent(this, MainActivity::class.java)
            intent_agregarSeguimiento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_agregarSeguimiento)*/
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
    }

}