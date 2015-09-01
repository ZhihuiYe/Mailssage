package mailssage.android.com.mailssage.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.database.ConversationDataSource;
import mailssage.android.com.mailssage.database.MessageDataSource;
import mailssage.android.com.mailssage.fragments.BottomMenuFragment;
import mailssage.android.com.mailssage.fragments.main.ContactListFragment;
import mailssage.android.com.mailssage.fragments.main.ConversationListFragment;
import mailssage.android.com.mailssage.fragments.main.SettingFragment;
import mailssage.android.com.mailssage.model.Contact;
import mailssage.android.com.mailssage.model.Conversation;
import mailssage.android.com.mailssage.model.Email;
import mailssage.android.com.mailssage.model.Message;


public class MainActivity extends Activity
    implements ConversationListFragment.CallBack,
        ContactListFragment.CallBack,
        BottomMenuFragment.CallBack
{

    public enum MainScreenFragment {
        MAIN,
        MESSAGE_ONLY,
        EMAIL_ONLY,
        CONTACTS,
        SETTING
    }

    public static final String LOGTAG = "MY_MAIN_ACTIVITY";
    public static final String CURRENT_MAIN_SCREEN_FRAGMENT = "CURRENT_MAIN_SCREEN_FRAGMENT";

    private static final int SETTING_REQUEST_CODE = 1000;
    private static final int CONTACT_REQUEST_CODE = 1001;
    private static final int CREATE_MESSAGE_REQUEST_CODE = 1002;

    private ConversationDataSource conversationDataSource;
    private List<Conversation> conversations;
    private MainScreenFragment currentMainScreenFragment = MainScreenFragment.MAIN;

    private SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_main);

        setting = getSharedPreferences(SettingFragment.SETTINGS, MODE_PRIVATE);

        //initialing data sources
        conversationDataSource = new ConversationDataSource(this);

        //get all the conversation data from the database
        conversations = conversationDataSource.getAllConversation(this);


        currentMainScreenFragment = MainScreenFragment.MAIN;

        ConversationListFragment conversationListFragment = new ConversationListFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.main_activity_fragment, conversationListFragment)
                .commit();

        BottomMenuFragment bottomMenuFragment = new BottomMenuFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.button_menu_fragment, bottomMenuFragment)
                .commit();
    }

    private void refresh() {

        Fragment newFragment = null;

        switch (currentMainScreenFragment) {
            case EMAIL_ONLY:
            case MESSAGE_ONLY:
            case MAIN:
                newFragment = new ConversationListFragment();
                break;

            case CONTACTS:
                newFragment = new ContactListFragment();
                break;

            case SETTING:
                newFragment = new SettingFragment();
                break;

            default:
                break;
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.main_activity_fragment, newFragment)
                .commit();

        BottomMenuFragment bottomMenuFragment = new BottomMenuFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.button_menu_fragment, bottomMenuFragment)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_conversation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_create_message:
                Log.i(LOGTAG, "Create new message button has been pressed.");
                Intent createMessageIntent = new Intent(this, CreateNewMessage.class);

                switch (currentMainScreenFragment) {
                    case MAIN:
                        String messageTypeInStr = setting.getString(CreateNewMessage.NEW_MESSAGE_TYPE
                                , Message.MessageType.OUT_MESSAGE.toString());
                        Message.MessageType messageType = Message.MessageType.strToEnum(messageTypeInStr);
                        createMessageIntent.putExtra(CreateNewMessage.NEW_MESSAGE_TYPE
                                , messageType);
                        break;
                    case MESSAGE_ONLY:
                        createMessageIntent.putExtra(CreateNewMessage.NEW_MESSAGE_TYPE
                                , Message.MessageType.OUT_MESSAGE);
                        break;
                    case EMAIL_ONLY:
                        createMessageIntent.putExtra(CreateNewMessage.NEW_MESSAGE_TYPE
                                , Message.MessageType.OUT_EMAIL);
                        break;
                    case CONTACTS:
                        return false;
                    case SETTING:
                        return false;
                }

                startActivityForResult(createMessageIntent, CREATE_MESSAGE_REQUEST_CODE);
                break;
            default:
                Log.i(LOGTAG, "Cannot identify the menu item.");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_MESSAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            //The newMessage is a Email or Message object depend on what the createMessageActivity tells
            String newMessageTypeInStr = data.getStringExtra(CreateNewMessage.NEW_MESSAGE_TYPE);
            Message newMessage;
            if (newMessageTypeInStr.equals(Message.MessageType.OUT_EMAIL.toString())) {
                newMessage = new Email(data.getBundleExtra(Message.MESSAGE_BUNDLE));
            } else {
                newMessage = new Message(data.getBundleExtra(Message.MESSAGE_BUNDLE));
            }

            Contact receiverContact = new Contact(data.getBundleExtra(Contact.CONTACT_BUNDLE));
            Log.i(LOGTAG, "A new message has been created from Create Message activity:\n"
                    + newMessage.toString()
                    + "\nwith contact:\n" + receiverContact);

            Conversation existingConversation = conversationDataSource
                    .findConversation(receiverContact.getContactID());

            //If we the conversation is existed then add the message to the conversation in the database
            if (existingConversation != null) {
                MessageDataSource messageDataSource = new MessageDataSource(this);
                long result = messageDataSource.insertMessage(newMessage
                        , existingConversation.getConversationDatabaseID());

                Log.i(LOGTAG, "The new message(Belong to conver: "
                        + existingConversation.getSenderContact().getContactID()
                        + ") has been inserted to the database. Insert ID: " + result
                        + newMessage.toString() + "\n" + receiverContact.toString());
            } else {
                //else create a new conversation and add the new message to the new conversation
                Conversation newConversation = new Conversation();
                newConversation.setSenderContact(receiverContact);
                newConversation.addMessage(newMessage);

                long result = conversationDataSource.insertConversation(newConversation);
                Log.i(LOGTAG, "New conversation has been created and inserted into the database with ID:"
                        + result + "\n" + newConversation.toString());
            }
        }

        refresh();
    }

    @Override
    public MainScreenFragment getCurrentMainScreenFragment() {
        return currentMainScreenFragment;
    }

    /**
     * BottomMenuFragment call back functions
     */

    @Override
    public void onButtonMenuClick(View view) {
        int viewID = view.getId();

        switch (viewID) {
            case R.id.homeButton:
                getActionBar().setTitle("Mailssage");
                currentMainScreenFragment = MainScreenFragment.MAIN;
                conversations = conversationDataSource.getAllConversation(this);
                break;

            case R.id.messageButton:
                getActionBar().setTitle("Messages");
                currentMainScreenFragment = MainScreenFragment.MESSAGE_ONLY;
                conversations = conversationDataSource.getMessageConverOnly(this);
                break;

            case R.id.emailButton:
                getActionBar().setTitle("Emails");
                currentMainScreenFragment = MainScreenFragment.EMAIL_ONLY;
                conversations = conversationDataSource.getEmailConverOnly(this);
                break;

            case R.id.contactButton:
                getActionBar().setTitle("Contacts");
                currentMainScreenFragment = MainScreenFragment.CONTACTS;
                break;

            case R.id.settingButton:
                getActionBar().setTitle("Settings");
                currentMainScreenFragment = MainScreenFragment.SETTING;
                break;

            default:
                break;
        }

        refresh();
    }

    /**
     * ContactListFragment call back
     */
    @Override
    public void onContactSelected(Contact selectedContact) {
        Contact contact = selectedContact;

        Intent intent = new Intent(this, ContactDetailActivity.class);
        intent.putExtra(Contact.CONTACT_BUNDLE,contact.toBundle());

        startActivityForResult(intent, CONTACT_REQUEST_CODE);
    }

    /**
     * ConversationListFragment call back
     * onHomeIconClick, onMessageIconClick and onEmailIconClick
     * are use ContactListFragment, it will call getConversations method to get the filtered conversation list
     */
    @Override
    public List<Conversation> getConversations() {
        return this.conversations;
    }

    @Override
    public void onConversationSelected(Conversation selectedConver) {
        Bundle bundle = selectedConver.toBundle();
        Intent intent = new Intent(this, MessageListActivity.class);
        intent.putExtra(Conversation.CONVERSATION_BUNDLE, bundle);
        intent.putExtra(CURRENT_MAIN_SCREEN_FRAGMENT, currentMainScreenFragment);
        startActivityForResult(intent, SETTING_REQUEST_CODE);
    }
}
