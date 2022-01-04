package com.example.filmssurf.other

import android.text.Editable

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

enum class SchoolSortType {
    NAME, ADDRESS, SPECIALIZATION
}

enum class StudentSortType {
    NAME, SEMESTER, SCHOOL
}