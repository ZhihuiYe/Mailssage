package mailssage.android.com.mailssage.array_adapter;

import android.app.Activity;
import android.content.Context;
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
import mailssage.android.com.mailssage.model.Conversation;
import mailssage.android.com.mailssage.model.Email;
import mailssage.android.com.mailssage.model.Message;
import mailssage.android.com.mailssage.utils.UIHelper;

/**
 * Created by Tank on 27/07/2015.
 */
public class MessageArrayAdapter extends ArrayAdapter<Message> {

    private static final String LOGTAG = "MY_ARRAY_ADAPTER_MES";

    private Context context;
    private List<Message> messages;
    private Conversation currentConver;
    private long previousMessageOwnerID;
    private Message currentMessage;

    public MessageArrayAdapter(Context context, int resource, List<Message> messages
            , Conversation currentConver) {
        super(context, resource, messages);
        this.currentConver = currentConver;
        this.context = context;
        this.messages = messages;

        this.previousMessageOwnerID = Contact.NO_BODY;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        currentMessage = messages.get(position);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View listItem;
        if (currentMessage.getOwnerContactID() != previousMessageOwnerID) {
            listItem = getListItemWithImage(inflater);
        } else {
            listItem = getListItemChild(inflater);
        }

        previousMessageOwnerID = currentMessage.getOwnerContactID();

        Log.i(LOGTAG, "Current message owner ID: " + currentMessage.getOwnerContactID());

        return listItem;
    }

    private View getListItemWithImage(LayoutInflater inflater) {
        View listItemWithImage = inflater.inflate(R.layout.message_listitem_with_image, null);

        ImageView messageTypeView = (ImageView) listItemWithImage.findViewById(R.id.message_type_icon);
        messageTypeView.setBackgroundResource(
                currentMessage.isEmail() ? R.drawable.ic_email_type : R.drawable.ic_message_type);

        ImageView messageProfileView = (ImageView) listItemWithImage.findViewById(R.id.message_profile_image);
        messageProfileView.setBackgroundResource(currentConver.getImageID(currentMessage.getOwnerContactID()));

        TextView senderNameView = (TextView) listItemWithImage.findViewById(R.id.sender_name);
        senderNameView.setText(currentConver.getContactName(currentMessage.getOwnerContactID()));

        TextView createTimeView = (TextView) listItemWithImage.findViewById(R.id.create_time);
        createTimeView.setText(UIHelper.getCreateTimeInShortFormat(currentMessage.getCreateTimeInLong()));

        TextView messagePreviewView = (TextView) listItemWithImage.findViewById(R.id.message_content);
        messagePreviewView.setText(currentMessage.isMessage() ?
                                    currentMessage.getContent()
                                    : ((Email)currentMessage).getSubject());

        return listItemWithImage;
    }

    private View getListItemChild(LayoutInflater inflater) {
        View listItemChild = inflater.inflate(R.layout.message_listitem_child, null);

        ImageView messageTypeView = (ImageView) listItemChild.findViewById(R.id.message_type_icon);
        messageTypeView.setBackgroundResource(
                currentMessage.isEmail() ? R.drawable.ic_email_type : R.drawable.ic_message_type);

        TextView createTimeView = (TextView) listItemChild.findViewById(R.id.create_time);
        createTimeView.setText(UIHelper.getCreateTimeInShortFormat(currentMessage.getCreateTimeInLong()));

        TextView messagePreviewView = (TextView) listItemChild.findViewById(R.id.message_content);
        messagePreviewView.setText(currentMessage.isMessage() ?
                currentMessage.getContent()
                : ((Email)currentMessage).getSubject());

        return listItemChild;
    }
}
