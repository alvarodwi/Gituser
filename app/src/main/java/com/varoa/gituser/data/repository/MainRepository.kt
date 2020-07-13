package com.varoa.gituser.data.repository

import android.content.Context
import com.varoa.gituser.data.AppDatabase
import com.varoa.gituser.data.model.User
import com.varoa.gituser.network.NetworkResult
import com.varoa.gituser.network.NetworkService
import com.varoa.gituser.network.SearchUsersResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainRepository(context: Context) {
    //instance helper
    companion object {
        private lateinit var INSTANCE: MainRepository

        fun getInstance(context: Context): MainRepository {
            synchronized(this) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = MainRepository(context)
                }
            }

            return INSTANCE
        }
    }

    //data source
    private val networkClient = NetworkService.retrofitService
    private val database = AppDatabase.getInstance(context)

    //api call
    suspend fun searchUsers(query: String): NetworkResult<SearchUsersResult> {
        Timber.d("Repo :: searchUsers($query)")
        return withContext(Dispatchers.IO) {
            try {
                NetworkResult.Success(networkClient.searchUsers(query))
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }

    suspend fun getUserDetail(username: String): NetworkResult<User> {
        Timber.d("Repo :: getUserDetail($username)")
        return withContext(Dispatchers.IO) {
            try {
                NetworkResult.Success(networkClient.getUserDetail(username))
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }

    suspend fun getUserFollowers(username: String): NetworkResult<List<User>?> {
        Timber.d("Repo :: getUserFollowers($username)")
        return withContext(Dispatchers.IO) {
            try {
                NetworkResult.Success(networkClient.getUserFollowers(username))
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }

    suspend fun getUserFollowed(username: String): NetworkResult<List<User>?> {
        Timber.d("Repo :: getUserFollowed($username)")
        return withContext(Dispatchers.IO) {
            try {
                NetworkResult.Success(networkClient.getUserFollowed(username))
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }

    //db call
    fun getAllUsers() = database.userDao.getAllUsers()

    fun getUser(id: Long) = database.userDao.getUser(id)

    suspend fun insertUser(user: User) {
        Timber.d("Repo :: insertUser($user)")
        withContext(Dispatchers.IO) {
            database.userDao.insertUser(user)
        }
    }

    //update skipped...

    suspend fun deleteUser(user: User) {
        Timber.d("Repo :: deleteUser($user)")
        withContext(Dispatchers.IO) {
            database.userDao.deleteUser(user)
        }
    }
}