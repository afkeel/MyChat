package com.example.mychat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mEnter_email;
    private EditText mEnter_password;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    public ImageView mImageView;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseStorage mStorage;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Views
        mEnter_email = findViewById(R.id.fieldEmail);
        mEnter_password = findViewById(R.id.fieldPassword);
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);
        mImageView = findViewById(R.id.img_Ic_start);
        // Buttons
        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.emailCreateAccountButton).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);
        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
    }
    // утсанавливаем текущее состаяние пользователя
    @Override
    public void onStart() {

        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            // идем в MainActivity
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        }
    }
    // регистрация пользователя
    public void regIn (String email, String password){

        if (vldForm()) {

            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            regInSuccess();
                        } else {

                            Toast.makeText(SignInActivity.this,"Не удалось зарегистрироваться",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // авторизация пользователя
    public void signIn (String email, String password){

        if (vldForm()) {

            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            // идем в MainActivity
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            finish();
                        } else {

                            Toast.makeText(SignInActivity.this,"Не верный логин или пароль",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // проверяем введен ли текст в поля
    private boolean vldForm() {

        boolean vld = true;
        String email = mEnter_email.getText().toString();
        if (TextUtils.isEmpty(email)) {

            mEnter_email.setError("Введите email");
            vld = false;
        } else {

            mEnter_email.setError(null);
        }
        String password = mEnter_password.getText().toString();
        if (TextUtils.isEmpty(password)) {

            mEnter_password.setError("Введите пароль");
            vld = false;
        } else {

            mEnter_password.setError(null);
        }
        return !vld;
    }
    //работа кнопок
    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.emailSignInButton) {

            signIn(mEnter_email.getText().toString(), mEnter_password.getText().toString());

        } else if (i == R.id.emailCreateAccountButton){

            regIn(mEnter_email.getText().toString(), mEnter_password.getText().toString());
        }
    }
    // если пользователь аутентифицирован
    private void regInSuccess() {

        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        String email = mAuth.getCurrentUser().getEmail();
        String name = "NoName";
        String surname = "NoSurname";
        writeNewUser(userID, name, surname, email);
        uploadFile(userID);
        // идем в MainActivity
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }
    // запись пользователя в бд
    public void writeNewUser(String userId, String name, String surname, String email) {

        User user = new User(name, surname, email);
        mDatabase.child("users").child(userId).child("userdata").setValue(user);
    }

    public void uploadFile(String userID) {

        StorageReference mountainsRef = mStorageRef.child("users").child(userID).child("icon_user");

        Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }





    // вывод сообщений и видимость кнопок
    /*private void updateUI(FirebaseUser user) {

        if (user != null) {
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
            findViewById(R.id.signedLayout).setVisibility(View.VISIBLE);

        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
            findViewById(R.id.signedLayout).setVisibility(View.GONE);
        }
    }*/
}
