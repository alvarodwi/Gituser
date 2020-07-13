package com.varoa.gituserfav.data.source

import android.content.ContentResolver
import android.content.ContentUris
import com.varoa.gituserfav.data.model.User
import com.varoa.gituserfav.utils.Constants

class UserDataSource(
    private val contentResolver: ContentResolver
) {
    fun fetchUsers(): List<User> {
        val result: MutableList<User> = mutableListOf()

        val cursor =
            contentResolver.query(Constants.CONTENT_URI, null, null, null, null)
        cursor?.let {
            cursor.moveToFirst()
            while(!cursor.isAfterLast){
                User(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(User.COLUMN_ID)),
                    username = cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USERNAME)),
                    avatar = cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_AVATAR))
                ).also {
                    result.add(it)
                    cursor.moveToNext()
                }
            }
        }
        cursor?.close()

        return result.toList()
    }

    fun fetchUserById(userId : Long) : User{
        val result = User()
        val uriWithId = ContentUris.withAppendedId(Constants.CONTENT_URI,userId)

        val cursor =
            contentResolver.query(uriWithId, null, null, null, null)
        cursor?.let {
            cursor.moveToFirst()
            result.apply {
                id = cursor.getLong(cursor.getColumnIndexOrThrow(User.COLUMN_ID))
                username = cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USERNAME))
                avatar = cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_AVATAR))
            }
        }
        cursor?.close()

        return result
    }
}