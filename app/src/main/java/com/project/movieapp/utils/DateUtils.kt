package com.project.movieapp.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtils private constructor() {
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("en", "EN"))
    private val apiDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    fun convertApiDateToDateTime(inputDate: String?): String {
        val date: Date?
        var outputDate = ""
        if (inputDate != null) {
            try {
                date = apiDateFormat.parse(inputDate)
                if (date != null) outputDate = dateFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return outputDate
    }

    companion object {
        fun newInstance(): DateUtils {
            return DateUtils()
        }
    }

}
