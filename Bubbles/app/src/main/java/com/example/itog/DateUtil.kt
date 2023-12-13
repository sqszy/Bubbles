package com.example.itog

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    companion object {
        fun getDaysAgoFromDate(dateStr: String): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val reviewDate = dateFormat.parse(dateStr)
            val currentDate = Calendar.getInstance().time

            val differenceInMillis = currentDate.time - reviewDate.time
            val differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24)

            return "$differenceInDays дней назад"
        }
    }
}
