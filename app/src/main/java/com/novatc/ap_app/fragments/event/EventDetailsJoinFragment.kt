package com.novatc.ap_app.fragments.event

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.novatc.ap_app.R
import com.novatc.ap_app.model.Request
import com.novatc.ap_app.model.User
import com.novatc.ap_app.viewModels.event.EventDetailsViewModel
import kotlinx.android.synthetic.main.fragment_event_details_general.view.*
import kotlinx.android.synthetic.main.fragment_event_details_join.*
import kotlinx.android.synthetic.main.fragment_event_details_join.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


class EventDetailsJoinFragment : Fragment() {
    val model: EventDetailsViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_details_join, container, false)
        model.event.observe(this, { event ->
            model.loadEventAttendees()
            setOnJoinListener(view)
        })

        model.attendees.observe(this, { attendees ->
            val numberOfAttendees = attendees.size
            view.numberOfAttendees.text = resources.getQuantityString(
                R.plurals.detail_event_attendees_text,
                numberOfAttendees,
                numberOfAttendees
            )
            if (model.userAlreadyJoinedEvent()) {
                switchToLeaveButton(view)
            } else {
                switchToJoinButton(view)
            }
        })
        return view
    }

    private fun setOnJoinListener(view: View) {
        view.eventDetailJoinButton.setOnClickListener {
            model.switchEventAttendance()
            model.switchAttendanceRequest.observe(this, { request ->
                if (request.status == Request.Status.ERROR) {
                    Toast.makeText(context, request.message!!, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun switchToJoinButton(view: View) {
        val button = view.eventDetailJoinButton
        button.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_baseline_add_24,
            0,
            0,
            0
        )
        button.text = resources.getString(R.string.detail_event_join_button)
        button.setBackgroundColor(
            resources.getColor(
                R.color.green,
                context!!.theme
            )
        )
    }

    private fun switchToLeaveButton(view: View) {
        val button = view.eventDetailJoinButton
        button.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_baseline_clear_24,
            0,
            0,
            0
        )
        button.text =
            resources.getString(R.string.detail_event_leave_button)
        button.setBackgroundColor(
            resources.getColor(
                R.color.radical_red,
                context!!.theme
            )
        )

    }


}