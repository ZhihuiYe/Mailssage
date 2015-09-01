package mailssage.android.com.mailssage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mailssage.android.com.mailssage.model.Contact;

/**
 * Created by Tank on 25/07/2015.
 */
public class ContactDataSource {

    private static final String LOGTAG = "MY_DS_CONTACT";

    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;

    public ContactDataSource(Context context) {
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
        Log.i(LOGTAG, "Database close.");
    }

    public long insertContact(Contact contact) {

        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_NAME, contact.getName());
        values.put(DatabaseOpenHelper.COLUMN_EMAIL, contact.getEmail());
        values.put(DatabaseOpenHelper.COLUMN_DESCRIPTION, contact.getDescription());
        values.put(DatabaseOpenHelper.COLUMN_IMAGE_ID, contact.getImageID());

        long insertID = database.insert(DatabaseOpenHelper.TABLE_NAME_CONTACTS, null, values);
        Log.i(LOGTAG, "A contact has inserted into the database with insert ID: " + insertID);
        
        return insertID;
    }

    public List<Contact> ContactCursorToList(Cursor cursor) {
        List<Contact> contacts = new ArrayList<>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                long contactID = cursor.getLong(
                        cursor.getColumnIndex(
                                DatabaseOpenHelper.COLUMN_CONTACT_ID));
                Log.i(LOGTAG, "Found a contact ID: " + contactID);

                int imageID = cursor.getInt(
                        cursor.getColumnIndex(
                                DatabaseOpenHelper.COLUMN_IMAGE_ID));
                Log.i(LOGTAG, "Found an image ID: " + imageID);

                String name = cursor.getString(
                        cursor.getColumnIndex(
                                DatabaseOpenHelper.COLUMN_NAME));
                Log.i(LOGTAG, "Found a name: " + name);

                String email = cursor.getString(
                        cursor.getColumnIndex(
                                DatabaseOpenHelper.COLUMN_EMAIL));
                Log.i(LOGTAG, "Found an email: " + email);

                String description = cursor.getString(
                        cursor.getColumnIndex(
                                DatabaseOpenHelper.COLUMN_DESCRIPTION));
                Log.i(LOGTAG, "Found a description: " + description);

                Contact newContact = new Contact();
                newContact.setContactID(contactID);
                newContact.setImageID(imageID);
                newContact.setName(name);
                newContact.setEmail(email);
                newContact.setDescription(description);

                contacts.add(newContact);
            }
        } else {
            Log.i(LOGTAG, "ContactCursorToList received an empty cursor.");
        }

        Log.i(LOGTAG, "Found contacts: " + contacts.toString());
        return contacts;
    }
    
    public List<Contact> findAllContacts() {
        Log.i(LOGTAG, "Start finding all contacts.");

        Cursor cursor = database.query(DatabaseOpenHelper.TABLE_NAME_CONTACTS, DatabaseOpenHelper.allColumnsContact,
                null, null, null, null, null, null);

        if (cursor == null) {
            Log.i(LOGTAG, "Return a null cursor.");
        } else {
            Log.i(LOGTAG, DatabaseOpenHelper.TABLE_NAME_CONVERSATION
                    + " returned " + cursor.getCount() + " rows");
        }

        return ContactCursorToList(cursor);
    }

    public Contact findContactByName(String name) {
        List<Contact> allContacts = findAllContacts();
        String givenName = name.replace(" ", "").replace("\n", "");

        for (Contact currentContact : allContacts) {
            String currentName = currentContact.getName()
                    .replace(" ", "").replace("\n", "");
            if ( currentName.equalsIgnoreCase(givenName)) {
                return currentContact;
            }
        }

        Log.i(LOGTAG, "Cannot find the contact with name: " + name);
        return null;
    }

    public Contact findContactByID(long contactID) {

        if (contactID == Contact.MY_CONTACT_ID) {
            Contact myContact = new Contact();
            myContact.setName("Me");

            return myContact;
        }

        String whereClause = DatabaseOpenHelper.COLUMN_CONTACT_ID + " = " + contactID;
        Cursor cursor = database.query(DatabaseOpenHelper.TABLE_NAME_CONTACTS, DatabaseOpenHelper.allColumnsContact,
                whereClause, null, null, null, null);

        if (cursor.getCount() == 0) {
            Log.i(LOGTAG, "Cannot found contact that has contact ID: " + contactID);
            cursor = database.query(DatabaseOpenHelper.TABLE_NAME_CONTACTS, DatabaseOpenHelper.allColumnsContact,
                    null, null, null, null, null);
            Log.i(LOGTAG, "But contact database has " + cursor.getCount() + " row.");
            return null;
        } else if (cursor.getCount() > 1) {
            Log.i(LOGTAG, "Cursor return more than one row: " + cursor.getCount());
        }

        //Only read the first one
        cursor.moveToNext();
        Contact newContact = new Contact();

        //Now only read the necessary info
        newContact.setContactID(
                cursor.getLong(
                        cursor.getColumnIndex(
                                DatabaseOpenHelper.COLUMN_CONTACT_ID)));

        newContact.setName(
                cursor.getString(
                        cursor.getColumnIndex(
                                DatabaseOpenHelper.COLUMN_NAME)));

        newContact.setImageID(
                cursor.getInt(
                        cursor.getColumnIndex(
                                DatabaseOpenHelper.COLUMN_IMAGE_ID)));

        Log.i(LOGTAG, "With the given contact ID: " + contactID + " found: " + newContact.toString());
        return newContact;
    }

    
}
