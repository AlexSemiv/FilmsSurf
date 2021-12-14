package com.example.filmssurf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmssurf.data.DataSource
import coursework.courseworkdb.SchoolEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeViewModel @Inject constructor(
    private val dataSource: DataSource
): ViewModel() {

    private val _schoolLiveData = MutableLiveData<SchoolEntity?>()
    val schoolLiveData: LiveData<SchoolEntity?> = _schoolLiveData

    fun getSchoolByName(name: String) {
        viewModelScope.launch {
            val school = dataSource.getSchoolByName(name)
            _schoolLiveData.postValue(school)
        }
    }
}