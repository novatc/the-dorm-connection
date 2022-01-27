package com.novatc.ap_app.fragments.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.novatc.ap_app.viewModels.RoomDetailsViewModel
import kotlinx.android.synthetic.main.fragment_room_details_book.view.*
import kotlinx.android.synthetic.main.fragment_room_details_general.view.*
import com.applandeo.materialcalendarview.EventDay

import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import kotlinx.android.synthetic.main.fragment_room_create.view.*
import java.util.*
import android.app.TimePickerDialog
import android.graphics.Color
import android.util.Log
import android.widget.*

import androidx.lifecycle.lifecycleScope
import com.novatc.ap_app.model.*
import kotlinx.android.synthetic.main.fragment_post_details.view.*
import kotlinx.android.synthetic.main.fragment_room_create.*
import kotlinx.android.synthetic.main.fragment_room_details_book.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class RoomDetailsBookFragment : Fragment(), TimePickerDialog.OnTimeSetListener{
    val model: RoomDetailsViewModel by activityViewModels()

    val partlyBookedDays: ArrayList<EventDay> = ArrayList()
    val fullyBookedDays: ArrayList<EventDay> = ArrayList()
    var bookedDaysViaDate: HashMap<String, ArrayList<Long>> = HashMap()
    lateinit var calendar: com.applandeo.materialcalendarview.CalendarView
    lateinit var selectedDate :Calendar
    lateinit var currentTimePicker :String
    lateinit var startingTime: TextView
    lateinit var endTime: TextView
    private var selectedRoom: Room = Room()
    private var currentUser: User = User()
    private var bookingListOnRoom: ArrayList<Booking> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.novatc.ap_app.R.layout.fragment_room_details_book, container, false)
        model.room.observe(this, { room ->
            selectedRoom = room
        })
        model.loadBookings()
        calendar = view.calendar
        var instance: Calendar = Calendar.getInstance()
        instance.add(Calendar.DATE, -1)
        calendar.setMinimumDate(instance)
        selectedDate = calendar.firstSelectedDate
        startingTime = view.starting_time_text
        endTime = view.end_time_text
        addDateListener()
        addSaveDateButtonListener(view)
        addTimeOfDayTextViewListener()
        addBookDateButtonListener(view)
        currentTimePicker = "start"
        getUserName()
        populateCalendar(view, selectedRoom)
        return view
    }

    fun addDateListener(){
        calendar.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                selectedDate = eventDay.calendar
                if(calendar.selectedDates.isEmpty()){
                   calendar.setDate(selectedDate)
                }
        calendar.setDate(selectedDate)
            }
        })
    }

    fun addSaveDateButtonListener(view: View){
        val setDateButton: Button = view.button2
        setDateButton.setOnClickListener {
            var day: String = selectedDate.get(Calendar.DAY_OF_MONTH).toString()
            day = addZeroToShortNumber(day, false)
            var month: String = (selectedDate.get(Calendar.MONTH)+1).toString()
            month = addZeroToShortNumber(month, false)
            var bookedDateText: String = day + "." + month +"."+ selectedDate.get(Calendar.YEAR)
            booked_date_1.text = bookedDateText
            booked_date_2.text = bookedDateText
            val layout = view.tutView1
            layout.visibility = View.GONE
            val layout2 = view.tutView2
            layout2.visibility = View.VISIBLE
        }
    }

    fun addBookDateButtonListener(view: View){
        val setDateButton: Button = view.book_timeslot_button
        setDateButton.setOnClickListener {
            if(startingTime.text.contains(":") && endTime.text.contains(":")){
                var startingHour: Int = startingTime.text.split(":")[0].toInt()
                var startingMinute: Int = startingTime.text.split(":")[1].toInt()
                var endingHour: Int = endTime.text.split(":")[0].toInt()
                var endingMinute: Int = endTime.text.split(":")[1].toInt()
                if(startingHour > endingHour){
                    Toast.makeText(context!!, com.novatc.ap_app.R.string.booking_time_incorrect, Toast.LENGTH_SHORT).show()
                }
                else if(startingHour == endingHour && startingMinute < endingMinute){
                    Toast.makeText(context!!, com.novatc.ap_app.R.string.booking_time_incorrect, Toast.LENGTH_SHORT).show()
                }
                else{
                    val startingDate = booked_date_1.text.toString() + " " + startingTime.text.toString()
                    val endingDate = booked_date_2.text.toString() + " " + endTime.text.toString()
                    val formatter: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.GERMAN)
                    val startingDateInMilliseconds: Long = LocalDateTime.parse(startingDate, formatter).atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
                    val endingDateInMilliseconds: Long = LocalDateTime.parse(endingDate, formatter).atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
                    when {
                        endingDateInMilliseconds - startingDateInMilliseconds < selectedRoom.minimumBookingTime?.toLong()!! -> {
                            Toast.makeText(context!!, com.novatc.ap_app.R.string.booking_time_falls_below_the_minimum_booking_time, Toast.LENGTH_SHORT).show()
                        }
                        endingDateInMilliseconds - startingDateInMilliseconds > selectedRoom.maximumBookingTime?.toLong()!! -> {
                            Toast.makeText(context!!, com.novatc.ap_app.R.string.booking_time_exceeds_maximum_booking_time, Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            var c: Booking? = Booking(
                                userID = currentUser.id,
                                startingDate = startingDateInMilliseconds,
                                endDate = endingDateInMilliseconds
                            )

                            lifecycleScope.launch {
                                selectedRoom.id?.let { it1 ->
                                    if (c != null) {
                                        model.addBooking(selectedRoom.id!!, c)
                                    }
                                }
                            }
                            Toast.makeText(context!!, com.novatc.ap_app.R.string.successful_booking, Toast.LENGTH_SHORT).show()
                            val layout = view.tutView1
                            layout.visibility = View.VISIBLE
                            val layout2 = view.tutView2
                            layout2.visibility = View.GONE
                        }
                    }
                }
            }
            else{
                Toast.makeText(context!!, com.novatc.ap_app.R.string.empty_field_error, Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun addTimeOfDayTextViewListener(){
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR)
        val minute = cal.get(Calendar.MINUTE)
        startingTime.setOnClickListener {
            currentTimePicker = "start"
            TimePickerDialog(context!!, this, hour, minute, true).show()
        }
        endTime.setOnClickListener{
            currentTimePicker = "end"
            TimePickerDialog(context!!, this, hour, minute, true).show()
        }
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        Log.d("Datepicker", "Stunde: $hour Minute: $minute")
        var hourString = addZeroToShortNumber(hour.toString(), false)
        var minuteString = addZeroToShortNumber(minute.toString(), false)
        var resultTime = hourString + ":" + minuteString
        if(currentTimePicker.equals("start")){
            startingTime.text = resultTime
        }
        else{
            endTime.text = resultTime
        }
    }

    @ExperimentalCoroutinesApi
    fun getUserName(){
        model.userProfile.observe(viewLifecycleOwner,{
            currentUser = it
        })
    }

    @ExperimentalCoroutinesApi
    private fun populateCalendar(view: View, room: Room) {
        model.bookingList.observe(this, { booking ->
            bookingListOnRoom = booking
            if(bookingListOnRoom.size > 0){
                fillCalendar()
            }
        })
    }

    fun fillCalendar(){
        val calendarList: ArrayList<Calendar> = ArrayList()
        var date = ""
        var dateList = ArrayList<String>()
        bookingListOnRoom.forEach {booking ->
            if(date != convertUnixToDate(booking.startingDate)){
                date = convertUnixToDate(booking.startingDate)
                dateList.add(date)
            }
            bookedDaysViaDate[date]?.add(booking.endDate - booking.startingDate)
            //calendar.setTimeInMillis(booking.startingDate)
            //partlyBookedDays.add(EventDay(calendar, com.novatc.ap_app.R.drawable.ic_dot_black, Color.parseColor("#228B22")))
            //calendarList.add(calendar)
        }
        dateList.forEach{
            date ->
            var firstBookedTime = 0L
            val calendar = Calendar.getInstance()
            var bookedTime = 0L
            bookedDaysViaDate[date]?.forEach{
                time ->
                if(firstBookedTime == 0L) {
                    firstBookedTime = time
                }
                bookedTime = (bookedTime + time)
            }
            calendar.setTimeInMillis(firstBookedTime)
            if(bookedTime > selectedRoom.minimumBookingTime?.toLong()!!){
                fullyBookedDays.add(EventDay(calendar, com.novatc.ap_app.R.drawable.ic_dot_black, Color.parseColor("#228B22")))
                calendarList.add(calendar)
            }
            else{
                partlyBookedDays.add(EventDay(calendar, com.novatc.ap_app.R.drawable.ic_dot_yellow, Color.parseColor("#228B22")))
            }
        }
        calendar.setEvents(partlyBookedDays)
        calendar.setEvents(fullyBookedDays)
        calendar.setDisabledDays(calendarList)
    }

    private fun addZeroToShortNumber(string: String, fromBehind:Boolean):String{
        if(string.length < 2){
            if (fromBehind){
                return string + "0"
            }
            else {
                return "0" + string
            }
        }
        else{
            return string
        }
    }

    private fun convertUnixToDate(unixDate: Long): String {
        return DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond((unixDate/1000))).split("T")[0]
    }

    private fun millisToHours(millis: Long): Long {
        return millis/3600000L
    }

    private fun findNextPossibleDate(){
        var instance: Calendar = Calendar.getInstance()
        instance.add(Calendar.DATE, -1)
        print("tst")
    }

}