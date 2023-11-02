package com.example.groceryapplication.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapplication.R;
import com.example.groceryapplication.adapter.ChatAdapter;
import com.example.groceryapplication.models.ChatMessage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private EditText messageEditText;
    private Button sendButton;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    private FirebaseFirestore firestore;
    private CollectionReference chatCollectionRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        chatCollectionRef = firestore.collection("chat_messages");

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        messageEditText = root.findViewById(R.id.edit_text_message);
        sendButton = root.findViewById(R.id.button_send);
        chatRecyclerView = root.findViewById(R.id.recycler_view_chat);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        chatRecyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        listenForNewMessages();

        return root;
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            ChatMessage chatMessage = new ChatMessage(messageText);

            chatCollectionRef.add(chatMessage)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            messageEditText.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(requireContext(), "Failed to send message", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void listenForNewMessages() {
        chatCollectionRef.orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(requireContext(), "Error listening for new messages", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (DocumentChange documentChange : snapshots.getDocumentChanges()) {
                            QueryDocumentSnapshot document = documentChange.getDocument();
                            if (document.exists()) {
                                ChatMessage chatMessage = document.toObject(ChatMessage.class);
                                chatMessages.add(chatMessage);
                                chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                            }
                        }
                    }
                });
    }
}