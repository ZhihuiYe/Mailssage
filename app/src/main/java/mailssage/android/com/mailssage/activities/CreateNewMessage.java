package mailssage.android.com.mailssage.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Calendar;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.database.ContactDataSource;
import mailssage.android.com.mailssage.fragments.main.SettingFragment;
import mailssage.android.com.mailssage.model.Contact;
import mailssage.android.com.mailssage.model.Email;
import mailssage.android.com.mailssage.model.Message;
import mailssage.android.com.mailssage.utils.UIHelper;

/**
 * Created by Tank on 01/08/2015.
 */
public class CreateNewMessage extends Activity {

    private static final String LOGTAG = "MY_CREATE_NEW_MESS";
    public static final String NEW_MESSAGE_TYPE = "NEW_MESSAGE_TYPE";
    private static SharedPreferences settings;
    private Message.MessageType newMessageType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);

        settings = getSharedPreferences(SettingFragment.SETTINGS, MODE_PRIVATE);
        newMessageType = (Message.MessageType) getIntent().getSerializableExtra(NEW_MESSAGE_TYPE);

        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_create_new_message_email, menu);
        return true;
    }

    public void refresh() {
        if (newMessageType.isEmail()) {
            Log.i(LOGTAG, "New message will be in email type.");
            setContentView(R.layout.activity_create_email);
        } else if (newMessageType.isMessage()) {
            Log.i(LOGTAG, "New message will be in normal message type.");
            setContentView(R.layout.activity_create_message);
        } else {
            Log.i(LOGTAG, "Cannot identify the new message type.");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);

                finish();
                break;

            case R.id.action_send:
                Log.i(LOGTAG, "Start saving the message.");

                String receiverName = UIHelper.getText(this, R.id.receiver);
                String subject = null;
                String messageContent = UIHelper.getText(this, R.id.new_message_content);

                //ignore this action, if the message contains no content
                if (messageContent == null || messageContent.isEmpty()) {
                    Log.i(LOGTAG, "The new message contains no content. ignore the send action.");
                    return false;
                }

                EditText subjectView = (EditText) this.findViewById(R.id.subject);
                if (subjectView != null) {
                    subject = subjectView.getText().toString();
                }

                //Start creating new message
                ContactDataSource contactDataSource = new ContactDataSource(this);
                Contact receiverContact = contactDataSource.findContactByName(receiverName);

                //If the system cannot find the contact, ignore the send action
                if (receiverContact == null) {
                    Log.i(LOGTAG, "Cannot find the contact, ignore the send action.");
                    return false;
                }


                Message newMessage;
                if (newMessageType.isMessage()) {
                    newMessage = new Message();
                } else {
                    newMessage = new Email();
                    ((Email) newMessage).setSubject(subject);
                }

                newMessage.setOwnerContactID(Contact.MY_CONTACT_ID);
                newMessage.setMessageType(newMessageType);
                newMessage.setCreateTime(Calendar.getInstance().getTimeInMillis());
                newMessage.setContent(messageContent);

                Log.i(LOGTAG, "New message/email has been created:\n" + newMessage.toString());

                //Pass the new message back to main activity.
                Intent newMessageIntent = new Intent();
                newMessageIntent.putExtra(Message.MESSAGE_BUNDLE, newMessage.toBundle());
                newMessageIntent.putExtra(Contact.CONTACT_BUNDLE, receiverContact.toBundle());
                newMessageIntent.putExtra(NEW_MESSAGE_TYPE, newMessageType.toString());

                setResult(RESULT_OK, newMessageIntent);
                finish();
                break;

            case R.id.action_change_to_email_type:
                Log.i(LOGTAG, "The new message type change to email");

                //Change the default setting
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(NEW_MESSAGE_TYPE, Message.MessageType.OUT_EMAIL.toString());
                editor.commit();

                //Change the local variable to insure the new message is the correct type
                newMessageType = Message.MessageType.OUT_EMAIL;

                refresh();
                break;

            case R.id.action_change_to_normal_message_type:
                Log.i(LOGTAG, "The new message type change to normal message");

                //Change the default setting
                SharedPreferences.Editor editor1 = settings.edit();
                editor1.putString(NEW_MESSAGE_TYPE, Message.MessageType.OUT_MESSAGE.toString());
                editor1.commit();

                //Change the local variable to insure the new message is the correct type
                newMessageType = Message.MessageType.OUT_MESSAGE;

                refresh();
                break;

            default:
                Log.i(LOGTAG, "Cannot identify the option item.");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
