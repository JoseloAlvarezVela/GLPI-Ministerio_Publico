package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.databinding.ActivityMainBinding
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsBinding

class TicketsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTicketsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.includeNavFooter.btnTicketsFooter.isFocusableInTouchMode = true

        //boton atras -- se pasó a NavFooterTicketsActivity
        /*binding.includeNavHeader.btnAtrasTickets.setOnClickListener {
            val intent_header_tickets = Intent(this@TicketsActivity, MainActivity::class.java)
            intent_header_tickets.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            Toast.makeText(this, "boton atras presionado", Toast.LENGTH_LONG ).show()
            startActivity(intent_header_tickets)
        }*/

        /*botones para desplegar y plegar descripciones -- se pasó a NavFooterTicketsActivity
        var clickG: Boolean = false
        var clickD: Boolean = false
        var clickA: Boolean = false

        binding.btnDesplegarGeneral.setOnClickListener{
            if(clickG == false){
                binding.general.isVisible = false
                clickG = true
            }else{
                binding.general.isVisible = true
                clickG = false
            }
        }

        binding.btnDesplegarDecripcion.setOnClickListener{
            if(clickD == false){
                binding.decripcion.isVisible = false
                clickD = true
            }else{
                binding.decripcion.isVisible = true
                clickD = false
            }
        }

        binding.btnDesplegarActor.setOnClickListener {
            if(clickA == false){
                binding.actor.isVisible = false
                clickA = true
            }else{
                binding.actor.isVisible = true
                clickA = false
            }
        }*/

    }
}