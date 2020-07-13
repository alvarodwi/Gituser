package com.varoa.gituser.data.seeder

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.varoa.gituser.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class UserSeeder internal constructor(context : Context, val id : Int){
    private var jsonString : String
    private var appDatabase: AppDatabase = AppDatabase.getInstance(context)

    init {
        val resourceReader = context.resources.openRawResource(id)
        jsonString = Scanner(resourceReader).useDelimiter("\\A").next()
    }

    private fun constructUserJson() : UserJson?{
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter<UserJson>(UserJson::class.java).fromJson(jsonString)
    }

    suspend fun seedUserData(){
        constructUserJson()?.let {
            withContext(Dispatchers.IO){
                it.userList.forEach {data->
                    appDatabase.userDao.insertUser(data)
                }
            }
        }
    }
}