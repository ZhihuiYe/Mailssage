package mailssage.android.com.mailssage.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.model.Contact;

/**
 * Created by Tank on 29/07/2015.
 */
public class ContactDetailFragment extends Fragment {

    private static final String LOGTAG = "MY_CONTACT_DETAIL_FRAG";
    private Contact currentContact;

    public ContactDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle contactBundle = getArguments();
        if (contactBundle == null) {
            Log.i(LOGTAG, "Received a null bundle.");
        } else if (!contactBundle.containsKey(Contact.NAME)) {
            Log.i(LOGTAG, "Received a non-Contact bundle.");
        } else {
            currentContact = new Contact(contactBundle);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View contactDetailView = inflater.inflate(R.layout.contact_detail_fragment, container, false);

        TextView contactNameView = (TextView) contactDetailView.findViewById(R.id.contact_name);
        contactNameView.setText(currentContact.getName());

        TextView contactEmailView = (TextView) contactDetailView.findViewById(R.id.contact_email);
        contactEmailView.setText(currentContact.getEmail());

        TextView contactDescriptionView = (TextView) contactDetailView.findViewById(R.id.contact_description);
        contactDescriptionView.setText(currentContact.getDescription());

        return contactDetailView;
    }
}
