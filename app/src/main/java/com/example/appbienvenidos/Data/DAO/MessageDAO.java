package com.example.appbienvenidos.Data.DAO;

import androidx.room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.appbienvenidos.Data.Entities.Message;

import java.util.List;

@Dao
public interface MessageDAO {

    @Insert
    void insertChatMessage(Message message);

    @Update
    void updateChatMessage(Message message);

    @Delete
    void deleteChatMessage(Message message);

    // Récupérer les messages entre deux utilisateurs
    @Query("SELECT * FROM chat_messages WHERE (sender_id = :user1Id AND receiver_id = :user2Id) OR (sender_id = :user2Id AND receiver_id = :user1Id) ORDER BY timestamp ASC")
    LiveData<List<Message>> getConversation(int user1Id, int user2Id);

    // Marquer les messages comme lus
    @Query("UPDATE chat_messages SET is_read = 1 WHERE sender_id = :senderId AND receiver_id = :receiverId AND is_read = 0")
    void markMessagesAsRead(int senderId, int receiverId);
}
