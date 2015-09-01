package mailssage.android.com.mailssage.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.fragments.ContactDetailFragment;
import mailssage.android.com.mailssage.model.Contact;

/**
 * Created by Tank on 29/07/2015.
 */
public class ContactDetailActivity extends Activity {

    private static final String LOGTAG = "MY_CONTACT_DETAIL_ACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);

        setContentView(R.layout.activity_contact_detail);



        Bundle contactBundle = getIntent().getBundleExtra(Contact.CONTACT_BUNDLE);

        if (contactBundle == null) {
            Log.i(LOGTAG, "Received a null bundle.");
            return;
        } else if ( !contactBundle.containsKey(Contact.NAME) ) {
            Log.i(LOGTAG, "Received a non-Contact bundle.");
            return;
        } else {
            ContactDetailFragment fragment = new ContactDetailFragment();
            fragment.setArguments(contactBundle);

            getFragmentManager().beginTransaction()
                    .add(R.id.contact_detail_activity, fragment)
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
