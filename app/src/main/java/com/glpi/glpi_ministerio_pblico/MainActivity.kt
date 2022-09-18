package com.glpi.glpi_ministerio_pblico

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.glpi.glpi_ministerio_pblico.databinding.ActivityMainBinding
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_Entities
import com.glpi.glpi_ministerio_pblico.ui.adapter.Data_Perfiles
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_Entities
import com.glpi.glpi_ministerio_pblico.ui.adapter.RecycleView_Adapter_Perfiles
import com.glpi.glpi_ministerio_pblico.ui.shared.token
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import com.google.android.material.navigation.NavigationView
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    /*creamos la lista de arreglos que tendrá los objetos de la clase Data_Perfiles
    esta lista de arreglos (dataModelArrayListPerfiles) funcionará como fuente de datos*/
    internal lateinit var dataModelArrayListPerfil: ArrayList<Data_Perfiles>
    internal lateinit var dataModelArrayListEntities: ArrayList<Data_Entities>
    //creamos el objeto de la clase RecycleView_Adapter_Tickets
    private var RecycleView_Adapter_Perfiles: RecycleView_Adapter_Perfiles? = null
    private var RecycleView_Adapter_Entities: RecycleView_Adapter_Entities? = null
    //creamos el objeto de la clase recyclerView
    private var recyclerViewPerfiles: RecyclerView? = null
    private var recyclerViewEntities: RecyclerView? = null



    companion object{
        var userName:String? = null
    }
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

        getUserID() //metodo que nos devuelve el id del usuario logeado con volley

        //accedemos a los elementos "id" del headerLayout con getHeaderView y lo guardamos en una variable
        //DEL MODAL ENTIDADES
        val headerView: View = binding.navView.getHeaderView(0).findViewById(R.id.linear_layout_DF)
        headerView.setOnClickListener{
            // mostramos el modal con el siguiente código
            val biulder = AlertDialog.Builder(this@MainActivity)
            val vista = layoutInflater.inflate(R.layout.modal_df,null)
            //pasando la vista al biulder
            biulder.setView(vista)
            //creando dialog
            val dialog = biulder.create()
            dialog.show()



            /*INICIO volley RECYCLERVIEW------------------------------------------------------------
            val url_DataTickets = "http://181.176.145.174:8080/api/user_entities" //online
            val stringRequestDataTickets = @RequiresApi(Build.VERSION_CODES.N)
            object : StringRequest(Request.Method.POST,
                url_DataTickets, Response.Listener { response ->
                    try {
                        val JS_DataEntities = JSONObject(response) //obtenemos el objeto json
                        dataModelArrayListEntities = ArrayList()

                        for (i in 0 until JS_DataEntities.length()){

                            val DataEntities = JS_DataEntities.getJSONArray("myentities")

                            val playerModel = Data_Entities()


                            playerModel.setGlpiMyEntities(DataEntities.getString(0))

                            dataModelArrayListEntities.add(playerModel)

                            Log.i("mensaje recycler ok: ","main activity: "+ playerModel.toString())
                        }
                        val recyclerEntities = vista.findViewById<RecyclerView>(R.id.recycler_entities)
                        recyclerEntities.layoutManager = LinearLayoutManager(this)
                        RecycleView_Adapter_Entities = RecycleView_Adapter_Entities(this,dataModelArrayListEntities)
                        recyclerEntities.adapter = RecycleView_Adapter_Entities

                    }catch (e:Exception){
                        e.printStackTrace()
                        Toast.makeText(this, "token expirado: $e", Toast.LENGTH_LONG).show()
                        Log.i("mensaje recycler e: ","recycler ERROR: "+e)
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
                }){
                override fun getParams(): Map<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    params.put("session_token", token.prefer.getToken())
                    return params
                }
            }
            this?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
            //FIN volley RECYCLERVIEW--------------------------------------------------------------*/

            //obtenemos los id's de modal_df
            val LinearLayout_mpfn: LinearLayout = vista.findViewById(R.id.LinearLayout_mpfn)
            val btn_mpfn_atras: Button = vista.findViewById(R.id.btn_mpfn_atras)
            val contenedor_mpfn: LinearLayout = vista.findViewById(R.id.contenedor_mpfn)
            val btn_mpfn_modal: Button = vista.findViewById(R.id.btn_mpfn_modal)
            val txt_mpfn: TextView = vista.findViewById(R.id.txt_mpfn)
            val LinearLayout_cobertura: LinearLayout = vista.findViewById(R.id.LinearLayout_cobertura)
            /*val LinearLayout_cobertura_: LinearLayout = vista.findViewById(R.id.LinearLayout_cobertura_)
                val btn_cobertura_atras: Button = vista.findViewById(R.id.btn_cobertura_atras)
            val LinearLayout_distritosFiscales: LinearLayout = vista.findViewById(R.id.LinearLayout_distritosFiscales)
                val btn_distritosFiscales_atras: Button = vista.findViewById(R.id.btn_distritosFiscales_atras)
                val btn_cobertura: Button = vista.findViewById(R.id.btn_cobertura)
                val txt_CoberturaNacional: TextView = vista.findViewById(R.id.txt_CoberturaNacional)
            val LinearLayout_DF_MDD: LinearLayout = vista.findViewById(R.id.LinearLayout_DF_MDD)
                val btn_DF_MDD: Button = vista.findViewById(R.id.btn_DF_MDD)
                val txt_DF_MDD: TextView = vista.findViewById(R.id.txt_DF_MDD)
            val LinearLayout_DF: LinearLayout = vista.findViewById(R.id.LinearLayout_DF)
                val btn_DF: Button = vista.findViewById(R.id.btn_DF)
                val txt_DF: TextView = vista.findViewById(R.id.txt_DF)
            val LinearLayout_madreDeDios: LinearLayout = vista.findViewById(R.id.LinearLayout_madreDeDios)*/


            //INICIO volley ------------------------------------------------------------
            val url_DataEntities = "http://181.176.145.174:8080/api/user_entities" //online
            //var EntitiesArray
            val stringRequestDataTickets = @RequiresApi(Build.VERSION_CODES.N)
            object : StringRequest(Request.Method.POST,
                url_DataEntities, Response.Listener { response ->
                    try {
                        val JS_DataEntities = JSONObject(response) //obtenemos el objeto json
                        val DataEntities = JS_DataEntities.getJSONArray("myentities")
                        val EntitiesArray = DataEntities.getJSONObject(0)
                        val Entities = EntitiesArray.getString("name")

                        val delim1 = "&#62"+";"
                        val getEntitiesArray = Entities.split(delim1).toTypedArray() //obtenemos el array de datos
                        val entitiesArray0 = getEntitiesArray[0] // accedemos a la posision 0

                        txt_mpfn.text = entitiesArray0

                        val newButton = Button(this)
                        val newTextView = TextView(this)

                        var iterador = 1
                        btn_mpfn_modal.setOnClickListener {
                            deleteButton(newButton,vista)
                            deleteTextView(newTextView,vista)
                            contenedor_mpfn.isVisible = false
                            LinearLayout_mpfn.isVisible = true
                            LinearLayout_cobertura.isVisible = true

                            createButton(newButton,vista)
                            createTextView(newTextView,getEntitiesArray[iterador],vista)

                            if (iterador>=getEntitiesArray.size){
                                iterador=getEntitiesArray.size-1
                            }else{
                                iterador++
                            }
                            Log.i("iterador",","+iterador)
                        }

                        btn_mpfn_atras.setOnClickListener {
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
                                    createTextView(newTextView,getEntitiesArray[iterador-1],vista)
                                }
                            }
                            Log.i("iterador",","+iterador)
                        }

                        newButton.setOnClickListener {
                            Toast.makeText(this, "boton clickeado", Toast.LENGTH_SHORT).show()

                            if (iterador < getEntitiesArray.size){
                                deleteButton(newButton,vista)
                                deleteTextView(newTextView,vista)

                                createButton(newButton,vista)
                                createTextView(newTextView,getEntitiesArray[iterador],vista)
                                iterador++
                            }else{
                                iterador = getEntitiesArray.size
                            }
                            Log.i("iterador",","+iterador)
                        }
                        Log.i("iterador",","+iterador)

                    }catch (e:Exception){
                        e.printStackTrace()
                        Toast.makeText(this, "token expirado: $e", Toast.LENGTH_LONG).show()
                        Log.i("mensaje recycler e: ","recycler ERROR: "+e)
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
                }){
                override fun getParams(): Map<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    params.put("session_token", token.prefer.getToken())
                    return params
                }
            }
            this?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
            //FIN volley ------------------------------------------------------------

            val btn_cerrar: Button = vista.findViewById<Button>(R.id.btn_salir_modal_df)
            btn_cerrar.setOnClickListener(){
                dialog.dismiss()
            }
        }

        var click_btnHardware = false
        var click_escalados = false
        //modal perfiles del usuario
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
            

            //inicio volley
            val url_DataTickets = "http://181.176.145.174:8080/api/user_profiles" //online
            val stringRequestDataTickets = @RequiresApi(Build.VERSION_CODES.N)
            object : StringRequest(Request.Method.POST,
                url_DataTickets, Response.Listener { response ->
                    try {
                        val JS_DataTickets = JSONArray(response) //obtenemos el objeto json
                        dataModelArrayListPerfil = ArrayList()

                        for (i in 0 until JS_DataTickets.length()){

                            val DataPerfiles = JS_DataTickets.getJSONObject(i)

                            val playerModel = Data_Perfiles()


                            playerModel.setGlpiPerfilLogin(DataPerfiles.getString("PERFIL"))

                            dataModelArrayListPerfil.add(playerModel)

                            Log.i("mensaje recycler ok: ","main activity: "+ playerModel.toString())
                        }
                        val recyclerPerfiles = vistaOp.findViewById<RecyclerView>(R.id.recycler_perfiles)
                        recyclerPerfiles.layoutManager = LinearLayoutManager(this)
                        RecycleView_Adapter_Perfiles = RecycleView_Adapter_Perfiles(this,dataModelArrayListPerfil)
                        recyclerPerfiles.adapter = RecycleView_Adapter_Perfiles

                    }catch (e:Exception){
                        e.printStackTrace()
                        Toast.makeText(this, "token expirado: $e", Toast.LENGTH_LONG).show()
                        Log.i("mensaje recycler e: ","recycler ERROR: "+e)
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
                }){
                override fun getParams(): Map<String, String>? {
                    val params: MutableMap<String, String> = HashMap()
                    params.put("session_token", token.prefer.getToken())
                    return params
                }
            }
            this?.let { VolleySingleton.getInstance(it).addToRequestQueue(stringRequestDataTickets) }
            //FIN volley




            //obtenemos los id's de modal_df
            val LinearLayout_hardwareGestor: LinearLayout = vistaOp.findViewById(R.id.LinearLayout_hardwareGestor)
                val btn_hardwareModalPerfil: Button = vistaOp.findViewById(R.id.btn_hardwareModalPerfil)
            val LinearLayout_operadorModal: LinearLayout = vistaOp.findViewById(R.id.LinearLayout_operadorModal)
                val btn_operadorModalPerfiles: Button = vistaOp.findViewById(R.id.btn_operadorModalPerfiles)
            val LinearLayout_ticketsEscalados: LinearLayout = vistaOp.findViewById(R.id.LinearLayout_ticketsEscalados)
                val btn_escaladosModalPerfiles: Button = vistaOp.findViewById(R.id.btn_escaladosModalPerfiles)
            val btn_cerrarModalPerfiles: Button = vistaOp.findViewById(R.id.btn_cerrarModalPerfiles)

            //iniciamos los eventos click - introducir codigo necesario aca

            if (click_btnHardware){
                LinearLayout_hardwareGestor.setBackgroundResource(R.color.modalPerfiles)
                LinearLayout_operadorModal.setBackgroundResource(R.color.modalPerfilesBlanco)
                LinearLayout_ticketsEscalados.setBackgroundResource(R.color.modalPerfilesBlanco)
            }else if (click_escalados){
                LinearLayout_ticketsEscalados.setBackgroundResource(R.color.modalPerfiles)
                LinearLayout_hardwareGestor.setBackgroundResource(R.color.modalPerfilesBlanco)
                LinearLayout_operadorModal.setBackgroundResource(R.color.modalPerfilesBlanco)
            }else{
                LinearLayout_operadorModal.setBackgroundResource(R.color.modalPerfiles)
                LinearLayout_hardwareGestor.setBackgroundResource(R.color.modalPerfilesBlanco)
                LinearLayout_ticketsEscalados.setBackgroundResource(R.color.modalPerfilesBlanco)
            }

            btn_hardwareModalPerfil.setOnClickListener {
                click_btnHardware = true
                click_escalados = false
                LinearLayout_hardwareGestor.setBackgroundResource(R.color.modalPerfiles)
                LinearLayout_operadorModal.setBackgroundResource(R.color.modalPerfilesBlanco)
                LinearLayout_ticketsEscalados.setBackgroundResource(R.color.modalPerfilesBlanco)

                binding.navView.getHeaderView(0).findViewById<TextView>(R.id.txt_perfil_user).setText("Hardware - Gestor")
                dialog.dismiss()
            }
            btn_operadorModalPerfiles.setOnClickListener {
                click_btnHardware = false
                click_escalados = false
                LinearLayout_operadorModal.setBackgroundResource(R.color.modalPerfiles)
                LinearLayout_hardwareGestor.setBackgroundResource(R.color.modalPerfilesBlanco)
                LinearLayout_ticketsEscalados.setBackgroundResource(R.color.modalPerfilesBlanco)

                binding.navView.getHeaderView(0).findViewById<TextView>(R.id.txt_perfil_user).setText("Operador")
                dialog.dismiss()
            }
            btn_escaladosModalPerfiles.setOnClickListener {
                click_escalados = true
                click_btnHardware = false
                LinearLayout_ticketsEscalados.setBackgroundResource(R.color.modalPerfiles)
                LinearLayout_hardwareGestor.setBackgroundResource(R.color.modalPerfilesBlanco)
                LinearLayout_operadorModal.setBackgroundResource(R.color.modalPerfilesBlanco)

                binding.navView.getHeaderView(0).findViewById<TextView>(R.id.txt_perfil_user).setText("Tickets escalados (DF)")
                dialog.dismiss()

            }
            btn_cerrarModalPerfiles.setOnClickListener {
                dialog.dismiss()
            }
        }

        //INICIO - boton filtro de la derecha - activity_filtro_right.xml
        binding.appBarMain.btnFiltroRight.setOnClickListener {
            binding.appBarMain.includeFiltroRight.LinearLayoutActivityFiltroRight.isVisible = true
        }
        binding.appBarMain.includeFiltroRight.LinearLayoutActivityFiltroRight.setOnClickListener {
            binding.appBarMain.includeFiltroRight.LinearLayoutActivityFiltroRight.isVisible = false
        }
        //boton que despliega menu de filtro por fechas
        var click_desplegar = false
        binding.appBarMain.includeFiltroRight.btnDesplegarFechaFiltroRight.setOnClickListener {
            if (click_desplegar == false){
                binding.appBarMain.includeFiltroRight.btnUltModificacionFiltroRight.isVisible = true
                binding.appBarMain.includeFiltroRight.btnFechaAperturaFiltroRight.isVisible = true
                binding.appBarMain.includeFiltroRight.btnFechaCierreFiltroRight.isVisible = true
                click_desplegar = true
            }else{
                binding.appBarMain.includeFiltroRight.btnUltModificacionFiltroRight.isVisible = false
                binding.appBarMain.includeFiltroRight.btnFechaAperturaFiltroRight.isVisible = false
                binding.appBarMain.includeFiltroRight.btnFechaCierreFiltroRight.isVisible = false
                click_desplegar = false
            }
        }
        //boton que abre calendario modal filtro por ultima modificación
        binding.appBarMain.includeFiltroRight.btnUltModificacionFiltroRight.setOnClickListener {
            binding.appBarMain.includeModalCalendario.LinearLayoutFiltroCalendario.isVisible = true
        }
        binding.appBarMain.includeModalCalendario.LinearLayoutFiltroCalendario.setOnClickListener {
            binding.appBarMain.includeModalCalendario.LinearLayoutFiltroCalendario.isVisible = false
        }
        //boton que abre calendario modal filtro por fecha de apertura
        binding.appBarMain.includeFiltroRight.btnFechaAperturaFiltroRight.setOnClickListener {
            binding.appBarMain.llyBackgroudAbm.isVisible = true
            binding.appBarMain.incMdfaptfr.llyBackgroundMdfaptfr.isVisible = true
        }
        //boton que abre calendario modal filtro por fecha de cierre
        binding.appBarMain.includeFiltroRight.btnFechaCierreFiltroRight.setOnClickListener {
            binding.appBarMain.llyBackgroudAbm.isVisible = true
            binding.appBarMain.incMdfcfr.llyBgMdfcfr.isVisible = true
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

    private fun deleteButton(newButton: Button, vista: View) {
        val cobertura = R.id.LinearLayout_cobertura
        vista.findViewById<LinearLayout>(cobertura).removeView(newButton)
    }

    private fun createButton(newButton: Button, vista: View) {
//*****************INICIO DISEÑO DEL BOTON******************************
        newButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fab_desplegar,0,0,0)
        newButton.setPadding(30,0,0,0)
        val Tamaño = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.LEFT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        //inicio tamaño de boton
        val layoutParams = RelativeLayout.LayoutParams(125, 150)
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
        newTextView.setPadding(30,0,0,0)
        //--
        //*****************FIN DISEÑO DEL TEXVIEW*******************************
        vista.findViewById<LinearLayout>(R.id.LinearLayout_cobertura).addView(newTextView)
    }


    private fun modal_perfiles() {

    }

    //metodo que nos devuelve el id del usuario logeado
    private fun getUserID() {
        //INICIO obtenemos perfil de usuario con volley
        val userPERFIL_ID = binding.navView.getHeaderView(0).findViewById<TextView>(R.id.txt_nameUser)
        val url_userID = "http://181.176.145.174:8080/api/user_id" //online
        val stringRequestPerfil = object : StringRequest(Request.Method.POST,
            url_userID, Response.Listener { response ->
                try {
                    val jsonjObject_session = JSONObject(response) //obtenemos el objeto json
                    val user = jsonjObject_session.getJSONObject("session")
                    userName = user.getString("glpifriendlyname")
                    //val userPerfil = user.getString("PERFIL")
                    userPERFIL_ID.setText(userName)
                    Log.i("mensaje main ok: ","main activity: "+ userName)
                }catch (e:Exception){
                    e.printStackTrace()
                    Toast.makeText(this, "token expirado: $e", Toast.LENGTH_LONG).show()
                    Log.i("mensaje main error: ","main activity ERROR: "+e)
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "ERROR CON EL SERVIDOR", Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params.put("session_token", prefer.getToken())
                return params
            }
        }
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequestPerfil)
        //FIN obtenemos perfil de usuario
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }*/

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
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


