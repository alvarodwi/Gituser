package com.varoa.gituserfav.utils

import android.net.Uri
import com.varoa.gituserfav.data.model.User

object Constants {
    private const val AUTHORITY = "com.varoa.gituser"
    private const val SCHEME = "content"

    val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(User.TABLE_NAME)
        .build()
}