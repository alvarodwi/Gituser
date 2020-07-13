package com.varoa.gituser.ui.common.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.varoa.gituser.R
import com.varoa.gituser.ui.detail.list.UserListFragment
import com.varoa.gituser.ui.detail.overview.OverviewFragment
import com.varoa.gituser.utils.Constants.TYPE_FOLLOWERS
import com.varoa.gituser.utils.Constants.TYPE_FOLLOWING

class DetailVpAdapter(
    private val username: String,
    private val context: Context,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    private val tabTitles = intArrayOf(
        R.string.text_overview,
        R.string.text_follower,
        R.string.text_following
    )

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = OverviewFragment.newInstance(username)
            1 -> fragment = UserListFragment.newInstance(username, TYPE_FOLLOWERS)
            2 -> fragment = UserListFragment.newInstance(username, TYPE_FOLLOWING)
        }
        return fragment as Fragment
    }

    override fun getCount(): Int = tabTitles.size

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabTitles[position])
    }
}