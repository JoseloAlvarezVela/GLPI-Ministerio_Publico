package com.glpi.glpi_ministerio_pblico

import android.R.attr.password
import android.accounts.AccountManager.KEY_PASSWORD
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    //declaramos
    internal lateinit var queue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        queue = Volley.newRequestQueue(this@LoginActivity)

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

                        val i = Intent(this@LoginActivity, MainActivity::class.java)
                        prefer.SaveToken(token_)//guardamos el token en el sharedPreference
                        Log.i("mensaje login:","Login: "+ prefer.getToken()+" : "+token_)
                        //
                        Toast.makeText(this@LoginActivity, "toast 1: $token_", Toast.LENGTH_LONG).show()
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(i)

                    } catch (e: Exception) {
                        Toast.makeText(this@LoginActivity, "USUARIO O CONTRASEÑA INCORRECTA.", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                        //Log.i("mensaje:",""+e) para mostrar mensaje por consola
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@LoginActivity,
                        "PROBLEMAS CON EL SERVIDOR"+ Response.ErrorListener{},
                        Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): Map<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    params.put("username",loginUserName.text.toString().trim { it <= ' ' })
                    params.put("password",loginUserPassword.text.toString().trim { it <= ' ' })
                    return params
                }
            }
            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
            //fin boton login volley iniciar sesion

            //*******************volley para obtener perfil***********************************

            //*******************volley para obtener perfil***********************************
        }
        CheckUserLogin() //verificamos si existe usuario logeado
        //Toast.makeText(this, prefer.getToken(), Toast.LENGTH_SHORT).show()
    }

    //verificamos si tenemos guardado el token
    private fun CheckUserLogin() {
        if(prefer.getToken() != "noToken" ){
            startActivity(Intent(this,MainActivity::class.java))
        }
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