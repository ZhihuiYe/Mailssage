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
import mailssage.android.com.mailssage.database.ContactDataSource;
import mailssage.android.com.mailssage.model.Email;
import mailssage.android.com.mailssage.model.Message;
import mailssage.android.com.mailssage.utils.UIHelper;

/**
 * Created by Tank on 31/07/2015.
 */
public class MessageDetailFragment extends Fragment {

    private static final String LOGTAG = "MY_MESSAGE_DETAIL_FRAG";
    private Message currentMessage;

    public MessageDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle messageBundle = getArguments();

        if (messageBundle == null) {
            Log.i(LOGTAG, "Received a null bundle");
        } else if (!messageBundle.containsKey(Message.MESSAGE_TYPE)) {
            Log.i(LOGTAG, "Received a non-message bundle");
        } else {
            Message.MessageType currentMessageType
                    = (Message.MessageType) messageBundle.getSerializable(Message.MESSAGE_TYPE);

            if (currentMessageType.isEmail()) {
                Log.i(LOGTAG, "current message is a email");
                currentMessage = new Email(messageBundle);
            } else {
                Log.i(LOGTAG, "current message is a message");
                currentMessage = new Message(messageBundle);
            }

            Log.i(LOGTAG, "Message detail fragment successfully received Message object:\n"
                    + currentMessage.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ContactDataSource contactDataSource = new ContactDataSource(getActivity());

        View messageDetailView;
        if (currentMessage.isMessage()) {
            messageDetailView = inflater.inflate(R.layout.message_detail_fragment_message, container, false);
        } else {
            messageDetailView = inflater.inflate(R.layout.message_detail_fragment_email, container, false);

            TextView subjectView = (TextView) messageDetailView.findViewById(R.id.subject);
            subjectView.setText( ((Email)currentMessage).getSubject());
        }


        TextView contactNameView = (TextView) messageDetailView.findViewById(R.id.contact_name);
        String contactName;
        long messageContactID = currentMessage.getOwnerContactID();

        contactName = contactDataSource.findContactByID(messageContactID).getName();
        contactNameView.setText(contactName);

        TextView createTimeView = (TextView) messageDetailView.findViewById(R.id.create_time);
        createTimeView.setText(UIHelper.getCreateTimeInFullFormat(currentMessage.getCreateTimeInLong()));

        TextView messageContentView = (TextView) messageDetailView.findViewById(R.id.message_content);
        messageContentView.setText(currentMessage.getContent());

        return messageDetailView;
    }
}
