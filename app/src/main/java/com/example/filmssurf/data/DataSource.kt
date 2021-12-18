package com.example.filmssurf.data

import coursework.courseworkdb.SchoolEntity
import coursework.courseworkdb.StudentEntity
import coursework.courseworkdb.StudentSubjectCrossRefEntity
import coursework.courseworkdb.SubjectEntity
import kotlinx.coroutines.flow.Flow

interface DataSource {

    // school
    suspend fun getSchoolByName(name: String): SchoolEntity?

    fun getSchoolsBySpecialization(specialization: String): Flow<List<SchoolEntity>>

    fun getSchoolsByAddress(address: String): Flow<List<SchoolEntity>>

    fun getAllSchoolsOrderByName(): Flow<List<SchoolEntity>>

    fun getAllSchoolsOrderByAddress(): Flow<List<SchoolEntity>>

    fun getAllSchoolsOrderBySpecialization(): Flow<List<SchoolEntity>>

    suspend fun insertSchool(
        name: String,
        specialization: String,
        address: String
    )

    suspend fun deleteSchoolByName(name: String)

    suspend fun deleteAllSchools()

    suspend fun getAllSchoolNames() : List<String>

    fun searchSchools(query: String): Flow<List<SchoolEntity>>

    //student
    suspend fun getStudentByName(name: String): StudentEntity?

    fun getStudentsBySemester(semester: Long): Flow<List<StudentEntity>>

    fun getStudentsBySchoolName(name: String): Flow<List<StudentEntity>>

    fun getAllStudentsSortedByName(): Flow<List<StudentEntity>>

    fun getAllStudentsSortedBySemester(): Flow<List<StudentEntity>>

    fun getAllStudentsSortedBySchool(): Flow<List<StudentEntity>>

    suspend fun insertStudent(
        name: String,
        semester: Long,
        schoolName: String
    )

    suspend fun deleteStudentByName(name: String)

    suspend fun deleteAllStudents()

    fun searchStudents(query: String): Flow<List<StudentEntity>>

    //subject
    suspend fun getSubjectByName(name: String): String?

    fun getAllSubjects(): Flow<List<String>>

    suspend fun insertSubject(name: String)

    suspend fun deleteSubjectByName(name: String)

    suspend fun deleteAllSubjects()

    fun searchSubjects(query: String): Flow<List<String>>

    // student subject cross ref
    fun getStudentSubjectByStudentName(studentName: String): Flow<List<StudentSubjectCrossRefEntity>>

    fun getStudentSubjectBySubjectName(subjectName: String): Flow<List<StudentSubjectCrossRefEntity>>

    fun getAllStudentSubject(): Flow<List<StudentSubjectCrossRefEntity>>

    suspend fun insertStudentSubject(
        studentName: String,
        subjectName: String
    )

    suspend fun deleteAllStudentSubject()

    suspend fun deleteStudentSubject(studentName: String, subjectName: String)
}