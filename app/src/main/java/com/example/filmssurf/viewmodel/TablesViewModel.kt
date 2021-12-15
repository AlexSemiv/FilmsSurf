package com.example.filmssurf.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courseworkdb.data.CourseworkData
import com.example.filmssurf.data.DataSource
import coursework.courseworkdb.SchoolEntity
import coursework.courseworkdb.StudentEntity
import coursework.courseworkdb.SubjectEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TablesViewModel @Inject constructor(
    private val dataSource: DataSource
) : ViewModel() {

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

    fun deleteSchool(school: SchoolEntity) {
        viewModelScope.launch {
            dataSource.deleteSchoolByName(school._name)
        }
    }

    fun getAllStudents() = dataSource.getAllStudents()

    fun deleteStudent(student: StudentEntity) {
        viewModelScope.launch {
            dataSource.deleteStudentByName(student._name)
        }
    }

    fun getAllSubjects() = dataSource.getAllSubjects()

    fun deleteSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            dataSource.deleteSubjectByName(subject._name)
        }
    }

    fun getStudentsBySchoolName(name: String) = dataSource.getStudentsBySchoolName(name)

    fun getSubjectsByStudentName(name: String) = dataSource.getStudentSubjectByStudentName(name)
        .map { it.map { stsub -> stsub._subject_name } }

    fun getStudentsBySubjectName(name: String) =
        dataSource.getStudentSubjectBySubjectName(name).map {
            it.map {
                viewModelScope.async {
                    dataSource.getStudentByName(it._student_name)
                }
            }.awaitAll()
        }
}