package com.novatc.ap_app.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.novatc.ap_app.fragments.room.RoomDetailsBookFragment
import com.novatc.ap_app.fragments.room.RoomDetailsGeneralFragment
import com.novatc.ap_app.fragments.room.RoomDetailsLocationFragment

class RoomDetailsStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    val fragments = listOf(
        RoomDetailsGeneralFragment(),
        RoomDetailsLocationFragment(),
        RoomDetailsBookFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}