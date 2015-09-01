package mailssage.android.com.mailssage.fragments.main;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.array_adapter.ContactArrayAdapter;
import mailssage.android.com.mailssage.database.ContactDataSource;
import mailssage.android.com.mailssage.model.Contact;

/**
 * Created by Tank on 29/07/2015.
 */
public class ContactListFragment extends ListFragment {

    private static final String LOGTAG = "MY_CONTACT_LIST_FRAGM";
    private ContactDataSource contactDataSource;
    private List<Contact> contacts;
    private CallBack currentAttachActivity;

    public ContactListFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getActionBar().setTitle("Contacts");

        contactDataSource = new ContactDataSource(getActivity());
        contacts = contactDataSource.findAllContacts();

        Log.i(LOGTAG, "Have received contacts:\n" + contacts.toString());

        if (contacts != null) {
            ContactArrayAdapter contactArrayAdapter = new ContactArrayAdapter(
                    getActivity(), R.layout.contact_listfragment, contacts);
            setListAdapter(contactArrayAdapter);
        } else {
            Log.i(LOGTAG, "Received a null contacts from database.");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Contact selectedContact = contacts.get(position);
        currentAttachActivity.onContactSelected(selectedContact);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        currentAttachActivity = (CallBack) activity;
    }

    public interface CallBack {
        void onContactSelected(Contact selectedContact);
    }
}
