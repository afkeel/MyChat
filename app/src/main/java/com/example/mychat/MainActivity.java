package com.example.mychat;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mEditTextMessage;

    RecyclerView mMessagesRecycler;
    List<Post> messages = new ArrayList<>();

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    FirebaseStorage mStorage;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Views
        mEditTextMessage = findViewById(R.id.message_input);
        mMessagesRecycler = findViewById(R.id.messages_recycler);
        // Buttons
        findViewById(R.id.send_message_b).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);
        findViewById(R.id.btnMenu).setOnClickListener(this);
        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // менеджер компоновки для управления позиционированием элементов
        mMessagesRecycler.setLayoutManager(new LinearLayoutManager(this));
        // инициализация и использование адаптера
        final DateAdapter dateAdapter = new DateAdapter(this, messages);
        mMessagesRecycler.setAdapter(dateAdapter);

        // добавляем из бд сообщения в массив
        mDatabase.child("messages").addChildEventListener(new ChildEventListener() {
            // слушаем новые сообщения
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Post post = dataSnapshot.getValue(Post.class);
                messages.add(new Post(post.uid, post.username, post.msg));
                dateAdapter.notifyDataSetChanged();
                mMessagesRecycler.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        String userID = requireNonNull(mAuth.getCurrentUser()).getUid();

        Uri file = Uri.fromFile(new File("C:/mychat/icon_user.png"));
        StorageReference riversRef = mStorageRef.child("users").child(userID).child("" + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });*/
    }
    // слушаем кнопки
    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.signOutButton){

            signOut();
        } else if (i == R.id.btnMenu) {

            startActivity(new Intent(MainActivity.this, MenuActivity.class));
        } else if (i == R.id.send_message_b){
            // получаем текст
            final String msg = mEditTextMessage.getText().toString();
            // если текста нет
            if (msg.equals("")) {

                Toast.makeText(getApplicationContext(), "Введите сообщение!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // длина сообщения не больше MAX_MESSAGE_LENGTH
            int MAX_MESSAGE_LENGTH = 50;
            if (msg.length() > MAX_MESSAGE_LENGTH) {

                Toast.makeText(getApplicationContext(), "Максимальная длина сообщения 50 символов",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // добавляем сообщение в бд
            final String userID = requireNonNull(mAuth.getCurrentUser()).getUid();
            mDatabase.child("users").child(userID).child("userdata").addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                    User user_arr = dataSnapshot.getValue(User.class);
                    String username = user_arr.username;
                    writeNewPost(userID, username, msg);
                    mEditTextMessage.setText("");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    // запись нового сообщения в бд
    private void writeNewPost(String userId, String username, String msg) {

        Map<String, Object> childUpdates = new HashMap<>();
        // берем уникальный ключ для сообщения
        String key = mDatabase.child("messages").push().getKey();
        // создаем сообщение через конструктор класса
        Post post = new Post(userId, username, msg);
        // добавляем сообщение через метод класса в HashMap
        Map<String, Object> postValues = post.toMap();
        // добавлем все postValues в HashMap
        childUpdates.put("/messages/" + key, postValues);
        childUpdates.put("/users/" + userId + "/message/" + key, postValues);
        // записываем в бд
        mDatabase.updateChildren(childUpdates);
    }
    // выход
    private void signOut() {

        mAuth.signOut();
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }
}
