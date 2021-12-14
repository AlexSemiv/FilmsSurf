package com.example.courseworkdb.data

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

    fun getAllSchools(): Flow<List<SchoolEntity>>

    suspend fun insertSchool(
        name: String,
        specialization: String,
        address: String
    )

    suspend fun deleteSchoolByName(name: String)

    suspend fun deleteAllSchools()

    //student
    suspend fun getStudentByName(name: String): StudentEntity?

    fun getStudentsBySemester(semester: Long): Flow<List<StudentEntity>>

    fun getStudentsBySchoolName(name: String): Flow<List<StudentEntity>>

    fun getAllStudents(): Flow<List<StudentEntity>>

    suspend fun insertStudent(
        name: String,
        semester: Long,
        schoolName: String
    )

    suspend fun deleteStudentByName(name: String)

    suspend fun deleteAllStudents()

    //subject
    suspend fun getSubjectByName(name: String): String?

    fun getAllSubjects(): Flow<List<String>>

    suspend fun insertSubject(name: String)

    suspend fun deleteSubjectByName(name: String)

    suspend fun deleteAllSubjects()

    // student subject cross ref
    suspend fun getStudentSubjectByStudentName(studentName: String): StudentSubjectCrossRefEntity?

    suspend fun getStudentSubjectBySubjectName(subjectName: String): StudentSubjectCrossRefEntity?

    fun getAllStudentSubject(): Flow<List<StudentSubjectCrossRefEntity>>

    suspend fun insertStudentSubject(
        studentName: String,
        subjectName: String
    )

    suspend fun deleteAllStudentSubject()
}