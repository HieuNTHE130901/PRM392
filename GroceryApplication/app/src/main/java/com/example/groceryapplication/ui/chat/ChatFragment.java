package com.example.groceryapplication.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.groceryapplication.R;
import com.example.groceryapplication.adapter.ChatMessageAdapter;
import com.example.groceryapplication.models.ChatMessage;
import com.example.groceryapplication.models.ChatRoom;
import com.example.groceryapplication.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class ChatFragment extends Fragment {

    // Member variables for UI elements and data
    EditText messageInput;
    ImageButton sendMessageBtn;
    RecyclerView recyclerView;
    String chatroomId;
    ChatMessageAdapter adapter;
    ChatRoom chatroomModel;
    String ADMIN_UID = "GJFRxk3BzygTUxgZGxzvOtCcCyr2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        // Generate a unique chatroom ID
        chatroomId = FirebaseUtil.currentUserId() + "_" + ADMIN_UID;

        // Initialize UI elements
        messageInput = root.findViewById(R.id.chat_message_input);
        sendMessageBtn = root.findViewById(R.id.message_send_btn);
        recyclerView = root.findViewById(R.id.chat_recycler_view);

        // Create or retrieve the chat room from Firebase
        getOrCreateChatroomModel();

        // Set up the chat message RecyclerView
        setupChatRecyclerView();

        // Handle sending messages when the send button is clicked
        sendMessageBtn.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            messageInput.setText("");
            if (!message.isEmpty()) {
                sendMessage(message);
            }
        });

        return root;
    }

    // Load chat messages from Firestore database and set up RecyclerView
    private void setupChatRecyclerView() {
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class).build();

        adapter = new ChatMessageAdapter(options, requireContext());
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        // Scroll to the latest message when a new message is added
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    // Send a new chat message
    private void sendMessage(String message) {
        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastSenderId(FirebaseUtil.currentUserId());
        chatroomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);

        ChatMessage chatMessageModel = new ChatMessage(message, FirebaseUtil.currentUserId(), Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        messageInput.setText("");
                    }
                });
    }

    // Join an existing chat room in the Firestore database or create a new one
    private void getOrCreateChatroomModel() {
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatroomModel = task.getResult().toObject(ChatRoom.class);
                if (chatroomModel == null) {
                    // If the chat room doesn't exist, create a new one
                    chatroomModel = new ChatRoom(chatroomId,
                            Arrays.asList(FirebaseUtil.currentUserId(), ADMIN_UID),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);
                }
            }
        });
    }
}
