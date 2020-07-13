package com.varoa.gituser.utils.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.varoa.gituser.R
import com.varoa.gituser.utils.NotificationHelper
import timber.log.Timber

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("Reminder :: ReminderReceiver.onReceive()")
        val notifData = NotificationHelper.NotificationData(
            256, //notif Id
            context.getString(R.string.reminder_notif_title),
            context.getString(R.string.reminder_notif_message),
            context.getString(R.string.reminder_notif_type)
        )
        NotificationHelper.showNotification(context,notifData)
    }
}
