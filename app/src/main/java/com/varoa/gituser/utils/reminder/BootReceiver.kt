package com.varoa.gituser.utils.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            //reshedule alarm setelah restart
            AlarmScheduler.scheduleAlarm(context)
        }
    }
}
