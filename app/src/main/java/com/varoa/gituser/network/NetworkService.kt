package com.varoa.gituser.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.varoa.gituser.data.model.User
import com.varoa.gituser.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val httpClient = OkHttpClient.Builder().apply {
    this.addInterceptor(
        Interceptor { chain ->
            return@Interceptor chain.proceed(
                chain.request()
                    .newBuilder()
                    .addHeader("Authorization",Constants.getAuthorizationKey())
                    .build()
            )
        }
    )
    this.addInterceptor(
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    )
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.GITHUB_API_BASE_URL)
    .client(httpClient.build())
    .build()

interface GithubApiService {
    //search user(s) by username query
    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String): SearchUsersResult

    //get user detail
    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): User

    //get users that is following this username
    @GET("users/{username}/followers")
    suspend fun getUserFollowers(@Path("username") username: String): List<User>?

    //get users that is followed by this username
    @GET("users/{username}/following")
    suspend fun getUserFollowed(@Path("username") username: String): List<User>?
}

object NetworkService {
    val retrofitService: GithubApiService by lazy {
        retrofit.create(GithubApiService::class.java)
    }
}