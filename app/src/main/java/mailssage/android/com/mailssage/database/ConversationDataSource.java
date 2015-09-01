package mailssage.android.com.mailssage.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mailssage.android.com.mailssage.model.Contact;
import mailssage.android.com.mailssage.model.Conversation;
import mailssage.android.com.mailssage.model.Message;
import mailssage.android.com.mailssage.utils.InsertRawData;

/**
 * Created by Tank on 23/07/2015.
 */
public class ConversationDataSource {

    private static final String LOGTAG = "MY_DS_CONVER";

    private DatabaseOpenHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;


    public ConversationDataSource(Context context) {
        dbHelper = new DatabaseOpenHelper(context);
        this.context = context;
        this.open();
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
        Log.i(LOGTAG, "Database opened.");
    }

    public void close() {
        dbHelper.close();
        Log.i(LOGTAG, "Database closed.");
    }


    public long insertConversation(Conversation givenConver) {
        long senderContactID = givenConver.getSenderContact().getContactID();
        long myContactID = Contact.MY_CONTACT_ID; // TODO: set the correct my contact id

        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_CONTACT_ID, senderContactID);
        values.put(DatabaseOpenHelper.COLUMN_TOTAL_MESSAGES, 0);
        values.put(DatabaseOpenHelper.COLUMN_TOTAL_EMAILS, 0);

        long insertId = database.insert(DatabaseOpenHelper.TABLE_NAME_CONVERSATION, null, values);
        givenConver.setConversationDatabaseID(insertId);
        Log.i(LOGTAG, "New Conversation has inserted. ID: " + insertId);

        //Insert all the messages
        List<Message> messages = givenConver.getLocalMessages();
        if (messages != null && messages.size() > 0) {

            MessageDataSource messageDataSource = new MessageDataSource(context);
            messageDataSource.open();

            long insertID;
            for (Message currentMes : messages) {
                Message.MessageType currentMessageType = currentMes.getMessageType();
                if (currentMessageType.equals(Message.MessageType.OUT_MESSAGE)
                        || currentMessageType.equals(Message.MessageType.OUT_EMAIL)) {
                    currentMes.setOwnerContactID(myContactID);
                } else {
                    currentMes.setOwnerContactID(senderContactID);
                }

                insertID = messageDataSource.insertMessage(currentMes, givenConver.getConversationDatabaseID());
                Log.i(LOGTAG, "A new IN/OUT message has inserted with insert ID: " + insertID
                        + "\n" + currentMes.toString());
            }
        }

        return insertId;
    }


    private List<Conversation> conversationCursorToList(Cursor cursor) {
        List<Conversation> conversations = new ArrayList<>();
        ContactDataSource contactDataSource = new ContactDataSource(context);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                long converID = cursor.getLong(
                        cursor.getColumnIndex(
                                DatabaseOpenHelper.COLUMN_CONVERSATION_ID));
                Log.i(LOGTAG, "Found a conversation ID: " + converID);

                int contactID = cursor.getInt(
                        cursor.getColumnIndex(
                                DatabaseOpenHelper.COLUMN_CONTACT_ID));
                Log.i(LOGTAG, "Found a contact ID: " + contactID);


                int totalMessage = cursor.getInt(
                        cursor.getColumnIndex(
                                DatabaseOpenHelper.COLUMN_TOTAL_MESSAGES));
                Log.i(LOGTAG, "Total number of message: " + totalMessage);

                int totalEmail = cursor.getInt(
                        cursor.getColumnIndex(
                                DatabaseOpenHelper.COLUMN_TOTAL_EMAILS));
                Log.i(LOGTAG, "Total number of email: " + totalEmail);

                Conversation newConver = new Conversation();
                newConver.setConversationDatabaseID(converID);
                newConver.setTotalNumberOfMessage(totalMessage);
                newConver.setTotalNumberOfEmail(totalEmail);
                newConver.setSenderContact(contactDataSource.findContactByID(contactID));

                conversations.add(newConver);
            }
        } else {
            Log.i(LOGTAG, "ConversationCursorToList received an empty cursor.");
        }

        Log.i(LOGTAG, "Found conversations: " + conversations.toString());
        return conversations;
    }

    public Conversation findConversation(long contactID) {
        Log.i(LOGTAG, "Start finding the conversation with contact ID: " + contactID);

        String selection = DatabaseOpenHelper.COLUMN_CONTACT_ID + "=" + contactID;

        Cursor cursor = database.query(DatabaseOpenHelper.TABLE_NAME_CONVERSATION
                , DatabaseOpenHelper.allConverColumns
                , selection, null, null, null, null, null);

        List<Conversation> foundConversation = conversationCursorToList(cursor);
        if (!foundConversation.isEmpty()) {
            return foundConversation.get(0);
        } else {
            return null;
        }
    }

    public List<Conversation> getAllConversation(Activity activity) {

        Log.i(LOGTAG, "Start finding all conversations.");

        Cursor cursor = database.query(DatabaseOpenHelper.TABLE_NAME_CONVERSATION
                , DatabaseOpenHelper.allConverColumns
                , null, null, null, null, null, null);

        if (cursor == null || cursor.getCount() <= 0) {
            Log.i(LOGTAG, "Cursor contains no useful info start inserting Raw data.");
            InsertRawData.insertConversationsToTheDatabase(activity);

            //run the query again
            cursor = database.query(DatabaseOpenHelper.TABLE_NAME_CONVERSATION
                    , DatabaseOpenHelper.allConverColumns
                    , null, null, null, null, null, null);
        }


        Log.i(LOGTAG, DatabaseOpenHelper.TABLE_NAME_CONVERSATION
                + " returned " + cursor.getCount() + " rows");


        return conversationCursorToList(cursor);
    }

    public enum FilterOption {
        MESSAGE_ONLY,
        EMAIL_ONLY
    }
    
    public List<Conversation> getEmailConverOnly(Activity activity) {
        return converFilter(activity, FilterOption.EMAIL_ONLY);
    }
    
    public List<Conversation> getMessageConverOnly(Activity activity) {
        return converFilter(activity, FilterOption.MESSAGE_ONLY);
    }
    
    public List<Conversation> converFilter(Activity activity, FilterOption filterOption){
        Log.i(LOGTAG, "Start finding conversations that contains messages.");

        List<Conversation> conversations = this.getAllConversation(activity);

        Iterator<Conversation> conversationIterator = conversations.iterator();
        List<Conversation> removeList = new ArrayList<>();
        Conversation currentConversation;

        while (conversationIterator.hasNext()) {
            currentConversation = conversationIterator.next();

            if (filterOption == FilterOption.MESSAGE_ONLY) {
                if (currentConversation.getTotalNumberOfMessage() == 0) {
                    removeList.add(currentConversation);
                }

                Log.i(LOGTAG, "Conversation (ID: " + currentConversation.getConversationDatabaseID() + "): "
                    + " contains " + currentConversation.getTotalNumberOfMessage() + " messages.");

            } else if (filterOption == FilterOption.EMAIL_ONLY) {
                if (currentConversation.getTotalNumberOfEmail() == 0) {
                    removeList.add(currentConversation);
                }

                Log.i(LOGTAG, "Conversation (ID: " + currentConversation.getConversationDatabaseID() + "): "
                        + " contains " + currentConversation.getTotalNumberOfEmail() + " emails.");
            }
        }

        for (Conversation currentRemoveConversation : removeList) {
            Log.i(LOGTAG, "Conversation (ID: " + currentRemoveConversation.getConversationDatabaseID() + "): "
                    + "has no messages" + currentRemoveConversation.getTotalNumberOfMessage()
                    + ", so remove from the list");
            conversations.remove(currentRemoveConversation);
        }

        return conversations;
    }
}
