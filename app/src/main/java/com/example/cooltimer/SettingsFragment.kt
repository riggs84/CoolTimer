package com.example.cooltimer

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment: PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferencies)
        val preference = findPreference<Preference>(MAX_INTERVAL)
        preference?.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        val toast = Toast.makeText(context, "You can not set that value", Toast.LENGTH_LONG)

        if (preference.key == MAX_INTERVAL) {
            try {
                (newValue as String).toInt()
            } catch(e: Exception) {
                toast.show()
                return false
            }
        }
        return true
    }

companion object {
    const val MAX_INTERVAL = "max_interval_pref"
}

}