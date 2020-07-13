package com.varoa.gituser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.varoa.gituser.data.seeder.UserSeeder
import com.varoa.gituser.ui.common.dialog.DialogSuccessFragment
import com.varoa.gituser.utils.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object{
        private const val FEATURE_NOTIF = 1
        private const val FEATURE_WIDGET = 2

        private const val FIRST_RUN = "firstRun"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //on first run, seed favorites data...
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val isFirstRun = prefs.getBoolean(FIRST_RUN,true)
        if(isFirstRun){
            Timber.d("Seeder :: Seeding your favorite user with raw data...")
            //untuk ganti data user yang akan otomatis di seed, bisa cek R.raw.githubuser yaaa
            //
            GlobalScope.launch {
                UserSeeder(this@MainActivity,R.raw.githubuser).seedUserData()
            }
            prefs.edit { putBoolean(FIRST_RUN,false) }
        }

        if(intent != null && intent.action != null) when(intent.action){
            getString(R.string.reminder_notif_type) -> showSuccessDialog(FEATURE_NOTIF)
            Constants.WIDGET_TOUCH_ACTION -> showSuccessDialog(FEATURE_WIDGET)
            else -> {  }
        }
    }

    private fun showSuccessDialog(feature : Int){
        val featureText = when(feature){
            FEATURE_NOTIF -> getString(R.string.reminder)
            FEATURE_WIDGET -> getString(R.string.widget)
            else -> "Unknown Feature"
        }

        DialogSuccessFragment.newInstance(
            -1,
            getString(R.string.success_title,featureText),
            getString(R.string.success_subtitle,featureText.toLowerCase(Locale.ROOT))
        ).show(supportFragmentManager,"success-dialog")
    }
}
