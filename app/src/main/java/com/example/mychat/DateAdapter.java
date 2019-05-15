package com.example.mychat;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

// адаптер для RecyclerView
public class DateAdapter extends RecyclerView.Adapter<ViewHolder> {

    private  List<Post> messages;
    private Context mContext;
    FirebaseStorage mStorage;
    StorageReference mStorageRef;

    public DateAdapter(Context context, List<Post> messages) {

        this.mContext = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_message, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Post messages_position = messages.get(position);

        String msg = messages_position.getMsg();
        String name = messages_position.getUsername();
        holder.message.setText(msg);
        holder.name.setText(name);

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        String uid = messages_position.getUid();

        StorageReference imageRef = mStorageRef.child("users").child(uid).child("icon_user");

        GlideApp.with(mContext)
                .load(imageRef)
                .into(holder.image);

//        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//
//            @Override
//            public void onSuccess(byte[] bytes) {
//
//                Bitmap bitmapProfile = ByteArrayToBitmap(bytes);
//
//                holder.image.setImageBitmap(bitmapProfile);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {

        return messages.size();
    }

//    public Bitmap ByteArrayToBitmap(byte[] bytes)
//    {
//        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
//        Bitmap bitmapProfile = BitmapFactory.decodeStream(arrayInputStream);
//        return bitmapProfile;
//    }
}