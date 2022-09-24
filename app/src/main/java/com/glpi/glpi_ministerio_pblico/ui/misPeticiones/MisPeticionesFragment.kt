package com.glpi.glpi_ministerio_pblico.ui.misPeticiones

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.nameLoginUser
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.databinding.FragmentMisPeticionesBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_Tickets
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_Tickets
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import com.glpi.glpi_ministerio_pblico.ui.tickets.NavFooterTicketsActivity
import org.json.JSONArray
import org.json.JSONObject

class MisPeticionesFragment : Fragment(), RecycleView_Adapter_Tickets.ontickteClickListener {

    /*creamos la lista de arreglos que tendrá los objetos de la clase Data_Tickets
   esta lista de arreglos (dataModelArrayList) funcionará como fuente de datos*/
    internal lateinit var dataModelArrayList: ArrayList<Data_Tickets>

    //creamos el objeto de la clase RecycleView_Adapter_Tickets
    private var recycleView_Adapter_Tickets: RecycleView_Adapter_Tickets? = null

    //creamos el objeto de la clase recyclerView
    private var recyclerView: RecyclerView? = null

    private var _binding: FragmentMisPeticionesBinding? = null

    lateinit var jsonTicketResponse: JSONArray

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
    //): View? {
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMisPeticionesBinding.inflate(inflater, container, false)

        recyclerView = binding.recycler//asignamos el recycleview de recycleview_tickets.xml

        /*Este método vinculará el objeto del adaptador a la vista del reciclador*/
        fun setupRecycler() {
            recyclerView!!.layoutManager = LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false)

            recycleView_Adapter_Tickets =
                getContext()?.let { RecycleView_Adapter_Tickets(it,dataModelArrayList,this) }
            recyclerView!!.adapter = recycleView_Adapter_Tickets
        }

        //metodo que nos devuelve los datos para los tickets
        //INICIO obtenemos perfil de usuario con volley
        //jsonTicketResponse = JSONArray()
        val url_DataTickets = "http://181.176.145.174:8080/api/user_tickets" //online
        val stringRequestDataTickets = @RequiresApi(Build.VERSION_CODES.N)
        object : StringRequest(Request.Method.POST,
            url_DataTickets, Response.Listener { response ->
                try {
                    val JS_DataTickets = JSONArray(response) //obtenemos el objeto json
                    //jsonTicketResponse = JSONArray(response) //obtenemos el objeto json

                    dataModelArrayList = ArrayList()
                    //Log.i("mensaje tasks in",""+jsonTicketResponse)
                    for (i in 0 until JS_DataTickets.length()){

                        val DataTickets = JS_DataTickets.getJSONObject(i)

                        val playerModel = Data_Tickets()
                        /*TODO buscar objeto por ID y guardar su contenido en una variable string, luego convertir
                         TODO ese string en array para buscar los atributos necesarios de forma local */

                        playerModel.setGlpiID(DataTickets.getString("ID"))
                        //MainActivity.idTicket = DataTickets.getString("ID") //aca recibiremos el id para adjutanr al volleyGet

                        playerModel.setGlpiTipo(DataTickets.getString("TIPO"))

                        playerModel.setGlpiDescripcion(DataTickets.getString("DESCRIPCION"))

                        //obtenemos el contenido de la descripción
                        val decoded: String = Html.fromHtml(DataTickets.getString("CONTENIDO")).toString()
                        val decoded2: Spanned = HtmlCompat.fromHtml(decoded,HtmlCompat.FROM_HTML_MODE_COMPACT)
                        playerModel.setGlpiContenido(decoded2.toString())
                        //Log.i("mensaje html: ",""+decoded2)

                        playerModel.setGlpiEstado(DataTickets.getString("ESTADO"))
                        playerModel.setCurrentTime(DataTickets.getString("FECHA"))

                        //obtenemos los datos del usuario logueado
                        val nombreLogin_ = DataTickets.getString("NOMBRE")
                        val apellidoLogin_ = DataTickets.getString("APELLIDO")
                        playerModel.setGlpiLoginName("$nombreLogin_ $apellidoLogin_")
                        nameLoginUser = "$nombreLogin_ $apellidoLogin_"

                        playerModel.setGlpiCategoria(DataTickets.getString("CATEGORIA"))
                        playerModel.setGlpiOrigen(DataTickets.getString("ORIGEN"))
                        playerModel.setGlpiUrgencia(DataTickets.getString("URGENCIA"))

                        //obtenemos los datos del operador
                        val JS_ResipientObject = DataTickets.getJSONObject("RECIPIENT")
                        val recipient = JS_ResipientObject.getJSONObject("0")
                        val Data_RecipientName = recipient.getString("NOMBRE")
                        val Data_RecipientApellido = recipient.getString("APELLIDO")
                        playerModel.setGlpiOperadorName("$Data_RecipientName $Data_RecipientApellido")


                        //obtenemos los datos del solicitante
                        val JS_RequesterObjet = DataTickets.getJSONObject("REQUESTER")
                        val DataRequester = JS_RequesterObjet.getJSONObject("0")
                        val DataRequesterName = DataRequester.getString("NOMBRE")
                        val DataRequesterApellido = DataRequester.getString("APELLIDO")
                        val DataRequesterCargo = DataRequester.getString("CARGO")
                        val DataRequesterUbicacion = DataRequester.getString("UBICACION")
                        val DataRequesterCorreo = DataRequester.getString("CORREO")
                        val DataRequesterTelefono = DataRequester.getString("TELEFONO")
                        playerModel.setGlpiRequestreName("$DataRequesterName $DataRequesterApellido")
                        playerModel.setGlpiRequestreCargo(DataRequesterCargo)
                        playerModel.setGlpiUbicacionSolicitante(DataRequesterUbicacion)
                        playerModel.setGlpiCorreoSolicitante(DataRequesterCorreo)
                        playerModel.setGlpiTelefonoSolicitante(DataRequesterTelefono)

                        //OBTENEMOS LOS TAKS
                        val jsResquestTask = DataTickets.getJSONObject("TASKS")
                        val jsonTasks = jsResquestTask.toString()
                        var iterador = 0
                        var taskDescriptions = ArrayList<String>()
                        var tasksIterador: JSONObject
                        Log.i("mensaje tasks out",""+jsonTasks[2])
                        if (jsonTasks[2] == '0'){
                            if (jsResquestTask.length() > 1){
                                for (i in 0 until jsResquestTask.length()){
                                    Log.i("mensaje tasks in SIZE",""+jsResquestTask.length())
                                    tasksIterador = jsResquestTask.getJSONObject(iterador.toString())
                                    taskDescriptions.add(tasksIterador.getString("DESCRIPCION"))
                                    Log.i("mensaje tasks for",""+iterador.toString()+": "+taskDescriptions)
                                    iterador++
                                }
                            }else{
                                val tasks = jsResquestTask.getJSONObject("0")
                                val tasksDescription = tasks.getString("DESCRIPCION")
                                Log.i("mensaje tasks in true",""+tasksDescription)
                            }
                        }else{
                            //val tasksEmpty = jsonTasks
                            //Log.i("mensaje tasks in false",""+jsResquestTask.getString("msg"))
                        }

                        Log.i("mensaje ok",""+dataModelArrayList)
                        //dataModelArrayList.sortBy { it.getGlpiTipo() }
                        dataModelArrayList.add(playerModel)
                    }
                    setupRecycler()

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "token expirado: $e", Toast.LENGTH_LONG).show()
                    Log.i("mensaje recycler e: ", "recycler ERROR: " + e)
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params.put("session_token", token.prefer.getToken())
                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
        //FIN obtenemos perfil de usuario

        //requesteVolleyTickets()

        val root = binding.root

        return root
    }

    fun requesteVolleyTickets(){
        val url_DataTickets = "http://181.176.145.174:8080/api/user_tickets" //online
        val stringRequestDataTickets = @RequiresApi(Build.VERSION_CODES.N)
        object : StringRequest(Request.Method.POST,
            url_DataTickets, Response.Listener { response ->
                try {
                    val JS_DataTickets = JSONArray(response) //obtenemos el objeto json
                    //jsonTicketResponse = JSONArray(response) //obtenemos el objeto json

                    dataModelArrayList = ArrayList()
                    //Log.i("mensaje tasks in",""+jsonTicketResponse)
                    for (i in 0 until JS_DataTickets.length()){

                        val DataTickets = JS_DataTickets.getJSONObject(i)

                        val playerModel = Data_Tickets()
                        /*TODO buscar objeto por ID y guardar su contenido en una variable string, luego convertir
                         TODO ese string en array para buscar los atributos necesarios de forma local */

                        playerModel.setGlpiID(DataTickets.getString("ID"))


                        playerModel.setGlpiTipo(DataTickets.getString("TIPO"))

                        playerModel.setGlpiDescripcion(DataTickets.getString("DESCRIPCION"))

                        //obtenemos el contenido de la descripción
                        val decoded: String = Html.fromHtml(DataTickets.getString("CONTENIDO")).toString()
                        val decoded2: Spanned = HtmlCompat.fromHtml(decoded,HtmlCompat.FROM_HTML_MODE_COMPACT)
                        playerModel.setGlpiContenido(decoded2.toString())
                        //Log.i("mensaje html: ",""+decoded2)

                        playerModel.setGlpiEstado(DataTickets.getString("ESTADO"))
                        playerModel.setCurrentTime(DataTickets.getString("FECHA"))

                        //obtenemos los datos del usuario logueado
                        val nombreLogin_ = DataTickets.getString("NOMBRE")
                        val apellidoLogin_ = DataTickets.getString("APELLIDO")
                        playerModel.setGlpiLoginName("$nombreLogin_ $apellidoLogin_")
                        nameLoginUser = "$nombreLogin_ $apellidoLogin_"

                        playerModel.setGlpiCategoria(DataTickets.getString("CATEGORIA"))
                        playerModel.setGlpiOrigen(DataTickets.getString("ORIGEN"))
                        playerModel.setGlpiUrgencia(DataTickets.getString("URGENCIA"))

                        //obtenemos los datos del operador
                        val JS_ResipientObject = DataTickets.getJSONObject("RECIPIENT")
                        val recipient = JS_ResipientObject.getJSONObject("0")
                        val Data_RecipientName = recipient.getString("NOMBRE")
                        val Data_RecipientApellido = recipient.getString("APELLIDO")
                        playerModel.setGlpiOperadorName("$Data_RecipientName $Data_RecipientApellido")


                        //obtenemos los datos del solicitante
                        val JS_RequesterObjet = DataTickets.getJSONObject("REQUESTER")
                        val DataRequester = JS_RequesterObjet.getJSONObject("0")
                        val DataRequesterName = DataRequester.getString("NOMBRE")
                        val DataRequesterApellido = DataRequester.getString("APELLIDO")
                        val DataRequesterCargo = DataRequester.getString("CARGO")
                        val DataRequesterUbicacion = DataRequester.getString("UBICACION")
                        val DataRequesterCorreo = DataRequester.getString("CORREO")
                        val DataRequesterTelefono = DataRequester.getString("TELEFONO")
                        playerModel.setGlpiRequestreName("$DataRequesterName $DataRequesterApellido")
                        playerModel.setGlpiRequestreCargo(DataRequesterCargo)
                        playerModel.setGlpiUbicacionSolicitante(DataRequesterUbicacion)
                        playerModel.setGlpiCorreoSolicitante(DataRequesterCorreo)
                        playerModel.setGlpiTelefonoSolicitante(DataRequesterTelefono)

                        //OBTENEMOS LOS TAKS
                        val jsResquestTask = DataTickets.getJSONObject("TASKS")
                        val jsonTasks = jsResquestTask.toString()
                        var iterador = 0
                        var taskDescriptions = ArrayList<String>()
                        var tasksIterador: JSONObject
                        Log.i("mensaje tasks out",""+jsonTasks[2])
                        if (jsonTasks[2] == '0'){
                            if (jsResquestTask.length() > 1){
                                for (i in 0 until jsResquestTask.length()){
                                    Log.i("mensaje tasks in SIZE",""+jsResquestTask.length())
                                    tasksIterador = jsResquestTask.getJSONObject(iterador.toString())
                                    taskDescriptions.add(tasksIterador.getString("DESCRIPCION"))
                                    Log.i("mensaje tasks for",""+iterador.toString()+": "+taskDescriptions)
                                    iterador++
                                }
                            }else{
                                val tasks = jsResquestTask.getJSONObject("0")
                                val tasksDescription = tasks.getString("DESCRIPCION")
                                Log.i("mensaje tasks in true",""+tasksDescription)
                            }
                        }else{
                            //val tasksEmpty = jsonTasks
                            //Log.i("mensaje tasks in false",""+jsResquestTask.getString("msg"))
                        }

                        Log.i("mensaje ok",""+taskDescriptions)
                        dataModelArrayList.add(playerModel)
                    }
                    //setupRecycler()

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "token expirado: $e", Toast.LENGTH_LONG).show()
                    Log.i("mensaje recycler e: ", "recycler ERROR: " + e)
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params.put("session_token", token.prefer.getToken())
                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTicketClick(
        TicketID: String, //TODO : pasar id y hacer nueva consulta a la API para evitar sobreCargar la vista
        OperadorName: Any,
        CurrentTime: Any,
        Contenido: Any,
        Tipo: String,
        Ubicacion: String,
        Correo: String,
        NameSolicitante: String,
        CargoSolicitante: String,
        TelefonoSolicitante: String,
        LoginName: String,
        TicketEstado: String,
        TicketCategoria: String,
        TicketOrigen: String,
        TicketUrgencia: String,
        taskName: String,
        taskDescription: String
    ) {
        val intent = Intent(context, NavFooterTicketsActivity::class.java)

        val bundle = Bundle()

        bundle.putString("TicketID", TicketID)
        bundle.putString("NameOperador", OperadorName.toString())
        bundle.putString("CurrentTime", CurrentTime.toString())
        bundle.putString("Contenido", Contenido.toString())
        bundle.putString("Tipo", Tipo)
        bundle.putString("Ubicacion", Ubicacion)
        bundle.putString("Correo", Correo)
        bundle.putString("NameSolicitante", NameSolicitante)
        bundle.putString("CargoSolicitante", CargoSolicitante)
        bundle.putString("TelefonoSolicitante", TelefonoSolicitante)
        bundle.putString("LoginName", LoginName)
        bundle.putString("TicketEstado", TicketEstado)
        bundle.putString("TicketCategoria", TicketCategoria)
        bundle.putString("TicketOrigen", TicketOrigen)
        bundle.putString("TicketUrgencia", TicketUrgencia)
        bundle.putString("taskName", taskName)
        bundle.putString("taskDescription", taskDescription)

        intent.putExtras(bundle)

        startActivity(intent)

    }

}