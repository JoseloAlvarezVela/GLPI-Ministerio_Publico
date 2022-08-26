package com.glpi.glpi_ministerio_pblico

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.glpi.glpi_ministerio_pblico.databinding.ActivityMainBinding
import com.glpi.glpi_ministerio_pblico.databinding.NavHeaderMainBinding

class NavHeaderMainActivity : AppCompatActivity() {
    private lateinit var binding: NavHeaderMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NavHeaderMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}