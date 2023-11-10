package com.example.groceryapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.groceryapplication.R;
import com.example.groceryapplication.models.ChatMessage;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class ChatMessageAdapter extends FirestoreRecyclerAdapter<ChatMessage, ChatMessageAdapter.ChatMessageViewHolder> {

    Context context;

    public ChatMessageAdapter(@NonNull FirestoreRecyclerOptions<ChatMessage> options, Context context) {
        super(options);
        this.context = context;
    }

    // Bind the data to the view holder
    @Override
    protected void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position, @NonNull ChatMessage model) {
        // Check if the message was sent by the current user
        if (model.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            // If the message was sent by the current user,
            // hide the left chat layout and show the right chat layout
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            // Set the message text in the right chat layout
            holder.rightChatTextview.setText(model.getMessage());
        } else {
            // If the message was sent by the admin,
            // hide the right chat layout and show the left chat layout
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            // Set the message text in the left chat layout
            holder.leftChatTextview.setText(model.getMessage());
        }
    }

    // Create a new view holder
    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single chat message item
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_message, parent, false);
        return new ChatMessageViewHolder(view);
    }

    // View holder class to hold references to UI elements for each chat message
    class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextview, rightChatTextview;

        public ChatMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize references to the UI elements in the chat message item layout
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);
        }
    }
}
