package com.glpi.glpi_ministerio_pblico.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.glpi.glpi_ministerio_pblico.LoginActivity
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.databinding.ActivityLogoutBinding
import com.glpi.glpi_ministerio_pblico.ui.shared.Prefer
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import org.json.JSONObject

class LogoutActivity : AppCompatActivity() {
    lateinit var queueLogout : RequestQueue

    lateinit var binding:ActivityLogoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(this, "borrando token: "+prefer.getToken(), Toast.LENGTH_LONG).show()

        deleteToken()

        onBackPressed()
    }

    private fun deleteToken() {
        //INICIO - volley para invalidar toke creado
        //val url = "http://192.168.0.5/glpi/api_glpi.php" // en casa
        //val url = "http://10.26.100.14/glpi/api_glpi.php" // en digitalizacion
        val url = "http://181.176.145.174:8080/api/user_logout" //online
        //queueLogout = Volley.newRequestQueue(this)
        Log.i("mensaje: ",""+ prefer.getToken())
        val stringRequest = object : StringRequest(Request.Method.POST,
            url, Response.Listener { response ->
                try {
                    val succes_ = response.toString()

                    //Toast.makeText(this, "Cerrando Sesión: $succes_", Toast.LENGTH_LONG).show()
                    Log.i("mensaje: ",""+ succes_)
                }catch (e:Exception){
                    e.printStackTrace()
                    Toast.makeText(this, "error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "token inválido", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                val tokenDel = prefer.delToken().toString()
                params.put("session_token","ei8f3hspqv2rtk5kank3in2lra")
                return params
            }
        }
        //queueLogout.add(stringRequest)
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
        //FIN - volley para invalidar toke creado
        prefer.delToken()
    }

    override fun onBackPressed(){
        val intent = Intent(this,LoginActivity::class.java)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }

}