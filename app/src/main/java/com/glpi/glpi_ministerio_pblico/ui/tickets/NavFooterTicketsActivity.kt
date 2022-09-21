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
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TicketID
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.databinding.ActivityNavFooterTicketsBinding
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import org.json.JSONObject


class NavFooterTicketsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavFooterTicketsBinding
    private lateinit var jsonObjectResponse: JSONObject

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavFooterTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DataToTicketsHistoricoActivity()

        creatNewLinearLayout()
        createTextView()

        binding.includeTicketsHistorico.layoutConversation.addView(creatNewLinearLayout())
        binding.includeTicketsHistorico.layoutConversation.addView(createTextView())
        creatNewLinearLayout().addView(createTextView())

        volleyResquestGet(urlApi_TicketID)


        //INICIO toogle buton tickets
        var clickTickets: Boolean = false
        var clickConvezaciones: Boolean = false
        var click_fab:Boolean = false //fab_opciones de layout activity_tickets_historico.xml

        binding.btnConversacionFooter.iconTint = ContextCompat.getColorStateList(this, R.color.textColor)
        binding.btnConversacionFooter.setTextColor(Color.parseColor("#175381"))

        binding.btnTicketsFooter.setOnClickListener {
            if(clickTickets == false){
                binding.btnTicketsFooter.iconTint = ContextCompat.getColorStateList(this, R.color.textColor)
                binding.btnTicketsFooter.setTextColor(Color.parseColor("#175381"))

                binding.btnConversacionFooter.iconTint = ContextCompat.getColorStateList(this, R.color.ticketsGris)
                binding.btnConversacionFooter.setTextColor(Color.parseColor("#676161"))

                binding.includeTickets.includeTicketsLayout.isVisible = true //se muestra
                binding.includeTicketsHistorico.includeTicketsHistoricoLayout.isVisible = false //se esconde
                //binding.btnTicketsFooterCOLOR.setBackgroundResource(R.color.ticketsGris)
                binding.btnConversacionFooterCOLOR.setBackgroundResource(R.color.ticketsBlanco)

                //************INICIO DE SETEO LOS FAB'S DE LAYOUT activity_tickets_historico.xml************
                binding.includeTicketsHistorico.fabSolucion.isVisible = false
                    binding.includeTicketsHistorico.btnFabSolucion.isVisible = false
                binding.includeTicketsHistorico.fabDocumentos.isVisible = false
                    binding.includeTicketsHistorico.btnFabDocumentos.isVisible = false
                binding.includeTicketsHistorico.fabTareas.isVisible = false
                    binding.includeTicketsHistorico.btnFabTareas.isVisible = false
                binding.includeTicketsHistorico.fabSeguimiento.isVisible = false
                    binding.includeTicketsHistorico.btnFabSeguimiento.isVisible = false
                binding.includeTicketsHistorico.fabBackgroud.isVisible = false
                click_fab = false
                //************FIN DE SETEO DE LOS FAB'S DE LAYOUT activity_tickets_historico.xml************

                clickTickets = true
                clickConvezaciones = false
            }
        }

        binding.btnConversacionFooter.setOnClickListener {
            if(clickConvezaciones == false){
                binding.btnTicketsFooter.iconTint = ContextCompat.getColorStateList(this, R.color.ticketsGris)
                binding.btnTicketsFooter.setTextColor(Color.parseColor("#676161"))

                binding.btnConversacionFooter.iconTint = ContextCompat.getColorStateList(this, R.color.textColor)
                binding.btnConversacionFooter.setTextColor(Color.parseColor("#175381"))

                binding.includeTicketsHistorico.includeTicketsHistoricoLayout.isVisible = true
                binding.includeTickets.includeTicketsLayout.isVisible = false
                binding.btnTicketsFooterCOLOR.setBackgroundResource(R.color.ticketsBlanco)
                //binding.btnConversacionFooterCOLOR.setBackgroundResource(R.color.ticketsGris)
                clickConvezaciones = true
                clickTickets = false
            }
        }
        //FIN toogle buton

        //boton atras - include de nav_header_tickets.xml
        binding.includeNavHeaderTickets.btnAtrasTickets.setOnClickListener {
            val intent_header_tickets = Intent(this@NavFooterTicketsActivity, MainActivity::class.java)
            intent_header_tickets.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_header_tickets)
        }

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

        //INICIO fab_opciones de layout activity_tickets_historico.xml
        binding.includeTicketsHistorico.fabDesplegarOpciones.setOnClickListener {
            if (click_fab == false){
                binding.includeTicketsHistorico.fabSolucion.isVisible = true
                    binding.includeTicketsHistorico.btnFabSolucion.isVisible = true
                binding.includeTicketsHistorico.fabDocumentos.isVisible = true
                    binding.includeTicketsHistorico.btnFabDocumentos.isVisible = true
                binding.includeTicketsHistorico.fabTareas.isVisible = true
                    binding.includeTicketsHistorico.btnFabTareas.isVisible = true
                binding.includeTicketsHistorico.fabSeguimiento.isVisible = true
                    binding.includeTicketsHistorico.btnFabSeguimiento.isVisible = true
                binding.includeTicketsHistorico.fabBackgroud.isVisible = true

                click_fab = true
            }else{
                binding.includeTicketsHistorico.fabSolucion.isVisible = false
                    binding.includeTicketsHistorico.btnFabSolucion.isVisible = false
                binding.includeTicketsHistorico.fabDocumentos.isVisible = false
                    binding.includeTicketsHistorico.btnFabDocumentos.isVisible = false
                binding.includeTicketsHistorico.fabTareas.isVisible = false
                    binding.includeTicketsHistorico.btnFabTareas.isVisible = false
                binding.includeTicketsHistorico.fabSeguimiento.isVisible = false
                    binding.includeTicketsHistorico.btnFabSeguimiento.isVisible = false
                binding.includeTicketsHistorico.fabBackgroud.isVisible = false

                click_fab = false
            }
        }
        binding.includeTicketsHistorico.fabBackgroud.setOnClickListener {
            binding.includeTicketsHistorico.fabSolucion.isVisible = false
                binding.includeTicketsHistorico.btnFabSolucion.isVisible = false
            binding.includeTicketsHistorico.fabDocumentos.isVisible = false
                binding.includeTicketsHistorico.btnFabDocumentos.isVisible = false
            binding.includeTicketsHistorico.fabTareas.isVisible = false
                binding.includeTicketsHistorico.btnFabTareas.isVisible = false
            binding.includeTicketsHistorico.fabSeguimiento.isVisible = false
                binding.includeTicketsHistorico.btnFabSeguimiento.isVisible = false
            binding.includeTicketsHistorico.fabBackgroud.isVisible = false
            binding.includeTicketsHistorico.fabDesplegarOpciones.isVisible = true

            click_fab = false
        }
        //FIN fab_opciones de layout activity_tickets_historico

        //INICIO eventos click de fab_opciones
        binding.includeTicketsHistorico.btnFabTareas.setOnClickListener {
            val intent_agregar_tarea = Intent(this@NavFooterTicketsActivity, TicketsAgregarTareaActivity::class.java)
            intent_agregar_tarea.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_agregar_tarea)
        }
        binding.includeTicketsHistorico.btnFabSeguimiento.setOnClickListener {
            val intent_agregar_seguimiento = Intent(this@NavFooterTicketsActivity, TicketsAgregarSeguimientoActivity::class.java)
            intent_agregar_seguimiento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_agregar_seguimiento)
        }
        binding.includeTicketsHistorico.btnFabSolucion.setOnClickListener {
            val intent_agregar_seguimiento = Intent(this@NavFooterTicketsActivity, TicketsAgregarSolucionActivity::class.java)
            intent_agregar_seguimiento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_agregar_seguimiento)
        }
        binding.includeTicketsHistorico.btnFabDocumentos.setOnClickListener {
            val intent_agregar_documento = Intent(this@NavFooterTicketsActivity, TicketsAgregarDocumentosActivity::class.java)
            intent_agregar_documento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_agregar_documento)
        }
        //fin eventos click de fab_opciones
    }

    private fun volleyResquestGet(urlApi_: String) {
        jsonObjectResponse = JSONObject()
        //val bundle = intent.extras
        //val TicketID_ = bundle!!.getString("TicketID").toString()

        val stringRequestDataTickets = @RequiresApi(Build.VERSION_CODES.N)
        object : StringRequest(Method.GET,
            urlApi_, Response.Listener { response ->
                try {
                    jsonObjectResponse = JSONObject(response)
                    val nTasks = jsonObjectResponse.getJSONObject("0")
                    if (jsonObjectResponse.length() > 1){

                        var iterador = 0
                        var taskDescriptions = ArrayList<String>()
                        var tasksIterador: JSONObject
                        for (i in  0 until jsonObjectResponse.length()){
                            tasksIterador = jsonObjectResponse.getJSONObject(iterador.toString())
                            val tasksTipo = tasksIterador.getString("TIPO")
                            iterador++
                            if (tasksTipo == "TASK"){



                            }else if( tasksTipo == "FOLLOWUP"){

                            }else{
                                //solution
                            }
                            Log.i("mensaje tipo",""+tasksTipo)
                        }

                    }else{
                        //do anything
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "token expirado: $e", Toast.LENGTH_LONG).show()
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
        this?.let {
            VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets)
        }
    }

    private fun creatNewLinearLayout(): LinearLayout {
        val newLayout = LinearLayout(this) // declaramos el componente
        //inicio tamaño de linearlayout
        val layoutParams = LinearLayout.LayoutParams(1000,500 )
        newLayout.layoutParams = layoutParams
        //fin tamaño de boton
        newLayout.setBackgroundColor(Color.parseColor("#FFF3DB"))
        return newLayout
    }

    private fun createTextView(): TextView {
        val newTextView = TextView(this)
        //*****************INICIO DISEÑO DEL TEXVIEW****************************
        newTextView.text = "este texto es de prueba"
        newTextView.setTextColor(Color.parseColor("#FFFFFF"))
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

        val TicketID_ = bundle!!.getString("TicketID")
        val NameOperador_ = bundle!!.getString("NameOperador")
        val CurrentTime_ = bundle!!.getString("CurrentTime")
        val Contenido_ = bundle!!.getString("Contenido")
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


        binding.includeNavHeaderTickets.txtTicketID.text = "Petición #$TicketID_"
        binding.includeNavHeaderTickets.txtUbicacion.text = Ubicacion_
        if (TicketEstado_ == "EN CURSO (Asignada)"){
            binding.includeNavHeaderTickets.txtTicketEstado.setBackgroundResource(R.drawable.ic_circulo_verde)
        }else{
            binding.includeNavHeaderTickets.txtTicketEstado.setBackgroundResource(R.drawable.ic_circulo)
        }

        binding.includeTicketsHistorico.txtNameOperador.text = NameOperador_
        binding.includeTicketsHistorico.txtCurrentTime.text = CurrentTime_
        binding.includeTicketsHistorico.txtDescripcionTicketHistorico.text = Contenido_

        binding.includeTickets.labelFechaOperadorApertura.text = "$CurrentTime_ - $NameOperador_"
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
        binding.includeTickets.labelUbicacion.text = Ubicacion_
        binding.includeTickets.labelEmail.text = Correo_
        binding.includeTickets.labelSolicitanteNombre.text = NameSolicitante_
        binding.includeTickets.labelSolicitanteCargo.text = CargoSolicitante_
        binding.includeTickets.labelSolicitanteCelular.text = TelefonoSolicitante_
        binding.includeTickets.labelAsignadoNombre.text = LoginName_
        binding.includeTickets.labelDescrTicket.text = Contenido_

        //SECTION TASKS
        binding.includeTicketsHistorico.txtTaskNameLogin.text = taskName
        binding.includeTicketsHistorico.txtTaskDescription.text = taskDescription
    }
}