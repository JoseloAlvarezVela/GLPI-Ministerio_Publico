package com.glpi.glpi_ministerio_pblico.ui.modals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import com.glpi.glpi_ministerio_pblico.R

class Modal_Perfiles_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modal_perfiles)

        findViewById<LinearLayout>(R.id.LinearLayout_hardwareGestor)

        Toast.makeText(this, "aca se puede escribir codigo", Toast.LENGTH_SHORT).show()

    }
}