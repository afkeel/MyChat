package com.example.mychat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class MenuActivity extends MainActivity implements View.OnClickListener {

    EditText mEditName;
    EditText mEditSurname;
    TextView mEditEmail;
    ImageView mIVMenu;

    FirebaseStorage mStorage;
    StorageReference mStorageRef;

    private final int Pick_image = 1;
    private Drawable mDrawable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // Views
        mEditName = findViewById(R.id.txt_Name);
        mEditSurname = findViewById(R.id.txt_Surname);
        mEditEmail = findViewById(R.id.txt_Email);
        mIVMenu = findViewById(R.id.img_View);
        // Buttons
        findViewById(R.id.btn_Edit_Image).setOnClickListener(this);
        findViewById(R.id.btn_Save_Change).setOnClickListener(this);
        // Firebase
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();

        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mDatabase.child("users").child(userID).child("userdata").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user_arr = dataSnapshot.getValue(User.class);
                if(user_arr!=null) {

                    String email = user_arr.email;
                    String name = user_arr.username;
                    String surname = user_arr.usersurname;
                    mEditEmail.setText(email);
                    mEditName.setText(name);
                    mEditSurname.setText(surname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        StorageReference imageRef = mStorageRef.child("users").child(userID).child("icon_user");

        GlideApp.with(this)
                .load(imageRef)
                .into(mIVMenu);
    }
    // слушаем кнопки
    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.btn_Save_Change) {

            String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            String email = mAuth.getCurrentUser().getEmail();
            String name = mEditName.getText().toString();
            String surname = mEditSurname.getText().toString();

            if (name.equals("") | surname.equals("")) {

                Toast.makeText(getApplicationContext(), "Введите текст!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            // длина сообщения не больше MAX_MESSAGE_LENGTH
            int MAX_TEXT_MENU_LENGTH = 15;
            if (name.length() > MAX_TEXT_MENU_LENGTH && surname.length() > MAX_TEXT_MENU_LENGTH ) {

                Toast.makeText(getApplicationContext(), "Максимальная длина сообщения 15 символов",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            writeNewUser(userID, name, surname, email);

            StorageReference mountainsRef = mStorageRef.child("users").child(userID).child("icon_user");

            // загружаем картинку в бд
            //mImageView.setDrawingCacheEnabled(true);
            //mImageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) mIVMenu.getDrawable()).getBitmap();
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

            Toast.makeText(getApplicationContext(), "Изменения сохранены",
                    Toast.LENGTH_SHORT).show();

        } else if (i == R.id.btn_Edit_Image){

            //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            //Тип получаемых объектов - image:
            photoPickerIntent.setType("image/*");
            //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
            startActivityForResult(photoPickerIntent, Pick_image);
        }
    }
    //Обрабатываем результат выбора в галерее:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {

            case Pick_image:
                if (resultCode == RESULT_OK) {

                    try {
                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        mIVMenu.setImageBitmap(selectedImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public void writeNewUser (String userId, String name, String surname, String email){

        User user = new User(name, surname, email);
        mDatabase.child("users").child(userId).child("userdata").setValue(user);
    }
}
