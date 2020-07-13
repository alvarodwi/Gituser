package com.varoa.gituser.ui.common.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.time.LocalTime

class TimePickerFragment(private val time : LocalTime = LocalTime.now(), private val listener : TimePickerListener) : DialogFragment(),TimePickerDialog.OnTimeSetListener {
    class TimePickerListener(val listener : (hour : Int,minute : Int) -> Unit){
        fun onTimeSet(hour : Int,minute: Int) = listener(hour,minute)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val use24HourFormat = true
        return TimePickerDialog(activity,this,time.hour,time.minute,use24HourFormat)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        listener.onTimeSet(hourOfDay,minute)
    }
}