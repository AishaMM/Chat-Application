package com.koddev.chatapp.Model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String date;
    private boolean isseen;
    private boolean media;

    public Chat(String sender, String receiver, String message, String date, boolean isseen, boolean media) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date = date;
        this.isseen = isseen;
        this.media = media;
    }

    public Chat() {
    }

    public boolean isMedia() {
        return media;
    }

    public void setMedia(boolean media) {
        media = media;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
