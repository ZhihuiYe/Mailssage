package mailssage.android.com.mailssage.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.activities.MainActivity;
import mailssage.android.com.mailssage.array_adapter.MessageArrayAdapter;
import mailssage.android.com.mailssage.database.MessageDataSource;
import mailssage.android.com.mailssage.model.Conversation;
import mailssage.android.com.mailssage.model.Message;

/**
 * Created by Tank on 27/07/2015.
 */
public class MessageListFragment extends ListFragment {

    private static final String LOGTAG = "MY_MESSAGE_LISTFRAGMENT";

    private CallBack messageListActivity;

    private Conversation selectedConversation;
    private List<Message> messages;

    public MessageListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle conversationBundle = getArguments();

        if (conversationBundle != null) {
            if (conversationBundle.containsKey(Conversation.BUNDLE_CONVERSATION_ID)) {
                selectedConversation = new Conversation(conversationBundle);
                Log.i(LOGTAG, "Conversation detail activity has received a conversation bundle.: "
                        + selectedConversation.toString());

                getActivity().setTitle(selectedConversation.getContactName());

                messages = getMessages();

                MessageArrayAdapter adapter = new MessageArrayAdapter(getActivity(),
                        R.layout.message_listitem_child,
                        messages, selectedConversation);
                setListAdapter(adapter);
            } else {
                Log.i(LOGTAG, "Received a non-Conversation bundle.");
            }
        } else {
            Log.i(LOGTAG, "Received a null bundle.");
        }
    }

    private List<Message> getMessages() {
        MessageDataSource messageDataSource = new MessageDataSource(getActivity());

        switch (messageListActivity.getCurrentMainScreenFragment()) {
            case MAIN:
                return selectedConversation.getAllTypeMessages(messageDataSource);
            case MESSAGE_ONLY:
                return selectedConversation.getAllNormalMessages(messageDataSource);
            case EMAIL_ONLY:
                return selectedConversation.getAllEmails(messageDataSource);
            default:
                return new ArrayList<>();
        }
    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.message_listfragment, container, false);
        return rootView;
    }

    public interface CallBack {
        void onMessageSelected(Message selectedMessage);

        MainActivity.MainScreenFragment getCurrentMainScreenFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.messageListActivity = (CallBack) activity;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Message selectedMessage = messages.get(position);
        messageListActivity.onMessageSelected(selectedMessage);
    }
}
