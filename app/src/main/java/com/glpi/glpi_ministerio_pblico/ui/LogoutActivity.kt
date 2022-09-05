package com.glpi.glpi_ministerio_pblico.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.glpi.glpi_ministerio_pblico.LoginActivity
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.databinding.ActivityLogoutBinding
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer

class LogoutActivity : AppCompatActivity() {
    lateinit var binding:ActivityLogoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(this, "borrando token: "+prefer.getToken(), Toast.LENGTH_LONG).show()

        prefer.delToken()
        onBackPressed()
    }

    override fun onBackPressed(){
        val intent = Intent(this,LoginActivity::class.java)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }
}