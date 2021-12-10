package com.novatc.ap_app.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.novatc.ap_app.R

class RoomDetailFragment : Fragment() {

    companion object {
        fun newInstance() = RoomDetailFragment()
    }

    private lateinit var viewModel: RoomDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.room_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RoomDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}