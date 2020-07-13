package com.varoa.gituser.utils

object Constants {
    const val GITHUB_API_BASE_URL = "https://api.github.com/"

    //https://www.dicoding.com/blog/apa-itu-rate-limit-pada-github-api/
    private const val PERSONAL_KEY = "8f91021a673e2e8dfdc742e2f6859f6fc6d71888"

    fun getAuthorizationKey() = "Basic $PERSONAL_KEY"

    //type on UserListFragment...
    const val TYPE_FOLLOWING = 101
    const val TYPE_FOLLOWERS = 102

    //content provider
    const val AUTHORITY = "com.varoa.gituser"

    //widget
    const val WIDGET_TOUCH_ACTION = "widgetTouch"
    const val WIDGET_FILL_EXTRA = "widgetFill"
    const val WIDGET_PENDING_INTENT_CODE = 32
}