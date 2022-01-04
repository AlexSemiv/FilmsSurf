package com.example.filmssurf.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.courseworkdb.data.CourseworkData
import com.example.courseworkdb.data.Student
import com.example.filmssurf.data.DataSource
import com.example.filmssurf.other.SchoolSortType
import com.example.filmssurf.other.StudentSortType
import coursework.courseworkdb.SchoolEntity
import coursework.courseworkdb.StudentEntity
import coursework.courseworkdb.SubjectEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TablesViewModel @Inject constructor(
    private val dataSource: DataSource
) : ViewModel() {

    private val _schoolSortFlow = MutableStateFlow(SchoolSortType.NAME)
    val schoolSortType: LiveData<SchoolSortType> = _schoolSortFlow.asLiveData()
    suspend fun setSchoolSortType(type: SchoolSortType) {
        _schoolSortFlow.emit(type)
    }

    private val _schoolSearchFlow = MutableStateFlow("")
    val schoolSearchQuery: LiveData<String> = _schoolSearchFlow.asLiveData()
    suspend fun setSchoolSearchQuery(query: String) {
        _schoolSearchFlow.emit(query)
    }

    private val _studentSortFlow = MutableStateFlow(StudentSortType.NAME)
    val studentSortType: LiveData<StudentSortType> = _studentSortFlow.asLiveData()
    suspend fun setStudentSortType(type: StudentSortType) {
        _studentSortFlow.emit(type)
    }

    private val _studentSearchFlow = MutableStateFlow("")
    val studentSearchQuery: LiveData<String> = _studentSearchFlow.asLiveData()
    suspend fun setStudentSearchQuery(query: String) {
        _studentSearchFlow.emit(query)
    }

    private val _subjectSearchFlow = MutableStateFlow("")
    val subjectSearchQuery: LiveData<String> = _subjectSearchFlow.asLiveData()
    suspend fun setSubjectSearchQuery(query: String) {
        _subjectSearchFlow.emit(query)
    }

    init {
        viewModelScope.launch {
            dataSource.deleteAllSchools()
            dataSource.deleteAllSubjects()
            dataSource.deleteAllStudents()
            dataSource.deleteAllStudentSubject()

            CourseworkData.schools.forEach {
                dataSource.insertSchool(
                    name = it.schoolName,
                    specialization = it.specialization,
                    address = it.address
                )
            }

            CourseworkData.subjects.forEach {
                dataSource.insertSubject(
                    name = it.subjectName
                )
            }

            CourseworkData.students.forEach {
                dataSource.insertStudent(
                    name = it.studentName,
                    semester = it.semester,
                    schoolName = it.schoolName
                )
            }

            CourseworkData.studentSubjectRelations.forEach {
                dataSource.insertStudentSubject(
                    studentName = it.studentName,
                    subjectName = it.subjectName
                )
            }
        }
    }

    private val sortedSchools = _schoolSortFlow
        .flatMapLatest {
            when(it) {
                SchoolSortType.NAME -> {
                    dataSource.getAllSchoolsOrderByName()
                }
                SchoolSortType.ADDRESS -> {
                    dataSource.getAllSchoolsOrderByAddress()
                }
                SchoolSortType.SPECIALIZATION -> {
                    dataSource.getAllSchoolsOrderBySpecialization()
                }
            }
        }

    private val searchedSchools = _schoolSearchFlow
        .flatMapLatest {
            dataSource.searchSchools(it)
        }

    val schools = sortedSchools.combine(searchedSchools) { sorted, searched ->
        sorted.intersect(searched.toSet()).toList()
    }

    private val sortedStudents = _studentSortFlow
        .flatMapLatest {
            when(it) {
                StudentSortType.NAME -> {
                    dataSource.getAllStudentsSortedByName()
                }
                StudentSortType.SEMESTER -> {
                    dataSource.getAllStudentsSortedBySemester()
                }
                StudentSortType.SCHOOL -> {
                    dataSource.getAllStudentsSortedBySchool()
                }
            }
        }

    private val searchedStudents = _studentSearchFlow
        .flatMapLatest {
            dataSource.searchStudents(it)
        }

    val students = sortedStudents.combine(searchedStudents) { sorted, searched ->
        sorted.intersect(searched.toSet()).toList()
    }

    fun deleteSchool(school: SchoolEntity) {
        viewModelScope.launch {
            dataSource.deleteSchoolByName(school._name)
        }
    }

    fun deleteStudent(student: StudentEntity) {
        viewModelScope.launch {
            dataSource.deleteStudentByName(student._name)
        }
    }

    val subjects = _subjectSearchFlow
        .flatMapLatest {
            dataSource.searchSubjects(it)
        }

    fun deleteSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            dataSource.deleteSubjectByName(subject._name)
        }
    }

    fun getStudentsBySchoolName(name: String) = dataSource.getStudentsBySchoolName(name)

    fun getSubjectsByStudentName(name: String) = dataSource.getStudentSubjectByStudentName(name)
        .map {
            it.map { cross ->
                cross._subject_name
            }
        }

    fun getStudentsBySubjectName(name: String) =
        dataSource.getStudentSubjectBySubjectName(name).map {
            it.map {
                viewModelScope.async {
                    dataSource.getStudentByName(it._student_name)
                }
            }.awaitAll()
        }
}