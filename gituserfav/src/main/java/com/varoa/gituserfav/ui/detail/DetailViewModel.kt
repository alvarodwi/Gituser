package com.varoa.gituserfav.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.varoa.gituserfav.data.model.User
import com.varoa.gituserfav.utils.BaseViewModel
import kotlinx.coroutines.launch

class DetailViewModel(private val userId : Long,app : Application) : BaseViewModel(app) {
    private val mUserData = MutableLiveData<User>()
    val userData : LiveData<User>
    get() = mUserData

    init {
        refreshUserData()
    }

    private fun refreshUserData(){
        viewModelScope.launch {
            mUserData.postValue(repository.getUser(userId))
        }
    }
}