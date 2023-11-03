package com.example.groceryapplication.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.groceryapplication.models.ChatMessage;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.groceryapplication.R;


public class ChatMessageAdapter extends FirestoreRecyclerAdapter<ChatMessage, ChatMessageAdapter.ChatMessageViewHolder> {

    public ChatMessageAdapter(@NonNull FirestoreRecyclerOptions<ChatMessage> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position, @NonNull ChatMessage model) {
        // Bind the chat message to the view
        holder.setMessageText(model.getMessage());
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatMessageViewHolder(view);
    }

    public class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageText;


        public ChatMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageTextView);
        }

        public void setMessageText(String message) {
            messageText.setText(message);
        }
    }
}
