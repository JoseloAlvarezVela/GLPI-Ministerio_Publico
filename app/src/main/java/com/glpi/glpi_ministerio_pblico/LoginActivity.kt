package com.glpi.glpi_ministerio_pblico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    internal lateinit var queue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        queue = Volley.newRequestQueue(this@LoginActivity)

        val loginUserName = findViewById<EditText>(R.id.login_user)
        val loginUserPassword = findViewById<EditText>(R.id.login_password)
        val LoginUserValidate = findViewById<Button>(R.id.login_validate)
        val url = "http://192.168.0.5/glpi/api_glpi.php" // en casa
        //var url = "http://192.168.47.55/glpi/api_glpi.php" // wifi de hasmin
        //var url = "http://192.168.43.75/glpi/api_glpi.php" // mi wifi phone
        //val url = "http://192.168.3.54/glpi/api_glpi.php" //en ofc. redes
        //val url = "http://192.168.3.10/glpi/api_glpi.php" //en ofc. digitalizacion.. no sirve

        /*/inicio volley validar token
        val stringRequest = object : StringRequest(Request.Method.POST,
            url, Response.Listener { response ->
                val jsonArray = JSONArray(response)
                val jsonObjecT_ = jsonArray[0]
                try {
                    //val session_token = response.getJSONObject("session_token")

                    /*val jsonObject = JSONObject(response)
                    val token_ = jsonObject.getString("session_token")*/

                    Toast.makeText(this@Login, "toast validado: $jsonObjecT_", Toast.LENGTH_LONG).show()
                    val i = Intent(this@Login, MainActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    //Toast.makeText(this@Login, "TOAST 1"+response, Toast.LENGTH_SHORT).show()
                    startActivity(i)

                } catch (e: Exception) {
                    Toast.makeText(this@Login, "token invalido"+jsonObjecT_, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                    //progressDialog.cancel()
                }
            }, Response.ErrorListener {
                //progressDialog.cancel()

                Toast.makeText(this@Login,
                    "PROBLEMAS CON EL SERVIDOR",
                    Toast.LENGTH_LONG).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["PATH"] = "validar"

                params["SESSIONTOKEN"] = "12345CAJSCO"

                return params
            }
        }
        queue.add(stringRequest)
        //fin inicio volley validar token*/


        //inicio boton login volley iniciar sesion
        LoginUserValidate.setOnClickListener {

            val stringRequest = object : StringRequest(Request.Method.POST,
                url, Response.Listener { response ->
                    Toast.makeText(this@LoginActivity, "toast 0", Toast.LENGTH_LONG).show()
                    try {
                        val jsonObject = JSONObject(response)
                        val token_ = jsonObject.getString("session_token")
                        Toast.makeText(this@LoginActivity, "toast 1: $token_", Toast.LENGTH_LONG).show()
                        val i = Intent(this@LoginActivity, MainActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(i)

                    } catch (e: Exception) {
                        Toast.makeText(this@LoginActivity, "USUARIO O CONTRASEÑA INCORRECTA."+e, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@LoginActivity,
                        "PROBLEMAS CON EL SERVIDOR"+ Response.ErrorListener{},
                        Toast.LENGTH_LONG).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["PATH"] = "login"

                    params["USER"] = loginUserName.text.toString().trim { it <= ' ' }
                    params["PASSWORD"] = loginUserPassword.text.toString().trim { it <= ' ' }

                    return params
                }
            }
            queue.add(stringRequest)
        }//fin boton login volley iniciar sesion
    }
}