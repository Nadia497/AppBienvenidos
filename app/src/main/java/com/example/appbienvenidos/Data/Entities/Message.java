package com.example.appbienvenidos.Data.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
@Entity(tableName = "chat_messages",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "sender_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "receiver_id",
                        onDelete = ForeignKey.CASCADE)
        })
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "sender_id")
    private int senderId; // Foreign Key to User

    @ColumnInfo(name = "receiver_id")
    private int receiverId; // Foreign Key to User

    @ColumnInfo(name = "message_content")
    private String messageContent;

    @ColumnInfo(name = "timestamp")
    private String timestamp; // Format YYYY-MM-DD HH:MM:SS

    @ColumnInfo(name = "is_read")
    private boolean isRead; // True ou False

    public Message(int senderId, int receiverId, String messageContent, String timestamp, boolean isRead) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
