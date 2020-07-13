package com.varoa.gituser.data.model

import android.content.ContentValues
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = User.TABLE_NAME)
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID) var id : Long = 0,
    @Json(name = "login")
    @ColumnInfo(name = COLUMN_USERNAME) var username : String = "",
    var name : String = "",
    var email : String? = "",
    @Json(name = "avatar_url")
    @ColumnInfo(name = COLUMN_AVATAR) var avatar : String = "",
    var bio : String? = "",
    var company : String? = "",
    var location : String? = "",
    @Json(name = "public_repos")
    var repository: Int = 0,
    var followers : Int = 0,
    var following : Int = 0,
    @Json(name="created_at")
    var joined : String = ""
) : Parcelable{
    companion object{
        const val TABLE_NAME = "users"
        //data that will used on consumer app...
        //WITHOUT DETAIL! if needed, detail will be synced via api =(
        //spare me from writing EVERY COLUMN here so it can be imported using content values...
        const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_AVATAR = "avatar"

        fun fromContentValues(values : ContentValues?) : User{
            return User().apply {
                if(values != null){
                    if(values.containsKey(COLUMN_ID)){
                        this.id = values.getAsLong(COLUMN_ID)
                    }
                    if(values.containsKey(COLUMN_USERNAME)){
                        this.username = values.getAsString(COLUMN_USERNAME)
                    }
                    if(values.containsKey(COLUMN_AVATAR)){
                        this.avatar = values.getAsString(COLUMN_AVATAR)
                    }
                }
            }
        }
    }
}