package mailssage.android.com.mailssage.utils;

import android.content.Context;
import android.util.Log;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import mailssage.android.com.mailssage.R;
import mailssage.android.com.mailssage.model.Contact;
import mailssage.android.com.mailssage.model.Conversation;
import mailssage.android.com.mailssage.model.Email;
import mailssage.android.com.mailssage.model.Message;

/**
 * Created by Tank on 21/07/2015.
 */
public class JDOMPullParser {

    private static final String LOGTAG = "MY_JDOM_PULL_PARSER";

    //Raw.conversations
    private static final String CONVERSATION_TAG = "conversation";
    private static final String SENDER_CONTACT_ID = "sender_contact_id";

    //Raw.messages
    private static final String IN_MESSAGE_TAG = "in_message";
    private static final String OUT_MESSAGE_TAG = "out_message";
    private static final String MESSAGE_OWNER_ID = "owner_id";
    private static final String MESSAGE_CREATE_TIME = "create_time";
    private static final String MESSAGE_CONTENT = "content";

    //Raw.messages
    private static final String IN_EMAIL_TAG = "in_email";
    private static final String OUT_EMAIL_TAG = "out_email";
    private static final String EMAIL_OWNER_ID = "owner_id";
    private static final String EMAIL_CREATE_TIME = "create_time";
    private static final String EMAIL_SUBJECT = "subject";
    private static final String EMAIL_CONTENT = "content";

    //Raw.contacts
    private static final String CONTACT_TAG = "contact";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE_ID = "image_id";


    private Document document;

    public JDOMPullParser() {
    }

    public List<?> parserXML(Context context, int fileID) {

        InputStream stream = context.getResources().openRawResource(fileID);
        SAXBuilder builder = new SAXBuilder();

        Log.i(LOGTAG, "Start parser a XML file");

        try {
            document = builder.build(stream);

            switch (fileID) {
                case R.raw.conversations:
                    return parserConversations();
                case R.raw.messages:
                    return parserMessages();
                case R.raw.emails:
                    return parserEmails();
                case R.raw.contacts:
                    return parserContacts();
                default:
                    Log.i(LOGTAG, "Cannot identify the XML file's ID.");
                    return new ArrayList<>();
            }
        } catch (JDOMException e) {
            Log.i(LOGTAG, e.toString());
        } catch (IOException e) {
            Log.i(LOGTAG, e.toString());
        }

        return new ArrayList<>();
    }

    private List<Email> parserEmails() {

        List<Email> emails = new ArrayList<>();
        Log.i(LOGTAG, "Start parser emails.");
        Element rootElement = document.getRootElement();
        
        List<Element> listInEmail = rootElement.getChildren(IN_EMAIL_TAG);
        Log.i(LOGTAG, "Found " + listInEmail.size() + " IN email tags");

        for (Element currentEmail : listInEmail) {
            long ownerID = Long.parseLong(currentEmail.getChildText(EMAIL_OWNER_ID));
            String subject = currentEmail.getChildText(EMAIL_SUBJECT);
            long createTime = Long.parseLong(currentEmail.getChildText(EMAIL_CREATE_TIME));
            String content = currentEmail.getChildText(EMAIL_CONTENT);
            Log.i(LOGTAG, "Found IN email; owner ID: " + ownerID
                    + " Subject: " + subject
                    + " Create Time: " + createTime
                    + " Content: " + content);

            Email newEmail = new Email();
            newEmail.setMessageType(Message.MessageType.IN_EMAIL);
            newEmail.setOwnerContactID(ownerID);
            newEmail.setSubject(subject);
            newEmail.setCreateTime(createTime);
            newEmail.setContent(content);

            emails.add(newEmail);
        }

        List<Element> listOutEmail = rootElement.getChildren(OUT_EMAIL_TAG);
        Log.i(LOGTAG, "Found " + listOutEmail.size() + " OUT email tags");

        for (Element currentEmail : listOutEmail) {
            long ownerID = Long.parseLong(currentEmail.getChildText(EMAIL_OWNER_ID));
            String subject = currentEmail.getChildText(EMAIL_SUBJECT);
            long createTime = Long.parseLong(currentEmail.getChildText(EMAIL_CREATE_TIME));
            String content = currentEmail.getChildText(EMAIL_CONTENT);
            Log.i(LOGTAG, "Found IN email; owner ID: " + ownerID
                    + " Subject: " + subject
                    + " Create Time: " + createTime
                    + " Content: " + content);

            Email newEmail = new Email();
            newEmail.setMessageType(Message.MessageType.OUT_EMAIL);
            newEmail.setOwnerContactID(ownerID);
            newEmail.setSubject(subject);
            newEmail.setCreateTime(createTime);
            newEmail.setContent(content);

            emails.add(newEmail);
        }

        return emails;
    }

    private List<Message> parserMessages() {

        List<Message> messages = new ArrayList<>();
        Log.i(LOGTAG, "Start parser messages.");
        Element rootElement = document.getRootElement();
        
        List<Element> listInMessage = rootElement.getChildren(IN_MESSAGE_TAG);
        Log.i(LOGTAG, "Found " + listInMessage.size() + " IN message tags");

        for (Element currentMessage : listInMessage) {
            long ownerID = Long.parseLong(currentMessage.getChildText(MESSAGE_OWNER_ID));
            long createTime = Long.parseLong(currentMessage.getChildText(MESSAGE_CREATE_TIME));
            String content = currentMessage.getChildText(MESSAGE_CONTENT);
            Log.i(LOGTAG, "Found IN message; Sender ID: " + ownerID
                    + " Create Time: " + createTime
                    + " Content: " + content);

            Message newMessage = new Message();
            newMessage.setMessageType(Message.MessageType.IN_MESSAGE);
            newMessage.setOwnerContactID(ownerID);
            newMessage.setCreateTime(createTime);
            newMessage.setContent(content);

            messages.add(newMessage);
        }

        List<Element> listOutMessage = rootElement.getChildren(OUT_MESSAGE_TAG);
        Log.i(LOGTAG, "Found " + listOutMessage.size() + " OUT message tags");

        for (Element currentMessage : listOutMessage) {
            long receiverID = Long.parseLong(currentMessage.getChildText(MESSAGE_OWNER_ID));
            long createTime = Long.parseLong(currentMessage.getChildText(MESSAGE_CREATE_TIME));
            String content = currentMessage.getChildText(MESSAGE_CONTENT);
            Log.i(LOGTAG, "Found OUT message: Receiver ID: " + receiverID
                    + " Create Time: " + createTime
                    + " Content: " + content);

            Message newMessage = new Message();
            newMessage.setMessageType(Message.MessageType.OUT_MESSAGE);
            newMessage.setOwnerContactID(receiverID);
            newMessage.setCreateTime(createTime);
            newMessage.setContent(content);
            messages.add(newMessage);
        }

        return messages;
    }


    private List<Contact> parserContacts() {

        List<Contact> contacts = new ArrayList<>();
        Log.i(LOGTAG, "Start parser contacts.");

        Element rootElement = document.getRootElement();
        List<Element> listContact = rootElement.getChildren(CONTACT_TAG);
        Log.i(LOGTAG, "Found " + listContact.size() + " contact tags");

        for (Element currentContact : listContact) {

            String name = currentContact.getChildText(NAME);
            Log.i(LOGTAG, "Found a contact name: " + name + " from the raw file.");

            String email = currentContact.getChildText(EMAIL);
            Log.i(LOGTAG, "Found a contact email: " + email + " from the raw file.");

            String description = currentContact.getChildText(DESCRIPTION);
            Log.i(LOGTAG, "Found a contact description: " + description + " from the raw file.");

            int imageID = Integer.parseInt(currentContact.getChildText(IMAGE_ID));
            Log.i(LOGTAG, "Found a contact image ID: " + imageID + " from the raw file.");


            Contact newContact = new Contact();
            newContact.setName(name);
            newContact.setEmail(email);
            newContact.setDescription(description);
            newContact.setImageID(imageID);

            contacts.add(newContact);
        }

        return contacts;
    }


    private List<Conversation> parserConversations() {

        List<Conversation> conversations = new ArrayList<>();
        Log.i(LOGTAG, "Start parser conversations");

        Element rootElement = document.getRootElement();
        List<Element> listConver = rootElement.getChildren(CONVERSATION_TAG);
        Log.i(LOGTAG, "Found " + listConver.size() + " conversation tags");

        for (Element currentConver : listConver) {
            String contactID = currentConver.getChildText(SENDER_CONTACT_ID);
            Log.i(LOGTAG, "Found sender ID: " + contactID + " from the raw file.");

            Conversation newConver = new Conversation();
            Contact newContact = new Contact();
            newContact.setContactID(Long.parseLong(contactID));
            newConver.setSenderContact(newContact);

            conversations.add(newConver);
        }

        Log.i(LOGTAG, "Raw file conversations:\n" + conversations.toString());
        return conversations;
    }
}
