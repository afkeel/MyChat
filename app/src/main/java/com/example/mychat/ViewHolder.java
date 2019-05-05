package com.example.mychat;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.view.View;

class ViewHolder extends RecyclerView.ViewHolder {

    TextView message;
    ViewHolder(View itemView){

        super(itemView);
        message = itemView.findViewById(R.id.message_item);
    }
}
