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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.novatc.ap_app.R
import com.novatc.ap_app.adapter.FreeTimeslotAdapter
import com.novatc.ap_app.fragments.room.RoomDateHelper.Companion.addZeroToShortNumber
import com.novatc.ap_app.fragments.room.RoomDateHelper.Companion.convertDateToUnix
import com.novatc.ap_app.fragments.room.RoomDateHelper.Companion.convertMillisToHoursAndMinutes
import com.novatc.ap_app.fragments.room.RoomDateHelper.Companion.convertUnixToDate
import com.novatc.ap_app.fragments.room.RoomDateHelper.Companion.convertUnixToHoursAndMinutes
import com.novatc.ap_app.model.*
import kotlinx.android.synthetic.main.fragment_room_details_book.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.properties.Delegates


class RoomDetailsBookFragment : Fragment(), TimePickerDialog.OnTimeSetListener{
    val model: RoomDetailsViewModel by activityViewModels()

    private val bookedDays: ArrayList<EventDay> = ArrayList()
    private val fullyBookedDays: ArrayList<EventDay> = ArrayList()
    var bookedDaysViaDate: HashMap<String, ArrayList<Long>> = HashMap()
    lateinit var calendar: com.applandeo.materialcalendarview.CalendarView
    lateinit var selectedDate :Calendar
    private var currentTimePicker: String = "start"
    lateinit var startingTime: TextView
    lateinit var endTime: TextView
    private var selectedRoom: Room = Room()
    private var currentUser: User = User()
    private var bookingListOnRoom: List<Booking> = ArrayList()
    private var availableTimeslots: ArrayList<FreeTimeslot> = ArrayList()
    private var bookedTimeslots: ArrayList<Long> = ArrayList()
    private val disabledDaysList: ArrayList<Calendar> = ArrayList()
    private val aDayInMilliseconds: Long = 86400000L
    private val anHourInMilliseconds: Long = 3600000L
    private val aMinuteInMilliseconds: Long = 60000
    var timezoneOffset by Delegates.notNull<Long>()
    private val today:Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_details_book, container, false)
        startObservers()
        calendar = view.calendar
        today.add(Calendar.DATE, -1)
        calendar.setMinimumDate(today)
        selectedDate = calendar.firstSelectedDate
        startingTime = view.starting_time_text
        endTime = view.end_time_text
        addDateListener()
        addSaveDateButtonListener(view)
        addBackDateButtonListener(view)
        addTimeOfDayTextViewListener()
        addBookDateButtonListener(view)
        timezoneOffset = getTimezone()
        getUserName()
        return view
    }

    //starts the observers to access live data of the bookings of this particular room
    private fun startObservers(){
        model.room.observe(this, { room ->
            selectedRoom = room
        })
        model.room.observe(this, {
            model.loadBookings(this)
        })
    }

    private fun getTimezone(): Long{
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
        return 0L
    }

    //saves the date that is selected in the displayed calendar
    private fun addDateListener(){
        calendar.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                selectedDate = eventDay.calendar
                if(isDisabled(selectedDate) || selectedDate.timeInMillis < today.timeInMillis){
                   findNextPossibleDate()
                }
                else{
                    calendar.setDate(selectedDate)
                }
            }
        })
    }

    //transfers the picked date to the bookings page and changes the layout to confirm the booking
    private fun addSaveDateButtonListener(view: View){
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
            switchView("view2", view)
        }
    }

    //lets the user navigate back to the calendar
    private fun addBackDateButtonListener(view: View){
        val backButton: Button = view.back_to_date
        backButton.setOnClickListener {
            switchView("view1", view)
        }
    }

    //checks if the booking is valid and saves it to the connected firebase databse
    private fun addBookDateButtonListener(view: View){
        val setDateButton: Button = view.book_timeslot_button
        setDateButton.setOnClickListener {
            if(startingTime.text.contains(":") && endTime.text.contains(":")){
                val startingDate = booked_date_1.text.toString() + " " + startingTime.text.toString()
                val endingDate = booked_date_1.text.toString() + " " + endTime.text.toString()
                val formatter: DateTimeFormatter =
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.GERMAN)
                val startingDateInMilliseconds: Long = LocalDateTime.parse(startingDate, formatter).atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
                val endingDateInMilliseconds: Long = LocalDateTime.parse(endingDate, formatter).atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
                if(validateChosenBookingTime(startingDateInMilliseconds, endingDateInMilliseconds)) {
                    uploadBooking(startingDateInMilliseconds, endingDateInMilliseconds)
                    switchView("view1",view)
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
    //switches between calendar view and booking view
    private fun switchView(viewName: String, view: View){
        if(viewName == "view2"){
            val layout = view.tutView1
            layout.visibility = View.GONE
            val layout2 = view.tutView2
            layout2.visibility = View.VISIBLE
        }
        else if(viewName == "view1"){
            val layout = view.tutView1
            layout.visibility = View.VISIBLE
            val layout2 = view.tutView2
            layout2.visibility = View.GONE
        }
    }

    private fun uploadBooking(startingDateInMilliseconds: Long, endingDateInMilliseconds: Long){
        val c = Booking(
            userID = currentUser.id,
            startingDate = startingDateInMilliseconds - timezoneOffset,
            endDate = endingDateInMilliseconds - timezoneOffset
        )
        lifecycleScope.launch {
            selectedRoom.id?.let { it1 ->
                model.addBooking(selectedRoom.id!!, c)
            }
        }
        val bottomNavView: BottomNavigationView =
            activity?.findViewById(R.id.bottomNav)!!
        Snackbar.make(bottomNavView, R.string.successful_booking, Snackbar.LENGTH_SHORT)
            .apply {
                anchorView = bottomNavView
            }.show()
    }

    //checks whether the chosen booking time is available and obeys the rules of the room
    // (e.g. within minimumBookingTime and maximumBookingTime)
    private fun validateChosenBookingTime(startingDateInMilliseconds:Long, endingDateInMilliseconds: Long): Boolean{
        when{
            endingDateInMilliseconds < startingDateInMilliseconds -> {
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView,  R.string.booking_time_incorrect, Snackbar.LENGTH_SHORT).apply {
                    anchorView = bottomNavView
                }.show()
                return false
            }
            endingDateInMilliseconds - startingDateInMilliseconds < selectedRoom.minimumBookingTime?.toLong()!! -> {
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView,  R.string.booking_time_falls_below_the_minimum_booking_time, Snackbar.LENGTH_SHORT).apply {
                    anchorView = bottomNavView
                }.show()
                return false
            }
            endingDateInMilliseconds - startingDateInMilliseconds > selectedRoom.maximumBookingTime?.toLong()!! -> {
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView,  R.string.booking_time_exceeds_maximum_booking_time, Snackbar.LENGTH_SHORT).apply {
                    anchorView = bottomNavView
                }.show()
                return false
            }
            !isInAvailableTimeslots(startingDateInMilliseconds) || !isInAvailableTimeslots(endingDateInMilliseconds)-> {
                val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottomNav)!!
                Snackbar.make(bottomNavView,  R.string.chosen_time_slot_has_already_been_booked, Snackbar.LENGTH_SHORT).apply {
                    anchorView = bottomNavView
                }.show()
                return false
            }
        }
        return true
    }

    private fun addTimeOfDayTextViewListener(){
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
        val hourString = addZeroToShortNumber(hour.toString(), false)
        val minuteString = addZeroToShortNumber(minute.toString(), false)
        val resultTime = "$hourString:$minuteString"
        if(currentTimePicker == "start"){
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
            if(bookingListOnRoom.isNotEmpty()){
                fillCalendar()
            }
        })
    }

    private fun fillCalendar(){
        bookedDays.clear()
        fullyBookedDays.clear()
        disabledDaysList.clear()
        bookedDaysViaDate.clear()
        var date = ""
        val dateList = ArrayList<String>()
        bookingListOnRoom.forEach {booking ->
            if(date != convertUnixToDate(booking.startingDate)){
                date = convertUnixToDate(booking.startingDate + timezoneOffset)
                dateList.add(date)
                bookedDaysViaDate.put(date, ArrayList())
            }
            bookedDaysViaDate[date]?.add(booking.endDate - booking.startingDate)
        }
        dateList.forEach{
            currentDate ->
            val calendar = Calendar.getInstance()
            var bookedTime = 0L
            bookedDaysViaDate[date]?.forEach{
                time ->
                bookedTime = (bookedTime + time)
            }
            calendar.setTimeInMillis(convertDateToUnix(currentDate).toLong())
            if(aDayInMilliseconds - bookedTime < selectedRoom.minimumBookingTime?.toLong()!!){
                bookedDays.add(EventDay(calendar, R.drawable.ic_dot_black, Color.parseColor("#228B22")))
                disabledDaysList.add(calendar)
            }
            else{
                bookedDays.add(EventDay(calendar, R.drawable.ic_dot_yellow, Color.parseColor("#228B22")))
            }
        }
        calendar.setEvents(bookedDays)
        calendar.setDisabledDays(disabledDaysList)
    }



    private fun createAvailableTimeslots(): ArrayList<FreeTimeslot>{
        availableTimeslots.clear()
        bookedTimeslots.clear()
        bookingListOnRoom.forEach{
            booking ->
            bookedTimeslots.add(booking.startingDate + timezoneOffset)
            bookedTimeslots.add(booking.endDate + timezoneOffset)
        }
        val dayStart: Long = convertDateToUnix(convertUnixToDate(selectedDate.timeInMillis)).toLong() +timezoneOffset
        val dayEnd: Long = convertDateToUnix(convertUnixToDate(selectedDate.timeInMillis)).toLong() + aDayInMilliseconds - 3600 + timezoneOffset
        bookedTimeslots.sortByDescending { it }
        bookedTimeslots.reverse()
        var test = ArrayList<String>()
        bookedTimeslots.forEach{
            slot ->
            test.add(convertUnixToHoursAndMinutes(slot))
        }
        var x = 0
        while (x < bookedTimeslots.size){
            if(x == 0){
                if(bookedTimeslots.get(x) - dayStart >= selectedRoom.minimumBookingTime?.toLong()!!){
                    availableTimeslots.add(FreeTimeslot(dayStart, bookedTimeslots.get(x)))
                }
            }
            else if(x == bookedTimeslots.size-1){
                if (dayEnd - bookedTimeslots.get(x) >= selectedRoom.minimumBookingTime?.toLong()!!){
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
        if(bookedTimeslots.size == 0){
            availableTimeslots.add(FreeTimeslot(dayStart, dayEnd))
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
        val bookingListAdapter = FreeTimeslotAdapter()
        recyclerView.adapter = bookingListAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        bookingListAdapter.differ.submitList(availableTimeslots)
        }



}