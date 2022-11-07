package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.data.database.TicketInfoDB
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsAgregarSolucionBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.*
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class TicketsAgregarSolucionActivity : AppCompatActivity(),
    RecycleView_Adapter_SolutionTemplate.onSolutionTemplateClickListener,
    RecycleView_Adapter_SolutionType.onSolutionTypeClickListener{
    lateinit var binding: ActivityTicketsAgregarSolucionBinding //declaramos binding para acceder variables

    private var recyclerViewSolutionTemplate: RecyclerView? = null
    private var recyclerViewSolutionType: RecyclerView? = null
    internal lateinit var dataModelArraySolutionTemplate: ArrayList<Data_SolutionTemplate>
    internal lateinit var dataModelArraySolutionType: ArrayList<Data_SolutionType>
    private var recyclerViewView_Adapter_SolutionTemplate: RecycleView_Adapter_SolutionTemplate? = null
    private var recyclerViewView_Adapter_SolutionType: RecycleView_Adapter_SolutionType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsAgregarSolucionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewSolutionTemplate = binding.includeModalSolutionTemplate.recyclerSolutionTemplate
        recyclerViewSolutionType = binding.includeModalSolutionType.recyclerSolutionType

        volleyRequestSolutionTemplate()
        volleyRequestSolutionType()

        val bundle = intent.extras
        when(bundle!!.getBoolean("flagOnEditClick")){
            true -> updateSolution()
            false -> insertSolution()
        }

        btn_fabs()
        btn_header()

    }

    private fun updateSolution() {

    }

    private fun btnAddSolution(flagUpdateSolution: Boolean, ticketSortsId: String, ticketInfoId: String) {
        binding.btnAddSolution.setOnClickListener {
            val solutionDescription = binding.edtSolutionDescription.text.toString()
            val solutionType = binding.btnSolutionType.tag.toString()
            Toast.makeText(this, flagUpdateSolution.toString(), Toast.LENGTH_SHORT).show()
            when{
                !flagUpdateSolution -> {
                    requestVolleyInsertSolution(solutionDescription,solutionType)
                    val ticketSortsStatus = "6"
                    val room =
                        Room.databaseBuilder(this, TicketInfoDB::class.java, "ticketInfoBD").build()
                    Toast.makeText(
                        this,
                        "${token.prefer.getTicketSortsId()} : $ticketSortsStatus",
                        Toast.LENGTH_SHORT
                    ).show()
                    lifecycleScope.launch {
                        room.daoTicketInfo()
                            .updateTicketInfo(token.prefer.getTicketSortsId(), ticketSortsStatus)
                        val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
                        for (element in getTicketInfoDB) {
                            Log.i(
                                "mensaje ticketSolution",
                                "$element\n"
                            )
                        }
                    }
                    MainActivity.updateFragmentFlag = true
                    val ticketSortsStatusString = "CERRADO"
                    val intentOnBack = Intent(this, NavFooterTicketsActivity::class.java)
                    intentOnBack.putExtra("ticketSortsStatus",ticketSortsStatusString)
                    intentOnBack.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intentOnBack)
                }
            }
        }
    }

    private fun insertSolution(){
        var ticketSortsId = ""
        var ticketSortsType = ""
        var ticketSortsStatus = ""
        var ticketPrivate = ""
        var ticketInfoId = ""
        val flagUpdateSolution = false
        val room =
            Room.databaseBuilder(this, TicketInfoDB::class.java, "ticketInfoBD").build()
        lifecycleScope.launch {
            val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
            for (item in getTicketInfoDB) {
                ticketSortsId = item.ticketSortsID
                ticketSortsType = item.ticketSortsType
                ticketSortsStatus = item.ticketSortsStatus
                binding.edtSolutionDescription.tag = "0"
                //ticketPrivate = binding.imgViewPadLock.tag.toString()

                binding.btnSolutionType.setOnClickListener {
                    binding.includeModalSolutionType.modalTemplateSolutionType.isVisible = true
                    binding.includeBackgroundGris.clBackgroundgrisBggris.isVisible = true
                    binding.lyFabsAtctaddsol.isVisible = false

                }

                when(ticketSortsType){
                    "SOLICITUD" -> binding.imgBtnTypeSolution.setImageResource(R.drawable.ic_interrogacion)
                    "INCIDENCIA" -> binding.imgBtnTypeSolution.setImageResource(R.drawable.ic_incidencia)
                }

                when (ticketSortsStatus) {
                    "EN CURSO (Asignada)" -> {
                        binding.imgBtnStatusSolutionHeader.tag = "2"
                        binding.imgBtnStatusSolutionHeader.setImageResource(R.drawable.ic_circulo_verde)
                    }
                    "2" -> {
                        binding.imgBtnStatusSolutionHeader.tag = "2"
                        binding.imgBtnStatusSolutionHeader.setImageResource(R.drawable.ic_circulo_verde)
                    }
                    "EN ESPERA" -> {
                        binding.imgBtnStatusSolutionHeader.tag = "4"
                        binding.imgBtnStatusSolutionHeader.setImageResource(R.drawable.ic_circulo)
                    }
                    "4" -> {
                        binding.imgBtnStatusSolutionHeader.tag = "4"
                        binding.imgBtnStatusSolutionHeader.setImageResource(R.drawable.ic_circulo)
                    }
                    "CERRADO" -> {
                        binding.imgBtnStatusSolutionHeader.setImageResource(R.drawable.ic_circulo_negro)
                        binding.imgBtnStatusSolutionHeader.tag = "6"
                    }
                }

                binding.tvIdTicket.text = "Petición #$ticketSortsId"
                btnAddSolution(flagUpdateSolution, ticketSortsId, ticketInfoId)
            }
        }
    }
    //INICIO - funcion que contiene botones en la cabecera del layout
    private fun btn_header() {
        binding.btnAtrasActtaddsol.setOnClickListener {
            onBackPressed()
        }
        //boton agregar solución
        binding.btnAddSolution.setOnClickListener {
            Toast.makeText(this, "solución añadida", Toast.LENGTH_LONG).show()
            val intent_agregarSolución = Intent(this, MainActivity::class.java)
            intent_agregarSolución.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_agregarSolución)
        }
    }
    //FIN - funcion que contiene botones en la cabecera del layout

    //INICIO - fabs que abre camara del celular y archivos del celular
    private fun btn_fabs() {
        var click_desplegar = false
        //archivos del celular
        binding.fabArchivoActtaddsol.setOnClickListener {
            Toast.makeText(this, "abrir archivos del celular", Toast.LENGTH_SHORT).show()
        }
        //camara de celular
        binding.fabFotoActtaddsol.setOnClickListener {
            Toast.makeText(this, "abrir camara de celular", Toast.LENGTH_SHORT).show()
        }
        /*modal plantilla
        binding.fabPlantillaActtaddsol.setOnClickListener {
            Toast.makeText(this, "abrir plantilla de solución", Toast.LENGTH_SHORT).show()
        }*/
        binding.btnFabSolutionTemplate.setOnClickListener {
            binding.includeBackgroundGris.clBackgroundgrisBggris.isVisible = true
            binding.includeModalSolutionTemplate.modalTemplateSolution.isVisible = true
            binding.lyFabsAtctaddsol.isVisible = false
        }
        //boton para desplegar y plegar los fabs
        binding.fabDesplegarAddsol.setOnClickListener {
            if (click_desplegar == false){
                binding.includeBackgroundGris.clBackgroundgrisBggris.isVisible = true // fondo gris
                binding.lyFabsAtctaddsol.isVisible = true
                click_desplegar = true
            }else{
                binding.lyFabsAtctaddsol.isVisible = false
                binding.includeBackgroundGris.clBackgroundgrisBggris.isVisible = false // fondo gris
                click_desplegar = false
            }
        }
        //fondo gris para cerra modal
        binding.includeBackgroundGris.clBackgroundgrisBggris.setOnClickListener {
            binding.lyFabsAtctaddsol.isVisible = false
            binding.includeBackgroundGris.clBackgroundgrisBggris.isVisible = false // fondo gris
            click_desplegar = false
        }
    }

    private fun requestVolleyInsertSolution(solutionDescription: String, solutionType: String) {
        val room =
            Room.databaseBuilder(this, TicketInfoDB::class.java, "ticketInfoBD").build()
        lifecycleScope.launch {
            val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
            for (item in getTicketInfoDB){
                val ticketSortsId = item.ticketSortsID
                //metodo que nos devuelve los datos para los tickets
                val stringRequestDataTickets = object : StringRequest(Method.POST,
                    MainActivity.urlApi_InsertSolution+ticketSortsId, Response.Listener { response ->
                        try {
                            val dataAddFollowup = JSONObject(response) //obtenemos el objeto json

                            Log.i("mensaje","$dataAddFollowup")

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(applicationContext, "token expirado_: $e", Toast.LENGTH_LONG).show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(applicationContext, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
                    }) {
                    override fun getParams(): Map<String, String>? {
                        val params: MutableMap<String, String> = HashMap()
                        params["session_token"] = token.prefer.getToken()
                        params["content"] = solutionDescription
                        params["solution_type"] = solutionType

                        Log.i("mensaje params","$params")
                        return params
                    }
                }
                VolleySingleton.getInstance(applicationContext).addToRequestQueue(stringRequestDataTickets)
                //FIN obtenemos perfil de usuario
            }
        }
    }

    private fun volleyRequestSolutionTemplate() {
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_SolutionTemplate, Response.Listener { response ->
                try {
                    dataModelArraySolutionTemplate = ArrayList()
                    val jsonObjectResponse = JSONArray(response)
                    for(i in  0 until jsonObjectResponse.length()){
                        var dataSolutionTemplate = jsonObjectResponse.getJSONObject(i)
                        var solutionTemplate = Data_SolutionTemplate()

                        solutionTemplate.solutionTemplateId = dataSolutionTemplate.getString("ID")
                        solutionTemplate.solutionTemplateName = dataSolutionTemplate.getString("NAME")
                        solutionTemplate.solutionTemplateContent = dataSolutionTemplate.getString("CONTENIDO")

                        dataModelArraySolutionTemplate.add(solutionTemplate)
                    }
                    setupRecyclerSolutionTemplate()
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

    private fun volleyRequestSolutionType() {
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_SolutionType, Response.Listener { response ->
                try {
                    dataModelArraySolutionType = ArrayList()
                    val jsonObjectResponse = JSONArray(response)
                    for(i in  0 until jsonObjectResponse.length()){
                        var dataSolutionType = jsonObjectResponse.getJSONObject(i)
                        var solutionType = Data_SolutionType()

                        solutionType.solutionTypeId = dataSolutionType.getString("ID")
                        solutionType.solutionTypeName = dataSolutionType.getString("NAME")

                        dataModelArraySolutionType.add(solutionType)
                    }
                    setupRecyclerSolutionType()
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

    private fun setupRecyclerSolutionTemplate() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true,)
        layoutManager.stackFromEnd = true

        recyclerViewSolutionTemplate!!.layoutManager = layoutManager
        recyclerViewView_Adapter_SolutionTemplate =
            RecycleView_Adapter_SolutionTemplate(this,dataModelArraySolutionTemplate,this)
        recyclerViewSolutionTemplate!!.adapter = recyclerViewView_Adapter_SolutionTemplate
    }

    private fun setupRecyclerSolutionType() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true,)
        layoutManager.stackFromEnd = true

        recyclerViewSolutionType!!.layoutManager = layoutManager
        recyclerViewView_Adapter_SolutionType =
            RecycleView_Adapter_SolutionType(this,dataModelArraySolutionType,this)
        recyclerViewSolutionType!!.adapter = recyclerViewView_Adapter_SolutionType
    }

    override fun onSolutionTemplateClick(
        solutionTemplateId: String,
        solutionTemplateName: String,
        solutionTemplateContent: String
    ) {
        binding.edtSolutionDescription.setText(MainActivity.decodeHtml(solutionTemplateContent))
        binding.edtSolutionDescription.tag = solutionTemplateId

        binding.includeModalSolutionTemplate.modalTemplateSolution.isVisible = false
        binding.includeBackgroundGris.clBackgroundgrisBggris.isVisible = false

    }

    override fun onSolutionTypeClick(solutionTemplateId: String, solutionTemplateName: String) {
        binding.btnSolutionType.text = MainActivity.decodeHtml(solutionTemplateName)
        binding.btnSolutionType.tag = solutionTemplateId

        binding.includeModalSolutionType.modalTemplateSolutionType.isVisible = false
        binding.includeBackgroundGris.clBackgroundgrisBggris.isVisible = false

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val ticketSortsStatusString = "SIN_CAMBIO_DE_ESTADO"
        val intentOnBackPressed = Intent(this, NavFooterTicketsActivity::class.java)
        intentOnBackPressed.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intentOnBackPressed.putExtra("ticketSortsStatus",ticketSortsStatusString)
        startActivity(intentOnBackPressed)

    }
}