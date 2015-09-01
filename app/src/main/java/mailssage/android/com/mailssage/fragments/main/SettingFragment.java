package mailssage.android.com.mailssage.fragments.main;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import mailssage.android.com.mailssage.R;

/**
 * Created by Tank on 20/07/2015.
 */
public class SettingFragment extends Fragment {

    private static final String LOGTAG = "SETTING_ACTIVITY";

    public static final String SETTINGS = "SETTINGS";
    public static final String USER_EMAIL = "USER_EMAIL";

    private View settingView = null;

    private SharedPreferences settings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingView = inflater.inflate(R.layout.setting_fragment, container, false);

        settings = getActivity().getSharedPreferences(SETTINGS, getActivity().MODE_PRIVATE);
        refreshDisplay();
        return settingView;
    }

    private void refreshDisplay() {
        String userEmail = settings.getString(USER_EMAIL, "Please enter you email");
        boolean checkbox = settings.getBoolean("CHECKBOX", false);

        EditText userEmailView = (EditText) settingView.findViewById(R.id.email);
        userEmailView.setText(userEmail);

        CheckBox checkBoxView = (CheckBox) settingView.findViewById(R.id.checkbox);
        checkBoxView.setChecked(checkbox);
    }

    public boolean savePreferences() {
        SharedPreferences.Editor editor = settings.edit();

        EditText emailView = (EditText) settingView.findViewById(R.id.email);
        CheckBox checkboxView = (CheckBox) settingView.findViewById(R.id.checkbox);

        String email = emailView.getText().toString();
        boolean checkBoxValue = checkboxView.isChecked();

        editor.putString(USER_EMAIL, email);
        editor.putBoolean("CHECKBOX", checkBoxValue);

        if (editor.commit()) {
            Log.i(LOGTAG, "Preferences has saved");
            return true;
        }
        else {
            Log.i(LOGTAG, "Fail to save the preferences");
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        savePreferences();
    }
}
