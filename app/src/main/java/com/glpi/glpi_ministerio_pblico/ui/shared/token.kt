package com.glpi.glpi_ministerio_pblico.ui.shared

import android.annotation.SuppressLint
import android.app.Application

//esta clase se va a ejecutar cada vez que se abra la aplicación antes del LoginActivity
class token:Application() {

    //creamos una clase para que todas las otras actividades puedan acceder a su contenido
    companion object{
        //instanciamos la clase Prefer.kt
        @SuppressLint("StaticFieldLeak")
        lateinit var prefer:Prefer
    }

    override fun onCreate() {
        super.onCreate()

        prefer = Prefer(applicationContext)

    }
}