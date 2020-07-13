package com.varoa.gituser.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.varoa.gituser.MainActivity
import com.varoa.gituser.R
import timber.log.Timber

object NotificationHelper {
    data class NotificationData(
        val notifId : Int,
        val title : String,
        val message : String,
        val type : String //buat channel type yaa
    )

    fun createNotificationChannel(
        context : Context,
        importance : Int,
        showBadge : Boolean,
        name : String,
        description: String
    ){
        // create NotificationChannel, but only on API 26+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Timber.d("Package name : ${context.packageName}")
            val channelId = context.getString(R.string.channel_name,name)
            val channel = NotificationChannel(channelId,name,importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            //register channel
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(
        context : Context,
        data : NotificationData
    ){
        val notificationBuilder = buildNotification(context,data)
        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(data.notifId,notificationBuilder.build())
    }

    private fun buildNotification(
        context : Context,
        data : NotificationData
    ) : NotificationCompat.Builder{
        val channelId = context.getString(R.string.channel_name,data.type)

        return NotificationCompat.Builder(context,channelId).apply {
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            //content
            setSmallIcon(R.drawable.ic_gituser)
            setContentTitle(data.title)
            setContentText(data.message)
            setGroup(data.type)
            //intent
            val intent = Intent(context,MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = data.type
            }
            val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            setContentIntent(pendingIntent)
        }
    }
}