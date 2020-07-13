package com.varoa.gituser.ui.detail

import android.app.Application
import android.content.Context
import com.varoa.gituser.R
import com.varoa.gituser.data.model.User
import com.varoa.gituser.utils.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailViewModel(userId : Long,app : Application) : BaseViewModel(app) {
    val userData = repository.getUser(userId)

    fun onFavoriteClicked(user: User,context : Context){
        Timber.d("Detail :: ${userData.value} from database")
        if(userData.value == null){
            addToFavorites(user)
            mMessage.postValue(context.getString(R.string.toFavorites))
        }else{
            removeFromFavorites(user)
            mMessage.postValue(context.getString(R.string.fromFavorites))
        }
    }

    private fun addToFavorites(user : User){
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }

    private fun removeFromFavorites(user : User){
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }
}