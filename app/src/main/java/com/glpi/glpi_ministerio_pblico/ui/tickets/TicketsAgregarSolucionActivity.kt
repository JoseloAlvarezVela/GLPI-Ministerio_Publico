package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsAgregarSolucionBinding
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsAgregarTareaBinding

class TicketsAgregarSolucionActivity : AppCompatActivity() {
    lateinit var binding: ActivityTicketsAgregarSolucionBinding //declaramos binding para acceder variables
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsAgregarSolucionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_fabs()
        btn_header()

    }
    //INICIO - funcion que contiene botones en la cabecera del layout
    private fun btn_header() {
        binding.btnAtrasActtaddsol.setOnClickListener {
            val intent_atras = Intent(this, NavFooterTicketsActivity::class.java)
            intent_atras.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_atras)
        }
        //boton agregar solución
        binding.btnAddsolActtaddsol.setOnClickListener {
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
    //FIN - fabs que abre modal plantilla,camara del celular y archivos del celular
}