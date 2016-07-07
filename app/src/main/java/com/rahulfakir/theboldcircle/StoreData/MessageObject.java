package com.rahulfakir.theboldcircle.StoreData;

/**
 * Created by rahul.fakir on 2016/05/28.
 */
public class MessageObject {
    private String messageID, message, dateStamp;
    private int messageType;

    public MessageObject() {

    }

    public MessageObject(String messageID, String message, String dateStamp, int messageType) {
        this.messageID = messageID;
        this.message = message;
        this.dateStamp = dateStamp;
        this.messageType = messageType;
    }


    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
