package com.glpi.glpi_ministerio_pblico.ui.misIncidencias

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.MainActivity.Companion.urlApi_TicketSortByIncident
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.databinding.FragmentMisIncidenciasBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_Tickets
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_Tickets
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import com.glpi.glpi_ministerio_pblico.ui.tickets.NavFooterTicketsActivity
import org.json.JSONObject



class MisIncidenciasFragment : Fragment(), RecycleView_Adapter_Tickets.onTicketClickListener {
    /*creamos la lista de arreglos que tendrá los objetos de la clase Data_Tickets
       esta lista de arreglos (dataModelArrayList) funcionará como fuente de datos*/
    internal lateinit var dataModelArrayListIncident: ArrayList<Data_Tickets>
    //creamos el objeto de la clase RecycleView_Adapter_Tickets
    private var recycleView_Adapter_TicketsIncidencias: RecycleView_Adapter_Tickets? = null

    //creamos el objeto de la clase recyclerView
    private var recyclerView: RecyclerView? = null

    private var _binding: FragmentMisIncidenciasBinding? = null
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
        _binding = FragmentMisIncidenciasBinding.inflate(inflater, container, false)


        volleyRequestSortByIncident(urlApi_TicketSortByIncident)
        Toast.makeText(context,"MisIncidenciasFragment", Toast.LENGTH_LONG).show()

        //return inflater.inflate(R.layout.fragment_mis_incidencias, container, false)
        val root = binding.root
        return root
    }

    private fun volleyRequestSortByIncident(urlApi_TicketSortByIncident: String) {
        //metodo que nos devuelve los datos para los tickets
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            urlApi_TicketSortByIncident, Response.Listener { response ->
                try {
                    val dataTicketsJson = JSONObject(response) //obtenemos el objeto json

                    dataModelArrayListIncident = ArrayList()

                    for (i in 0 until dataTicketsJson.length()){

                        val dataTickets = dataTicketsJson.getJSONObject(i.toString())
                        val ticketsModel = Data_Tickets()

                        ticketsModel.ticketSortsId = dataTickets.getString("ID")
                        ticketsModel.ticketSortsType = dataTickets.getString("TIPO")
                        ticketsModel.ticketSortsDescription = dataTickets.getString("DESCRIPCION")
                        ticketsModel.ticketSortsContent =
                            MainActivity.decodeHtml(dataTickets.getString("CONTENIDO"))
                        ticketsModel.ticketSortsStatus = dataTickets.getString("ESTADO")
                        ticketsModel.ticketSortsDate = dataTickets.getString("FECHA")
                        ticketsModel.ticketSortsModificationDate = dataTickets.getString("FECHA_MODIFICACION")
                        ticketsModel.ticketSortsCloseDate = dataTickets.getString("FECHA_CIERRE")
                        ticketsModel.ticketSortsCreationDate = dataTickets.getString("FECHA_CREACION")

                        //obtenemos el id del operador, creador del ticket
                        ticketsModel.ticketSortsIdRecipient = dataTickets.getString("ID_RECIPIENT")

                        //obtenemos los datos del tecnico asigando al ticket
                        val idPositionTechnician = dataTickets.getJSONObject("ID_TECHNICIAN")
                        val dataTechnician = idPositionTechnician.getJSONObject("0")
                        val technician = dataTechnician.getJSONObject("0")
                        ticketsModel.ticketSortsIdTechnician = technician.getString("ID_USER")
                        ticketsModel.ticketSortsUserTechnician = technician.getString("USUARIO")
                        ticketsModel.ticketSortsNameTechnician = technician.getString("NOMBRE")
                        ticketsModel.ticketSortsLastNameTechnician = technician.getString("APELLIDO")
                        ticketsModel.ticketSortsPhoneTechnician = technician.getString("TELEFONO")
                        ticketsModel.ticketSortsPositionTechnician = technician.getString("CARGO")
                        ticketsModel.ticketSortsEmailTechnician = technician.getString("CORREO")
                        ticketsModel.ticketSortsLocationTechnician = technician.getString("UBICACION")
                        ticketsModel.ticketSortsEntityTechnician = technician.getString("ENTIDAD")
                        //MainActivity.idUserTechnician = idTechnician


                        //obtenemos los datos del solicitante
                        val idPositionResquester = dataTickets.getJSONObject("ID_REQUESTER")
                        val dataRequester = idPositionResquester.getJSONObject("0")
                        val requester = dataRequester.getJSONObject("0")
                        ticketsModel.ticketSortsIdRequester = requester.getString("ID_USER")
                        ticketsModel.ticketSortsUserRequester = requester.getString("USUARIO")
                        ticketsModel.ticketSortsNameRequester = requester.getString("NOMBRE")
                        ticketsModel.ticketSortsLastNameRequester = requester.getString("APELLIDO")
                        ticketsModel.ticketSortsPhoneRequester = requester.getString("TELEFONO")
                        ticketsModel.ticketSortsPositionRequester = requester.getString("CARGO")
                        ticketsModel.ticketSortsEmailRequester = requester.getString("CORREO")
                        ticketsModel.ticketSortsLocationRequester = requester.getString("UBICACION")
                        ticketsModel.ticketSortsEntityRequester = requester.getString("ENTIDAD")

                        //obtenemos datos del tecnico asignado al ticket
                        ticketsModel.ticketSortsUser = dataTickets.getString("USUARIO")
                        ticketsModel.ticketSortsNameUser = dataTickets.getString("NOMBRE")
                        ticketsModel.ticketSortsLastNameUser = dataTickets.getString("APELLIDO")

                        /*MainActivity.userTechnician = dataTickets.getString("USUARIO")
                        val technicianName = dataTickets.getString("NOMBRE")
                        MainActivity.nameTechnician = technicianName
                        val technicianLastName = dataTickets.getString("APELLIDO")
                        MainActivity.lastNameTechnician = technicianLastName
                        ticketsModel.setTechnicianName("$technicianName $technicianLastName")
                        MainActivity.nameLoginUser = "$technicianName $technicianLastName"*/

                        ticketsModel.ticketSortsCategory = dataTickets.getString("CATEGORIA")
                        ticketsModel.ticketSortsSource = dataTickets.getString("ORIGEN")
                        ticketsModel.ticketSortsUrgency = dataTickets.getString("URGENCIA")

                        dataModelArrayListIncident.add(ticketsModel)
                    }
                    setupRecycler()

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "token expirado_: $e", Toast.LENGTH_LONG).show()
                    //Log.i("mensaje recycler e: ", "recycler ERROR: $e")
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()

                return params
            }
        }
        context?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
        //FIN obtenemos perfil de usuario
    }

    fun setupRecycler() {
        recyclerView = binding.recyclerViewMisIncidencias//asignamos el recycleview de recycleview_tickets.xml
        /*recyclerView!!.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, true,)*/
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //layoutManager.stackFromEnd = true

        recyclerView!!.layoutManager = layoutManager

        recycleView_Adapter_TicketsIncidencias =
            context?.let { RecycleView_Adapter_Tickets(it,dataModelArrayListIncident,this) }
        recyclerView!!.adapter = recycleView_Adapter_TicketsIncidencias
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTicketClick(
        ticketSortsId: String,
        ticketSortsType: String,
        ticketSortsContent: String,
        ticketSortsStatus: String,
        ticketSortsCreationDate: String,
        ticketSortsModificationDate: String,
        ticketSortsIdRecipient: String,

        ticketSortsIdTechnician: String,
        ticketSortsNameTechnician: String,
        ticketSortsLastNameTechnician: String,
        ticketSortsPhoneTechnician: String,
        ticketSortsEmailTechnician: String,

        ticketSortsIdRequester: String,
        ticketSortsNameRequester: String,
        ticketSortsLastNameRequester: String,
        ticketSortsPhoneRequester: String,
        ticketSortsPositionRequester: String,
        ticketSortsEmailRequester: String,
        ticketSortsLocationRequester: String,

        ticketSortsCategory: String,
        ticketSortsSource: String,
        ticketSortsUrgency: String
    ) {
        val intent = Intent(context, NavFooterTicketsActivity::class.java)
        val bundle = Bundle()

        bundle.putString("ticketSortsId", ticketSortsId)
        bundle.putString("ticketSortsType", ticketSortsType)
        bundle.putString("ticketSortsContent", ticketSortsContent)
        bundle.putString("ticketSortsStatus", ticketSortsStatus)
        bundle.putString("ticketSortsCreationDate", ticketSortsCreationDate)
        bundle.putString("ticketSortsModificationDate", ticketSortsCreationDate)
        bundle.putString("ticketSortsIdRecipient", ticketSortsIdRecipient)

        bundle.putString("ticketSortsIdTechnician", ticketSortsIdTechnician)
        bundle.putString("ticketSortsNameTechnician", ticketSortsNameTechnician)
        bundle.putString("ticketSortsLastNameTechnician", ticketSortsLastNameTechnician)
        bundle.putString("ticketSortsPhoneTechnician", ticketSortsPhoneTechnician)
        bundle.putString("ticketSortsEmailTechnician", ticketSortsEmailTechnician)

        bundle.putString("ticketSortsIdRequester", ticketSortsIdRequester)
        bundle.putString("ticketSortsNameRequester", ticketSortsNameRequester)
        bundle.putString("ticketSortsLastNameRequester", ticketSortsLastNameRequester)
        bundle.putString("ticketSortsPhoneRequester", ticketSortsPhoneRequester)
        bundle.putString("ticketSortsPositionRequester", ticketSortsPositionRequester)
        bundle.putString("ticketSortsEmailRequester", ticketSortsEmailRequester)
        bundle.putString("ticketSortsLocationRequester", ticketSortsLocationRequester)

        bundle.putString("ticketSortsCategory", ticketSortsCategory)
        bundle.putString("ticketSortsSource", ticketSortsSource)
        bundle.putString("ticketSortsUrgency", ticketSortsUrgency)

        intent.putExtras(bundle)
        startActivity(intent)
    }
}