package com.example.courseworkdb.data

import kotlin.random.Random

data class School(
    val schoolName: String,
    val specialization: String,
    val address: String
)

data class Subject(
    val subjectName: String
)

data class Student(
    val studentName: String,
    val semester: Long,
    val schoolName: String
)

data class StudentSubjectCrossRef(
    val studentName: String,
    val subjectName: String
)

object CourseworkData {
    val schools = listOf(
        School("Jake Wharton School", "Math", "London, 12"),
        School("Kotlin School", "Programming", "Moscow, 22"),
        School("JetBrains School", "Programming", "Moscow, 24"),
        School("JavaScript School", "Programming", "Orenburg, 33"),
        School("Mail.ru School", "Math", "Moscow, 44"),
        School("Weather Mapper School", "Building", "Saint-Petersburg, 50")
    )

    val subjects = listOf(
        Subject("Dating for programmers"),
        Subject("Avoiding depression"),
        Subject("Bug Fix Meditation"),
        Subject("Logcat for Newbies"),
        Subject("How to use Google"),
        Subject("How to create word office file"),
        Subject("Where you can find well paid job"),
        Subject("How to survive in world without programming"),
        Subject("Best practices in user interfaces with assembler language"),
        Subject("How don't hating unity engine every day")
    )

    private val randomNames = listOf(
        "Courtney Butler",
        "Mark Baker",
        "Julie Phillips",
        "Robin Castillo",
        "Ben Wilson",
        "Helen Smith",
        "Cindy Stanley",
        "Thomas Mitchell",
        "Thomas Hansen",
        "Johnnie Robinson",
        "Christina Day",
        "Terri Williams",
        "William Wood",
        "Timothy Carpenter",
        "James Thompson",
        "Mary Wolfe",
        "Bertha Schultz",
        "Antonio Harmon",
        "Roland Thompson",
        "Heather Thompson",
        "Karen Williams",
        "Ida Hernandez",
        "Todd Lawrence",
        "Tracy Quinn",
        "Cindy Banks",
        "Gloria White",
        "Ann Hart",
        "Elizabeth Wilkerson",
        "Michael Ward",
        "Melanie Baker",
        "James Evans",
        "Angela Jones",
        "Carolyn Rowe",
        "Evelyn Thomas",
        "Sherry Stevens",
        "James Carter",
        "Benjamin Lopez",
        "Laura Johnson",
        "Barbara Cobb",
        "Edward Simon",
        "Daniel Johnson",
        "Robert Pierce",
        "John Cortez",
        "Stanley Gregory",
        "Jerry Lyons",
        "Casey Johnson",
        "Richard Gray",
        "Bobby Long",
        "James Page",
        "Patricia Marsh"
    )

    private val randomSemester = listOf(1, 2, 3, 4, 5)

    val students = mutableListOf<Student>()
    val studentSubjectRelations = mutableSetOf<StudentSubjectCrossRef>()

    init {
        var i = 0
        repeat(40) {
            val nameIndex = Random(i++).nextInt(randomNames.size)
            val semesterIndex = Random(i++).nextInt(randomSemester.size)
            val schoolIndex = Random(i++).nextInt(schools.size)
            students.add(
                Student(
                    randomNames[nameIndex],
                    randomSemester[semesterIndex].toLong(),
                    schools[schoolIndex].schoolName
                )
            )
        }

        repeat(100) {
            val studentIndex = Random(i++).nextInt(students.size)
            val subjectIndex = Random(i++).nextInt(subjects.size)
            studentSubjectRelations.add(
                StudentSubjectCrossRef(
                    studentName = students[studentIndex].studentName,
                    subjectName = subjects[subjectIndex].subjectName
                )
            )
        }
    }
}