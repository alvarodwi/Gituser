package com.varoa.gituserfav.utils

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.varoa.gituserfav.data.repository.MainRepository
import com.varoa.gituserfav.ui.detail.DetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {
    //coroutine
    private val viewModelJob = Job()
    protected val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    //data
    protected val repository = MainRepository.getInstance(app.applicationContext)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val params : Any, val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            if(modelClass.isAssignableFrom(DetailViewModel::class.java)){
                return DetailViewModel(params as Long,app) as T
            }
            throw IllegalArgumentException("Unable To Construct ViewModel")
        }
    }
}