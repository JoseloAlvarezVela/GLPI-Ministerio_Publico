package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.databinding.ActivityNavHeaderTicketsBinding
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsBinding

class NavHeaderTickets : AppCompatActivity() {
    private lateinit var binding: ActivityNavHeaderTicketsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavHeaderTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}