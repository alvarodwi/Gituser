package com.varoa.gituser.ui.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.core.content.edit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.varoa.gituser.R
import com.varoa.gituser.ui.common.dialog.TimePickerFragment
import com.varoa.gituser.utils.reminder.AlarmScheduler
import timber.log.Timber
import java.time.LocalTime

class SettingsFragment : PreferenceFragmentCompat() {
    companion object {
        const val TIME_PICKER_REMINDER = "TimePickerReminder"
    }

    private var reminder : SwitchPreference? = null
    private var reminderTime: Preference? = null
    private var locale: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        //preference items
        reminder = findPreference(getString(R.string.reminder_key))
        reminderTime = findPreference(getString(R.string.reminder_time_key))
        locale = findPreference(getString(R.string.locale_key))

        reminder?.setOnPreferenceChangeListener { _, newValue ->
            if(newValue as Boolean){
                Timber.d("Reminder Turned On")
                AlarmScheduler.scheduleAlarm(requireContext())
            }else{
                Timber.d("Reminder Turned Off")
                AlarmScheduler.cancelAlarm(requireContext())
            }
            true
        }

        reminderTime?.let { pref ->
            setupDefaultsValue(pref)
            pref.summary = requireContext().getString(
                R.string.reminder_time_summary,
                pref.sharedPreferences.getString(pref.key, "")
            )
            pref.setOnPreferenceClickListener {
                promptTimePickerDialog(it)
                true
            }
        }

        locale?.setOnPreferenceClickListener {
            intentToLocale()
            true
        }
    }

    private fun intentToLocale() {
        Intent(Settings.ACTION_LOCALE_SETTINGS).also { intent ->
            startActivity(intent)
        }
    }

    private fun promptTimePickerDialog(pref: Preference) {
        val prefTime = pref.sharedPreferences.getString(pref.key, "")
        TimePickerFragment(
            LocalTime.parse(prefTime),
            TimePickerFragment.TimePickerListener { hour, minute ->
                //update preference
                pref.sharedPreferences.edit {
                    putString(
                        pref.key,
                        LocalTime.of(hour, minute).toString()
                    )
                }
                //update summary preference manually ;v
                pref.summary = requireContext().getString(
                    R.string.reminder_time_summary,
                    pref.sharedPreferences.getString(pref.key, "")
                )
                //reschedule alarm
                AlarmScheduler.scheduleAlarm(requireContext())
            }).show(childFragmentManager, TIME_PICKER_REMINDER)
    }

    private fun setupDefaultsValue(pref: Preference?){
        pref?.let {
            val valueInside = pref.sharedPreferences.getString(pref.key,null)
            Timber.d("Pref ${pref.key} value = $valueInside")
            if(valueInside == null){
                //set new value
                pref.sharedPreferences.edit {
                    putString(
                        pref.key,
                        LocalTime.of(9, 0).toString()
                    )
                }
            }
        }
    }
}