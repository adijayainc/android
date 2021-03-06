package org.owntracks.android.ui.preferences

import android.os.Bundle
import androidx.preference.DropDownPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import dagger.Binds
import dagger.Module
import org.owntracks.android.R
import org.owntracks.android.injection.modules.android.FragmentModules.BaseFragmentModule
import org.owntracks.android.injection.scopes.PerFragment
import org.owntracks.android.support.Preferences

@PerFragment
class AdvancedFragment : AbstractPreferenceFragment() {
    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferencesFix(savedInstanceState, rootKey)
        setPreferencesFromResource(R.xml.preferences_advanced, rootKey)
        val remoteConfigurationPreference = findPreference<SwitchPreferenceCompat>(getString(R.string.preferenceKeyRemoteConfiguration))
        val remoteCommandPreference = findPreference<SwitchPreferenceCompat>(getString(R.string.preferenceKeyRemoteCommand))
        val remoteCommandAndConfigurationChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            if (newValue is Boolean) {
                when (preference.key) {
                    getString(R.string.preferenceKeyRemoteCommand) -> if (!newValue) remoteConfigurationPreference?.isChecked = false
                    getString(R.string.preferenceKeyRemoteConfiguration) -> if (newValue) remoteCommandPreference?.isChecked = true
                }
            }
            true
        }
        remoteConfigurationPreference?.onPreferenceChangeListener = remoteCommandAndConfigurationChangeListener
        remoteCommandPreference?.onPreferenceChangeListener = remoteCommandAndConfigurationChangeListener

        val geocoderDropDownPreference = findPreference<DropDownPreference>(getString(R.string.preferenceKeyReverseGeocodeProvider))
        geocoderDropDownPreference?.setOnPreferenceChangeListener { _, newValue ->
            preferences.reverseGeocodeProvider = newValue.toString()
            setOpenCageAPIKeyPreferenceVisibility()
            true
        }
        setOpenCageAPIKeyPreferenceVisibility()
    }

    private fun setOpenCageAPIKeyPreferenceVisibility() {
        findPreference<EditTextPreference>(getString(R.string.preferenceKeyOpencageGeocoderApiKey))?.isVisible = preferences.reverseGeocodeProvider == Preferences.REVERSE_GEOCODE_PROVIDER_OPENCAGE
    }

    @Module(includes = [BaseFragmentModule::class])
    internal abstract class FragmentModule {
        @Binds
        @PerFragment
        abstract fun bindFragment(reportingFragment: AdvancedFragment): AdvancedFragment
    }
}
