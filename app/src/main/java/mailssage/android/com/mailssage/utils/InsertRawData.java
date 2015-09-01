package mailssage.android.com.mailssage.utils;

import android.app.Activity;
import android.util.Log;

import java.util.List;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.database.ContactDataSource;
import mailssage.android.com.mailssage.database.ConversationDataSource;
import mailssage.android.com.mailssage.database.MessageDataSource;
import mailssage.android.com.mailssage.model.Contact;
import mailssage.android.com.mailssage.model.Conversation;
import mailssage.android.com.mailssage.model.Email;
import mailssage.android.com.mailssage.model.Message;

/**
 * Created by Tank on 22/08/2015.
 */
public class InsertRawData {

    private static final String LOGTAG = "MY_INSERT_RAW_DATA";

    public static boolean insertConversationsToTheDatabase(Activity activity) {
        Log.i(LOGTAG, "Start inserting conversations.");

        ConversationDataSource conversationDataSource = new ConversationDataSource(activity);
        ContactDataSource contactDataSource = new ContactDataSource(activity);
        MessageDataSource messageDataSource = new MessageDataSource(activity);
        List<Conversation> conversationsFromXML;
        List<Message> messagesFromXML;
        List<Email> emailsFromXML;
        List<Contact> contactsFromXML;
        List<Contact> contacts;

        JDOMPullParser parser = new JDOMPullParser();

        //Check does the database contains any contact data, if no, insert the raw data
        contacts = contactDataSource.findAllContacts();
        if (contacts.isEmpty()) {
            Log.i(LOGTAG, "Cannot find any contact from the database. Start inserting data.");

            contactsFromXML = (List<Contact>) parser.parserXML(activity, R.raw.contacts);
            for (Contact currentContact : contactsFromXML) {
                contactDataSource.insertContact(currentContact);
            }

            contacts = contactDataSource.findAllContacts();
        }

        conversationsFromXML = (List<Conversation>) parser.parserXML(activity, R.raw.conversations);
        messagesFromXML = (List<Message>) parser.parserXML(activity, R.raw.messages);
        emailsFromXML = (List<Email>) parser.parserXML(activity, R.raw.emails);

        for (Conversation currentConver : conversationsFromXML) {
            boolean isMyMessage = true;
            int myContactID = Contact.MY_CONTACT_ID;
            int senderContactID = (int) currentConver.getSenderContact().getContactID();

            //Set the sender contact
            currentConver.setSenderContact(contacts.get(senderContactID));

            //Insert messages to each conversations
            for (Message currentMessage : messagesFromXML) {
                if (isMyMessage) {
                    currentMessage.setOwnerContactID(myContactID);
                    isMyMessage = false;
                } else {
                    currentMessage.setOwnerContactID(senderContactID);
                    isMyMessage = true;
                }

                currentConver.addMessage(currentMessage);
            }

            for (Email currentEmail : emailsFromXML) {
                if (isMyMessage) {
                    currentEmail.setOwnerContactID(myContactID);
                    isMyMessage = false;
                } else {
                    currentEmail.setOwnerContactID(senderContactID);
                    isMyMessage = true;
                }

                currentConver.addMessage(currentEmail);
            }

            //Start inserting the conversation objects (with its messages) to the database
            long converInsertID = conversationDataSource.insertConversation(currentConver);
            Log.i(LOGTAG, "A conversation from XML has been inserted to DB with instert ID: "
                    + converInsertID + " And has "
                    + currentConver.getAllTypeMessages(messageDataSource).size()
                    + " messages also been inserted.");

        }
        return true;
    }
}
