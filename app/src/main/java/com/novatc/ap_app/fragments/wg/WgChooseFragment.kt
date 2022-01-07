package com.novatc.ap_app.fragments.wg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.novatc.ap_app.R
import kotlinx.android.synthetic.main.fragment_wg_choose.view.*

class WgChooseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wg_choose, container, false)
        setupOnSelectWgListener(view)
        setupOnCreateWgListener(view)
        return view
    }

    fun setupOnSelectWgListener(view: View) {
        view.wgChooseSelectExistingButton.setOnClickListener {
            view.findNavController().navigate(WgChooseFragmentDirections.actionWgChooseToWgFragment())
        }
    }

    fun setupOnCreateWgListener(view: View) {
        view.wgChooseCreateNewButton.setOnClickListener {
            view.findNavController().navigate(WgChooseFragmentDirections.actionWgChooseToWgCreateFragment())
        }
    }

}