package mailssage.android.com.mailssage.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Tank on 20/07/2015.
 */
public class UIHelper {

    private static final String LOGTAG = "MY_UI_HELPER";

    private static final String fullDate = "HH:mm:ss EEE dd MMM yyyy";

    public static String getCreateTimeInFullFormat(long createTime) {
        Calendar createTimeInCal = Calendar.getInstance();
        createTimeInCal.setTimeInMillis(createTime);

        SimpleDateFormat dateFormat = new SimpleDateFormat(fullDate);

        return dateFormat.format(createTimeInCal.getTime());
    }

    private static final String clockOnly = "HH:mm ";
    private static final String theDayOfTheWeekOnly = "EEE   ";
    private static final String dateOnly = "MMM yy";


    public static String getCreateTimeInShortFormat(long createTime) {
        Calendar createTimeInCal = Calendar.getInstance();
        createTimeInCal.setTimeInMillis(createTime);

        String selectedFormat;
        Calendar yesterday = Calendar.getInstance();
        Calendar lastWeek = Calendar.getInstance();

        yesterday.roll(Calendar.DATE, 1);
        if (createTimeInCal.after(yesterday)) {
            selectedFormat = clockOnly;
        } else {
            lastWeek.roll(Calendar.WEEK_OF_YEAR, 1);
            if (createTimeInCal.after(lastWeek)) {
                selectedFormat = theDayOfTheWeekOnly;
            } else {
                selectedFormat = dateOnly;
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(selectedFormat);

        return dateFormat.format(createTimeInCal.getTime());
    }

    public static void displayText(Activity activity, int id, String text) {
        TextView textView = (TextView) activity.findViewById(id);
        textView.setText(text);
    }

    public static String getText(Activity activity, int id) {
        EditText editText = (EditText) activity.findViewById(id);
        return editText.getText().toString();
    }

    public static boolean getCheckBoxValue(Activity activity, int id) {
        CheckBox checkBox = (CheckBox) activity.findViewById(id);
        return checkBox.isChecked();
    }

    public static void setCheckBoxValue(Activity activity, int id, Boolean value) {
        CheckBox checkBox = (CheckBox) activity.findViewById(id);
        checkBox.setChecked(value);
    }
}
