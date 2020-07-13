package com.varoa.gituser.data.seeder

import com.squareup.moshi.Json
import com.varoa.gituser.data.model.User

//for json parsing!
//bentuk json dari resources kan object users yang isinya array user :D
data class UserJson(
    @Json(name = "users")
    val userList: List<User>
)