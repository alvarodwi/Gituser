package com.varoa.gituser.ui.detail.list

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.varoa.gituser.data.model.User
import com.varoa.gituser.network.NetworkResult
import com.varoa.gituser.utils.BaseViewModel
import com.varoa.gituser.utils.Constants.TYPE_FOLLOWING
import kotlinx.coroutines.launch
import timber.log.Timber

class UserListViewModel(private val username : String,app : Application) : BaseViewModel(app) {
    private val usersData = MutableLiveData<List<User>>()
    val isUsersDataEmpty = usersData.map {
        it.isNullOrEmpty()
    }

    fun getUsersData() = usersData

    fun getUserSocials(type : Int){
        when(type){
            TYPE_FOLLOWING -> getFollowing()
            else -> getFollowers()
        }
    }

    private fun getFollowers(){
        viewModelScope.launch {
            mLoading.value = true
            when(val result = repository.getUserFollowers(username)){
                is NetworkResult.Success -> usersData.value = result.data
                is NetworkResult.Error -> {
                    Timber.e(result.exception)
                    mMessage.value = result.exception.message
                }
            }
            mLoading.value = false
        }
    }

    private fun getFollowing(){
        viewModelScope.launch {
            mLoading.value = true
            when(val result = repository.getUserFollowed(username)){
                is NetworkResult.Success -> usersData.value = result.data
                is NetworkResult.Error -> mMessage.value = result.exception.message
            }
            mLoading.value = false
        }
    }
}