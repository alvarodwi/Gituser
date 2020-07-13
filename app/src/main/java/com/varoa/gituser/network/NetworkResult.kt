package com.varoa.gituser.network

import com.squareup.moshi.Json
import com.varoa.gituser.data.model.User

sealed class NetworkResult<out R> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Exception) : NetworkResult<Nothing>()
}

//data class based on json result
data class SearchUsersResult(
    @Json(name = "total_count")
    val totalCount: Int = 0,
    @Json(name = "incomplete_results")
    val incompleteResults: Boolean = false,
    @Json(name = "items")
    val searchResult : List<User>?
)