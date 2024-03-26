package algonquin.cst2355.winter2024;

import static java.lang.ref.Cleaner.create;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Entity;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2355.winter2024.databinding.ActivityChatRoomBinding;
import algonquin.cst2355.winter2024.databinding.SentMessageBinding;
import algonquin.cst2355.winter2024.databinding.RecieveMessageBinding;
import data.ChatMessageDAO;
import data.ChatRoomViewModel;
import data.MessageDatabase;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    protected ArrayList<ChatMessage> messages;
    ChatRoomViewModel chatModel;
    private RecyclerView.Adapter myAdapter;
    ChatMessageDAO mDAO;
    Executor thread = Executors.newSingleThreadExecutor();
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("ToolbarAction", "Toolbar item clicked: " + item.getItemId());
        if (item.getItemId() == R.id.item_1) {
            // Show an AlertDialog to confirm deletion of all messages
            new AlertDialog.Builder(ChatRoom.this)
                    .setMessage("Do you want to delete all messages?")
                    .setTitle("Confirmation")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Clear all messages from the list
                        messages.clear();
                        // Notify the adapter that all items have been removed
                        myAdapter.notifyDataSetChanged();
                        // Show a Snackbar for feedback
                        Snackbar.make(binding.recyclerView, "All messages deleted", Snackbar.LENGTH_LONG).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else if (item.getItemId() == R.id.item_2) {
            // Show the About Toast message
            Toast.makeText(this, "Version 1.0, code by @Mo", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ChatRoomViewModel chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();


        MessageDatabase db = Room.databaseBuilder(getApplicationContext(),MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        setSupportActionBar(binding.toolbar);



        /**
         * Checks if messeges is null and instantiates a new arrayList to post the value.
         * This resolves the NullPointer exception thrown for null object references.
         */
        if(messages == null)
        {
//            messages = new ArrayList<>();
//            chatModel.messages.postValue( messages );

            chatModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessage() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recyclerView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }



        binding.sendButton.setOnClickListener(click -> {
            String input = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EE, dd-MMM-yyyy hh:mm:ss a");
            String currentDate = sdf.format(new Date());
            boolean type = true;
            messages.add(new ChatMessage(input, currentDate, type));

            myAdapter.notifyItemInserted(messages.size()-1);

            //clear the text input field for next re-use.
            binding.textInput.setText("");
        });

        binding.recieveButton.setOnClickListener(click -> {
            String input = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDate = sdf.format(new Date());
            boolean type = false;
            messages.add(new ChatMessage(input, currentDate, type));

            myAdapter.notifyItemInserted(messages.size()-1);

            //clear the text input field for next re-use.
            binding.textInput.setText("");
        });

        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            @NonNull
            @Override
            /**
             * This is responsible for creating a layout for a row, and setting the TextViews in code.
             */
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                /**
                 * Checks if the viewType is 0 (sendButton was pressed) and sets the layout to the sent_message.xml
                 * else if its 1 (receiveButton was pressed) it sets the layout to receive_message.xml.
                 */
                if (viewType == 0){
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }else{
                    RecieveMessageBinding binding = RecieveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(binding.getRoot());
                }

            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.message);
                holder.timeText.setText(obj.timeSent);
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            //@Override
            /**
             * This function sets the layout to load for different rows.
             */
            public int getItemViewType(int position) {
                ChatMessage chat = messages.get(position);
                if (chat.isSentButton) {
                    return 0;
                } else {
                    return 1;
                }

            }

        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * This class object represents everything that goes on a row as a list.
     */
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}