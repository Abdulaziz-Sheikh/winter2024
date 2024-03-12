package algonquin.cst2355.winter2024;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2355.winter2024.databinding.ActivityChatRoomBinding;
import algonquin.cst2355.winter2024.databinding.SentMessageBinding;
import algonquin.cst2355.winter2024.databinding.RecieveMessageBinding;
import data.ChatRoomViewModel;
public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    protected ArrayList<ChatMessage> messages;

    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ChatRoomViewModel chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        /**
         * Checks if messeges is null and instantiates a new arrayList to post the value.
         * This resolves the NullPointer exception thrown for null object references.
         */
        if(messages == null)
        {
            messages = new ArrayList<>();
            chatModel.messages.postValue( messages );
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