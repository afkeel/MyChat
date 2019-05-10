package com.example.mychat;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

class ViewHolder extends RecyclerView.ViewHolder {

    TextView message;
    TextView name;
    ViewHolder(View itemView){

        super(itemView);
        message = itemView.findViewById(R.id.person_message);
        name = itemView.findViewById(R.id.person_name);
    }
}
