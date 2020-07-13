package com.varoa.gituser.data.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.varoa.gituser.data.model.User

@Dao
interface UserDao {
    //note : tabel users hanya dipakai untuk menyimpan favorited users saja...
    //karena submisi tidak menuliskan "offline caching" sebagai syarat atau optional :D

    //get all users,order it by username!
    @Query("SELECT * from users ORDER BY username ASC")
    fun getAllUsers() : LiveData<List<User>>

    //get user detail
    @Query("SELECT * from users WHERE id = :id")
    fun getUser(id : Long) : LiveData<User?>

    //function for content provider
    //return it as Cursor!
    //https://github.com/android/architecture-components-samples/tree/master/PersistenceContentProviderSample
    @Query("SELECT * from users ORDER BY username ASC")
    fun getAllUsersAsCursor() : Cursor?

    @Query("SELECT * from users WHERE id = :id")
    fun getUserAsCursor(id : Long?) : Cursor?

    @Query("DELETE FROM users WHERE id = :id")
    fun deleteUserById(id : Long?) : Int

    //end of content provider queries...

    //create user
    @Insert
    fun insertUser(user : User) : Long

    @Update
    fun updateUser(user : User) : Int

    @Delete
    fun deleteUser(user : User) : Int
}