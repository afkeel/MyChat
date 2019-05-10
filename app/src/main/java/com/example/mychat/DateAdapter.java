package com.example.mychat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

// адаптер для RecyclerView
public class DateAdapter extends RecyclerView.Adapter<ViewHolder> {

    private  List<Post> messages;
    private LayoutInflater inflater;

    public DateAdapter(Context context, List<Post> messages) {

        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.item_message, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post messages_position = messages.get(position);

        String msg = messages_position.getMsg();
        String uid = messages_position.getUid();
        holder.message.setText(msg);
        holder.name.setText(uid);
    }

    @Override
    public int getItemCount() {

        return messages.size();
    }
}