package com.varoa.gituserfav.data.repository

import android.content.Context
import com.varoa.gituserfav.data.model.User
import com.varoa.gituserfav.data.source.UserDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainRepository(ctx: Context) {
    //instance helper
    companion object {
        private lateinit var INSTANCE: MainRepository

        fun getInstance(context: Context): MainRepository {
            synchronized(this) {
                if (!Companion::INSTANCE.isInitialized) {
                    INSTANCE = MainRepository(context)
                }
            }

            return INSTANCE
        }
    }

    //data source
    private val dataSource = UserDataSource(ctx.contentResolver)

    suspend fun getFavoriteUsers() : List<User>{
        Timber.d("Repo :: getFavoriteUsers()")
        return withContext(Dispatchers.IO){
            dataSource.fetchUsers()
        }
    }

    suspend fun getUser(id: Long) : User{
        Timber.d("Repo :: getUser($id)")
        return withContext(Dispatchers.IO){
            dataSource.fetchUserById(id)
        }
    }
}