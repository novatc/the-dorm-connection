package com.novatc.ap_app.fragments.dorm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.DormAdapter
import com.novatc.ap_app.model.Dorm
import com.novatc.ap_app.viewModels.ChooseDormFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_choose_dorm.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class ChooseDormFragment : Fragment(), DormAdapter.OnItemClickListener {
    var dormList: ArrayList<Dorm> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_choose_dorm, container, false)
        populateDormList(view)


        view.tv_create_new_dorm.setOnClickListener {
            val action = ChooseDormFragmentDirections.actionChooseDormFragmentToCreateDormFragment()
            findNavController().navigate(action)
        }

        return view
    }

    @ExperimentalCoroutinesApi
    private fun populateDormList(view: View) {
        val recyclerView: RecyclerView = view.rv_dorm_list
        val model: ChooseDormFragmentViewModel by viewModels()
        model.dormList.observe(this, { dorms ->
            dormList = dorms
            recyclerView.adapter = DormAdapter(dormList, this)
        })
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onItemClick(position: Int) {
        val dorm = dormList[position]
        val action = ChooseDormFragmentDirections.actionChooseDormFragmentToDormDetailFragment(dorm)
        findNavController().navigate(action)

    }


}