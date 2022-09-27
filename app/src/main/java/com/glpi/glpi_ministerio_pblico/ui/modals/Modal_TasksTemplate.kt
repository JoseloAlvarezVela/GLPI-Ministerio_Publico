package com.glpi.glpi_ministerio_pblico.ui.modals

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.databinding.ActivityTicketsAgregarTareaBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_TasksTemplate
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_Tickets
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_Perfiles
import java.util.ArrayList

class Modal_TasksTemplate: AppCompatActivity() {
    private lateinit var binding: ActivityTicketsAgregarTareaBinding
    internal lateinit var dataModelArrayListTasksTemplate: ArrayList<Data_TasksTemplate>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketsAgregarTareaBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}