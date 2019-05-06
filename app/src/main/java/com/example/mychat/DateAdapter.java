package com.example.mychat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

// адаптер для RecyclerView
public class DateAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<String> messages;
    //List<Person> persons;
    private LayoutInflater inflater;

    DateAdapter(Context context, ArrayList<String> messages) {

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

        String msg = messages.get(position);
        //String name = persons.get(position).name;
        //int photoId = persons.get(position).photoId;

        holder.message.setText(msg);
        //holder.name.setText(name);
        //holder.photo.setImageResource(photoId);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
