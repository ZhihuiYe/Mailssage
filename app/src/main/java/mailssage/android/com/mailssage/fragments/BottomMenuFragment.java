package mailssage.android.com.mailssage.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.activities.MainActivity;

/**
 * Created by Tank on 22/08/2015.
 */
public class BottomMenuFragment extends Fragment {

    CallBack attachedActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        attachedActivity = (CallBack) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.MainScreenFragment currentMainScreenFragment = attachedActivity.getCurrentMainScreenFragment();

        View menuView = inflater.inflate(R.layout.botton_menu_fragment, container, false);

        Button pressedButton = null;
        int activeIconID = 0;

        switch (currentMainScreenFragment) {
            case MAIN:
                pressedButton = (Button) menuView.findViewById(R.id.homeButton);
                activeIconID = R.drawable.ic_action_active_home;
                break;
            case MESSAGE_ONLY:
                pressedButton = (Button) menuView.findViewById(R.id.messageButton);
                activeIconID = R.drawable.ic_action_active_messages;
                break;
            case EMAIL_ONLY:
                pressedButton = (Button) menuView.findViewById(R.id.emailButton);
                activeIconID = R.drawable.ic_action_active_emails;
                break;
            case CONTACTS:
                pressedButton = (Button) menuView.findViewById(R.id.contactButton);
                activeIconID = R.drawable.ic_action_active_contacts;
                break;
            case SETTING:
                pressedButton = (Button) menuView.findViewById(R.id.settingButton);
                activeIconID = R.drawable.ic_action_active_settings;
                break;
            default:
                break;
        }

        if (pressedButton != null && activeIconID != 0) {
            pressedButton.setBackgroundResource(activeIconID);
        }

        return menuView;
    }

    public interface CallBack{
        MainActivity.MainScreenFragment getCurrentMainScreenFragment();

        void onButtonMenuClick(View view);
    }
}
