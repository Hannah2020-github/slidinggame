package com.hannah.slidinggame

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference

class SettingsActivity : AppCompatActivity() {

    companion object {
        private val MUSIC_KEY = "MUSIC_PREF"

        fun isSoundOn(context: Context) : Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MUSIC_KEY, true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }

    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val context = preferenceManager.context
            val screen = preferenceManager.createPreferenceScreen(context)

            val music = SwitchPreference(context)
            music.setTitle(R.string.music_question)
            music.summaryOn = resources.getString(R.string.yes)
            music.summaryOff = resources.getString(R.string.no)
            music.isChecked = true
            music.key = MUSIC_KEY
            screen.addPreference(music)

            preferenceScreen = screen

        }
    }
}