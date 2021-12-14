package com.example.filmssurf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courseworkdb.data.CourseworkData
import com.example.courseworkdb.data.DataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TablesViewModel @Inject constructor(
    private val dataSource: DataSource
): ViewModel() {

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

    fun getAllSchools() = dataSource.getAllSchools()

    fun getAllStudents() = dataSource.getAllStudents()

    fun getAllSubjects() = dataSource.getAllSubjects()
}