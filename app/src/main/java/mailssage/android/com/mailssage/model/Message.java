package mailssage.android.com.mailssage.model;

import android.os.Bundle;
import android.util.Log;

/**
 * Created by Tank on 20/07/2015.
 */
public class Message
        implements Comparable<Message> {

    public enum MessageType {
        IN_MESSAGE(new byte[] {(byte)1}),
        OUT_MESSAGE(new byte[] {(byte)2}),
        IN_EMAIL(new byte[] {(byte)3}),
        OUT_EMAIL(new byte[] {(byte)4}),
        UNKNOW(new byte[] {(byte)0});

        private final byte[] byteValue;

        MessageType(byte[] byteValue) {
            this.byteValue = byteValue;
        }

        public static MessageType byteToEnum(byte[] byteValue) {
            switch (byteValue[0]) {
                case (byte) 1:
                    return IN_MESSAGE;
                case (byte) 2:
                    return OUT_MESSAGE;
                case (byte) 3:
                    return IN_EMAIL;
                case (byte) 4:
                    return OUT_EMAIL;
                default:
                    return UNKNOW;
            }
        }

        private static final String inMessageInStr = "IN_MESSAGE";

        private static final String outMessageInStr = "OUT_MESSAGE";

        private static final String inEmailInStr = "IN_EMAIL";

        private static final String outEmailInStr = "OUT_EMAIL";

        private static final String unknowInStr = "UNKNOW";

        public static MessageType strToEnum(String typeInStr) {
            switch (typeInStr) {
                case inMessageInStr:
                    return IN_MESSAGE;
                case outMessageInStr:
                    return OUT_MESSAGE;
                case inEmailInStr:
                    return IN_EMAIL;
                case outEmailInStr:
                    return OUT_MESSAGE;
                default:
                    return UNKNOW;
            }
        }

        public boolean isEmail() {
            if (this.equals(IN_EMAIL) || this.equals(OUT_EMAIL)) {
                return true;
            } else {
                return false;
            }
        }

        public boolean isMessage() {
            if (this.equals(OUT_MESSAGE) || this.equals(IN_MESSAGE)) {
                return true;
            } else {
                return false;
            }
        }

        public byte[] getBytes() {
            return byteValue;
        }

        @Override
        public String toString() {
            switch (this) {
                case IN_MESSAGE:
                    return inMessageInStr;
                case OUT_MESSAGE:
                    return outMessageInStr;
                case IN_EMAIL:
                    return inEmailInStr;
                case OUT_EMAIL:
                    return outEmailInStr;
                case UNKNOW:
                    return unknowInStr;
                default:
                    return "UNKNOW_TYPE";
            }
        }
    }

    @Override
    public int compareTo(Message anotherMessage) {
        return (int) (this.getCreateTimeInLong() - anotherMessage.getCreateTimeInLong());
    }

    private static final String LOGTAG = "MESSAGE";

    //Bundle keys
    public static final String MESSAGE_BUNDLE = "MESSAGE_BUNDLE";
    public static final String DATABASE_ID = "DATABASE_ID";
    public static final String OWNER_ID = "OWNER_ID";
    public static final String MESSAGE_TYPE = "MESSAGE_TYPE";
    public static final String CREATE_TIME = "CREATE_TIME";
    public static final String CONTENT = "CONTENT";

    //The sender ID is the sender of the incoming message
    //And the receiver ID is who receive the out-going message
    private long databaseID;
    private long ownerContactID;
    private MessageType messageType;
    private long createTime = 0;
    private String content = "Empty message";


    public Message() {
    }

    public Message(Bundle bundle) {
        if (bundle != null) {
            this.databaseID = bundle.getLong(DATABASE_ID);
            this.ownerContactID = bundle.getLong(OWNER_ID);
            this.messageType = (MessageType) bundle.getSerializable(MESSAGE_TYPE);
            this.createTime = bundle.getLong(CREATE_TIME);
            this.content = bundle.getString(CONTENT);
        } else {
            Log.i(LOGTAG, "bundle is null.");
        }
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        bundle.putLong(DATABASE_ID, this.databaseID);
        bundle.putLong(OWNER_ID, this.ownerContactID);
        bundle.putSerializable(MESSAGE_TYPE, this.messageType);
        bundle.putLong(CREATE_TIME, this.createTime);
        bundle.putString(CONTENT, this.content);

        return bundle;
    }

    public boolean isEmail() {
        return this.messageType.isEmail();
    }

    public boolean isMessage() {
        return this.messageType.isMessage();
    }

    public boolean isInCommingMessage() {
        if (messageType.equals(MessageType.IN_EMAIL)
                || messageType.equals(MessageType.IN_MESSAGE)) {
            return true;
        } else {
            return false;
        }
    }

    //Getters and Setters
    public long getDatabaseID() { return databaseID; }

    public void setDatabaseID(long databaseID) { this.databaseID = databaseID; }

    public long getOwnerContactID() { return ownerContactID; }

    public void setOwnerContactID(long receiverID) { this.ownerContactID = receiverID; }

    public MessageType getMessageType() {
        if (messageType == null) {
            Log.i(LOGTAG, "Please set the message type before reading it.");
        }

        return messageType;
    }

    public void setMessageType(MessageType type) { this.messageType = type; }

    public long getCreateTimeInLong() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "\nMessage sender/receiver ID: " + ownerContactID + "\n"
                + "Message Type: " + (messageType == null? "null" : messageType.toString()) + "\n"
                + "Create Time: " + createTime + "\n"
                + "Content: " + content + "\n";
    }
}
