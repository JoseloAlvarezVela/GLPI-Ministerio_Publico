package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsAgregarTareaBinding

class TicketsAgregarTareaActivity : AppCompatActivity() {
    lateinit var binding: ActivityTicketsAgregarTareaBinding //declaramos binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsAgregarTareaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_fabs()
        btn_atras()
        btn_agregarTarea()
        btn_agregarCat()

    }

    private fun btn_agregarCat() {
        binding.btnAddcatActtaddt.setOnClickListener {
            binding.LayoutBackgroudAgregarTarea.isVisible = true
            binding.includeModalPlantillaAddcat.modalPlantillaAgregarCategoria.isVisible = true
        }

        binding.includeModalPlantillaAddcat.btnSalirModalAddcat.setOnClickListener {
            binding.includeModalPlantillaAddcat.modalPlantillaAgregarCategoria.isVisible = false
            binding.LayoutBackgroudAgregarTarea.isVisible = false
        }
    }

    //función que añade la tarea y te devuelve al menu principal
    private fun btn_agregarTarea() {
        binding.btnAddtActtaddt.setOnClickListener {
            Toast.makeText(this, "tarea añadida", Toast.LENGTH_LONG).show()
            val intent_agregarTarea = Intent(this, MainActivity::class.java)
            intent_agregarTarea.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent_agregarTarea)
        }
    }
    //funcion que regresa a la vista anterior: activity_nav_footer_tickets.xml
    private fun btn_atras() {
       binding.btnAtrasActtaddt.setOnClickListener {
           val intent_atras = Intent(this, NavFooterTicketsActivity::class.java)
           intent_atras.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
           startActivity(intent_atras)
       }
    }

    //funcion que muestra los FAB's
    private fun btn_fabs(){
        var click: Boolean = false

        //INICIO DE inicialización de variables
        val desplegar_fabs = binding.fabDesplegarAddTarea
        val abrir_plantilla = binding.btnFabPlantilla
            val layout_plantilla = binding.fabPlantilla
        val abrir_camara = binding.btnFabFoto
            val layout_camara = binding.fabFoto
        val abrir_archivo = binding.btnFabArchivo
            val layout_archivo = binding.fabArchivo
        val hide_fab = binding.LayoutFabAgregarTarea
        val backGround = binding.LayoutBackgroudAgregarTarea
        //FIN DE inicialización de variables

        //INICIO de eventos de click de los FAB's
        desplegar_fabs.setOnClickListener {
            if (click == false){
                layout_plantilla.isVisible = true
                layout_camara.isVisible = true
                layout_archivo.isVisible = true
                backGround.isVisible = true
                click = true
            }else{
                layout_plantilla.isVisible = false
                layout_camara.isVisible = false
                layout_archivo.isVisible = false
                backGround.isVisible = false
                click = false
            }

        }
        abrir_plantilla.setOnClickListener {
            binding.includeModalPlantillaTarea.modalPlantillaAgregarTarea.isVisible = true
            hide_fab.isVisible = false
        }
        abrir_camara.setOnClickListener {
            Toast.makeText(this, "introduce código que abre la camara del celular", Toast.LENGTH_SHORT).show()
        }
        abrir_archivo.setOnClickListener {
            Toast.makeText(this, "Abrir Archivos", Toast.LENGTH_SHORT).show()
        }
        backGround.setOnClickListener {
            binding.includeModalPlantillaTarea.modalPlantillaAgregarTarea.isVisible = false
            binding.includeModalPlantillaAddcat.modalPlantillaAgregarCategoria.isVisible = false
            layout_plantilla.isVisible = false
            layout_camara.isVisible = false
            layout_archivo.isVisible = false
            backGround.isVisible = false
            binding.LayoutFabAgregarTarea.isVisible = true
            click = false
        }
        //FIN de eventos de click de los FAB's
    }
}