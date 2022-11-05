package com.glpi.glpi_ministerio_pblico.ui.tickets

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.MainActivity

import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.VolleySingleton
import com.glpi.glpi_ministerio_pblico.data.database.TicketInfoDB
import com.glpi.glpi_ministerio_pblico.databinding.FragmentTicketInfoBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_TicketInfo
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecyclerAdapter
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TicketInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TicketInfoFragment : Fragment(), RecyclerAdapter.onConversationClickListener {


    private var _binding: FragmentTicketInfoBinding? = null
    private val binding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    private var recycleViewAdapterTicketsInfo: RecyclerAdapter? = null
    private lateinit var jsonObjectResponse: JSONObject
    internal lateinit var dataModelArrayListTicketInfo: ArrayList<Data_TicketInfo>

    lateinit private var progressBarTicketInfo: ProgressBar

    private lateinit var jsonArrayIdRecipient: JSONArray

    var isEditFollowup = false
    var positionFollowup = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentTicketInfoBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_ticket_info, container, false)
        progressBarTicketInfo = binding.progressBarTicketInfo

        getTicketInfo()
        volleyRequestTicketInfo()
        volleyRequestIdRecipient()





        val root = binding.root
        return root
    }


    @SuppressLint("SetTextI18n")
    private fun getTicketInfo(){
        val room  = Room.databaseBuilder(requireContext(), TicketInfoDB::class.java,"ticketInfoBD").build()
        lifecycleScope.launch{
            val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
            for (item in getTicketInfoDB) {
                //descripción del ticket
                binding.tvTicketInfoId.text = "Petición #${item.ticketSortsID}"
                when(item.ticketSortsStatus){
                    "EN CURSO (Asignada)" -> {
                        binding.tvTicketInfoStatus.setBackgroundResource(R.drawable.ic_circulo_verde)
                        binding.tvTicketInfoStatus.tag = "2"
                    }
                    "2" -> {
                        binding.tvTicketInfoStatus.setBackgroundResource(R.drawable.ic_circulo_verde)
                        binding.tvTicketInfoStatus.tag = "2"
                    }
                    "EN ESPERA" -> {
                        binding.tvTicketInfoStatus.setBackgroundResource(R.drawable.ic_circulo)
                        binding.tvTicketInfoStatus.tag = "4"
                    }
                    "4" -> {
                        binding.tvTicketInfoStatus.setBackgroundResource(R.drawable.ic_circulo)
                        binding.tvTicketInfoStatus.tag = "4"
                    }
                    "CERRADO" -> binding.tvTicketInfoStatus.setBackgroundResource(R.drawable.ic_circulo_negro)
                    "6" -> {
                        binding.tvTicketInfoStatus.setBackgroundResource(R.drawable.ic_circulo_negro)
                        binding.tvTicketInfoStatus.tag = "6"
                    }
                }

                binding.tvTicketInfoLocation.text = item.ticketSortsLocationRequester

                binding.includeTicketsHistorico.txtDescripcionTicketHistorico.text = item.ticketSortsContent
                binding.includeTicketsHistorico.txtCurrentTime.text = "Fecha de Cración: ${item.ticketSortsCreationDate}"
                binding.includeTicketsHistorico.txtModificationDate.text = "Ultima modificación: ${item.ticketSortsModificationDate}"
            }

        }

        /*when(MainActivity.updateFragmentFlag){
            true -> {
                Log.i("mensaje prefersort", prefer.getTicketSortsStatus())
                when(prefer.getTicketSortsStatus()){
                    "2" -> binding.tvTicketInfoStatus.setBackgroundResource(R.drawable.ic_circulo_verde)
                    "4" -> binding.tvTicketInfoStatus.setBackgroundResource(R.drawable.ic_circulo)
                }
            }
            false -> {
                val room  = Room.databaseBuilder(requireContext(), TicketInfoDB::class.java,"ticketInfoBD").build()
                lifecycleScope.launch{
                    val getTicketInfoDB = room.daoTicketInfo().getTicketInfo()
                    for (item in getTicketInfoDB) {
                        //descripción del ticket
                        binding.tvTicketInfoId.text = "Petición #${item.ticketSortsID}"
                        when(item.ticketSortsStatus){
                            "EN CURSO (Asignada)" -> binding.tvTicketInfoStatus.setBackgroundResource(R.drawable.ic_circulo_verde)
                            "EN ESPERA" -> binding.tvTicketInfoStatus.setBackgroundResource(R.drawable.ic_circulo)
                            "CERRADO" -> binding.tvTicketInfoStatus.setBackgroundResource(R.drawable.ic_circulo_negro)
                        }
                        binding.tvTicketInfoLocation.text = item.ticketSortsLocationRequester

                        binding.includeTicketsHistorico.txtDescripcionTicketHistorico.text = item.ticketSortsContent
                        binding.includeTicketsHistorico.txtCurrentTime.text = "Fecha de Cración: ${item.ticketSortsCreationDate}"
                        binding.includeTicketsHistorico.txtModificationDate.text = "Ultima modificación: ${item.ticketSortsModificationDate}"
                    }

                }
            }
        }*/
    }


    private fun volleyRequestIdRecipient(){
        /*val bundle = intent.extras
        val idRecipient = bundle!!.getString("ticketSortsIdRecipient")*/

        Log.i("mensaje prefer","${MainActivity.urlApi_TasksUsers}${prefer.getRecipientId()} ")
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_TasksUsers +prefer.getRecipientId(), Response.Listener { response ->
                try {
                    jsonArrayIdRecipient = JSONArray()
                    jsonArrayIdRecipient = JSONArray(response)
                    val dataId = jsonArrayIdRecipient.getJSONObject(0)
                    val nameId = dataId.getString("NOMBRE")
                    val lastNameId = dataId.getString("APELLIDO")
                    val cellPhone = dataId.getString("TELEFONO")


                    binding.includeTicketsHistorico.txtNameOperador.text = "$nameId $lastNameId"
                    //binding.includeTickets.txtTasksUserName.text = " - $nameId $lastNameId"
                    if (cellPhone != " " && cellPhone != "null"){
                        //binding.includeTickets.labelAsignadoCelular.text = cellPhone
                        //binding.includeTickets.labelAsignadoCelular.isVisible = true
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
        VolleySingleton.getInstance(requireContext()).addToRequestQueue(stringRequestDataTickets)
        //FIN volley ------------------------------------------------------------
    }

    /*private fun volleyRequestIdTechnician(){
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_TasksUsers+ prefer.getId(), Response.Listener { response ->
                try {
                    //val jsonArrayTechnicianTask = JSONArray()
                    val jsonArrayTechnicianTask = JSONArray(response)
                    val dataTechnician = jsonArrayTechnicianTask.getJSONObject(0)
                    val nameTechnicianTask = dataTechnician.getString("NOMBRE")
                    val lastNameTechnicianTask = dataTechnician.getString("APELLIDO")
                    b
                    //binding.includeTickets.labelAsignadoNombre.text = "$nameId $lastNameId"
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = token.prefer.getToken()
                return params
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequestDataTickets)
    }*/

    //consultamos la información de ticket por id del ticket
    private fun volleyRequestTicketInfo() {
        /*val bundle = intent.extras
        val ticketSortsId = bundle!!.getString("ticketSortsId")*/

        jsonObjectResponse = JSONObject()

        Log.i("mensaje prefer","${MainActivity.urlApi_TicketID}${223866}")
        val stringRequestDataTickets = object : StringRequest(Method.POST,
            MainActivity.urlApi_TicketID+prefer.getTicketSortsId(), Response.Listener { response ->
                try {

                    dataModelArrayListTicketInfo = ArrayList()
                    jsonObjectResponse = JSONObject(response)
                    var ticketInfoJson: JSONObject
                    for (i in  0 until jsonObjectResponse.length()){
                        val ticketInfo = Data_TicketInfo()
                        if (jsonObjectResponse.getString(i.toString())[2] == 'T'){

                            ticketInfoJson = jsonObjectResponse.getJSONObject(i.toString())

                            ticketInfo.ticketInfoType = ticketInfoJson.getString("TIPO")

                            ticketInfo.ticketInfoId = ticketInfoJson.getString("ID")

                            if (ticketInfo.ticketInfoType != "SOLUTION"){
                                ticketInfo.ticketInfoPrivate = ticketInfoJson.getString("PRIVADO")
                            }

                            ticketInfo.ticketInfoIdUser = ticketInfoJson.getString("ID_USUARIO")

                            ticketInfo.ticketInfoNameUser = ticketInfoJson.getString("NOMBRE")
                            ticketInfo.ticketInfoLastNameUser = ticketInfoJson.getString("APELLIDO")

                            ticketInfo.ticketInfoDate = ticketInfoJson.getString("FECHA") //para ordenar el historico

                            ticketInfo.ticketInfoCreationDate = ticketInfoJson.getString("FECHA_CREACION")
                            if (ticketInfo.ticketInfoType == "FOLLOWUP"){
                                ticketInfo.ticketInfoModificationDate = ticketInfoJson.getString("FECHA_MODIFICACION")
                                ticketInfo.ticketInfoIdSource = ticketInfoJson.getString("ID_ORIGEN")
                                ticketInfo.ticketInfoSource = ticketInfoJson.getString("ORIGEN")
                            }

                            ticketInfo.ticketInfoContent =
                                MainActivity.decodeHtml(ticketInfoJson.getString("CONTENIDO"))

                            if (ticketInfo.ticketInfoType == "TASK"){
                                ticketInfo.ticketInfoModificationDate = ticketInfoJson.getString("FECHA_EDICION")
                                ticketInfo.ticketInfoIdTechnician = ticketInfoJson.getString("TECNICO")
                                ticketInfo.ticketInfoTimeToSolve = ticketInfoJson.getString("DURACION").split(".")[0]

                                ticketInfo.ticketInfoStatus = ticketInfoJson.getString("ESTADO") //pendiente o terminado

                                ticketInfo.ticketInfoIdCategory = ticketInfoJson.getString("ID_CATEGORIA")
                                ticketInfo.ticketInfoCategory = ticketInfoJson.getString("CATEGORIA")

                            }
                            dataModelArrayListTicketInfo.add(ticketInfo)
                        }
                    }
                    progressBarTicketInfo.isVisible = false
                    binding.includeTicketsHistorico.layoutHistorico.isVisible = true
                    setupRecycler()
                } catch (e: Exception) {
                    e.printStackTrace()
                    //Toast.makeText(context, "sin valor para ...: $e", Toast.LENGTH_LONG).show()
                    //Log.i("mensaje entitis dentroE",""+response.get(0))
                }
            }, Response.ErrorListener {
                //Toast.makeText(context, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = prefer.getToken()
                return params
            }
        }
        this?.let {
            context?.let { it1 -> VolleySingleton.getInstance(it1).addToRequestQueue(stringRequestDataTickets) }
        }

        //getTicketsConversationInfo()
    }

    fun setupRecycler() {
        recyclerView = binding.recyclerViewTicketInfoFragment//asignamos el recycleview de recycleview_tickets.xml
        /*recyclerView!!.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, true,)*/
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //layoutManager.stackFromEnd = true

        dataModelArrayListTicketInfo.sortByDescending { it.ticketInfoDate}
        recyclerView!!.layoutManager = layoutManager
        recycleViewAdapterTicketsInfo = RecyclerAdapter(requireContext(),dataModelArrayListTicketInfo,this)
        recyclerView!!.adapter = recycleViewAdapterTicketsInfo
    }




    override fun onEditClick(
        ticketInfoType: String,
        ticketInfoContent: String,
        ticketInfoPrivate: String,
        ticketInfoIdTechnician: String,
        ticketInfoStatus: String,
        ticketInfoIdTemplate: String,
        ticketInfoIdCategory: String,
        ticketInfoCategory: String,
        ticketInfoId: String,
        ticketInfoIdSource: String,
        ticketInfoSource: String,
        ticketInfoTimeToSolve: String,
        adapterPosition: Int
    ) {
        val flagOnEditClick = true

        val intentTasks = (Intent(context,TicketsAgregarTareaActivity::class.java))
        intentTasks.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val intentFollowUp = (Intent(context,TicketsAgregarSeguimientoActivity::class.java))
        intentFollowUp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        when(ticketInfoType){
            "TASK" -> {
                intentTasks.putExtra("ticketInfoContent",ticketInfoContent)
                intentTasks.putExtra("ticketInfoPrivate",ticketInfoPrivate)
                intentTasks.putExtra("ticketInfoIdTechnician",ticketInfoIdTechnician)
                intentTasks.putExtra("ticketInfoStatus",ticketInfoStatus)
                intentTasks.putExtra("ticketInfoIdTemplate",ticketInfoIdTemplate)
                intentTasks.putExtra("ticketInfoIdCategory",ticketInfoIdCategory)
                intentTasks.putExtra("ticketInfoCategory",ticketInfoCategory)
                intentTasks.putExtra("ticketInfoId",ticketInfoId) //origen del
                intentTasks.putExtra("ticketInfoTimeToSolve",ticketInfoTimeToSolve)
                intentTasks.putExtra("flagOnEditClick",flagOnEditClick)
                startActivity(intentTasks)
            }
            "FOLLOWUP" -> {
                intentFollowUp.putExtra("ticketPrivate",ticketInfoPrivate)
                intentFollowUp.putExtra("ticketInfoContent",ticketInfoContent)
                intentFollowUp.putExtra("ticketInfoId",ticketInfoId)
                intentFollowUp.putExtra("ticketInfoIdSource",ticketInfoIdSource)
                intentFollowUp.putExtra("ticketInfoSource",ticketInfoSource)
                intentFollowUp.putExtra("flagOnEditClick",flagOnEditClick)
                startActivity(intentFollowUp)
            }
        }
        isEditFollowup = true
        positionFollowup = adapterPosition
    }

    override fun onFabClick() {
        TODO("Not yet implemented")
    }

}