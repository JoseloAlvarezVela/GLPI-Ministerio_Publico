package com.glpi.glpi_ministerio_pblico.ui.tickets

//import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.flag
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TasksUsers
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.data.database.TicketInfoDB
import com.glpi.glpi_ministerio_pblico.databinding.ActivityNavFooterTicketsBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_TicketInfo
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecyclerAdapter
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class NavFooterTicketsActivity : AppCompatActivity(),RecyclerAdapter.onConversationClickListener {
    private lateinit var binding: ActivityNavFooterTicketsBinding
    private lateinit var jsonObjectResponse: JSONObject

    /*creamos la lista de arreglos que tendrá los objetos de la clase Data_Tickets
   esta lista de arreglos (dataModelArrayList) funcionará como fuente de datos*/
    internal lateinit var dataModelArrayListConversation: ArrayList<Data_TicketInfo>
    //internal lateinit var dataModelArrayListConverzation: ArrayList<Data_Tickets>

    private lateinit var jsonArrayIdRecipient: JSONArray
    private lateinit var jsonArrayIdTechnician: JSONArray
    private lateinit var jsonArrayIdRequester: JSONArray

    lateinit private var progressBarTicketConversation: ProgressBar

    lateinit var ticketId: String
    //lateinit var ticketType: String
    lateinit var ticketOrigin: String

    lateinit var adapter: RecyclerAdapter

    //INICIO toogle buton tickets
    var clickTickets: Boolean = false
    var clickConvezaciones: Boolean = false
    var click_fab:Boolean = false //fab_opciones de layout activity_tickets_historico.xml

    var isEditFollowup = false
    var positionFollowup = -1

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavFooterTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        progressBarTicketConversation = binding.progressBarTicketConversation

        volleyRequestIdRecipient() //datos del operador
        //volleyRequestTicketInfo(urlApi_TicketID)

        if(MainActivity.updateFragmentFlag){
            Log.i("mensaje replace","fragemn ")
            replaceFragment(TicketInfoFragment())
            MainActivity.updateFragmentFlag = false
        }


        activityHeader()
        btnPetition()
        btnFabs()
        toggleButtonFooter()

        getTicketsConversationInfo()
    }

    //nota:eliminar fragment de fondo
    private fun replaceFragment(ticketInfoFragment: TicketInfoFragment) {
        val f2 = ticketInfoFragment
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.ticketInfoFragment, f2)
        transaction.addToBackStack(null)
        transaction.commit()

        /*val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayoutFragment,misPeticionesFragment).commit()*/

    }

    private fun btnFabs(){
        //INICIO fab_opciones de layout activity_tickets_historico.xml
        binding.includeFabs.fabDesplegarOp.setOnClickListener {
            if (!click_fab){
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
            progressBarTicketConversation.isVisible = true
            binding.fragmentTicketInfoLayout.isVisible = false

            val flagOnEditClick = false
            val intentAddTask = Intent(this, TicketsAgregarTareaActivity::class.java)

            intentAddTask.putExtra("flagOnEditClick", flagOnEditClick)//TODO: AGREGAR A LOS DEMAS

            intentAddTask.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentAddTask)

            hideFabs()
        }
        binding.includeFabs.btnFabSeguimiento.setOnClickListener {
            progressBarTicketConversation.isVisible = true
            binding.fragmentTicketInfoLayout.isVisible = false

            val flagOnEditClick = false
            val intentAddFollowup = Intent(this, TicketsAgregarSeguimientoActivity::class.java)
            intentAddFollowup.putExtra("flagOnEditClick", flagOnEditClick)//TODO: AGREGAR A LOS DEMAS
            intentAddFollowup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentAddFollowup)

            hideFabs()
        }
        binding.includeFabs.btnFabSolucion.setOnClickListener {
            val bundleIn = intent.extras
            val ticketSortsId = bundleIn!!.getString("ticketSortsId")
            val intentAddSolution = Intent(this, TicketsAgregarSolucionActivity::class.java)
            val bundle = Bundle()
            bundle.putString("TicketID", ticketSortsId)
            intentAddSolution.putExtras(bundle)
            intentAddSolution.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentAddSolution)

            hideFabs()
        }
        binding.includeFabs.btnFabDocumentos.setOnClickListener {
            val bundleIn = intent.extras
            val ticketSortsId = bundleIn!!.getString("ticketSortsId")
            val flagBtnFabDocuments = "true"
            val intentAddDocument = Intent(this, TicketsAgregarDocumentosActivity::class.java)
            val bundle = Bundle()
            bundle.putString("TicketID", ticketSortsId)
            bundle.putString("flagBtnFabDocuments", flagBtnFabDocuments)
            intentAddDocument.putExtras(bundle)
            intentAddDocument.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentAddDocument)

            hideFabs()
        }
        //fin eventos click de fab_opciones
    }

    fun hideFabs(){
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
                //binding.ticketConversation.isVisible = false
                binding.fragmentTicketInfoLayout.isVisible = false
                binding.includeFabs.linearLayoutFabs.isVisible = false
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
                //binding.ticketConversation.isVisible = true
                binding.fragmentTicketInfoLayout.isVisible = true
                binding.includeFabs.linearLayoutFabs.isVisible = true
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



    fun setupRecyclerView(){
        val recyclerViewNews = binding.recyclerViewConversation
        //val newsList = dataModelArrayListConversation
        adapter = RecyclerAdapter(this,dataModelArrayListConversation,this)
        //val newsAdapter = RecyclerAdapter(this,dataModelArrayListConversation,this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true

        dataModelArrayListConversation.sortBy { it.ticketInfoDate}//ordenar por fecha
        //recyclerViewNews.adapter = newsAdapter
        recyclerViewNews.adapter = adapter
        recyclerViewNews.layoutManager = layoutManager

        //adapter.notifyItemInserted(0)
        //newsAdapter.notifyItemChanged(positionFollowup)

        if (adapter.itemCount == 0){
            binding.recyclerViewConversation.isVisible = false
            binding.includeTicketsHistorico.layoutHistorico.isVisible = true
        }
        //recyclerViewNews.setHasFixedSize(true)
    }

    @SuppressLint("SetTextI18n")
    private fun getTicketsConversationInfo(){

        val room  = Room.databaseBuilder(this, TicketInfoDB::class.java,"ticketInfoBD").build()
        lifecycleScope.launch{
            val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
            for (item in getTicketInfoDB){
                binding.includeNavHeaderTickets.txtTicketID.text = "Petición #${item.ticketSortsID}"
                when(item.ticketSortsStatus){
                    "EN CURSO (Asignada)" -> binding.includeNavHeaderTickets.txtTicketEstado.setBackgroundResource(R.drawable.ic_circulo_verde)
                    "2" -> {
                        binding.includeNavHeaderTickets.txtTicketEstado.setBackgroundResource(R.drawable.ic_circulo_verde)
                        binding.includeNavHeaderTickets.txtTicketEstado.tag = "2"
                    }
                    "EN ESPERA" -> binding.includeNavHeaderTickets.txtTicketEstado.setBackgroundResource(R.drawable.ic_circulo)
                    "4" -> {
                        binding.includeNavHeaderTickets.txtTicketEstado.setBackgroundResource(R.drawable.ic_circulo)
                        binding.includeNavHeaderTickets.txtTicketEstado.tag = "4"
                    }
                    "CERRADO" -> binding.includeNavHeaderTickets.txtTicketEstado.setBackgroundResource(R.drawable.ic_circulo_negro)
                }

                //descripción del ticket
                //binding.includeTicketsHistorico.txtDescripcionTicketHistorico.text = item.ticketSortsContent
                binding.includeNavHeaderTickets.txtUbicacion.text = item.ticketSortsLocationRequester


                //info petición
                binding.includeTickets.labelPrioridad.text = item.ticketSortsUrgency
                when(item.ticketSortsUrgency){
                    "ALTA" -> {
                        val compoundDrawables: Array<Drawable> = binding.includeTickets.labelPrioridad.compoundDrawables
                        val drawableLeft = compoundDrawables[0].mutate()
                        drawableLeft.colorFilter =
                            PorterDuffColorFilter(Color.parseColor("#FF8800"), PorterDuff.Mode.SRC_IN)
                    }
                    "MEDIA" -> {
                        val compoundDrawables: Array<Drawable> = binding.includeTickets.labelPrioridad.compoundDrawables
                        val drawableLeft = compoundDrawables[0].mutate()
                        drawableLeft.colorFilter =
                            PorterDuffColorFilter(Color.parseColor("#FBFF0F"), PorterDuff.Mode.SRC_IN)
                    }
                    "BAJA" -> {
                        val compoundDrawables: Array<Drawable> = binding.includeTickets.labelPrioridad.compoundDrawables
                        val drawableLeft = compoundDrawables[0].mutate()
                        drawableLeft.colorFilter =
                            PorterDuffColorFilter(Color.parseColor("#06B711"), PorterDuff.Mode.SRC_IN)
                    }
                }

                binding.includeTickets.labelSolicitudIncidencia.text = item.ticketSortsType
                when(item.ticketSortsType){
                    "SOLICITUD" -> {
                        binding.includeTickets.txtSolicitud.text ="?"
                        binding.includeTickets.tvSolicitud.isVisible = true
                        binding.includeTickets.tvIncidencia.isVisible = false

                    }
                    "INCIDENCIA" -> {
                        binding.includeTickets.tvSolicitud.isVisible = false
                        binding.includeTickets.tvIncidencia.isVisible = true
                    }
                }

                binding.includeTickets.labelFechaOperadorApertura.text = "Fecha de Creación: ${item.ticketSortsCreationDate}"
                binding.includeTickets.labelCategoria.text = item.ticketSortsCategory
                binding.includeTickets.labelUbicacion.text = item.ticketSortsLocationRequester
                binding.includeTickets.labelOrigen.text = item.ticketSortsSource
                binding.includeTickets.labelDescrTicket.text = item.ticketSortsContent
                binding.includeTickets.labelSolicitanteNombre.text = "${item.ticketSortsNameRequester} ${item.ticketSortsLastNameRequester}"
                binding.includeTickets.labelSolicitanteCargo.text = "${item.ticketSortsPositionRequester}"
                binding.includeTickets.labelAsignadoNombre.text = "${item.ticketSortsNameTechnician} ${item.ticketSortsLastNameTechnician}"
            }
        }
    }

    private fun volleyRequestIdRecipient(){
        /*val bundle = intent.extras
        val idRecipient = bundle!!.getString("ticketSortsIdRecipient")*/

        Log.i("mensaje prefer","$urlApi_TasksUsers${prefer.getRecipientId()} ")
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlApi_TasksUsers+prefer.getRecipientId(), Response.Listener { response ->
            try {
                jsonArrayIdRecipient = JSONArray()
                jsonArrayIdRecipient = JSONArray(response)
                val dataId = jsonArrayIdRecipient.getJSONObject(0)
                val nameId = dataId.getString("NOMBRE")
                val lastNameId = dataId.getString("APELLIDO")
                val cellPhone = dataId.getString("TELEFONO")

                binding.includeTicketsHistorico.txtNameOperador.text = "$nameId $lastNameId"
                binding.includeTickets.txtTasksUserName.text = " - $nameId $lastNameId"
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

    override fun onBackPressed() {
        super.onBackPressed()

        val room  = Room.databaseBuilder(this, TicketInfoDB::class.java,"ticketInfoBD").build()
        lifecycleScope.launch{
            room.daoTicketInfo().deleteTicketInfo(prefer.getTicketSortsId())

            if (room.daoTicketInfo().getTicketInfo().isEmpty()){
                Toast.makeText(applicationContext, "se borró los datos de la base de datos", Toast.LENGTH_SHORT).show()
            }
        }
        prefer.deleteTicketSortsId()
        prefer.deleteRecipientId()
        prefer.deleteNameTechnicianTask()


        val intentOnBack = Intent(this, MainActivity::class.java)
        intentOnBack.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intentOnBack)
    }

    override fun onEditClick(
        ticketInfoType: String,
        ticketInfoContent: String,
        ticketInfoPrivate: String,
        ticketInfoIdTechnician: String,
        ticketInfoStatus: String,
        ticketInfoIdTemplate: String,
        ticketInfoIdCategory: String,
        ticketInfoCategory: String,
        ticketInfoId: String,
        ticketInfoIdSource: String,
        ticketInfoSource: String,
        ticketInfoTimeToSolve: String,
        adapterPosition: Int
    ) {
        val flagOnEditClick = true
        //recuperamos el id del ticket
        val bundle = intent.extras
        val ticketSortsId = bundle!!.getString("ticketSortsId")
        val ticketSortsStatus = bundle!!.getString("ticketSortsStatus")
        val ticketOrigin = bundle!!.getString("TicketOrigen")
        val ticketSortsIdTechnician = bundle!!.getString("ticketSortsIdTechnician")
        val ticketSortsType = bundle!!.getString("ticketSortsType")
        val ticketSortsSource = bundle!!.getString("ticketSortsSource")

        val intentTasks = (Intent(this,TicketsAgregarTareaActivity::class.java))
        intentTasks.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val intentFollowUp = (Intent(this,TicketsAgregarSeguimientoActivity::class.java))
        intentFollowUp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        //Log.i("mensaje edit: ",""+glpiConversationTipo)

        if (ticketInfoType == "TASK"){
            intentTasks.putExtra("ticketSortsId",ticketSortsId)
            intentTasks.putExtra("ticketSortsStatus",ticketSortsStatus)
            intentTasks.putExtra("ticketInfoContent",ticketInfoContent)
            intentTasks.putExtra("ticketInfoPrivate",ticketInfoPrivate)
            intentTasks.putExtra("ticketInfoIdTechnician",ticketInfoIdTechnician)
            intentTasks.putExtra("ticketInfoStatus",ticketInfoStatus)
            intentTasks.putExtra("ticketInfoIdTemplate",ticketInfoIdTemplate)
            intentTasks.putExtra("ticketInfoIdCategory",ticketInfoIdCategory)
            intentTasks.putExtra("ticketInfoCategory",ticketInfoCategory)
            intentTasks.putExtra("ticketInfoId",ticketInfoId) //origen del
            intentTasks.putExtra("ticketInfoTimeToSolve",ticketInfoTimeToSolve)
            intentTasks.putExtra("ticketSortsType",ticketSortsType)
            intentTasks.putExtra("flagOnEditClick",flagOnEditClick)
            startActivity(intentTasks)
        }else{
            intentFollowUp.putExtra("ticketSortsId",ticketSortsId)
            intentFollowUp.putExtra("ticketSortsStatus",ticketSortsStatus)
            intentFollowUp.putExtra("ticketPrivate",ticketInfoPrivate)
            intentFollowUp.putExtra("ticketInfoContent",ticketInfoContent)
            intentFollowUp.putExtra("ticketOrigin",ticketOrigin)
            intentFollowUp.putExtra("ticketSortsIdTechnician",ticketSortsIdTechnician)
            intentFollowUp.putExtra("ticketSortsType",ticketSortsType)
            intentFollowUp.putExtra("ticketInfoId",ticketInfoId)
            intentFollowUp.putExtra("ticketSortsSource",ticketSortsSource)
            intentFollowUp.putExtra("ticketInfoIdSource",ticketInfoIdSource)
            intentFollowUp.putExtra("ticketInfoSource",ticketInfoSource)
            intentFollowUp.putExtra("flagOnEditClick",flagOnEditClick)
            startActivity(intentFollowUp)
        }

        isEditFollowup = true
        positionFollowup = adapterPosition
    }

    override fun onFabClick() {
        val bundle = intent.extras
        Toast.makeText(this, bundle!!.getString("Contenido"), Toast.LENGTH_SHORT).show()
        val Contenido_ = bundle!!.getString("Contenido")
    }
}