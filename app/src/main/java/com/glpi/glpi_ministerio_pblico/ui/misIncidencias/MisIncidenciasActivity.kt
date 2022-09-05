package com.glpi.glpi_ministerio_pblico.ui.misIncidencias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.glpi.glpi_ministerio_pblico.databinding.ActivityMisIncidenciasBinding

class MisIncidenciasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMisIncidenciasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisIncidenciasBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}