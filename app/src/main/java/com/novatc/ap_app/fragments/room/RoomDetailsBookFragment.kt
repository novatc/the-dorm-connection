package com.novatc.ap_app.fragments.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.novatc.ap_app.viewModels.room.RoomDetailsViewModel
import kotlinx.android.synthetic.main.fragment_room_details_book.view.*
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import java.util.*
import android.app.TimePickerDialog
import android.graphics.Color
import android.util.Log
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.BookingListAdapter
import com.novatc.ap_app.model.*
import com.novatc.ap_app.viewModels.pinboard.PostDetailsViewModel
import kotlinx.android.synthetic.main.fragment_post_details.view.*
import kotlinx.android.synthetic.main.fragment_room_details_book.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class RoomDetailsBookFragment : Fragment(), TimePickerDialog.OnTimeSetListener{
    val model: RoomDetailsViewModel by activityViewModels()

    val bookedDays: ArrayList<EventDay> = ArrayList()
    val fullyBookedDays: ArrayList<EventDay> = ArrayList()
    var bookedDaysViaDate: HashMap<String, ArrayList<Long>> = HashMap()
    lateinit var calendar: com.applandeo.materialcalendarview.CalendarView
    lateinit var selectedDate :Calendar
    lateinit var currentTimePicker :String
    lateinit var startingTime: TextView
    lateinit var endTime: TextView
    private var selectedRoom: Room = Room()
    private var currentUser: User = User()
    private var bookingListOnRoom: List<Booking> = ArrayList<Booking>()
    private var availableTimeslots: ArrayList<FreeTimeslot> = ArrayList()
    private var bookedTimeslots: ArrayList<Long> = ArrayList()
    val disabledDaysList: ArrayList<Calendar> = ArrayList()
    private val aDayInMilliseconds: Long = 86400000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.novatc.ap_app.R.layout.fragment_room_details_book, container, false)
        model.room.observe(this, { room ->
            selectedRoom = room
        })
        model.room.observe(this, { room ->
            model.loadBookings(this)
        })
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
        return view
    }

    fun addDateListener(){
        calendar.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                selectedDate = eventDay.calendar
                if(isDisabled(selectedDate)){
                   findNextPossibleDate()
                }
                else{
                    calendar.setDate(selectedDate)
                }
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
            min_booking_time_text.text = selectedRoom.minimumBookingTime?.let { it1 ->
                convertMillisToHoursAndMinutes(
                    it1.toLong())
            }
            max_booking_time_text.text = selectedRoom.maximumBookingTime?.let { it1 ->
                convertMillisToHoursAndMinutes(
                    it1.toLong())
            }
            createAvailableTimeslots()
            populateFreeTimeslotList(view)
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
                    val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                    Snackbar.make(bottomNavView,  R.string.booking_time_incorrect, Snackbar.LENGTH_SHORT).apply {
                        anchorView = bottomNavView
                    }.show()
                }
                else if(startingHour == endingHour && startingMinute < endingMinute){
                    val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                    Snackbar.make(bottomNavView,  R.string.booking_time_incorrect, Snackbar.LENGTH_SHORT).apply {
                        anchorView = bottomNavView
                    }.show()
                }
                else{
                    val startingDate = booked_date_1.text.toString() + " " + startingTime.text.toString()
                    val endingDate = booked_date_1.text.toString() + " " + endTime.text.toString()
                    val formatter: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.GERMAN)
                    val startingDateInMilliseconds: Long = LocalDateTime.parse(startingDate, formatter).atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
                    val endingDateInMilliseconds: Long = LocalDateTime.parse(endingDate, formatter).atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
                    when {
                        endingDateInMilliseconds - startingDateInMilliseconds < selectedRoom.minimumBookingTime?.toLong()!! -> {
                            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                            Snackbar.make(bottomNavView,  R.string.booking_time_falls_below_the_minimum_booking_time, Snackbar.LENGTH_SHORT).apply {
                                anchorView = bottomNavView
                            }.show()
                        }
                        endingDateInMilliseconds - startingDateInMilliseconds > selectedRoom.maximumBookingTime?.toLong()!! -> {
                            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                            Snackbar.make(bottomNavView,  R.string.booking_time_exceeds_maximum_booking_time, Snackbar.LENGTH_SHORT).apply {
                                anchorView = bottomNavView
                            }.show()
                        }
                        !isInAvailableTimeslots(startingDateInMilliseconds) || !isInAvailableTimeslots(endingDateInMilliseconds)-> {
                            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                            Snackbar.make(bottomNavView,  R.string.chosen_time_slot_has_already_been_booked, Snackbar.LENGTH_SHORT).apply {
                                anchorView = bottomNavView
                            }.show()
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
                            val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                            Snackbar.make(bottomNavView,  R.string.successful_booking, Snackbar.LENGTH_SHORT).apply {
                                anchorView = bottomNavView
                            }.show()
                            val layout = view.tutView1
                            layout.visibility = View.VISIBLE
                            val layout2 = view.tutView2
                            layout2.visibility = View.GONE
                        }
                    }
                }
            }
            else{
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView,  R.string.empty_field_error, Snackbar.LENGTH_SHORT).apply {
                    anchorView = bottomNavView
                }.show()
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

    fun populateCalendar() {
        model.bookings.observe(this, { booking ->
            bookingListOnRoom = booking
            if(bookingListOnRoom.size > 0){
                fillCalendar()
            }
        })
    }

    fun fillCalendar(){
        bookedDays.clear()
        fullyBookedDays.clear()
        disabledDaysList.clear()
        bookedDaysViaDate.clear()
        var date = ""
        var dateList = ArrayList<String>()
        bookingListOnRoom.forEach {booking ->
            if(date != convertUnixToDate(booking.startingDate)){
                date = convertUnixToDate(booking.startingDate)
                dateList.add(date)
                bookedDaysViaDate.put(date, ArrayList())
            }
            bookedDaysViaDate[date]?.add(booking.endDate - booking.startingDate)
        }
        dateList.forEach{
            date ->
            val calendar = Calendar.getInstance()
            var bookedTime = 0L
            bookedDaysViaDate[date]?.forEach{
                time ->
                bookedTime = (bookedTime + time)
            }
            calendar.setTimeInMillis(convertDateToUnix(date).toLong())
            if(aDayInMilliseconds - bookedTime < selectedRoom.minimumBookingTime?.toLong()!!){
                bookedDays.add(EventDay(calendar, com.novatc.ap_app.R.drawable.ic_dot_black, Color.parseColor("#228B22")))
                disabledDaysList.add(calendar)
            }
            else{
                bookedDays.add(EventDay(calendar, com.novatc.ap_app.R.drawable.ic_dot_yellow, Color.parseColor("#228B22")))
            }
        }
        calendar.setEvents(bookedDays)
        calendar.setDisabledDays(disabledDaysList)
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

    private fun createAvailableTimeslots(): ArrayList<FreeTimeslot>{
        bookingListOnRoom.forEach{
            booking ->
            bookedTimeslots.add(booking.startingDate)
            bookedTimeslots.add(booking.endDate)
        }
        var dayStart: Long = convertDateToUnix(convertUnixToDate(selectedDate.timeInMillis)).toLong()
        var dayEnd: Long = convertDateToUnix(convertUnixToDate(selectedDate.timeInMillis)).toLong() + aDayInMilliseconds
        bookedTimeslots.sortedWith(Comparator<Long>{ a,b ->
            when {
                a > b -> 1
                a < b -> -1
                else -> 0
            }
        })
        var x = 0
        while (x < bookedTimeslots.size){
            if(x == 0){
                if(bookedTimeslots.get(x) - dayStart > selectedRoom.minimumBookingTime?.toLong()!!){
                    availableTimeslots.add(FreeTimeslot(dayStart, bookedTimeslots.get(x)))
                }
            }
            else if(x == bookedTimeslots.size-1){
                if (dayEnd - bookedTimeslots.get(x) > selectedRoom.minimumBookingTime?.toLong()!!){
                    availableTimeslots.add(FreeTimeslot(bookedTimeslots.get(x), dayEnd))
                }
            }
            else if(x%2 == 1){
                if (bookedTimeslots.get(x+1) - bookedTimeslots.get(x) > selectedRoom.minimumBookingTime?.toLong()!!){
                    availableTimeslots.add(FreeTimeslot(bookedTimeslots.get(x), bookedTimeslots.get(x+1)))
                }
            }
            x++
        }
        return availableTimeslots
    }

    private fun isInAvailableTimeslots(timeToBook: Long): Boolean{
        if (bookedTimeslots.size < 1){
            return true
        }
        else{
            var x = 0
            while (x < bookedTimeslots.size){
                if(x%2 == 0){
                    if(timeToBook >= bookedTimeslots.get(x) && timeToBook <= bookedTimeslots.get(x+1)){
                    return false
                    }}
                x++
            }
        }
        return true
    }

    private fun convertUnixToDate(unixDate: Long): String {
        return DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond((unixDate/1000))).split("T")[0]
    }

    private fun convertDateToUnix(date: String): String {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond.toString() + "000"
    }

    private fun millisToHours(millis: Long): Long {
        return millis/3600000L
    }

    private fun findNextPossibleDate(){
        var dateSearcher: Calendar = Calendar.getInstance()
        dateSearcher.add(Calendar.DATE, 0)
        var x = 0
        if(disabledDaysList.size > 0){
            while (x <= disabledDaysList.size){
                if(dateSearcher.time < disabledDaysList.get(x).time){
                    print("test")
                    selectedDate = dateSearcher
                    calendar.setDate(selectedDate)
                    break
                }
                else{
                    dateSearcher.add(Calendar.DATE,x)
                }
                x++
            }
            dateSearcher.add(Calendar.DATE,x)
            calendar.setDate(dateSearcher)
        }
    }

    private fun isDisabled(date: Calendar): Boolean{
        disabledDaysList.forEach{
            day ->
            if(day.time == date.time){
                return true
            }
        }
        return false
    }

    private fun populateFreeTimeslotList(view: View) {
        val recyclerView: RecyclerView = view.free_timeslots
        val bookingListAdapter = BookingListAdapter()
        recyclerView.adapter = bookingListAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        bookingListAdapter.differ.submitList(availableTimeslots)
        }

    private fun convertMillisToHoursAndMinutes(millis: Long):String{
        var hours = millisToHours(millis).toInt()
        var minutes = ((millis - (hours * 3600000))/60000).toInt()
        var hoursAndMillis = addZeroToShortNumber(hours.toString(),false) + ":" + addZeroToShortNumber(minutes.toString(),false)
        return hoursAndMillis
    }

}