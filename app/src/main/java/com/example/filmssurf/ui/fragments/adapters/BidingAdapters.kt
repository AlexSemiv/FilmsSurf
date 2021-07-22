package com.example.filmssurf.ui.fragments.adapters

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.filmssurf.other.Utils.DEBUG_TAG
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("app:setDate")
fun dateFormat(view: TextView, dateFormat: String?){
    try {
        val original = SimpleDateFormat("yyyy-MM-dd", Locale("ru", "RU"))
        val target = SimpleDateFormat("dd MMMM yyyy", Locale("ru", "RU"))
        val date = original.parse(dateFormat ?: "1000-10-01")
        val formattedDate = target.format(date)
        view.text = formattedDate
    } catch (e: Exception) {
        Log.d(DEBUG_TAG, "ParseException: ${e.message}")
    }
}