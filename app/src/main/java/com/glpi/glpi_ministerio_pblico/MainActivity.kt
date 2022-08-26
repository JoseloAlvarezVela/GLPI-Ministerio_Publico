package com.glpi.glpi_ministerio_pblico

import android.os.Bundle
import android.view.Menu
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
import com.glpi.glpi_ministerio_pblico.databinding.ActivityMainBinding
import com.glpi.glpi_ministerio_pblico.databinding.ModalDfBinding
import com.glpi.glpi_ministerio_pblico.databinding.ModalPerfilesBinding
import com.glpi.glpi_ministerio_pblico.databinding.NavHeaderMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_mis_peticiones,R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}


