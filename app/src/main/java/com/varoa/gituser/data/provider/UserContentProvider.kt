package com.varoa.gituser.data.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.varoa.gituser.data.AppDatabase
import com.varoa.gituser.data.model.User
import com.varoa.gituser.utils.Constants.AUTHORITY

class UserContentProvider : ContentProvider() {
    companion object {
        private const val CODE_USER_DIR = 1
        private const val CODE_USER_ITEM = 2

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var appDatabase: AppDatabase
        
        init {
            // uri untuk mengakses users list
            // content://com.varoa.gituser/users
            MATCHER.addURI(AUTHORITY, User.TABLE_NAME, CODE_USER_DIR)

            // uri untuk mengakses item user
            // content://com.varoa.gituser/users/:id
            MATCHER.addURI(AUTHORITY, "${User.TABLE_NAME}/#", CODE_USER_ITEM)
        }
    }
    private lateinit var mContext: Context

    override fun onCreate(): Boolean {
        mContext = context as Context
        appDatabase = AppDatabase.getInstance(mContext)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return when (MATCHER.match(uri)) {
            CODE_USER_DIR -> appDatabase.userDao.getAllUsersAsCursor()
            CODE_USER_ITEM -> appDatabase.userDao.getUserAsCursor(ContentUris.parseId(uri))
            else -> null
        }.apply {
            this?.setNotificationUri(mContext.contentResolver, uri)
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (MATCHER.match(uri)) {
            CODE_USER_DIR -> {
                val id: Long = appDatabase.userDao.insertUser(User.fromContentValues(values))
                mContext.contentResolver.notifyChange(uri, null)
                //return new content uri...
                ContentUris.withAppendedId(uri, id)
            }
            CODE_USER_ITEM -> {
                throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return when (MATCHER.match(uri)) {
            CODE_USER_DIR -> {
                throw IllegalArgumentException("Invalid URI, cannot update without ID: $uri")
            }
            CODE_USER_ITEM -> {
                val count: Int = appDatabase.userDao.updateUser(User.fromContentValues(values))
                mContext.contentResolver.notifyChange(uri, null)
                //return count
                count
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return when (MATCHER.match(uri)) {
            CODE_USER_DIR -> {
                throw IllegalArgumentException("Invalid URI, cannot update without ID: $uri")
            }
            CODE_USER_ITEM -> {
                val count: Int = appDatabase.userDao.deleteUserById(ContentUris.parseId(uri))
                mContext.contentResolver.notifyChange(uri, null)
                //return count
                count
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }
}
