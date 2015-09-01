package mailssage.android.com.mailssage.array_adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.model.Conversation;

/**
 * Created by Tank on 20/07/2015.
 */
public class ConversationArrayAdapter extends ArrayAdapter<Conversation> {

    private static final String LOGTAG = "MY_ARRAY_ADAPTER_CONVER";

    private Context context;
    private List<Conversation> conversations;

    public ConversationArrayAdapter(Context context, int resource, List<Conversation> conversations) {
        super(context, resource, conversations);
        this.context = context;
        this.conversations = conversations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Conversation currentConversation = conversations.get(position);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.conversation_listitem, null);

        ImageView senderImage = (ImageView) view.findViewById(R.id.sender_image);
        senderImage.setImageResource(currentConversation.getImageID());

        TextView senderName = (TextView) view.findViewById(R.id.sender_name);
        senderName.setText(currentConversation.getContactName());

        TextView latestMessageCreateTime = (TextView) view.findViewById(R.id.latest_message_create_time);
        latestMessageCreateTime.setText(currentConversation.getLatestMessageCreateTime());

        TextView latestMessage = (TextView) view.findViewById(R.id.latest_message);
        latestMessage.setText(currentConversation.getLatestMessage());

        return view;
    }
}
