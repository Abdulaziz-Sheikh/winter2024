package data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2355.winter2024.ChatMessage;


@Database(entities = {ChatMessage.class}, version =1)
public abstract class MessageDatabase extends RoomDatabase {

    public abstract ChatMessageDAO cmDAO();
}