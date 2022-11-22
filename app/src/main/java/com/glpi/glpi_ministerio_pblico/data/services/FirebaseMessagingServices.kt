package com.glpi.glpi_ministerio_pblico.data.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.glpi.glpi_ministerio_pblico.R
import com.glpi.glpi_ministerio_pblico.ui.shared.token.Companion.prefer
import com.glpi.glpi_ministerio_pblico.utilities.NFMbyPass
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val CHANNEL_ID = "NOTIFICATION_CHANNEL"
//const val  CHANNEL_NAME = "com.example.fcmpushnotification"
const val  CHANNEL_NAME = "com.glpi.glpi_ministerio_pblico"

class FirebaseMessagingServices: FirebaseMessagingService() {

    //Generar notificacion
    private fun generateNotification(title: String, message: String){
        //channel id, channel name---------
        // Create an Intent for the activity you want to start
        val intent = Intent(this, NFMbyPass::class.java)
        // Create the TaskStackBuilder
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(intent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        var builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_background)
            setAutoCancel(true)
            setVibrate(longArrayOf(1000,1000,1000,1000))
            setOnlyAlertOnce(true)
            setContentIntent(resultPendingIntent)
        }
        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //verificar si android es mayor a android Oreo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }

    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(title: String, message: String) : RemoteViews {
        val remoteView = RemoteViews("com.glpi.glpi_ministerio_pblico", R.layout.push_notification)
        //remoteView.setTextViewText(R.id.title, "TICKET N°: $title")
        remoteView.setTextViewText(R.id.title, "GLPI MP")
        prefer.saveTicketSortsId(title)
        //prefer.saveNotificationTicketId(title)
        //Log.i("mensaje noti",title)
        remoteView.setTextViewText(R.id.messageTitle, "Nuevo Ticket Asigando: $title")
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.image, R.drawable.ic_new_ticket)

        return remoteView
    }

    //mostrar la notificacion
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if(message.notification !=null){
            generateNotification(message.notification!!.title!!, message.notification!!.body!!.replace("-","\n"))
        }
    }

}