package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;



import java.util.List;

import algonquin.cst2355.winter2024.ChatMessage;

@Dao
public interface ChatMessageDAO {
    @Insert
    public long insertMessage(ChatMessage m);
    @Query("Select * from ChatMessage")
    public List<ChatMessage> getAllMessage();
    @Delete
    void deleteMessage(ChatMessage m);
    @Query("DELETE FROM ChatMessage") // Assuming your table is named 'chat_messages'
    void deleteAllMessages();



}
