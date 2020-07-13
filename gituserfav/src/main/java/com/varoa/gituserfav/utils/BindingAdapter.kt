package com.varoa.gituserfav.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.varoa.gituserfav.GlideApp
import timber.log.Timber

@BindingAdapter("loadImage")
fun ImageView.loadImage(url : String?){
    url?.let {
        GlideApp.with(this.context)
            .load(url)
            .circleCrop()
            .into(this)
    }
}

@BindingAdapter("toggleVisibility")
fun View.toggleVisibility(flag : Boolean){
    if(flag){
        Timber.d("Toogle Visible")
        this.visibility = View.VISIBLE
    }else{
        Timber.d("Toogle Gone")
        this.visibility = View.GONE
    }
}

