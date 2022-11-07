package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.decodeHtml
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.data.database.TicketInfoDB
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsAgregarSeguimientoBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.*
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class TicketsAgregarSeguimientoActivity : AppCompatActivity(),
    RecycleView_Adapter_FollowupTemplate.onFollowTemplateClickListener,
    RecycleViw_Adapter_ListStatusAllowed.onStatusAllowedClickListener {
    private var recyclerView: RecyclerView? = null
    private var recyclerViewListStatusAllowed: RecyclerView? = null
    private var recyclerViewListSourceTypes: RecyclerView? = null

    internal lateinit var dataModelArrayListStatusAllowed: ArrayList<Data_ListStatusAllowed>
    internal lateinit var dataModelArrayListFollowupTemplate: ArrayList<Data_FollowupTemplate>

    private var recyclerView_Adapter_FollowupTemplate: RecycleView_Adapter_FollowupTemplate? = null
    private var recyclerView_Adapter_ListStatusAllowed: RecycleViw_Adapter_ListStatusAllowed? = null


    private lateinit var binding: ActivityTicketsAgregarSeguimientoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsAgregarSeguimientoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.includeModalFollowupTemplate.recyclerFollowupTemplate
        recyclerViewListStatusAllowed = binding.includeListStatusAllowed.recyclerFollowupListAllowed
        recyclerViewListSourceTypes = binding.includeModalListSourceTypes.recyclerListSourceTypes

        volleyRequestListStatusAllowed()
        volleyRequestFollowupTemplates()

        val bundle = intent.extras
        when (bundle!!.getBoolean("flagOnEditClick")) {
            true -> updateFollowup()
            false -> getInfoFollowup()
        }
        modalListStatusAllowed()
        modalListSourceTypes()
        appBarHeaderFollowup()
        imgBtnPadLock()
        btnDeployFab()
    }

    private fun btnAddFollowup(flagUpdateFollow: Boolean, ticketId: String, ticketInfoId: String) {
        binding.btnAddFollowup.setOnClickListener {
            //Toast.makeText(this, "Tarea añadida", Toast.LENGTH_LONG).show()
            val followupDescription = binding.edtFollowupDescription.text
            val followupPrivate = binding.imgViewPadLock.tag.toString()
            val listStatusAllowedId = binding.btnStatusFollowup.tag.toString()
            val requestType = "4" //ESTO DEBE SETEAR ISAAC EN BACKEND

            when {
                followupDescription.isNullOrBlank() -> Toast.makeText(
                    this,
                    "sin descripción",
                    Toast.LENGTH_SHORT
                ).show()

                flagUpdateFollow -> {
                    requestVolleyUpdateFollowup(
                        requestType,
                        ticketInfoId,
                        ticketId,
                        listStatusAllowedId,
                        followupPrivate,
                        followupDescription
                    )
                    val ticketSortsStatus = binding.btnStatusFollowup.tag.toString()
                    val room =
                        Room.databaseBuilder(this, TicketInfoDB::class.java, "ticketInfoBD").build()
                    /*Toast.makeText(
                        this,
                        "${prefer.getTicketSortsId()} : $ticketSortsStatus",
                        Toast.LENGTH_SHORT
                    ).show()*/
                    lifecycleScope.launch {
                        room.daoTicketInfo()
                            .updateTicketInfo(prefer.getTicketSortsId(), ticketSortsStatus)
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
                !flagUpdateFollow -> {
                    requestVolleyInsertFollowup(
                        requestType,
                        ticketId,
                        listStatusAllowedId,
                        followupPrivate,
                        followupDescription
                    )
                    val ticketSortsStatus = binding.btnStatusFollowup.tag.toString()
                    val room =
                        Room.databaseBuilder(this, TicketInfoDB::class.java, "ticketInfoBD").build()
                    /*Toast.makeText(
                        this,
                        "${prefer.getTicketSortsId()} : $ticketSortsStatus",
                        Toast.LENGTH_SHORT
                    ).show()*/
                    lifecycleScope.launch {
                        room.daoTicketInfo()
                            .updateTicketInfo(prefer.getTicketSortsId(), ticketSortsStatus)
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
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getInfoFollowup() {
        var ticketSortsId = ""
        var ticketSortsStatus = ""
        var ticketPrivate = ""
        var ticketInfoId = ""
        val flagGetTicketInfo = false
        val room =
            Room.databaseBuilder(this, TicketInfoDB::class.java, "ticketInfoBD").build()
        lifecycleScope.launch {
            val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
            for (item in getTicketInfoDB) {
                ticketSortsId = item.ticketSortsID
                ticketSortsStatus = item.ticketSortsStatus
                ticketPrivate = binding.imgViewPadLock.tag.toString()

                when (ticketSortsStatus) {
                    "EN CURSO (Asignada)" -> {
                        val imagen = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_circulo_verde
                        )
                        val drawables: Array<Drawable> = binding.btnStatusFollowup.compoundDrawables
                        binding.btnStatusFollowup.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusFollowup.text = "EN CURSO (Asignada)"
                        binding.btnStatusFollowup.tag = "2"
                        binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_verde)
                    }
                    "2" -> {
                        val imagen = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_circulo_verde
                        )
                        val drawables: Array<Drawable> = binding.btnStatusFollowup.compoundDrawables
                        binding.btnStatusFollowup.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusFollowup.text = "EN CURSO (Asignada)"
                        binding.btnStatusFollowup.tag = "2"
                        binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_verde)
                    }
                    "EN ESPERA" -> {
                        val imagen =
                            ContextCompat.getDrawable(applicationContext, R.drawable.ic_circulo)
                        val drawables: Array<Drawable> = binding.btnStatusFollowup.compoundDrawables
                        binding.btnStatusFollowup.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusFollowup.text = "EN ESPERA"
                        binding.btnStatusFollowup.tag = "4"
                        binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo)
                    }
                    "4" -> {
                        val imagen =
                            ContextCompat.getDrawable(applicationContext, R.drawable.ic_circulo)
                        val drawables: Array<Drawable> = binding.btnStatusFollowup.compoundDrawables
                        binding.btnStatusFollowup.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusFollowup.text = "EN ESPERA"
                        binding.btnStatusFollowup.tag = "4"
                        binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo)
                    }
                    "CERRADO" -> {
                        val imagen = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_circulo_negro
                        )
                        val drawables: Array<Drawable> = binding.btnStatusFollowup.compoundDrawables
                        binding.btnStatusFollowup.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_negro)
                    }
                }
                when (ticketPrivate) {
                    null -> binding.imgViewPadLock.tag = "0"
                    "SI" -> binding.imgViewPadLock.tag = "1"
                    "NO" -> binding.imgViewPadLock.tag = "0"
                }
                binding.tvIdTicket.text = "Petición #$ticketSortsId"
                btnAddFollowup(flagGetTicketInfo, ticketSortsId, ticketInfoId)
            }
        }
    }

    private fun modalListStatusAllowed() {
        binding.btnStatusFollowup.setOnClickListener {
            binding.includeListStatusAllowed.modalListStatusAllowed.isVisible = true
            binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = true
        }

        binding.includeListStatusAllowed.btnCloseModalListStatusAllowed.setOnClickListener {
            binding.includeListStatusAllowed.modalListStatusAllowed.isVisible = false
            binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = false
        }
    }

    private fun modalListSourceTypes() {
        binding.btnSourceTypes.setOnClickListener {
            binding.includeModalListSourceTypes.modalListSourceTypes.isVisible = true
            binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = true
        }

        binding.includeModalListSourceTypes.btnCloseModalSourceTypes.setOnClickListener {
            binding.includeModalListSourceTypes.modalListSourceTypes.isVisible = false
            binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = false
        }
    }

    private fun imgBtnPadLock() {
        val bundle = intent.extras
        var ticketPrivate = bundle!!.getString("ticketPrivate")
        var newTicketPrivate = "NO"
        var flagImgViewPadLock = false
        Log.i("mensaje type", ticketPrivate.toString())
        if (ticketPrivate == "SI") {
            //Toast.makeText(this, "Segumiento Privado", Toast.LENGTH_SHORT).show()
            flagImgViewPadLock = false
            binding.imgViewPadLock.setOnClickListener {
                if (flagImgViewPadLock) {
                    binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_cerrado)

                    binding.imgViewPadLock.tag = "1"
                    flagImgViewPadLock = false
                } else {
                    binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_abierto)
                    binding.imgViewPadLock.tag = "0"
                    flagImgViewPadLock = true
                }
            }
        } else {
            flagImgViewPadLock = true

            binding.imgViewPadLock.setOnClickListener {
                if (flagImgViewPadLock) {
                    binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_cerrado)
                    Toast.makeText(this, "Segumiento Privado", Toast.LENGTH_SHORT).show()
                    binding.imgViewPadLock.tag = "1"
                    newTicketPrivate = "NO"
                    flagImgViewPadLock = false
                } else {
                    binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_abierto)
                    binding.imgViewPadLock.tag = "0"
                    newTicketPrivate = "SI"
                    flagImgViewPadLock = true
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateFollowup() {
        var ticketSortsId = ""
        var ticketSortsStatus = ""
        val flagUpdateFollow = true

        val bundle = intent.extras
        val ticketPrivate = bundle!!.getString("ticketPrivate")
        val ticketInfoContent = bundle!!.getString("ticketInfoContent")
        val ticketInfoId = bundle!!.getString("ticketInfoId").toString()
        val ticketInfoSource = bundle!!.getString("ticketInfoSource").toString()

        val room = Room.databaseBuilder(this, TicketInfoDB::class.java, "ticketInfoBD").build()
        lifecycleScope.launch {
            val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
            for (item in getTicketInfoDB) {
                ticketSortsId = item.ticketSortsID
                ticketSortsStatus = item.ticketSortsStatus

                binding.tvIdTicket.text = "Petición #$ticketSortsId"

                Log.i("mensaje sort", ticketSortsStatus)
                when (ticketSortsStatus) {
                    "EN CURSO (Asignada)" -> {
                        val imagen = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_circulo_verde
                        )
                        val drawables: Array<Drawable> = binding.btnStatusFollowup.compoundDrawables
                        binding.btnStatusFollowup.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusFollowup.text = "EN CURSO (Asignada)"
                        binding.btnStatusFollowup.tag = "2"
                        binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_verde)
                    }
                    "2" -> {
                        val imagen = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_circulo_verde
                        )
                        val drawables: Array<Drawable> = binding.btnStatusFollowup.compoundDrawables
                        binding.btnStatusFollowup.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusFollowup.text = "EN CURSO (Asignada)"
                        binding.btnStatusFollowup.tag = "2"
                        binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_verde)
                    }
                    "EN ESPERA" -> {
                        val imagen =
                            ContextCompat.getDrawable(applicationContext, R.drawable.ic_circulo)
                        val drawables: Array<Drawable> = binding.btnStatusFollowup.compoundDrawables
                        binding.btnStatusFollowup.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusFollowup.text = "EN ESPERA"
                        binding.btnStatusFollowup.tag = "4"
                        binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo)
                    }
                    "4" -> {
                        val imagen =
                            ContextCompat.getDrawable(applicationContext, R.drawable.ic_circulo)
                        val drawables: Array<Drawable> = binding.btnStatusFollowup.compoundDrawables
                        binding.btnStatusFollowup.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.btnStatusFollowup.text = "EN ESPERA"
                        binding.btnStatusFollowup.tag = "4"
                        binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo)
                    }
                    "CERRADO" -> {
                        val imagen = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_circulo_negro
                        )
                        val drawables: Array<Drawable> = binding.btnStatusFollowup.compoundDrawables
                        binding.btnStatusFollowup.setCompoundDrawablesWithIntrinsicBounds(
                            drawables[0],
                            drawables[0],
                            imagen,
                            drawables[0]
                        )
                        binding.imgBtnStatusHeader.setImageResource(R.drawable.ic_circulo_negro)
                    }
                }
            }
        }

        binding.btnSourceTypes.text = ticketInfoSource
        when (ticketInfoSource) {
            "Correo electrónico" -> binding.btnSourceTypes.tag = "2"
            "Documento" -> binding.btnSourceTypes.tag = "5"
            "Formcreator" -> binding.btnSourceTypes.tag = "8"
            "Teléfono" -> binding.btnSourceTypes.tag = "3"
            "Whatsapp" -> binding.btnSourceTypes.tag = "4"
        }

        when (ticketPrivate) {
            "SI" -> {
                binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_cerrado)
                binding.imgViewPadLock.tag = "1"
            }
            "NO" -> {
                binding.imgViewPadLock.setImageResource(R.drawable.ic_candado_abierto)
                binding.imgViewPadLock.tag = "0"
            }
        }

        binding.edtFollowupDescription.setText(ticketInfoContent)
        binding.btnStatusFollowup.text = ticketSortsStatus
        when (ticketSortsStatus) {
            "EN CURSO (Asignada)" -> binding.btnStatusFollowup.tag = "2"
            "EN CURSO (Planificación)" -> binding.btnStatusFollowup.tag = "3"
            "EN ESPERA" -> binding.btnStatusFollowup.tag = "4"
            "SOLUCIONADO" -> binding.btnStatusFollowup.tag = "5"
        }

        btnAddFollowup(flagUpdateFollow, ticketSortsId, ticketInfoId)
        //MainActivity.flagEdit = false
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
        Log.i("mensaje link", "${MainActivity.urlApi_UpdateFollowup + ticketInfoId}")
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_UpdateFollowup + ticketInfoId, Response.Listener { response ->
                try {
                    val dataAddFollowup = JSONObject(response) //obtenemos el objeto json

                    Log.i("mensaje", "$dataAddFollowup")

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

                    Log.i("mensaje", "$dataAddFollowup")

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

    //INICIO - funcion de fabs que abre camara del celular y archivos del celular
    private fun btnDeployFab() {
        var click_desplegar = false
        //boton para desplegar y plegar los fabs
        binding.fabDesplegarAddseg.setOnClickListener {
            if (click_desplegar == false) {
                binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = true
                binding.lyFabsActtaddseg.isVisible = true
                click_desplegar = true
            } else {
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

    private fun volleyRequestFollowupTemplates() {
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_FollowupTemplates, Response.Listener { response ->
                try {
                    dataModelArrayListFollowupTemplate = ArrayList()

                    val jsonObjectResponse = JSONArray(response)
                    var iterador = 0
                    for (i in 0 until jsonObjectResponse.length()) {
                        val nTemplate = jsonObjectResponse.getJSONObject(i)
                        val player = Data_FollowupTemplate()
                        player.setnameFollowupTemplates(nTemplate.getString("NOMBRE"))
                        player.setcontentFollowupTemplates(nTemplate.getString("CONTENIDO"))
                        //iterador++
                        Log.i("mensaje posicion", "" + nTemplate.getString("NOMBRE"))
                        Log.i("mensaje posicion", "" + jsonObjectResponse.length())
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

    private fun setupRecycler() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true

        recyclerView!!.layoutManager = layoutManager

        recyclerView_Adapter_FollowupTemplate =
            RecycleView_Adapter_FollowupTemplate(this, dataModelArrayListFollowupTemplate, this)
        recyclerView!!.adapter = recyclerView_Adapter_FollowupTemplate
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

    override fun onFollowupTemplateClick(
        nameFollowupTemplate: String,
        contentFollowupTemplate: String
    ) {
        Toast.makeText(this, "" + nameFollowupTemplate, Toast.LENGTH_LONG).show()
        binding.edtFollowupDescription.setText(decodeHtml(contentFollowupTemplate))
        //Log.i("mensaje clikc","${binding.edtFollowupDescription.text}")
        binding.includeModalFollowupTemplate.modalPlantillaAddFollowup.isVisible = false
        binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = false
    }

    override fun onSelectStatusClick(listStatusAllowedId: String, listStatusAllowedName: String) {
        //Toast.makeText(this, "$listStatusAllowedName seleccionado", Toast.LENGTH_SHORT).show()
        binding.btnStatusFollowup.text = listStatusAllowedName
        binding.btnStatusFollowup.tag = listStatusAllowedId

        when (binding.btnStatusFollowup.text) {
            "EN CURSO (Asignada)" -> {
                val imagen =
                    ContextCompat.getDrawable(applicationContext, R.drawable.ic_circulo_verde)
                var drawables: Array<Drawable> = binding.btnStatusFollowup.compoundDrawables
                binding.btnStatusFollowup.setCompoundDrawablesWithIntrinsicBounds(
                    drawables[0],
                    drawables[0],
                    imagen,
                    drawables[0]
                )
                binding.btnStatusFollowup.tag = "2"
            }
            "EN ESPERA" -> {
                val imagen = ContextCompat.getDrawable(applicationContext, R.drawable.ic_circulo)
                var drawables: Array<Drawable> = binding.btnStatusFollowup.compoundDrawables
                binding.btnStatusFollowup.setCompoundDrawablesWithIntrinsicBounds(
                    drawables[0],
                    drawables[0],
                    imagen,
                    drawables[0]
                )
                binding.btnStatusFollowup.tag = "4"
            }
        }
        binding.includeListStatusAllowed.modalListStatusAllowed.isVisible = false
        binding.includeBackgroundgrisAtctaddseg.clBackgroundgrisBggris.isVisible = false
    }
}