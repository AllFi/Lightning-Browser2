package acr.browser.lightning.settings.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import acr.browser.lightning.BrowserApp;
import acr.browser.lightning.R;
import acr.browser.lightning.constant.Constants;
import acr.browser.lightning.dialog.BrowserDialog;
import acr.browser.lightning.utils.ProxyUtils;
import acr.browser.lightning.utils.Utils;
import allfi.touch.logger.TouchLogger;

/**
 * Created by Александр on 07.08.2017.
 */

public class TouchSettingsFragment extends LightningPreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    private Activity mActivity;
    private static final String SETTINGS_BUFFER_SIZE = "buf_size";
    private Preference bufSize;
    private CharSequence[] mBufSizeChoices;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference_touch_settings);

        BrowserApp.getAppComponent().inject(this);

        mActivity = getActivity();

        initPrefs();
    }

    private void initPrefs(){
        bufSize = findPreference(SETTINGS_BUFFER_SIZE);
        bufSize.setOnPreferenceClickListener(this);
        mBufSizeChoices = getResources().getStringArray(R.array.buf_size_choices_array);
    }

    private void setProxyChoice(int choice) {
        int size =(int)Math.pow(10,choice);
        TouchLogger.SetBufSize(size);
        mPreferenceManager.setBufSize(choice);
    }

    private void bufSizePicker() {
        AlertDialog.Builder picker = new AlertDialog.Builder(mActivity);
        picker.setTitle(R.string.buf_size);
        picker.setSingleChoiceItems(mBufSizeChoices, mPreferenceManager.getBufSize(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setProxyChoice(which);
                    }
                });
        picker.setPositiveButton(R.string.action_ok, null);
        Dialog dialog = picker.show();
        BrowserDialog.setDialogSize(mActivity, dialog);
    }

    @Override
    public boolean onPreferenceClick(@NonNull Preference preference) {
        switch (preference.getKey()) {
            case SETTINGS_BUFFER_SIZE:
                bufSizePicker();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
        boolean checked = false;
        if (newValue instanceof Boolean) {
            checked = Boolean.TRUE.equals(newValue);
        }
        switch (preference.getKey()) {

            default:
                return false;
        }
    }
}
