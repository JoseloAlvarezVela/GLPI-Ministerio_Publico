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

        //inicio toogle buton tickets
        var clickTickets: Boolean = false
        var clickConvezaciones: Boolean = false

        binding.btnTicketsFooter.setOnClickListener {
            if(clickTickets == false){
                binding.includeTickets.includeTicketsLayout.isVisible = true //se muestra
                binding.includeTicketsHistorico.includeTicketsHistoricoLayout.isVisible = false //se esconde
                binding.btnTicketsFooterCOLOR.setBackgroundResource(R.color.ticketsGris)
                binding.btnConversacionFooterCOLOR.setBackgroundResource(R.color.ticketsBlanco)
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
        //fin toogle buton

        //boton atras -- include de nav_header_tickets.xml
        binding.includeNavHeaderTickets.btnAtrasTickets.setOnClickListener {
            val intent_header_tickets = Intent(this@NavFooterTicketsActivity, MainActivity::class.java)
            intent_header_tickets.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            Toast.makeText(this, "boton atras presionado", Toast.LENGTH_LONG ).show()
            startActivity(intent_header_tickets)
        }

        //botones para desplegar y plegar descripciones
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

    }
}