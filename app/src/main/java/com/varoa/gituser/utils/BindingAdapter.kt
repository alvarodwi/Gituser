package com.varoa.gituser.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.varoa.gituser.GlideApp
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

@BindingAdapter("printNumber")
fun TextView.printNumber(num : Number){
    text = num.toString()
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

@BindingAdapter("toggleIcon")
fun View.toggleIcon(param : String?){
    if(!param.isNullOrEmpty()){
        this.visibility = View.VISIBLE
    }else{
        this.visibility = View.GONE
    }
}