package com.varoa.gituser.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.varoa.gituser.data.model.User
import com.varoa.gituser.network.NetworkResult
import com.varoa.gituser.utils.BaseViewModel
import kotlinx.coroutines.launch

class SearchViewModel(app: Application) : BaseViewModel(app) {
    //live data for this ui
    private val searchResult = MutableLiveData<List<User>>()

    val isSearchResultEmpty = searchResult.map {
        it.isEmpty()
    }

    init {
        seedFavoritesData()
    }

    fun commenceSearch(username : String){
        //do search from coroutine scope
        viewModelScope.launch {
            //set loading as true
            mLoading.value = true
            when(val result = repository.searchUsers(username)){
                is NetworkResult.Success -> searchResult.value = result.data.searchResult
                is NetworkResult.Error -> mMessage.value = result.exception.message
            }
            mLoading.value = false
        }
    }

    fun getSearchResult() = searchResult

    //seeding from JSON!
    private fun seedFavoritesData(){

    }
}