package com.example.mychat;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.view.View;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView message;
    public TextView name;

    public ViewHolder(View itemView){

        super(itemView);
        message = itemView.findViewById(R.id.person_message);
        name = itemView.findViewById(R.id.person_name);
    }
}
