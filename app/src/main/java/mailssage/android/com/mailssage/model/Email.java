package mailssage.android.com.mailssage.model;

import android.os.Bundle;
import android.util.Log;

/**
 * Created by Tank on 29/08/2015.
 */
public class Email extends Message {

    private static final String LOGTAG = "MY_EMAIL";

    private String subject;

    //Bundle keys
    public static final String SUBJECT = "SUBJECT";

    public Email() {
    }

    public Email(Bundle bundle) {
        super(bundle);

        if (bundle != null) {
            this.subject = bundle.getString(SUBJECT);
        } else {
            Log.i(LOGTAG, "Email constructor received null bundle.");
        }
    }

    @Override
    public Bundle toBundle() {
        Bundle emailBundle = super.toBundle();
        emailBundle.putString(SUBJECT, this.subject);

        return emailBundle;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "\nEmail sender/receiver ID: " + getOwnerContactID() + "\n"
                + "Email Type: " + (getMessageType() == null? "null" : getMessageType().toString()) + "\n"
                + "Subject: " + getSubject() + "\n"
                + "Create Time: " + getCreateTimeInLong() + "\n"
                + "Content: " + getContent() + "\n";
    }
}
