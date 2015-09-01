package mailssage.android.com.mailssage.model;

import android.os.Bundle;
import android.provider.ContactsContract;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.database.ContactDataSource;

/**
 * Created by Tank on 25/07/2015.
 */
public class Contact {

    public static final int DEFAULT_USER_IMAGE = R.drawable.ic_default_user_image;
    private static final String UNKNOW = "UNKNOW";
    public static final int MY_CONTACT_ID = Integer.MAX_VALUE;
    public static final int NO_BODY = Integer.MIN_VALUE;

    //Bundle keys
    public static final String CONTACT_BUNDLE = "CONTACT_BUNDLE";
    public static final String DATABASE_ID = "DATABASE_ID";
    public static final String CONTACT_ID = "CONTACT_ID";
    public static final String IMAGE_ID = "IMAGE_ID";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String DESCRIPTION = "DESCRIPTION";

    private long contractID;
    private int imageID = DEFAULT_USER_IMAGE;
    private String name = UNKNOW;
    private String email = UNKNOW;
    private String description = "";


    public Contact() { }

    public Contact(Bundle bundle) {
        this.contractID = bundle.getLong(CONTACT_ID);
        this.imageID = bundle.getInt(IMAGE_ID);
        this.name = bundle.getString(NAME);
        this.email = bundle.getString(EMAIL);
        this.description = bundle.getString(DESCRIPTION);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        bundle.putLong(CONTACT_ID, this.contractID);
        bundle.putInt(IMAGE_ID, this.imageID);
        bundle.putString(NAME, this.name);
        bundle.putString(EMAIL, this.email);
        bundle.putString(DESCRIPTION, this.description);

        return bundle;
    }


    //Getters and Setters
    public long getContactID() {
        return contractID;
    }

    public void setContactID(long id) { this.contractID = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImageID() {
        //TODO return imageID;
        return DEFAULT_USER_IMAGE;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    @Override
    public String toString() {
        return "ID: " + contractID + " Contact: " + name
                + " Email: " + email + " Description: " + description;
    }
}
