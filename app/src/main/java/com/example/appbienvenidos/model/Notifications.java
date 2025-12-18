package com.example.appbienvenidos.model;

import androidx.credentials.exceptions.domerrors.DataCloneError;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Notifications {
    private String recipientId;
    private String senderName;
    private String action;
    private String spotTitle;

    @ServerTimestamp
    private Date timestamp;

    public Notifications() {}

    public Notifications(String recipientId, String senderName, String action, String spotTitle){
        this.recipientId = recipientId;
        this.senderName = senderName;
        this.action = action;
        this.spotTitle = spotTitle;
        this.timestamp = new Date();
    }

    public String getRecipientId() {return recipientId;}
    public String getSenderName() {return senderName;}
    public String getAction() {return action;}

    public String getSpotTitle() {return spotTitle;}
    public Date getTimestamp() {return timestamp;}
}
