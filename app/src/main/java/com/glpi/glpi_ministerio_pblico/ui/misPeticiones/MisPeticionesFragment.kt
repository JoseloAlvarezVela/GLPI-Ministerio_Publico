package com.glpi.glpi_ministerio_pblico.ui.misPeticiones

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.databinding.FragmentMisPeticionesBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_Tickets
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_Tickets
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import org.json.JSONArray

class MisPeticionesFragment : Fragment() {

    /*creamos la lista de arreglos que tendrá los objetos de la clase Data_Tickets
   esta lista de arreglos (dataModelArrayList) funcionará como fuente de datos*/
    internal lateinit var dataModelArrayList: ArrayList<Data_Tickets>

    //creamos el objeto de la clase RecycleView_Adapter_Tickets
    private var recycleView_Adapter_Tickets: RecycleView_Adapter_Tickets? = null

    //creamos el objeto de la clase recyclerView
    private var recyclerView: RecyclerView? = null

    private var _binding: FragmentMisPeticionesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMisPeticionesBinding.inflate(inflater, container, false)

        recyclerView = binding.recycler//asignamos el recycleview de recycleview_tickets.xml

        fun setupRecycler() {
            recyclerView!!.layoutManager = LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false)

            recycleView_Adapter_Tickets =
                getContext()?.let { RecycleView_Adapter_Tickets(it,dataModelArrayList) }
            recyclerView!!.adapter = recycleView_Adapter_Tickets
        }

        //metodo que nos devuelve los datos para los tickets
        //INICIO obtenemos perfil de usuario con volley

        val url_DataTickets = "http://181.176.145.174:8080/api/user_tickets" //online
        val stringRequestDataTickets = object : StringRequest(Request.Method.POST,
            url_DataTickets, Response.Listener { response ->
                try {
                    val JS_DataTickets = JSONArray(response) //obtenemos el objeto json
                    dataModelArrayList = ArrayList()

                    for (i in 0 until JS_DataTickets.length()){

                        val DataTickets = JS_DataTickets.getJSONObject(i)

                        val playerModel = Data_Tickets()
                        playerModel.setGlpiID(DataTickets.getString("ID"))
                        playerModel.setGlpiDescripcion(DataTickets.getString("DESCRIPCION"))
                        playerModel.setCurrentTime(DataTickets.getString("FECHA"))
                        playerModel.setGlpiName(DataTickets.getString("TECNICO"))

                        dataModelArrayList.add(playerModel)

                        Log.i("mensaje recycler ok: ","main activity: "+ playerModel)
                    }
                    setupRecycler()

                }catch (e:Exception){
                    e.printStackTrace()
                    Toast.makeText(context, "token expirado: $e", Toast.LENGTH_LONG).show()
                    Log.i("mensaje recycler e: ","recycler ERROR: "+e)
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params.put("session_token", token.prefer.getToken())
                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
        //FIN obtenemos perfil de usuario

        /*Este método vinculará el objeto del adaptador a la vista del reciclador
        fun setupRecycler() {
            recyclerView!!.layoutManager = LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false)

            recycleView_Adapter_Tickets = RecycleView_Adapter_Tickets(this,dataModelArrayList)
            recyclerView!!.adapter = recycleView_Adapter_Tickets


        }*/

        val root = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}