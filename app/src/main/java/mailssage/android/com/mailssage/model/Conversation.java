package mailssage.android.com.mailssage.model;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mailssage.android.com.mailssage.database.MessageDataSource;

/**
 * Created by Tank on 20/07/2015.
 */
public class Conversation {

    private static final String LOGTAG = "CONVERSATION";

    //Bundle keys
    public static final String CONVERSATION_BUNDLE = "CONVERSATION_BUNDLE";
    public static final String BUNDLE_CONVERSATION_ID = "BUNDLE_CONVERSATION_ID";
    public static final String BUNDLE_TOTAL_MESSAGES = "BUNDLE_TOTAL_MESSAGES";
    public static final String BUNDLE_TOTAL_EMAILS = "BUNDLE_TOTAL_EMAILS";
    public static final String BUNDLE_SENDER_CONTACT = "BUNDLE_SENDER_CONTACT";

    private long conversationDatabaseID;

    private Contact senderContact;
    private int totalNumberOfMessage = 0;
    private int totalNumberOfEmail = 0;
    private List<Message> messages = new ArrayList<Message>();

    public Conversation() {
    }


    public Conversation(Bundle bundle) {
        this.conversationDatabaseID = bundle.getLong(BUNDLE_CONVERSATION_ID);
        this.totalNumberOfMessage = bundle.getInt(BUNDLE_TOTAL_MESSAGES);
        this.totalNumberOfEmail = bundle.getInt(BUNDLE_TOTAL_EMAILS);
        this.senderContact = new Contact(bundle.getBundle(BUNDLE_SENDER_CONTACT));
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_CONVERSATION_ID, this.conversationDatabaseID);
        bundle.putInt(BUNDLE_TOTAL_MESSAGES, this.totalNumberOfMessage);
        bundle.putInt(BUNDLE_TOTAL_EMAILS, this.totalNumberOfEmail);
        bundle.putBundle(BUNDLE_SENDER_CONTACT, this.senderContact.toBundle());

        return bundle;
    }


    public boolean addMessage(Message newMessage) {
        if (messages == null) {
            messages = new ArrayList<Message>();
        }

        messages.add(newMessage);
        Log.i(LOGTAG, "Added a new message");
        return true;
    }


    //Setters and getters
    public String getLatestMessage() {
        return "Latest Message";
    }

    public String getLatestMessageCreateTime() {
        return "10:00";
    }

    public int getImageID() { return senderContact.getImageID(); }

    public int getImageID(long contactID) {
        if (contactID == senderContact.getContactID()) {
            return senderContact.getImageID();
        } else {
            return Contact.DEFAULT_USER_IMAGE;
        }
    }

    public List<Message> getLocalMessages() {
        if (messages == null) {
            return new ArrayList<>();
        } else {
            return messages;
        }
    }

    public List<Message> getAllTypeMessages(MessageDataSource dataSource) {
            messages = dataSource.getAllTypeMessages(this.conversationDatabaseID);
            return this.messages;
    }

    public List<Message> getAllNormalMessages(MessageDataSource dataSource) {
        return dataSource.getAllNormalMessages(this.conversationDatabaseID);
    }

    public List<Message> getAllEmails(MessageDataSource dataSource) {
        return dataSource.getAllEmails(this.conversationDatabaseID);
    }

    public long getConversationDatabaseID() { return conversationDatabaseID; }

    public void setConversationDatabaseID(long conversationDatabaseID) {
        this.conversationDatabaseID = conversationDatabaseID;
    }

    public int getTotalNumberOfMessage() { return totalNumberOfMessage; }

    public void setTotalNumberOfMessage(int total) { this.totalNumberOfMessage = total; }

    public int getTotalNumberOfEmail() { return totalNumberOfEmail; }

    public void setTotalNumberOfEmail(int total) { this.totalNumberOfEmail = total; }

    public Contact getSenderContact() {
        return senderContact;
    }

    public String getContactName() {
        return this.senderContact.getName();
    }

    public String getContactName(long contactID) {
        if (contactID == this.senderContact.getContactID()) {
            return senderContact.getName();
        } else {
            return "ME";
        }
    }

    public void setSenderContact(Contact senderContract) { this.senderContact = senderContract; }

    @Override
    public String toString() {
        return "\nConversation ID: " + conversationDatabaseID + "\n"
                + "Contact: " + senderContact.toString() + "\n"
                + "Total number of message: " + getLocalMessages().size()
                + (messages == null ? " No message " : messages.toString());
    }

    public String toString(MessageDataSource dataSource) {
        return "\nConversation ID: " + conversationDatabaseID + "\n"
                + "Contact: " + senderContact.toString() + "\n"
                + "Total number of message: " + getAllTypeMessages(dataSource).size()
                + (messages == null ? " No message " : messages.toString());
    }
}
