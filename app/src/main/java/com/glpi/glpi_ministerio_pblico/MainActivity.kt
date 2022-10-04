package com.glpi.glpi_ministerio_pblico

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.databinding.ActivityMainBinding
import com.glpi.glpi_ministerio_pblico.ui.misPeticiones.MisPeticionesFragment
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import com.glpi.glpi_ministerio_pblico.utilities.Utils_Global
import com.google.android.material.navigation.NavigationView
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


//class MainActivity : AppCompatActivity(),RecycleView_Adapter_Perfiles.ItemClickListener {
class MainActivity : AppCompatActivity(){
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    companion object{
        val urlApi_Entities: String = "http://181.176.145.174:8080/api/user_entities"
        val urlApi_Profiles: String = "http://181.176.145.174:8080/api/user_profiles"
        val urlApi_TicketID: String = "http://181.176.145.174:8080/api/ticket_info/" //consulta si el ticket tiene tareas,soluciones o seguimiente, pasarle id de ticket
        val urlApi_Ticket: String = "http://181.176.145.174:8080/api/ticket_sorts/General"
        val urlApi_TicketSorts: String = "http://181.176.145.174:8080/api/ticket_sorts/SortByStatus"
        val urlApi_TasksUsers: String = "http://181.176.145.174:8080/api/task_users/" //para consultar id de: user,technician,requester
        val urlApi_TasksTemplate: String = "http://181.176.145.174:8080/api/task_templates"

        lateinit var nameLoginUser: String
        lateinit var jsonObjectResponse: JSONObject
        lateinit var jsonArrayResponse: JSONArray

        var flag = false

        //variables para filtrar tickets
        var flagFilter = false
        var checkBoxNewTicket = false
        var checkBoxAssignedTicket = false
        var checkBoxPlannedTicket = false
        var checkBoxWaitTicket = false
        var checkBoxCloseTicket = false
        var checkNewTicket: String = "0"
        var checkAssignedTicket: String = "0"
        var checkPlannedTicket: String = "0"
        var checkWaitTicket: String = "0"
        var checkCloseTicket: String = "0"

        fun decodeHtml(contenido: String): String{
            val decoded: String = Html.fromHtml(contenido).toString()
            val decoded2: Spanned = HtmlCompat.fromHtml(decoded, HtmlCompat.FROM_HTML_MODE_COMPACT)
            return decoded2.toString()
        }
    }

    //lateinit var jsonTicketResponse: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        /*binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        /* menu should be considered as top level destinations.*/
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_mis_peticiones,R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.acttivity_misIncidencias
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        userEntities()

        userProfiles()

        //INICIO - boton filtro de la derecha - activity_filtro_right.xml
        binding.appBarMain.btnFiltroRight.setOnClickListener {
            binding.appBarMain.includeFiltroRight.LinearLayoutActivityFiltroRight.isVisible = true
        }
        binding.appBarMain.includeFiltroRight.LinearLayoutActivityFiltroRight.setOnClickListener {
            binding.appBarMain.includeFiltroRight.LinearLayoutActivityFiltroRight.isVisible = false
        }
        //check para filtroRight de tickets

        binding.appBarMain.includeFiltroRight.checkBoxNewTicket.isChecked = true
        binding.appBarMain.includeFiltroRight.checkBoxAssignedTicket.isChecked = true
        binding.appBarMain.includeFiltroRight.checkBoxPlannedTicket.isChecked = true

        binding.appBarMain.includeFiltroRight.btnApplyFilter.setOnClickListener {
            flagFilter = true
            if (binding.appBarMain.includeFiltroRight.checkBoxNewTicket.isChecked){
                checkNewTicket = "1"
            }
            if (binding.appBarMain.includeFiltroRight.checkBoxAssignedTicket.isChecked){
                checkAssignedTicket = "2"
            }
            if (binding.appBarMain.includeFiltroRight.checkBoxPlannedTicket.isChecked){
                checkPlannedTicket = "3"
            }
            if (binding.appBarMain.includeFiltroRight.checkBoxWaitTicket.isChecked){
                checkWaitTicket = "4"
            }
            if (binding.appBarMain.includeFiltroRight.checkBoxCloseTicket.isChecked){
                checkCloseTicket = "6"
            }
            //checkBoxNewTicket = binding.appBarMain.includeFiltroRight.checkBoxNewTicket.isChecked
            //checkBoxAssignedTicket = binding.appBarMain.includeFiltroRight.checkBoxAssignedTicket.isChecked
            //checkBoxPlannedTicket = binding.appBarMain.includeFiltroRight.checkBoxPlannedTicket.isChecked
            //checkBoxWaitTicket = binding.appBarMain.includeFiltroRight.checkBoxWaitTicket.isChecked
            //checkBoxCloseTicket = binding.appBarMain.includeFiltroRight.checkBoxCloseTicket.isChecked

            //cerramos y volvemos a abrir el fragment para recargar su contenido
            replaceFragment(MisPeticionesFragment())

        }

        binding.appBarMain.includeFiltroRight.btnClearFilter.setOnClickListener {
            flagFilter = false
            checkNewTicket = "0"
            checkAssignedTicket = "0"
            checkPlannedTicket = "0"
            checkWaitTicket = "0"
            checkCloseTicket = "0"
            binding.appBarMain.includeFiltroRight.checkBoxNewTicket.isChecked = false
            binding.appBarMain.includeFiltroRight.checkBoxAssignedTicket.isChecked = false
            binding.appBarMain.includeFiltroRight.checkBoxPlannedTicket.isChecked = false
            binding.appBarMain.includeFiltroRight.checkBoxWaitTicket.isChecked = false
            binding.appBarMain.includeFiltroRight.checkBoxCloseTicket.isChecked = false

            //cerramos y volvemos a abrir el fragment para recargar su contenido
            replaceFragment(MisPeticionesFragment())
        }

        //boton que despliega menu de filtro por fechas
        var clickDesplegar = false
        binding.appBarMain.includeFiltroRight.btnDesplegarFechaFiltroRight.setOnClickListener {
            if (clickDesplegar == false){
                binding.appBarMain.includeFiltroRight.btnUltModificacionFiltroRight.isVisible = true
                binding.appBarMain.includeFiltroRight.btnFechaAperturaFiltroRight.isVisible = true
                binding.appBarMain.includeFiltroRight.btnFechaCierreFiltroRight.isVisible = true
                clickDesplegar = true
            }else{
                binding.appBarMain.includeFiltroRight.btnUltModificacionFiltroRight.isVisible = false
                binding.appBarMain.includeFiltroRight.btnFechaAperturaFiltroRight.isVisible = false
                binding.appBarMain.includeFiltroRight.btnFechaCierreFiltroRight.isVisible = false
                clickDesplegar = false
            }
        }
        //boton que abre calendario modal filtro por ultima modificación
        var flagUltimaModificacion = false
        var flagFechaApertura = false
        var flagFechaCierre = false
        binding.appBarMain.includeFiltroRight.btnUltModificacionFiltroRight.setOnClickListener {
            binding.appBarMain.includeModalCalendario.LinearLayoutFiltroCalendario.isVisible = true
            flagUltimaModificacion = true
            flagFechaApertura = false
            flagFechaCierre = false
        }
        //boton dia inicio del calendario
        val calendarView: CalendarView = binding.appBarMain.includeModalCalendario.calendarView
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("es", "ES"))//obtenemos fecha actual
        sdf.timeZone = TimeZone.getTimeZone("UTC-5")
        val currentdate = sdf.format(Date()).replace("/",",")
        val textElements: ArrayList<String> = currentdate.split(",") as ArrayList

        val calendar = Calendar.getInstance()
        calendar.set(textElements[2].toInt(),textElements[1].toInt()-1,textElements[0].toInt())
        calendarView.maxDate = calendar.timeInMillis//asignamos la fecha maxima que puede seleccionar
        calendarView.setOnDateChangeListener { calendarView, i, i2, i3 ->
            val i2_ = i2+1//se le tiene que aumentar 1 ya que devuelve con un mes de retraso
            binding.appBarMain.includeModalCalendario.btnStarDay.setOnClickListener {
                binding.appBarMain.includeModalCalendario.btnAceptarFecha.isEnabled = true
                binding.appBarMain.includeModalCalendario.btnEndDay.isEnabled = true

                binding.appBarMain.includeModalCalendario.fechaStart.text =
                    "Dia Inicio: $i3/$i2_/$i"
            }
            binding.appBarMain.includeModalCalendario.btnEndDay.setOnClickListener {
                //binding.appBarMain.includeModalCalendario.imgDelete.isVisible = true
                binding.appBarMain.includeModalCalendario.fechaEnd.text =
                    "   Dia Fin: $i3/$i2_/$i"
            }
        }
        binding.appBarMain.includeModalCalendario.btnAceptarFecha.setOnClickListener {
            binding.appBarMain.includeModalCalendario.LinearLayoutFiltroCalendario.isVisible = false

            if (flagUltimaModificacion == true){
                val dateRange = binding.appBarMain.includeModalCalendario.fechaStart.text.toString() +"\n"+
                        binding.appBarMain.includeModalCalendario.fechaEnd.text.toString()
                Toast.makeText(this, "Ultima Modificación: $dateRange", Toast.LENGTH_SHORT).show()
            }else if (flagFechaApertura == true){
                val dateRange = binding.appBarMain.includeModalCalendario.fechaStart.text.toString() +"\n"+
                        binding.appBarMain.includeModalCalendario.fechaEnd.text.toString()
                Toast.makeText(this, "Fecha Apertura: $dateRange", Toast.LENGTH_SHORT).show()
            }else if (flagFechaCierre == true){
                val dateRange = binding.appBarMain.includeModalCalendario.fechaStart.text.toString() +"\n"+
                        binding.appBarMain.includeModalCalendario.fechaEnd.text.toString()
                Toast.makeText(this, "Fecha de Cierre: $dateRange", Toast.LENGTH_SHORT).show()
            }


            binding.appBarMain.includeModalCalendario.fechaStart.text = ""
            binding.appBarMain.includeModalCalendario.fechaEnd.text = ""
            binding.appBarMain.includeModalCalendario.btnAceptarFecha.isEnabled = false
            binding.appBarMain.includeModalCalendario.btnEndDay.isEnabled = false
        }


        binding.appBarMain.includeModalCalendario.LinearLayoutFiltroCalendario.setOnClickListener {
            binding.appBarMain.includeModalCalendario.LinearLayoutFiltroCalendario.isVisible = false
        }
        //boton que abre calendario modal filtro por fecha de apertura
        binding.appBarMain.includeFiltroRight.btnFechaAperturaFiltroRight.setOnClickListener {
            binding.appBarMain.includeModalCalendario.LinearLayoutFiltroCalendario.isVisible = true
            //binding.appBarMain.llyBackgroudAbm.isVisible = true
            flagUltimaModificacion = false
            flagFechaApertura = true
            flagFechaCierre = false

            //binding.appBarMain.incMdfaptfr.llyBackgroundMdfaptfr.isVisible = true
        }
        //boton que abre calendario modal filtro por fecha de cierre
        binding.appBarMain.includeFiltroRight.btnFechaCierreFiltroRight.setOnClickListener {
            binding.appBarMain.includeModalCalendario.LinearLayoutFiltroCalendario.isVisible = true
            //binding.appBarMain.llyBackgroudAbm.isVisible = true
            flagUltimaModificacion = false
            flagFechaApertura = false
            flagFechaCierre = true

            //binding.appBarMain.incMdfcfr.llyBgMdfcfr.isVisible = true
        }
        //boton que abre buscador modal filtro por apellidos
        binding.appBarMain.includeFiltroRight.btnBsActfr.setOnClickListener {
            binding.appBarMain.incMdbsfr.llyMdbsfr.isVisible = true
            binding.appBarMain.llyBackgroudAbm.isVisible = true
        }
        //


        //fondo gris transparente que se muestra atras de los modals
        binding.appBarMain.llyBackgroudAbm.setOnClickListener {
            if(binding.appBarMain.incMdfaptfr.llyBackgroundMdfaptfr.isVisible){
                binding.appBarMain.incMdfaptfr.llyBackgroundMdfaptfr.isVisible = false
            }
            else if(binding.appBarMain.incMdfcfr.llyBgMdfcfr.isVisible){
                binding.appBarMain.incMdfcfr.llyBgMdfcfr.isVisible = false
            }
            else if(binding.appBarMain.incMdbsfr.llyMdbsfr.isVisible){
                binding.appBarMain.incMdbsfr.llyMdbsfr.isVisible = false
            }
            binding.appBarMain.llyBackgroudAbm.isVisible = false
        }
        //FIN - boton filtro de la derecha - activity_filtro_right.xml
    }

    //nota:eliminar fragment de fondo
    private fun replaceFragment(misPeticionesFragment: MisPeticionesFragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayoutFragment,misPeticionesFragment).commit()
    }


    private fun userEntities() {
        //accedemos a los elementos "id" del headerLayout con getHeaderView y lo guardamos en una variable
        //DEL MODAL ENTIDADES
        //requestVolley(urlApi_Entities)
        Utils_Global.volleyRequestID(this,urlApi_Entities)
        val headerView: View = binding.navView.getHeaderView(0).findViewById(R.id.linear_layout_DF)
        headerView.setOnClickListener{
            // mostramos el modal con el siguiente código
            val biulder = AlertDialog.Builder(this)
            val vista = layoutInflater.inflate(R.layout.modal_df,null)
            //pasando la vista al biulder
            biulder.setView(vista)
            //creando dialog
            val dialog = biulder.create()
            dialog.show()

            //obtenemos los id's de modal_df - entities del User Login
            val LinearLayout_mpfn: LinearLayout = vista.findViewById(R.id.LinearLayout_mpfn)
            val btn_mpfn_atras: Button = vista.findViewById(R.id.btn_mpfn_atras)
            val contenedor_mpfn: LinearLayout = vista.findViewById(R.id.contenedor_mpfn)
            val btn_mpfn_modal: Button = vista.findViewById(R.id.btn_mpfn_modal)
            //val txt_mpfn: TextView = vista.findViewById(R.id.txt_mpfn)
            val LinearLayout_cobertura: LinearLayout = vista.findViewById(R.id.LinearLayout_cobertura)



            val newButton = Button(this)
            val newTextView = TextView(this)
            var iterador = 1
            btn_mpfn_modal.setOnClickListener {
                val DataEntities = jsonObjectResponse.getJSONArray("myentities")
                val entitiesArray_ = DataEntities.getJSONObject(0)
                val entitiesName = entitiesArray_.getString("name").toString()
                val getEntitiesArray_ = entitiesName.replace("&#62","").split(";",",")
                val newArrayEntities = getEntitiesArray_.toTypedArray() // convertimos a array

                binding.navView.getHeaderView(0).findViewById<TextView>(R.id.txt_entities).text =
                    newArrayEntities[iterador]

                //Log.i("mensaje entitie ok",""+newArrayEntites[iterador])

                deleteButton(newButton,vista)
                deleteTextView(newTextView,vista)
                contenedor_mpfn.isVisible = false
                LinearLayout_mpfn.isVisible = true
                LinearLayout_cobertura.isVisible = true

                createButton(newButton,vista)
                createTextView(newTextView,newArrayEntities[iterador],vista)

                if (iterador>=newArrayEntities.size){
                    iterador=newArrayEntities.size-1
                }else{
                    iterador++
                }
            }

            btn_mpfn_atras.setOnClickListener {
                val DataEntities = jsonObjectResponse.getJSONArray("myentities")
                val entitiesArray_ = DataEntities.getJSONObject(0)
                val entitiesName = entitiesArray_.getString("name").toString()

                var getEntitiesArray_ = entitiesName.replace("&#62","").split(";",",")
                val newArrayEntites = getEntitiesArray_.toTypedArray() // convertimos a array

                iterador--
                if (iterador < 2){
                    contenedor_mpfn.isVisible = true
                    LinearLayout_mpfn.isVisible = false
                    LinearLayout_cobertura.isVisible = false
                }else{
                    deleteButton(newButton,vista)
                    deleteTextView(newTextView,vista)

                    if (iterador>1){
                        createButton(newButton,vista)
                        createTextView(newTextView,newArrayEntites[iterador-1],vista)
                    }
                }//Log.i("iterador",","+newArrayEntites[iterador])
            }

            newButton.setOnClickListener {

                val DataEntities = jsonObjectResponse.getJSONArray("myentities")
                val entitiesArray_ = DataEntities.getJSONObject(0)
                val entitiesName = entitiesArray_.getString("name").toString()
                //Log.i("mensaje entitie ok",""+entitiesName)
                //Toast.makeText(this, "boton clickeado", Toast.LENGTH_SHORT).show()
                var getEntitiesArray_ = entitiesName.replace("&#62","").split(";",",")
                val newArrayEntites = getEntitiesArray_.toTypedArray() // convertimos a array

                if (iterador < newArrayEntites.size){
                    deleteButton(newButton,vista)
                    deleteTextView(newTextView,vista)

                    createButton(newButton,vista)
                    createTextView(newTextView,newArrayEntites[iterador],vista)

                    binding.navView.getHeaderView(0).findViewById<TextView>(R.id.txt_entities).text =
                        newArrayEntites[iterador]

                    iterador++
                    if(iterador >= newArrayEntites.size){
                        deleteButton(newButton,vista)
                        Toast.makeText(this, "No existe mas Entidades", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    iterador = getEntitiesArray_.size
                }
            }//Log.i("iterador",","+iterador)

            val btn_cerrar: Button = vista.findViewById(R.id.btn_salir_modal_df)
            btn_cerrar.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    //METHODS SECTION-------------------------------------------------------------------------------
    private fun userProfiles(){
        //modal perfiles del usuario
        //requestVolley(urlApi_Profiles)
        Utils_Global.volleyRequestID(this,urlApi_Profiles)
        val headerView_operador: View = binding.navView.getHeaderView(0).findViewById(R.id.linear_layout_Operador)
        headerView_operador.setOnClickListener{
            // mostramos el modal con el siguiente código
            val biulder = AlertDialog.Builder(this@MainActivity)
            val vistaOp = layoutInflater.inflate(R.layout.modal_perfiles,null)
            //pasando la vista al biulder
            biulder.setView(vistaOp)
            //creando dialog
            val dialog = biulder.create()
            dialog.show()

            //obtenemos los id's de modal_df
            val btn_cerrarModalPerfiles: Button = vistaOp.findViewById(R.id.btn_cerrarModalPerfiles)

            val perfilSelected =
                binding.navView.getHeaderView(0).findViewById<TextView>(R.id.txt_perfil_user).text.toString()

            for (i in 0 until jsonArrayResponse.length()){
                val dataPerfiles = jsonArrayResponse.getJSONObject(i)
                //array.add(DataPerfiles.getString("PERFIL"))
                val newLinearLayout = LinearLayout(this)
                val newButtonPerfil = Button(this)

                newButtonPerfil.text = dataPerfiles.getString("PERFIL")

                //INICIO DISEÑO DE lINEALAYOUT
                if (newButtonPerfil.text.toString() == perfilSelected){
                    newLinearLayout.setBackgroundResource(R.color.modalPerfiles)
                }
                val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                newLinearLayout.layoutParams = layoutParams
                //FIN DISEÑO DE lINEALAYOUT

                //INICIO DISEÑO DE BUTTON
                //-----------------
                val typedValue = TypedValue()
                getTheme().resolveAttribute(
                    android.R.attr.selectableItemBackground,
                    typedValue,
                    true
                )
                //it's probably a good idea to check if the color wasn't specified as a resource
                if (typedValue.resourceId != 0) {
                    newButtonPerfil.setBackgroundResource(typedValue.resourceId)
                } else {
                    // this should work whether there was a resource id or not
                    newButtonPerfil.setBackgroundColor(typedValue.data)
                }
                //--------

                val ButtonParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                newButtonPerfil.layoutParams = ButtonParams
                //FIN DISEÑO DE BUTTON

                vistaOp.findViewById<LinearLayout>(R.id.linearLayout_prueba).addView(newLinearLayout)
                newLinearLayout.addView(newButtonPerfil)
                newButtonPerfil.setOnClickListener {

                    binding.navView.getHeaderView(0).findViewById<TextView>(R.id.txt_perfil_user).text =
                        newButtonPerfil.text.toString()
                    dialog.dismiss()
                }
            }//Log.i("mensaje perfiles",""+ jsonArrayResponse)

            btn_cerrarModalPerfiles.setOnClickListener {
                dialog.dismiss()
            }
        }
    }
    //método que hace las peticiones a la apiRest, requiere como parametro url de tipo String
    private fun requestVolley(urlApi_: String){
        val stringRequestDataTickets = @RequiresApi(Build.VERSION_CODES.N)
        object : StringRequest(Request.Method.POST,
            urlApi_, Response.Listener { response ->
                try {
                    //Log.i("mensaje entitis dentro2",""+ response[0])
                    if(response[0] == '['){
                        jsonArrayResponse = JSONArray(response)
                    }else{
                        jsonObjectResponse = JSONObject(response)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                    Toast.makeText(this, "token expirado: $e", Toast.LENGTH_LONG).show()
                    //Log.i("mensaje entitis dentroE",""+response.get(0))
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["session_token"] = prefer.getToken()
                return params
            }
        }
        this?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
        //FIN volley ------------------------------------------------------------
    }

    private fun deleteButton(newButton: Button, vista: View) {
        val cobertura = R.id.LinearLayout_cobertura
        vista.findViewById<LinearLayout>(cobertura).removeView(newButton)
    }

    private fun createButton(newButton: Button, vista: View) {
//*****************INICIO DISEÑO DEL BOTON******************************
        newButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fab_desplegar,0,0,0)
        newButton.setPadding(30,0,0,0)

        //inicio tamaño de boton
        val layoutParams = RelativeLayout.LayoutParams(100, 100)
        newButton.layoutParams = layoutParams
        //fin tamaño de boton

        ////************
        val typedValue = TypedValue()
        getTheme().resolveAttribute(
            android.R.attr.selectableItemBackground,
            typedValue,
            true
        )
        //it's probably a good idea to check if the color wasn't specified as a resource
        if (typedValue.resourceId != 0) {
            newButton.setBackgroundResource(typedValue.resourceId)
        } else {
            // this should work whether there was a resource id or not
            newButton.setBackgroundColor(typedValue.data)
        }
        //************

        //********************FIN DISEÑO DEL BOTON******************************
        vista.findViewById<LinearLayout>(R.id.LinearLayout_cobertura).addView(newButton)
    }

    private fun deleteTextView(newTextView: TextView, vista: View) {
        vista.findViewById<LinearLayout>(R.id.LinearLayout_cobertura).removeView(newTextView)
    }

    private fun createTextView(newTextView: TextView, entitiesArray: String, vista: View) {
        //*****************INICIO DISEÑO DEL TEXVIEW****************************
        newTextView.text = entitiesArray
        newTextView.setTextColor(Color.parseColor("#FFFFFF"))
        //--
        val fontFamily = Typeface.createFromAsset(
            assets,
            "font/averia_sans_libre_light.ttf"
        )
        newTextView.setTypeface(fontFamily)
        //--
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            newTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        }
        newTextView.setPadding(30,32,0,32)
        //--
        //*****************FIN DISEÑO DEL TEXVIEW*******************************
        vista.findViewById<LinearLayout>(R.id.LinearLayout_cobertura).addView(newTextView)
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }*/

    /*override fun onItemClickPerfiles(TipoPerfil: String) {//sin uso
        val intent = Intent(this, NavFooterTicketsActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, TipoPerfil, Toast.LENGTH_LONG).show()
        Log.i("click perfiles",""+TipoPerfil)
    }*/


    override fun onSupportNavigateUp(): Boolean { //slide de la izquierda
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.txt_nameUser).text = "nameLoginUser" //TODO: ASIGANAR NOMBRE
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //onKeyDown controlamos la pulsación del boton atras por defecto del celular para que cierre la app
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        return super.onKeyDown(keyCode, event)
    }
}