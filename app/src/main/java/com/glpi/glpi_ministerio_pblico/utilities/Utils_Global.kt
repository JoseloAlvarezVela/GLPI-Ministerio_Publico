package com.glpi.glpi_ministerio_pblico.utilities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.jsonArrayResponse
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.jsonObjectResponse
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.databinding.ActivityNavFooterTicketsBinding
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class Utils_Global: AppCompatActivity() {

    companion object{
        fun volleyRequestID(context: MainActivity, urlApi: String){
            val stringRequestDataTickets = object : StringRequest(Method.POST,urlApi, Response.Listener { response ->
                try {
                    if(response[0] == '['){
                        jsonArrayResponse = JSONArray(response)
                    }else{
                        jsonObjectResponse = JSONObject(response)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }){
                override fun getParams(): Map<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    params["session_token"] = token.prefer.getToken()
                    return params
                }
            }
            VolleySingleton.getInstance(context).addToRequestQueue(stringRequestDataTickets)
            //FIN volley ------------------------------------------------------------
        }
    }
}