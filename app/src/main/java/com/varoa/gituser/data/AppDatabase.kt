package com.varoa.gituser.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.varoa.gituser.data.dao.UserDao
import com.varoa.gituser.data.model.User


@Database(entities = [User::class],version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao : UserDao

    companion object{
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context : Context) : AppDatabase{
            synchronized(this){
                var instance =  INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "GitUserDB"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }

    }
}