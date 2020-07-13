package com.varoa.gituser.utils.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.varoa.gituser.GlideApp
import com.varoa.gituser.R
import com.varoa.gituser.data.AppDatabase
import com.varoa.gituser.data.model.User
import com.varoa.gituser.utils.Constants

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private lateinit var database: AppDatabase

    override fun onCreate() {
        database = AppDatabase.getInstance(context)
    }

    override fun onDestroy() {}

    override fun onDataSetChanged() {
        //unused...
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun hasStableIds(): Boolean = false

    override fun getItemId(p0: Int): Long = 0

    override fun getViewTypeCount(): Int = 1

    override fun getViewAt(p0: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.item_stack_widget)
        val widgetItems = getFavoriteUsersData()
        //image loading
        GlideApp.with(context)
            .asBitmap()
            .load(widgetItems[p0].avatar)
            .placeholder(R.drawable.default_placeholder)
            .error(R.drawable.no_internet_dino)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    remoteViews.setImageViewBitmap(R.id.stackItemImg, resource)
                }

                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)
                    remoteViews.setImageViewResource(R.id.stackItemImg, R.drawable.default_placeholder)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    remoteViews.setImageViewResource(R.id.stackItemImg, R.drawable.no_internet_dino)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    remoteViews.setImageViewResource(R.id.stackItemImg, R.drawable.default_placeholder)
                }
            })

        //fill intent
        val fillIntent = Intent()
        fillIntent.putExtra(Constants.WIDGET_FILL_EXTRA,p0)
        remoteViews.setOnClickFillInIntent(R.id.stackItemImg,fillIntent)

        return remoteViews
    }

    override fun getCount(): Int = getFavoriteUsersData().size

    private fun getFavoriteUsersData(): List<User> {
        val result = ArrayList<User>()

        val cursor = database.userDao.getAllUsersAsCursor()
        cursor?.let {
            while (cursor.moveToNext()) {
                User(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(User.COLUMN_ID)),
                    username = cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_USERNAME)),
                    avatar = cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_AVATAR))
                ).also {
                    result.add(it)
                }
            }
            cursor.close()
        }

        return result
    }
}