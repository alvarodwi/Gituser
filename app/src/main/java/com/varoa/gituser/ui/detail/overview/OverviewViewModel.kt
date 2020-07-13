package com.varoa.gituser.ui.detail.overview

import android.app.Application
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.varoa.gituser.data.model.User
import com.varoa.gituser.network.NetworkResult
import com.varoa.gituser.utils.BaseViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class OverviewViewModel(private val username: String, private val app: Application) :
    BaseViewModel(app) {
    private val mUserData = MutableLiveData<User>()
    val userData : LiveData<User>
    get() = mUserData

    //when running code inspection, this statement is marked as 'Calling new methods on older versions'
    //this is because java 8 Time aren't supported on older devices
    //usually we use ThreeTenABP but since AndroidStudio 4.0, we can use library desugaring
    //see more in module build.gradle or https://developer.android.com/studio/write/java8-support
    //somehow when using inspect code, this is still counted as ERROR =(
    private val gitUserDateFormatter =
        DateTimeFormatter.ofPattern("dd MMMM yyyy", getLocale())
    val joinedDate = userData.map {
        gitUserDateFormatter.format(
            LocalDateTime.ofInstant(Instant.parse(it.joined), ZoneId.systemDefault())
        )
    }

    //api callback
    fun getUserDetail() {
        viewModelScope.launch {
            mLoading.value = true
            when (val result = repository.getUserDetail(username)) {
                is NetworkResult.Success -> mUserData.value = result.data
                is NetworkResult.Error -> mMessage.value = result.exception.message
            }
            mLoading.value = false
        }
    }

    //https://stackoverflow.com/questions/39589841/configuration-locale-variable-is-deprecated-android
    private fun getLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            app.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            app.resources.configuration.locale
        }
    }
}