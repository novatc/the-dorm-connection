package com.novatc.ap_app.fragments.room

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

//class that handles generic conversion from and to milliseconds and returns readable dates
class RoomDateHelper {

    companion object {
        fun convertUnixToDate(unixDate: Long): String {
            return DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond((unixDate/1000))).split("T")[0]
        }

        fun convertUnixToHoursAndMinutes(unixDate: Long): String {
            val time = DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond((unixDate/1000))).split("T")[1]
            return time.split(":")[0] + ":" + time.split(":")[1]
        }

        fun convertDateToUnix(date: String): String {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(
                ZoneId.systemDefault()).toInstant().epochSecond.toString() + "000"
        }

        private fun millisToHours(millis: Long): Long {
            return millis/3600000L
        }
        fun convertMillisToHoursAndMinutes(millis: Long): String {
            val hours = millisToHours(millis).toInt()
            val minutes = ((millis - (hours * 3600000)) / 60000).toInt()
            return addZeroToShortNumber(hours.toString(), false) + ":" + addZeroToShortNumber(
                minutes.toString(),
                false
            )
        }

        fun addZeroToShortNumber(string: String, fromBehind:Boolean):String{
            return if(string.length < 2){
                if (fromBehind){
                    string + "0"
                } else {
                    "0" + string
                }
            } else{
                string
            }
        }

        fun getTimezone(): Long{
            val anHourInMilliseconds = 3600000L
            val aMinuteInMilliseconds = 60000L
            val calendar = Calendar.getInstance(
                TimeZone.getTimeZone("GMT"),
                Locale.getDefault()
            )
            val currentLocalTime = calendar.time
            val date: DateFormat = SimpleDateFormat("z")
            val localTime: String = date.format(currentLocalTime)
            if(localTime.length > 3){
                val prefix = localTime[3].toString()
                val hours = (localTime[4].toString() + localTime[5].toString()).toInt()
                val minutes = (localTime[7].toString() + localTime[8].toString()).toInt()
                var offsetInMillis = hours * anHourInMilliseconds + minutes * aMinuteInMilliseconds
                if(prefix == "-"){
                    offsetInMillis *= -1
                }
                return offsetInMillis
            }
            else if(localTime == "MEZ"){
                return anHourInMilliseconds
            }
            return 0L
        }
    }

}