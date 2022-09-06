package com.glpi.glpi_ministerio_pblico

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.glpi.glpi_ministerio_pblico.databinding.ActivityMainBinding
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import com.google.android.material.navigation.NavigationView
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    internal lateinit var queueUser: RequestQueue // variable que se usa con volley

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
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
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_mis_peticiones,R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.acttivity_misIncidencias
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //INICIO obtenemos perfil de usuario
        val user = binding.navView.getHeaderView(0).findViewById<TextView>(R.id.txt_nameUser)
        queueUser = Volley.newRequestQueue(this)
        val url = "http://192.168.0.5/glpi/api_glpi.php" // en casa
        val stringRequestPerfil = object : StringRequest(Request.Method.POST,
            url, Response.Listener {
                    response ->
                //val jsonArrayPefil = JSONArray(response)
                //val perfilUser = jsonArrayPefil[0]
                try {
                    val jsonObject_ = JSONObject(response)
                    val profile = jsonObject_.getJSONArray("myprofiles")
                    val item = profile.getJSONObject(0)
                    /*val valorNombre: String = item.getString("name") //obtiene valor
                    prefer.SaveUser(item.getString("name"))*/
                    //asignamo el nombre del usuario logeado
                    user.setText(item.getString("name"))
                    //Toast.makeText(this, "Usuario "+prefer.getUser(), Toast.LENGTH_LONG).show()
                }catch (e:Exception){
                    e.printStackTrace()
                    Toast.makeText(this, "error $e", Toast.LENGTH_LONG).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(this, "problemas al obtener nombre de usuario", Toast.LENGTH_SHORT).show()
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["PATH"] = "profile"

                params["SESSIONTOKEN"] = prefer.getToken()
                return params
            }
        }
        queueUser.add(stringRequestPerfil)
        //FIN obtenemos perfil de usuario


        //accedemos a los elementos "id" del headerLayout con getHeaderView y lo guardamos en una variable
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

            //obtenemos los id's de modal_df
            val LinearLayout_mpfn: LinearLayout = vista.findViewById(R.id.LinearLayout_mpfn)
                val btn_mpfn_atras: Button = vista.findViewById(R.id.btn_mpfn_atras)
            val LinearLayout_cobertura_: LinearLayout = vista.findViewById(R.id.LinearLayout_cobertura_)
                val btn_cobertura_atras: Button = vista.findViewById(R.id.btn_cobertura_atras)
            val LinearLayout_distritosFiscales: LinearLayout = vista.findViewById(R.id.LinearLayout_distritosFiscales)
                val btn_distritosFiscales_atras: Button = vista.findViewById(R.id.btn_distritosFiscales_atras)
            val contenedor_mpfn: LinearLayout = vista.findViewById(R.id.contenedor_mpfn)
                val btn_mpfn_modal: Button = vista.findViewById(R.id.btn_mpfn_modal)
                val txt_mpfn: TextView = vista.findViewById(R.id.txt_mpfn)
            val LinearLayout_cobertura: LinearLayout = vista.findViewById(R.id.LinearLayout_cobertura)
                val btn_cobertura: Button = vista.findViewById(R.id.btn_cobertura)
                val txt_CoberturaNacional: TextView = vista.findViewById(R.id.txt_CoberturaNacional)
            val LinearLayout_DF_MDD: LinearLayout = vista.findViewById(R.id.LinearLayout_DF_MDD)
                val btn_DF_MDD: Button = vista.findViewById(R.id.btn_DF_MDD)
                val txt_DF_MDD: TextView = vista.findViewById(R.id.txt_DF_MDD)
            val LinearLayout_DF: LinearLayout = vista.findViewById(R.id.LinearLayout_DF)
                val btn_DF: Button = vista.findViewById(R.id.btn_DF)
                val txt_DF: TextView = vista.findViewById(R.id.txt_DF)
            val LinearLayout_madreDeDios: LinearLayout = vista.findViewById(R.id.LinearLayout_madreDeDios)

            //realizamos los clicksListeners necesarios
            btn_mpfn_modal.setOnClickListener{
                contenedor_mpfn.isVisible = false
                LinearLayout_mpfn.isVisible = true
                LinearLayout_cobertura.isVisible = true
            }
            btn_cobertura.setOnClickListener {
                LinearLayout_mpfn.isVisible = false
                LinearLayout_cobertura.isVisible = false
                LinearLayout_cobertura_.isVisible = true
                LinearLayout_DF.isVisible = true
            }
            btn_DF.setOnClickListener {
                LinearLayout_cobertura_.isVisible = false
                LinearLayout_DF.isVisible = false
                LinearLayout_distritosFiscales.isVisible = true
                LinearLayout_madreDeDios.isVisible = true
            }
            //botones espejo------------------------------------------
            btn_distritosFiscales_atras.setOnClickListener {
                LinearLayout_cobertura_.isVisible = true
                LinearLayout_DF.isVisible = true
                LinearLayout_distritosFiscales.isVisible = false
                LinearLayout_madreDeDios.isVisible = false
            }
            btn_cobertura_atras.setOnClickListener {
                LinearLayout_mpfn.isVisible = true
                LinearLayout_cobertura.isVisible = true
                LinearLayout_cobertura_.isVisible = false
                LinearLayout_DF.isVisible = false
            }
            btn_mpfn_atras.setOnClickListener {
                contenedor_mpfn.isVisible = true
                LinearLayout_mpfn.isVisible = false
                LinearLayout_cobertura.isVisible = false
            }

            val btn_cerrar: Button = vista.findViewById<Button>(R.id.btn_salir_modal_df)
            btn_cerrar.setOnClickListener(){
                dialog.dismiss()
            }
        }

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
            val LinearLayout_hardwareGestor: LinearLayout = vistaOp.findViewById(R.id.LinearLayout_hardwareGestor)
                val btn_hardwareModalPerfil: Button = vistaOp.findViewById(R.id.btn_hardwareModalPerfil)
            val LinearLayout_operadorModal: LinearLayout = vistaOp.findViewById(R.id.LinearLayout_operadorModal)
                val btn_operadorModalPerfiles: Button = vistaOp.findViewById(R.id.btn_operadorModalPerfiles)
            val LinearLayout_ticketsEscalados: LinearLayout = vistaOp.findViewById(R.id.LinearLayout_ticketsEscalados)
                val btn_escaladosModalPerfiles: Button = vistaOp.findViewById(R.id.btn_escaladosModalPerfiles)
            val btn_cerrarModalPerfiles: Button = vistaOp.findViewById(R.id.btn_cerrarModalPerfiles)

            //iniciamos los eventos click - introducir codigo necesario aca
            btn_hardwareModalPerfil.setOnClickListener {
                LinearLayout_hardwareGestor.setBackgroundResource(R.color.modalPerfiles)
                LinearLayout_operadorModal.setBackgroundResource(R.color.modalPerfilesBlanco)
                LinearLayout_ticketsEscalados.setBackgroundResource(R.color.modalPerfilesBlanco)
            }
            btn_operadorModalPerfiles.setOnClickListener {
                LinearLayout_operadorModal.setBackgroundResource(R.color.modalPerfiles)
                LinearLayout_hardwareGestor.setBackgroundResource(R.color.modalPerfilesBlanco)
                LinearLayout_ticketsEscalados.setBackgroundResource(R.color.modalPerfilesBlanco)
            }
            btn_escaladosModalPerfiles.setOnClickListener {
                LinearLayout_ticketsEscalados.setBackgroundResource(R.color.modalPerfiles)
                LinearLayout_hardwareGestor.setBackgroundResource(R.color.modalPerfilesBlanco)
                LinearLayout_operadorModal.setBackgroundResource(R.color.modalPerfilesBlanco)

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
        //Toast.makeText(this, "token: "+prefer.getToken(), Toast.LENGTH_LONG).show()//mostramos el token guardado

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

    //controlamos la pulsación del boton atras por defecto del celular para que cierre la app
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        return super.onKeyDown(keyCode, event)
    }
}


