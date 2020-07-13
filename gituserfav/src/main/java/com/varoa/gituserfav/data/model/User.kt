package com.varoa.gituserfav.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id : Long = 0,
    var username : String = "",
    var avatar : String = ""
) : Parcelable{
    companion object{
        const val TABLE_NAME = "users"
        //data that will used on consumer app...
        //WITHOUT DETAIL! if needed, detail will be synced via api =(
        //spare me from writing EVERY COLUMN here so it can be imported using content values...
        const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_AVATAR = "avatar"
    }
}