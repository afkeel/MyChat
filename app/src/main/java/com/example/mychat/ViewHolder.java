package com.example.mychat;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

class ViewHolder extends RecyclerView.ViewHolder {

    TextView message;
    TextView name;
    ImageView photo;
    ViewHolder(View itemView){

        super(itemView);
        message = itemView.findViewById(R.id.message_item);
        //name = itemView.findViewById(R.id.person_name);
        //photo = itemView.findViewById(R.id.person_photo);
    }
}
