package com.varoa.gituser.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

//static function to show snackbar!
fun View.showSnackbar(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_LONG).show()
}
