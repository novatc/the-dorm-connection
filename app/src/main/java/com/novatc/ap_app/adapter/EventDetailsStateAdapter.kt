package com.novatc.ap_app.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.novatc.ap_app.fragments.event.EventDetailsGeneralFragment
import com.novatc.ap_app.fragments.event.EventDetailsJoinFragment
import com.novatc.ap_app.fragments.event.EventDetailsLocationFragment

class EventDetailsStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    val fragments = listOf(
        EventDetailsGeneralFragment(),
        EventDetailsLocationFragment(),
        EventDetailsJoinFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}