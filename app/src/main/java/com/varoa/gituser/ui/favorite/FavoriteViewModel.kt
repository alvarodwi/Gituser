package com.varoa.gituser.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.varoa.gituser.data.model.User
import com.varoa.gituser.utils.BaseViewModel

class FavoriteViewModel(app : Application) : BaseViewModel(app) {
    //data from db :)
    private val mUserList = repository.getAllUsers()
    val userList : LiveData<List<User>>
        get() = mUserList

    val isUserListEmpty = mUserList.map {
        it.isNullOrEmpty()
    }
}