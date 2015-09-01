package mailssage.android.com.mailssage.fragments.main;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.array_adapter.ConversationArrayAdapter;
import mailssage.android.com.mailssage.model.Conversation;

/**
 * Created by Tank on 20/07/2015.
 */
public class ConversationListFragment extends ListFragment {

    private static final String LOGTAG = "MY_CONVER_LISTFRAGMENT";


    private List<Conversation> conversations;
    private CallBack currentAttachedActivity;

    public ConversationListFragment() { }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //get the current activity from the onAttach method
        this.currentAttachedActivity = (CallBack) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conversations = currentAttachedActivity.getConversations();

        ConversationArrayAdapter adapter = new ConversationArrayAdapter(getActivity(),
                R.layout.conversation_listitem,
                conversations);
        setListAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.conversation_listfragment, container, false);
        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Conversation selectedConver = conversations.get(position);
        currentAttachedActivity.onConversationSelected(selectedConver);
    }

    public interface CallBack {
        void onConversationSelected(Conversation selectedConver);

        List<Conversation> getConversations();
    }
}
