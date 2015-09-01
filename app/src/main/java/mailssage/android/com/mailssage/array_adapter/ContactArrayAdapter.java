package mailssage.android.com.mailssage.array_adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.model.Contact;

/**
 * Created by Tank on 29/07/2015.
 */
public class ContactArrayAdapter extends ArrayAdapter<Contact> {

    private static final String LOGTAG = "MY_CONTACT_ADAPTER";
    private Context context;
    private List<Contact> contacts;

    public ContactArrayAdapter(Context context, int resource, List<Contact> contacts) {
        super(context, resource, contacts);
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Contact currentContact = contacts.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View contactView = inflater.inflate(R.layout.contact_listitem, null);

        ImageView contactImageView = (ImageView) contactView.findViewById(R.id.contact_image);
        contactImageView.setImageResource(currentContact.getImageID());

        TextView contactNameView = (TextView) contactView.findViewById(R.id.contact_name);
        String  name = currentContact.getName();
        if (name == null) { name = "UNKNOW"; }
        contactNameView.setText(name);

        TextView contactDescriptionView = (TextView) contactView.findViewById(R.id.contact_description);
        contactDescriptionView.setText(currentContact.getDescription());

        return contactView;
    }
}
