package com.example.playlistmaker2.mediaLibrary.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaLibraryPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    companion object {
        private const val MEDIA_LIB_TABS = 2
    }

    override fun getItemCount(): Int = MEDIA_LIB_TABS

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) FavoriteTracksFragment.newInstance() else PlaylistFragment.newInstance()
    }
}