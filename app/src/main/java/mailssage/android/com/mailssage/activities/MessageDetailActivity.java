package mailssage.android.com.mailssage.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.fragments.MessageDetailFragment;
import mailssage.android.com.mailssage.model.Message;

/**
 * Created by Tank on 31/07/2015.
 */
public class MessageDetailActivity extends Activity {

    private static final String LOGTAG = "MY_MESSAGE_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);

        setContentView(R.layout.activity_message_detail);

        Bundle messageBundle = getIntent().getBundleExtra(Message.MESSAGE_BUNDLE);
        if (messageBundle == null) {
            Log.i(LOGTAG, "Received a null bundle.");
            return;
        } else if (!messageBundle.containsKey(Message.MESSAGE_TYPE)) {
            Log.i(LOGTAG, "Received a non-message bundle.");
            return;
        } else {
            MessageDetailFragment fragment = new MessageDetailFragment();
            fragment.setArguments(messageBundle);

            getFragmentManager().beginTransaction()
                    .add(R.id.message_detail_activity, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                break;
            default:
                Log.i(LOGTAG, "Cannot identify the selected menu item.");
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
