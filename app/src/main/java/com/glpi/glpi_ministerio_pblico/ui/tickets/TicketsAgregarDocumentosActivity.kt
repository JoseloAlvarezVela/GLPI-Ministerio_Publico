package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsAgregarDocumentosBinding

class TicketsAgregarDocumentosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTicketsAgregarDocumentosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsAgregarDocumentosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_fabs()
        btn_header()

    }
    //INICIO - funcion que contiene botones en la cabecera del layout
    private fun btn_header() {
        binding.btnAtrasActtadddoc.setOnClickListener {
            val intent_atras = Intent(this, NavFooterTicketsActivity::class.java)
            intent_atras.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_atras)
        }
        //boton agregar solución
        binding.btnAdddocActtadddoc.setOnClickListener {
            Toast.makeText(this, "documentos agregados", Toast.LENGTH_LONG).show()
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
        binding.fabArchivoActtadddoc.setOnClickListener {
            Toast.makeText(this, "abrir archivos del celular", Toast.LENGTH_SHORT).show()
        }
        //camara de celular
        binding.fabFotoActtadddoc.setOnClickListener {
            Toast.makeText(this, "abrir camara de celular", Toast.LENGTH_SHORT).show()
        }
        /*modal plantilla
        binding.fabPlantillaActtaddsol.setOnClickListener {
            Toast.makeText(this, "abrir plantilla de solución", Toast.LENGTH_SHORT).show()
        }*/
        //boton para desplegar y plegar los fabs
        binding.fabDesplegarAdddoc.setOnClickListener {
            if (click_desplegar == false){
                binding.includeBggrisActtadddoc.clBackgroundgrisBggris.isVisible = true // fondo gris
                binding.lyFabsActtadddoc.isVisible = true
                click_desplegar = true
            }else{
                binding.lyFabsActtadddoc.isVisible = false
                binding.includeBggrisActtadddoc.clBackgroundgrisBggris.isVisible = false // fondo gris
                click_desplegar = false
            }
        }
        //fondo gris para cerra modal
        binding.includeBggrisActtadddoc.clBackgroundgrisBggris.setOnClickListener {
            binding.lyFabsActtadddoc.isVisible = false
            binding.includeBggrisActtadddoc.clBackgroundgrisBggris.isVisible = false // fondo gris
            click_desplegar = false
        }
    }
    //FIN - fabs que abre modal plantilla,camara del celular y archivos del celular
}