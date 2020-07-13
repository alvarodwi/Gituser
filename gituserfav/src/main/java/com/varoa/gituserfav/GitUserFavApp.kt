package com.varoa.gituserfav

import android.app.Application
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

//for timber logging :D
//marked as unused, well it's somewhat true
//the only usage is in AndroidManifest, on application tag
class GitUserFavApp : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
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
class GitUserFavGlideModule : AppGlideModule()