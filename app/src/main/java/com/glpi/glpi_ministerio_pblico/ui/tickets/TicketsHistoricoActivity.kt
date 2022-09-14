package com.glpi.glpi_ministerio_pblico.ui.tickets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsHistoricoBinding

class TicketsHistoricoActivity : AppCompatActivity() {
    lateinit var binding: ActivityTicketsHistoricoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsHistoricoBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}