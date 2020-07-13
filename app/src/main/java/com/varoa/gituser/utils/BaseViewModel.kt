package com.varoa.gituser.utils

import android.app.Application
import androidx.lifecycle.*
import com.varoa.gituser.data.repository.MainRepository
import com.varoa.gituser.ui.detail.DetailViewModel
import com.varoa.gituser.ui.detail.list.UserListViewModel
import com.varoa.gituser.ui.detail.overview.OverviewViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {
    //coroutine
    private val viewModelJob = Job()
    protected val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    //data
    protected val repository = MainRepository.getInstance(app.applicationContext)

    //live data that is reusable!
    protected val mMessage = MutableLiveData<String>()
    val message: LiveData<String>
        get() = mMessage

    protected val mLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = mLoading

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val params : Any, val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            when {
                modelClass.isAssignableFrom(OverviewViewModel::class.java) -> {
                    return OverviewViewModel(params as String, app) as T
                }
                modelClass.isAssignableFrom(UserListViewModel::class.java) -> {
                    return UserListViewModel(params as String, app) as T
                }
                modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                    return DetailViewModel(params as Long,app) as T
                }
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }
    }
}