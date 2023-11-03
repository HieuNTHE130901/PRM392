package com.example.groceryapplication.activities;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapplication.R;
import com.example.groceryapplication.adapter.ChatMessageAdapter;
import com.example.groceryapplication.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import com.example.groceryapplication.models.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {

    private ChatMessageAdapter adapter;
    private EditText messageInput;
    private Button sendMessageButton;
    private RecyclerView recyclerView;
    private String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageInput = findViewById(R.id.messageInput);
        sendMessageButton = findViewById(R.id.sendButton);
        recyclerView = findViewById(R.id.recyclerView);

        setupRecyclerView();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageInput.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendChatMessage(message);
                }
            }
        });
    }

    private String getchatRoom() {
        return FirebaseUtil.currentUserId();
    }
    private void setupRecyclerView() {
        Query query = db.collection("chatMessages")
                .document(getchatRoom()) // Reference the specific chat room
                .collection("chat")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();

        adapter = new ChatMessageAdapter(options);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void sendChatMessage(String message) {
        // Create a new ChatMessage object with the current user's ID and the message
        ChatMessage chatMessage = new ChatMessage(message, currentUserId, Timestamp.now());
        // Replace "room1" with your chat room identifier
        String chatRoom = getchatRoom();

        // Add the message to Firestore under the specified chat room
        db.collection("chatMessages")
                .document(chatRoom) // Use the chat room identifier as the document ID
                .collection("chat") // Create a subcollection named "chat" for chat messages
                .add(chatMessage)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // Message sent successfully
                            messageInput.setText(""); // Clear the input field
                            recyclerView.scrollToPosition(0);
                        } else {
                            // Handle the error
                            // You can display an error message or log the error here
                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
