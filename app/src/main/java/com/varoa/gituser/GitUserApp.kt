package com.varoa.gituser

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.varoa.gituser.utils.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

//for timber logging :D
//marked as unused, well it's somewhat true
//the only usage is in AndroidManifest, on application tag
class GitUserApp : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()

        NotificationHelper.createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_HIGH,
            true,
            getString(R.string.reminder_notif_type),
            getString(R.string.reminder_notif_desc)
            )
    }

    //plant timber tree
    private fun delayedInit(){
        applicationScope.launch {
            Timber.plant(Timber.DebugTree())
        }
    }
}

//https://bumptech.github.io/glide/doc/generatedapi.html
@GlideModule
class GitUserGlideModule : AppGlideModule()