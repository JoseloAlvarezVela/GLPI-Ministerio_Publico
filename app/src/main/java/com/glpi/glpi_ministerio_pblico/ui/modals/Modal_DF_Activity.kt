package com.glpi.glpi_ministerio_pblico.ui.modals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.databinding.ModalDfBinding

class Modal_DF_Activity : AppCompatActivity() {

    //enlazamos el binding con el layout
    private lateinit var binding: ModalDfBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*inflamos el binding
        binding = ModalDfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Toast.makeText(this@Modal_DF_Activity, "clase kotlin", Toast.LENGTH_LONG).show()
        val btn_mpfn_modal_ = findViewById<Button>(R.id.btn_mpfn_modal)

        btn_mpfn_modal_.setOnClickListener {
            Toast.makeText(this@Modal_DF_Activity, "si funciona", Toast.LENGTH_LONG).show()
            binding.contenedorMpfn.isVisible = false
            binding.textViewMpfn.isVisible = true
            binding.contenedorCoberturaNacional.isVisible = true
        }*/
    }
}