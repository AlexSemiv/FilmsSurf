package com.example.courseworkdb.data

import com.example.courseworkdb.CourseDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import coursework.courseworkdb.SchoolEntity
import coursework.courseworkdb.StudentEntity
import coursework.courseworkdb.StudentSubjectCrossRefEntity
import coursework.courseworkdb.SubjectEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DataSourceImpl(
    source: CourseDatabase
) : DataSource {

    private val studentQueries = source.studentEntityQueries
    private val subjectQueries = source.subjectEntityQueries
    private val schoolQueries = source.schoolEntityQueries
    private val studentSubjectQueries = source.studentSubjectCrossRefEntityQueries

    // school
    override suspend fun getSchoolByName(name: String): SchoolEntity? {
        return withContext(Dispatchers.IO) {
            schoolQueries.getSchoolByName(
                name = name
            ).executeAsOneOrNull()
        }
    }

    override fun getSchoolsBySpecialization(specialization: String): Flow<List<SchoolEntity>> {
        return schoolQueries.getSchoolsBySpecialization(
            specialization = specialization
        ).asFlow().mapToList()
    }

    override fun getSchoolsByAddress(address: String): Flow<List<SchoolEntity>> {
        return schoolQueries.getSchoolsByAddress(
            address = address
        ).asFlow().mapToList()
    }

    override fun getAllSchools(): Flow<List<SchoolEntity>> {
        return schoolQueries.getAllSchools().asFlow().mapToList()
    }

    override suspend fun insertSchool(name: String, specialization: String, address: String) {
        withContext(Dispatchers.IO) {
            schoolQueries.insertSchool(
                _name = name,
                _specialization = specialization,
                _address = address
            )
        }
    }

    override suspend fun deleteSchoolByName(name: String) {
        withContext(Dispatchers.IO) {
            schoolQueries.deleteSchoolByName(
                name = name
            )
        }
    }

    override suspend fun deleteAllSchools() {
        withContext(Dispatchers.IO) {
            schoolQueries.deleteAllSchools()
        }
    }

    // student
    override suspend fun getStudentByName(name: String): StudentEntity? {
        return withContext(Dispatchers.IO) {
            studentQueries.getStudentByName(
                name = name
            ).executeAsOneOrNull()
        }
    }

    override fun getStudentsBySemester(semester: Long): Flow<List<StudentEntity>> {
        return studentQueries.getStudentsBySemester(
            semester = semester
        ).asFlow().mapToList()
    }

    override fun getStudentsBySchoolName(name: String): Flow<List<StudentEntity>> {
        return studentQueries.getStudentsBySchoolName(
            schoolName = name
        ).asFlow().mapToList()
    }

    override fun getAllStudents(): Flow<List<StudentEntity>> {
        return studentQueries.getAllStudents().asFlow().mapToList()
    }

    override suspend fun insertStudent(name: String, semester: Long, schoolName: String) {
        withContext(Dispatchers.IO) {
            studentQueries.insertStudent(
                _name = name,
                _semester = semester,
                _school_name = schoolName
            )
        }
    }

    override suspend fun deleteStudentByName(name: String) {
        withContext(Dispatchers.IO) {
            studentQueries.deleteStudentByName(
                name = name
            )
        }
    }

    override suspend fun deleteAllStudents() {
        withContext(Dispatchers.IO) {
            studentQueries.deleteAllStudents()
        }
    }

    // subject
    override suspend fun getSubjectByName(name: String): String? {
        return withContext(Dispatchers.IO) {
            subjectQueries.getSubjectByName(
                name = name
            ).executeAsOneOrNull()
        }
    }

    override fun getAllSubjects(): Flow<List<String>> {
        return subjectQueries.getAllSubjects().asFlow().mapToList()
    }

    override suspend fun insertSubject(name: String) {
        withContext(Dispatchers.IO) {
            subjectQueries.insertSubject(
                _name = name
            )
        }
    }

    override suspend fun deleteSubjectByName(name: String) {
        withContext(Dispatchers.IO) {
            subjectQueries.deleteSubjectByName(
                name = name
            )
        }
    }

    override suspend fun deleteAllSubjects() {
        withContext(Dispatchers.IO) {
            subjectQueries.deleteAllSubjects()
        }
    }

    // student subject
    override suspend fun getStudentSubjectByStudentName(studentName: String): StudentSubjectCrossRefEntity? {
        return withContext(Dispatchers.IO) {
            studentSubjectQueries.getStudentSubjectByStudentName(
                studentName = studentName
            ).executeAsOneOrNull()
        }
    }

    override suspend fun getStudentSubjectBySubjectName(subjectName: String): StudentSubjectCrossRefEntity? {
        return withContext(Dispatchers.IO) {
            studentSubjectQueries.getStudentSubjectBySubjectName(
                subjectName = subjectName
            ).executeAsOneOrNull()
        }
    }

    override fun getAllStudentSubject(): Flow<List<StudentSubjectCrossRefEntity>> {
        return studentSubjectQueries.getAllStudentSubject().asFlow().mapToList()
    }

    override suspend fun insertStudentSubject(studentName: String, subjectName: String) {
        withContext(Dispatchers.IO) {
            studentSubjectQueries.insertStudentSubject(
                _student_name = studentName,
                _subject_name = subjectName
            )
        }
    }

    override suspend fun deleteAllStudentSubject() {
        withContext(Dispatchers.IO) {
            studentSubjectQueries.deleteAllStudentSubject()
        }
    }
}