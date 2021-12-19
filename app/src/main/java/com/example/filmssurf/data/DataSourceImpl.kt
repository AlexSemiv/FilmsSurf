package com.example.filmssurf.data

import com.example.courseworkdb.CourseDatabase
import com.example.filmssurf.data.DataSource
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import coursework.courseworkdb.SchoolEntity
import coursework.courseworkdb.StudentEntity
import coursework.courseworkdb.StudentSubjectCrossRefEntity
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

    override fun getAllSchoolsOrderByName(): Flow<List<SchoolEntity>> {
        return schoolQueries.getAllSchoolsOrderByName().asFlow().mapToList()
    }

    override fun getAllSchoolsOrderByAddress(): Flow<List<SchoolEntity>> {
        return schoolQueries.getAllSchoolsOrderByAddress().asFlow().mapToList()
    }

    override fun getAllSchoolsOrderBySpecialization(): Flow<List<SchoolEntity>> {
        return schoolQueries.getAllSchoolsOrderBySpecialization().asFlow().mapToList()
    }

    override suspend fun getAllSchoolNames(): List<String> {
        return withContext(Dispatchers.IO) {
            schoolQueries.getAllSchoolNames()
        }.executeAsList()
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

    override suspend fun updateSchool(name: String, specialization: String, address: String) {
        withContext(Dispatchers.IO) {
            schoolQueries.updateSchoolByName(
                name = name,
                specialization = specialization,
                address = address
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

    override fun searchSchools(query: String): Flow<List<SchoolEntity>> {
        return schoolQueries.searchSchools(query).asFlow().mapToList()
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

    override fun getAllStudentsSortedByName(): Flow<List<StudentEntity>> {
        return studentQueries.getAllStudentsSortedByName().asFlow().mapToList()
    }

    override fun getAllStudentsSortedBySchool(): Flow<List<StudentEntity>> {
        return studentQueries.getAllStudentsSortedBySchool().asFlow().mapToList()
    }

    override fun getAllStudentsSortedBySemester(): Flow<List<StudentEntity>> {
        return studentQueries.getAllStudentsSortedBySemester().asFlow().mapToList()
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

    override suspend fun updateStudent(name: String, semester: Long, schoolName: String) {
        withContext(Dispatchers.IO) {
            studentQueries.updateStudentByName(
                name = name,
                semester = semester,
                schoolName = schoolName
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

    override fun searchStudents(query: String): Flow<List<StudentEntity>> {
        return studentQueries.searchStudents(query).asFlow().mapToList()
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

    override fun searchSubjects(query: String): Flow<List<String>> {
        return subjectQueries.searchSubjects(query).asFlow().mapToList()
    }

    // student subject
    override fun getStudentSubjectByStudentName(studentName: String): Flow<List<StudentSubjectCrossRefEntity>> {
        return studentSubjectQueries.getStudentSubjectByStudentName(
            studentName = studentName
        ).asFlow().mapToList()
    }

    override fun getStudentSubjectBySubjectName(subjectName: String): Flow<List<StudentSubjectCrossRefEntity>> {
        return studentSubjectQueries.getStudentSubjectBySubjectName(
            subjectName = subjectName
        ).asFlow().mapToList()
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

    override suspend fun deleteStudentSubject(studentName: String, subjectName: String) {
        withContext(Dispatchers.IO) {
            studentSubjectQueries.deleteStudentSubject(
                studentName = studentName,
                subjectName = subjectName
            )
        }
    }
}