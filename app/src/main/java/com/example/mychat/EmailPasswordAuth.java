package com.example.mychat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailPasswordAuth extends AppCompatActivity implements View.OnClickListener{

    private EditText mEnter_email;
    private EditText mEnter_password;
    private TextView mStatusTextView;
    private TextView mDetailTextView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_pass_auth);
        // Views
        mEnter_email = findViewById(R.id.fieldEmail);
        mEnter_password = findViewById(R.id.fieldPassword);
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);
        // Buttons
        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.emailCreateAccountButton).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);
        //Firebase
        mAuth = FirebaseAuth.getInstance();
    }
    // утсанавливаем текущее состаяние пользователя
    @Override
    public void onStart() {

        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
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

                            Toast.makeText(EmailPasswordAuth.this,"Регистрация успешна",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {

                            Toast.makeText(EmailPasswordAuth.this,"Не удалось зарегистрироваться",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
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

                            Toast.makeText(EmailPasswordAuth.this,"Авторизация успешна",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {

                            Toast.makeText(EmailPasswordAuth.this,"Не верный логин или пароль",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
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
    // выход
    private void signOut() {

        mAuth.signOut();
        updateUI(null);
    }
    // вывод сообщений и видимость кнопок
    private void updateUI(FirebaseUser user) {

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
    }
    //работа кнопок
    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.emailSignInButton) {

            signIn(mEnter_email.getText().toString(), mEnter_password.getText().toString());

        } else if (i == R.id.emailCreateAccountButton){

            regIn(mEnter_email.getText().toString(), mEnter_password.getText().toString());

        } else if (i == R.id.signOutButton){

            signOut();
        }
    }
}