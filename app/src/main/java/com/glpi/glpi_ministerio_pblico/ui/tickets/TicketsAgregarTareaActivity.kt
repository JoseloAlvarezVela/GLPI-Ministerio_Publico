package com.glpi.glpi_ministerio_pblico.ui.tickets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsAgregarTareaBinding

class TicketsAgregarTareaActivity : AppCompatActivity() {
    lateinit var binding: ActivityTicketsAgregarTareaBinding //declaramos binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsAgregarTareaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //INICIO de eventos de click de los FAB's
        binding.btnFabPlantilla.setOnClickListener {
            binding.includeModalPlantillaTarea.modalPlantillaAgregarTarea.isVisible = true
        }
        //FIN de eventos de click de los FAB's
    }
}