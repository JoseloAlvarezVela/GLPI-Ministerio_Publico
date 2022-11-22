package com.glpi.glpi_ministerio_pblico

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import com.glpi.glpi_ministerio_pblico.ui.tickets.NavFooterTicketsActivity
import com.glpi.glpi_ministerio_pblico.ui.tickets.TicketsAgregarTareaActivity
import com.glpi.glpi_ministerio_pblico.utilities.NFMbyPass
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    internal lateinit var queue: RequestQueue
    lateinit private var progressBarAction_ : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefer.deleteTicketSortsId()

        val data = intent.extras
        if (data != null) {
            //Aqui puedes obtener tus datos
            val ticketId = data.getString("ticketId")
            prefer.saveTicketSortsId(ticketId.toString())
            //getIntent().removeExtra("ticketId")
            Log.i("mensaje ticket_id push", ticketId.toString())
            checkUserLogin(ticketId.toString())
            //prefer.saveNotificationTicketId(ticketId.toString())

        /*}else if (prefer.getTicketSortsId() != "noTicket"){
            Log.i("mensaje ticket_idPref", prefer.getTicketSortsId())
            checkUserLogin(prefer.getTicketSortsId())*/
        }else{
            Log.i("mensaje ticket_nulo", prefer.getTicketSortsId())
            checkUserLogin("null")
        }

        queue = Volley.newRequestQueue(this@LoginActivity)
        val TIMEOUT = 10000


        val loginUserName = findViewById<EditText>(R.id.login_user)
        val loginUserPassword = findViewById<EditText>(R.id.login_password)
        val loginUserValidate = findViewById<Button>(R.id.login_validate)
        val url = "http://181.176.145.174:8080/api/user_login" //online

        //inicio boton login volley iniciar sesion
        loginUserValidate.setOnClickListener {
            val stringRequest = object : StringRequest(Request.Method.POST,
                url, Response.Listener { response ->
                    //Toast.makeText(this@LoginActivity, "toast 0", Toast.LENGTH_LONG).show()
                    try {
                        val jsonObject = JSONObject(response)
                        val token_ = jsonObject.getString("session_token")

                        val i = Intent(this, MainActivity::class.java)
                        prefer.SaveToken(token_)//guardamos el token en el sharedPreference
                        Log.i("mensajeT login:","Login: "+ prefer.getToken()+" : "+token_)


                        Toast.makeText(this, "toast 1: $token_", Toast.LENGTH_LONG).show()
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(i)

                    } catch (e: Exception) {
                        Toast.makeText(this, "USUARIO O CONTRASEÑA INCORRECTA.", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                        //Log.i("mensaje:",""+e) para mostrar mensaje por consola
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this,
                        "Sin conexión a internet",Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): Map<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    params["username"] = loginUserName.text.toString().trim { it <= ' ' }
                    params["password"] = loginUserPassword.text.toString().trim { it <= ' ' }
                    return params
                }
            }
            stringRequest.retryPolicy = DefaultRetryPolicy(
                TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
            //fin boton login volley iniciar sesion
        }
    }

    //verificamos si tenemos guardado el token
    private fun checkUserLogin(ticketId: String) {

        progressBarAction_ = findViewById(R.id.progressBarAction)

        val url = "http://181.176.145.174:8080/api/user_entities" //online
        val stringRequest = object : StringRequest(Request.Method.POST,
            url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val token_ = jsonObject.getString("myentities")
                    Log.i("mensajeT tokenValidate:",""+ prefer.getToken()+" : "+token_)
                    Log.i("mensajeT idPush: ",""+ prefer.getTicketSortsId())
                    if(prefer.getToken() != "noToken" && ticketId != "null"){

                        val intent = Intent(this, NFMbyPass::class.java)
                        //intent.putExtra("flagNotificationPush",true)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)


                        //intent.putExtra("flagNotificationPush",true)
                        //startActivity(Intent(this,NavFooterTicketsActivity::class.java))
                    }else if(prefer.getToken() != "noToken"){
                        Toast.makeText(this, "no se capturó ticket id", Toast.LENGTH_SHORT).show()
                        Log.i("mensajeT idPushPref: ",""+ prefer.getNotificationTicketId())
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("flagNotificationPush",false)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }


                } catch (e: Exception) {
                    Toast.makeText(this, "SESIÓN EXPIRADA.", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                    Log.i("mensajeT tokenEXPIRADO:",""+ prefer.getToken())
                    prefer.deleteToken()
                    val layoutLogin_ = findViewById<LinearLayout>(R.id.LayoutLogin)
                    val progressBarText_ = findViewById<TextView>(R.id.progressBarText)
                    progressBarAction_.isVisible = false
                    progressBarText_.isVisible = false
                    layoutLogin_.isVisible = true

                }
            }, Response.ErrorListener {
                Toast.makeText(this,
                    "Problemas con el servidor",Toast.LENGTH_LONG).show()
                Log.i("mensajeT tokenValidate ",""+prefer.getToken())
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = prefer.getToken()
                return params
            }
        }
        Handler().postDelayed({
            val layoutLogin_ = findViewById<LinearLayout>(R.id.LayoutLogin)
            val progressBarText_ = findViewById<TextView>(R.id.progressBarText)
            progressBarAction_!!.isVisible = false
            progressBarText_.isVisible = false
            layoutLogin_.isVisible = true
        },10000/* 10 second */)
        stringRequest.retryPolicy = DefaultRetryPolicy(
            10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)

    }

    //controlamos la pulsación del boton atras por defecto del celular para que cierre la app
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        return super.onKeyDown(keyCode, event)
    }
}