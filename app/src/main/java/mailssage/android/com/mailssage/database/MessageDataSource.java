package mailssage.android.com.mailssage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mailssage.android.com.mailssage.model.Email;
import mailssage.android.com.mailssage.model.Message;

/**
 * Created by Tank on 25/07/2015.
 */
public class MessageDataSource {

    private static final String LOGTAG = "MY_DS_MESSAGE";

    private DatabaseOpenHelper dbHelper;
    private SQLiteDatabase database;

    public MessageDataSource(Context context) {
        dbHelper = new DatabaseOpenHelper(context);
        this.open();
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
        Log.i(LOGTAG, "Message database opened.");
    }

    public void close() {
        dbHelper.close();
        Log.i(LOGTAG, "Message database closed.");
    }

    public long insertMessage(Message givenMessage, long conversationDatabaseID) {

        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_MESSAGE_TYPE, givenMessage.getMessageType().getBytes());
        values.put(DatabaseOpenHelper.COLUMN_CONVERSATION_ID, conversationDatabaseID);
        values.put(DatabaseOpenHelper.COLUMN_OWNER_CONTACT_ID, givenMessage.getOwnerContactID());
        values.put(DatabaseOpenHelper.COLUMN_CREATE_TIME, givenMessage.getCreateTimeInLong());
        values.put(DatabaseOpenHelper.COLUMN_CONTENT, givenMessage.getContent());

        long insertID;
        if (givenMessage.isEmail()) {
            values.put(DatabaseOpenHelper.COLUMN_SUBJECT, ((Email) givenMessage).getSubject());
            insertID = database.insert(DatabaseOpenHelper.TABLE_NAME_EMAILS, null, values);
        } else {
            insertID = database.insert(DatabaseOpenHelper.TABLE_NAME_NORMAL_MESSAGES, null, values);
        }

        incrementConversationStatistic(conversationDatabaseID, givenMessage.getMessageType());

        return insertID;
    }

    private void incrementConversationStatistic(long conversationDatabaseID, Message.MessageType messageType) {

        String[] selectedColumns = {DatabaseOpenHelper.COLUMN_TOTAL_EMAILS
                , DatabaseOpenHelper.COLUMN_TOTAL_MESSAGES};
        String selection = DatabaseOpenHelper.COLUMN_CONVERSATION_ID + "=" + conversationDatabaseID;

        Cursor cursor = database.query(DatabaseOpenHelper.TABLE_NAME_CONVERSATION
                , selectedColumns, selection
                , null, null, null, null);

        if (cursor == null || cursor.getCount() <= 0) {
            Log.i(LOGTAG, "Cannot find the conversation from the database.");
            return;
        }

        cursor.moveToFirst();
        int currentVale;

        ContentValues newValue = new ContentValues();
        switch (messageType) {
            case OUT_MESSAGE:
            case IN_MESSAGE:
                currentVale = cursor.getInt(
                        cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_TOTAL_MESSAGES));
                newValue.put(DatabaseOpenHelper.COLUMN_TOTAL_MESSAGES, currentVale + 1);
                break;

            case OUT_EMAIL:
            case IN_EMAIL:
                currentVale = cursor.getInt(
                        cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_TOTAL_EMAILS));
                newValue.put(DatabaseOpenHelper.COLUMN_TOTAL_EMAILS, currentVale + 1);
                break;

            default:
                Log.i(LOGTAG, "incrementConversationStatistic: cannot identify message type.");
                break;
        }

        int result = database.update(DatabaseOpenHelper.TABLE_NAME_CONVERSATION, newValue, selection, null);
        Log.i(LOGTAG, "Result: " + result);
    }

    public List<Message> getAllTypeMessages(long conversationID) {
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(this.getAllNormalMessages(conversationID));
        allMessages.addAll(this.getAllEmails(conversationID));

        Collections.sort(allMessages, Collections.reverseOrder());
        return allMessages;
    }

    public List<Message> getAllEmails(long conversationID) {
        Log.i(LOGTAG, "Finding messages with conversation ID: " + conversationID);
        List<Message> allEmails;
        String selection = DatabaseOpenHelper.COLUMN_CONVERSATION_ID + "=" + conversationID;

        Cursor cursor = database.query(DatabaseOpenHelper.TABLE_NAME_EMAILS
                , DatabaseOpenHelper.allEmailColumns
                , selection, null, null, null, null);

        if (cursor == null) {
            Log.i(LOGTAG, "Database (" + DatabaseOpenHelper.TABLE_NAME_EMAILS
                    + ") return a null cursor.");
            return new ArrayList<>();
        } else {
            Log.i(LOGTAG, DatabaseOpenHelper.TABLE_NAME_EMAILS
                    + " returned " + cursor.getCount() + " rows.");
        }

        allEmails = messageCursorToList(cursor);
        Collections.sort(allEmails, Collections.reverseOrder());
        return allEmails;
    }


    public List<Message> getAllNormalMessages(long conversationID) {

        Log.i(LOGTAG, "Finding messages with conversation ID: " + conversationID);
        List<Message> allNormalMessages;
        String selection = DatabaseOpenHelper.COLUMN_CONVERSATION_ID + "=" + conversationID;

        //Start with messages table
        Cursor cursor = database.query(DatabaseOpenHelper.TABLE_NAME_NORMAL_MESSAGES
                , DatabaseOpenHelper.allNormalMessageColumns
                , selection, null, null, null, null);

        if (cursor == null) {
            Log.i(LOGTAG, "Database (" + DatabaseOpenHelper.TABLE_NAME_NORMAL_MESSAGES
                    + ") return a null cursor.");
            return new ArrayList<>();
        } else {
            Log.i(LOGTAG, DatabaseOpenHelper.TABLE_NAME_NORMAL_MESSAGES
                    + " returned " + cursor.getCount() + " rows.");
        }

        allNormalMessages = messageCursorToList(cursor);

        Collections.sort(allNormalMessages, Collections.reverseOrder());
        return allNormalMessages;
    }

    private List<Message> messageCursorToList(Cursor cursor) {

        List<Message> messages = new ArrayList<>();

        if (cursor == null) {
            Log.i(LOGTAG, "Message cursor to List received a null cursor");
            return messages;
        } else if (cursor.getCount() == 0) {
            Log.i(LOGTAG, "Message cursor to list received a empty cursor contains 0 rows.");
        }


        while (cursor.moveToNext()) {

            long messageID = cursor.getLong(
                    cursor.getColumnIndex(
                            DatabaseOpenHelper.COLUMN_MESSAGE_ID));

            Message.MessageType messageType = Message.MessageType.byteToEnum(cursor.getBlob(
                    cursor.getColumnIndex(
                            DatabaseOpenHelper.COLUMN_MESSAGE_TYPE)));

            String subject = null;
            if (messageType.equals(Message.MessageType.IN_EMAIL)
                    || messageType.equals(Message.MessageType.OUT_EMAIL)) {
                subject = cursor.getString(
                        cursor.getColumnIndex(
                                DatabaseOpenHelper.COLUMN_SUBJECT));
            }

            long ownerID = cursor.getLong(
                    cursor.getColumnIndex(
                            DatabaseOpenHelper.COLUMN_OWNER_CONTACT_ID));

            long createTime = cursor.getLong(
                    cursor.getColumnIndex(
                            DatabaseOpenHelper.COLUMN_CREATE_TIME));

            String content = cursor.getString(
                    cursor.getColumnIndex(
                            DatabaseOpenHelper.COLUMN_CONTENT));

            Message newMessage;

            if (messageType.equals(Message.MessageType.OUT_MESSAGE)
                    || messageType.equals(Message.MessageType.IN_MESSAGE)) {
                newMessage = new Message();
            } else if (messageType.equals(Message.MessageType.OUT_EMAIL)
                    || messageType.equals(Message.MessageType.IN_EMAIL)) {
                newMessage = new Email();
                ((Email) newMessage).setSubject(subject);
            } else {
                newMessage = new Message();
                Log.i(LOGTAG, "Cannot identify the message type: " + messageType.toString());
            }

            newMessage.setDatabaseID(messageID);
            newMessage.setMessageType(messageType);
            newMessage.setOwnerContactID(ownerID);
            newMessage.setCreateTime(createTime);
            newMessage.setContent(content);

            messages.add(newMessage);
        }

        Log.i(LOGTAG, "Have get following message objects from the given cursor:\n"
                + messages.toString());
        return messages;
    }
}
