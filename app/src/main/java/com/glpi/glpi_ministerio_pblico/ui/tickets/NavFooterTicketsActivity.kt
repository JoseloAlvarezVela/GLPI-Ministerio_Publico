package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.databinding.ActivityNavFooterTicketsBinding

class NavFooterTicketsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavFooterTicketsBinding
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavFooterTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //INICIO toogle buton tickets
        var clickTickets: Boolean = false
        var clickConvezaciones: Boolean = false
        var click_fab:Boolean = false //fab_opciones de layout activity_tickets_historico

        binding.btnTicketsFooter.setOnClickListener {
            if(clickTickets == false){
                binding.includeTickets.includeTicketsLayout.isVisible = true //se muestra
                binding.includeTicketsHistorico.includeTicketsHistoricoLayout.isVisible = false //se esconde
                binding.btnTicketsFooterCOLOR.setBackgroundResource(R.color.ticketsGris)
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
                binding.includeTicketsHistorico.includeTicketsHistoricoLayout.isVisible = true
                binding.includeTickets.includeTicketsLayout.isVisible = false
                binding.btnTicketsFooterCOLOR.setBackgroundResource(R.color.ticketsBlanco)
                binding.btnConversacionFooterCOLOR.setBackgroundResource(R.color.ticketsGris)
                clickConvezaciones = true
                clickTickets = false
            }
        }
        //FIN toogle buton

        //boton atras -- include de nav_header_tickets.xml
        binding.includeNavHeaderTickets.btnAtrasTickets.setOnClickListener {
            val intent_header_tickets = Intent(this@NavFooterTicketsActivity, MainActivity::class.java)
            intent_header_tickets.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_header_tickets)
            //Toast.makeText(this, "boton atras presionado", Toast.LENGTH_LONG ).show()
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

        //INICIO fab_opciones de layout activity_tickets_historico
        //var click_fab:Boolean = false
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
            val intent_agregar_seguimiento = Intent(this@NavFooterTicketsActivity, TicketsAgregarTareaActivity::class.java)
            intent_agregar_seguimiento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_agregar_seguimiento)
        }
        //fin eventos click de fab_opciones
    }
}