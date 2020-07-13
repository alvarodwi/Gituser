package com.varoa.gituser.utils.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import com.varoa.gituser.R
import timber.log.Timber
import java.time.*

//helper to assist alarm creation!
object AlarmScheduler {
    private const val DAILY_ALARM_ID = 128

    fun scheduleAlarm(context: Context) {
        Timber.d("scheduleAlarm()")
        //local time from preferences string...
        val alarmTime = LocalTime.parse(
            PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.reminder_time_key), "09:00")
        ) ?: LocalTime.of(9, 0)
        Timber.d("Reminder :: Scheduled for $alarmTime")

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)

        // =( localTime didn't have convert to millis
        // the only option is convert it to Instant, like shown below
        // java Time is easier to use from my side, but it's an hassle to get timeInMillis like in Calendar
        val zoneDateTime = ZonedDateTime.of(
            LocalDateTime.of(LocalDate.now(), alarmTime),
            ZoneId.systemDefault()
        )

        val pendingIntent = PendingIntent.getBroadcast(context, DAILY_ALARM_ID, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            zoneDateTime.toInstant().toEpochMilli(),
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context) {
        Timber.d("cancelAlarm()")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, DAILY_ALARM_ID, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }
}