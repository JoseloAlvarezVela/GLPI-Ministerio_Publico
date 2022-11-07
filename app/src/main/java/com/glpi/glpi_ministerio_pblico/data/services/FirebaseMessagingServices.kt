package com.glpi.glpi_ministerio_pblico.data.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.glpi.glpi_ministerio_pblico.MainActivity
import com.glpi.glpi_ministerio_pblico.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val CHANNEL_ID = "NOTIFICATION_CHANNEL"
const val  CHANNEL_NAME = "com.example.fcmpushnotification"

class FirebaseMessagingServices: FirebaseMessagingService() {

    //Generar notificacion
    private fun generateNotification(title: String, message: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent : PendingIntent
        //var pendingIntent =   PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent  = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            pendingIntent =   PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        }

        //channel id, channel name
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        //Attach la notificacion creada a un layout custom
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
        val remoteView = RemoteViews("com.example.fcmpushnotification", R.layout.push_notification)
        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.image, R.drawable.ic_task_information)

        return remoteView
    }

    //mostrar la notificacion
    override fun onMessageReceived(message: RemoteMessage) {
        if(message.notification !=null){
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }


}