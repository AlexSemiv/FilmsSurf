package com.example.filmssurf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmssurf.data.DataSource
import coursework.courseworkdb.SchoolEntity
import coursework.courseworkdb.StudentEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
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

    fun changeExistedSchool(
        name: String,
        specialization: String,
        address: String
    ) {
        viewModelScope.launch {
            dataSource.insertSchool(
                name, specialization, address
            )
        }
    }

    private val _studentLiveData = MutableLiveData<StudentEntity?>()
    val studentLiveData: LiveData<StudentEntity?> = _studentLiveData
    private val _schoolNamesLiveData = MutableLiveData<List<String>>()
    val schoolNamesLiveData: LiveData<List<String>> = _schoolNamesLiveData


    fun getStudentByName(name: String) {
        viewModelScope.launch {
            val schools = dataSource.getAllSchoolNames()
            _schoolNamesLiveData.postValue(schools)

            val student = dataSource.getStudentByName(name)
            _studentLiveData.postValue(student)
        }
    }

    fun changeExistedStudent(
        name: String,
        semester: Long,
        schoolName: String
    ) {
        viewModelScope.launch {
            dataSource.insertStudent(
                name, semester, schoolName
            )
        }
    }

    fun getAllSubjects() = dataSource.getAllSubjects()

    fun getSubjectsByStudentName(name: String) = dataSource.getStudentSubjectByStudentName(name)
        .map {
            it.map { cross ->
                cross._subject_name
            }
        }

    fun deleteStudentSubject(studentName: String, subjectName: String) {
        viewModelScope.launch {
            dataSource.deleteStudentSubject(
                studentName = studentName,
                subjectName = subjectName
            )
        }
    }

    fun insertStudentSubject(studentName: String, subjectName: String) {
        viewModelScope.launch {
            dataSource.insertStudentSubject(
                studentName = studentName,
                subjectName = subjectName
            )
        }
    }

    fun getAllStudents() = dataSource.getAllStudents()

    fun getStudentsBySubjectName(name: String) = dataSource.getStudentSubjectBySubjectName(name)
}