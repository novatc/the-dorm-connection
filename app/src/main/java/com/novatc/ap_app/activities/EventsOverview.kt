package com.novatc.ap_app.activities

import android.os.Bundle
import com.novatc.ap_app.R
import model.BaseActivity

class EventsOverview : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_overview)
    }
}