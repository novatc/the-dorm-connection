package com.novatc.ap_app.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novatc.ap_app.model.Dorm
import com.novatc.ap_app.model.Post
import com.novatc.ap_app.repository.DormRepository
import com.squareup.okhttp.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class ChooseDormFragmentViewModel @Inject constructor(
    private val dormRepository: DormRepository
): ViewModel(){
    private var _dorms: MutableLiveData<ArrayList<Dorm>> = MutableLiveData<ArrayList<Dorm>>()

    init {
        loadDorms()
    }

    // Variable for exposing livedata to other classes
    internal var dormList: MutableLiveData<ArrayList<Dorm>>
        get() {
            return _dorms
        }
        set(value) {
            _dorms = value
        }

    @ExperimentalCoroutinesApi
    private fun loadDorms(){
        viewModelScope.launch(Dispatchers.IO){
            dormRepository.getPostsAsFlow().collect { dorms->
                val dormListFromDB = ArrayList<Dorm>()
                dorms.forEach {
                    dormListFromDB.add(it)
                }
                withContext(Dispatchers.Main){
                    _dorms.value = dormListFromDB
                }
            }
        }
    }
}