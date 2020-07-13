package com.varoa.gituserfav.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.varoa.gituserfav.data.model.User
import com.varoa.gituserfav.utils.BaseViewModel
import kotlinx.coroutines.launch

class FavoriteViewModel(app: Application) : BaseViewModel(app) {
    //data from content values :)
    //https://medium.com/@jossypaul/loading-data-from-contentprovider-using-coroutines-and-livedata-34aa5e79b8ba
    private val mUserList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>>
        get() = mUserList

    val isUserListEmpty = mUserList.map {
        it.isNullOrEmpty()
    }

    init {
        refreshUserList()
    }

    fun refreshUserList(){
        viewModelScope.launch {
            mUserList.postValue(repository.getFavoriteUsers())
        }
    }
}