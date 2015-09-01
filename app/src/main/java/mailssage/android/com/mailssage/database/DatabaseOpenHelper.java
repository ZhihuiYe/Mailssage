package mailssage.android.com.mailssage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Tank on 23/07/2015.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {


    private static final String LOGTAG = "MY_DATABASE_HELPER";

    private static final String DATABASE_NAME_CONVER = "conversations.db";
    private static final int DATABASE_VERSION_CONVER = 66;

    /**
     *   Conversation Table
     *   |conversation_id|contact_id|total_messages|total_emails|
     */
    public static final String TABLE_NAME_CONVERSATION = "conversations";
    public static final String COLUMN_CONVERSATION_ID = "conversation_id";
    //The contact ID of the person who having the conversation with
    public static final String COLUMN_CONTACT_ID = "contact_id";
    public static final String COLUMN_TOTAL_MESSAGES = "total_messages";
    public static final String COLUMN_TOTAL_EMAILS = "total_emails";

    private static final String CREATE_TABLE_CONVER =
            "CREATE TABLE " + TABLE_NAME_CONVERSATION + " ( " +
                    COLUMN_CONVERSATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CONTACT_ID + " INTEGER, " +
                    COLUMN_TOTAL_MESSAGES + " INTEGER NOT NULL DEFAULT 0, " +
                    COLUMN_TOTAL_EMAILS + " INTEGER NOT NULL DEFAULT 0" + " );";

    public static final String[] allConverColumns = {
            DatabaseOpenHelper.COLUMN_CONVERSATION_ID,
            DatabaseOpenHelper.COLUMN_CONTACT_ID,
            DatabaseOpenHelper.COLUMN_TOTAL_MESSAGES,
            DatabaseOpenHelper.COLUMN_TOTAL_EMAILS
    };


    /** Normal Message Table
     * |message_id|conversation_id|owner_contact_id|create_time|content|
     */
    public static final String TABLE_NAME_NORMAL_MESSAGES = "messages";
    public static final String COLUMN_MESSAGE_ID = "message_id";
    public static final String COLUMN_MESSAGE_TYPE = "message_type";
//    public static final String COLUMN_CONVERSATION_ID = "conversation_id";
    public static final String COLUMN_OWNER_CONTACT_ID = "owner_contact_id";
    public static final String COLUMN_CREATE_TIME = "create_time";
    public static final String COLUMN_CONTENT = "content";

    private static final String CREATE_TABLE_MESSAGES =
            "CREATE TABLE " + TABLE_NAME_NORMAL_MESSAGES + " ( " +
                    COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MESSAGE_TYPE + " BLOB, " +
                    COLUMN_CONVERSATION_ID + " INTEGER, " +
                    COLUMN_OWNER_CONTACT_ID + " INTEGER, " +
                    COLUMN_CREATE_TIME + " INTEGER, " +
                    COLUMN_CONTENT + " TEXT " + " );";

    public static final String[] allNormalMessageColumns = {
            COLUMN_MESSAGE_ID,
            COLUMN_MESSAGE_TYPE,
            COLUMN_CONVERSATION_ID,
            COLUMN_OWNER_CONTACT_ID,
            COLUMN_CREATE_TIME,
            COLUMN_CONTENT
    };

    /**
     * Email Table
     * |email_id|conversation_id|owner_contact_id|create_time|subject|content|
     */
    public static final String TABLE_NAME_EMAILS = "emails";
//    public static final String COLUMN_EMAIL_ID = "email_id";
//    public static final String COLUMN_EMAIL_TYPE = "email_type";
//    public static final String COLUMN_CONVERSATION_ID = "conversation_id";
//    public static final String COLUMN_OWNER_CONTACT_ID = "owner_contact_id";
    public static final String COLUMN_SUBJECT = "subject";
//    public static final String COLUMN_CREATE_TIME = "create_time";
//    public static final String COLUMN_CONTENT = "content";

    private static final String CREATE_TABLE_EMAILS =
            "CREATE TABLE " + TABLE_NAME_EMAILS + " ( " +
                    COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MESSAGE_TYPE + " BLOB, " +
                    COLUMN_CONVERSATION_ID + " INTEGER, " +
                    COLUMN_OWNER_CONTACT_ID + " INTEGER, " +
                    COLUMN_SUBJECT + " TEXT, " +
                    COLUMN_CREATE_TIME + " INTEGER, " +
                    COLUMN_CONTENT + " TEXT " + " );";

    public static final String[] allEmailColumns = {
            COLUMN_MESSAGE_ID,
            COLUMN_MESSAGE_TYPE,
            COLUMN_CONVERSATION_ID,
            COLUMN_OWNER_CONTACT_ID,
            COLUMN_CREATE_TIME,
            COLUMN_CONTENT,

            COLUMN_SUBJECT
    };


    /**
     * Contact Table
     * |contact_id|name|email|description|image_id|
     */
    public static final String TABLE_NAME_CONTACTS = "contacts";
//    public static final String COLUMN_CONTACT_ID = "contact_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE_ID = "image_id";

    private static final String CREATE_TABLE_CONTACTS =
            "CREATE TABLE " + TABLE_NAME_CONTACTS + " ( " +
                    COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IMAGE_ID + " INTEGER " + " );";

    public static final String[] allColumnsContact = {
            COLUMN_CONTACT_ID,
            COLUMN_NAME,
            COLUMN_EMAIL,
            COLUMN_DESCRIPTION,
            COLUMN_IMAGE_ID
    };

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME_CONVER, null, DATABASE_VERSION_CONVER);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONVER);
        Log.i(LOGTAG, "Table has been created." + TABLE_NAME_CONVERSATION);

        db.execSQL(CREATE_TABLE_MESSAGES);
        Log.i(LOGTAG, "Table has been created." + TABLE_NAME_NORMAL_MESSAGES);

        db.execSQL(CREATE_TABLE_EMAILS);
        Log.i(LOGTAG, "Table has been created." + TABLE_NAME_EMAILS);

        db.execSQL(CREATE_TABLE_CONTACTS);
        Log.i(LOGTAG, "Table has been created." + TABLE_NAME_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONVERSATION);
        Log.i(LOGTAG, "Table: " + TABLE_NAME_CONVERSATION + " has been dropped.");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NORMAL_MESSAGES);
        Log.i(LOGTAG, "Table: " + TABLE_NAME_NORMAL_MESSAGES + " has been dropped.");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EMAILS);
        Log.i(LOGTAG, "Table: " + TABLE_NAME_EMAILS + " has been dropped.");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONTACTS);
        Log.i(LOGTAG, "Table: " + TABLE_NAME_CONTACTS + " has been dropped.");

        onCreate(db);
        Log.i(LOGTAG, "Database has been upgraded from "
                + oldVersion + " to " + newVersion);
    }
}
