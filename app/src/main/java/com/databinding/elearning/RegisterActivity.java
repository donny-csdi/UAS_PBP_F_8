package com.databinding.elearning;

import static com.databinding.elearning.MyApplication.CHANNEL_1_ID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_PICTURE = 1;

    EditText nama, alamat, hp, password, email;
    CircleImageView picture;
    Button register;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextView login;
    String userID;
    private NotificationManagerCompat notificationManager;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        notificationManager = NotificationManagerCompat.from(this);

        picture = findViewById(R.id.register_picture);
        nama = findViewById(R.id.nama_lengkap);
        alamat = findViewById(R.id.alamat);
        hp = findViewById(R.id.no_hp);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginAkun);

        register = findViewById(R.id.createAkun);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(RegisterActivity.this);
                View selectMediaView = layoutInflater
                        .inflate(R.layout.layout_select_media, null);

                final AlertDialog alertDialog = new AlertDialog
                        .Builder(selectMediaView.getContext()).create();

                Button btnKamera = selectMediaView.findViewById(R.id.btn_kamera);
                Button btnGaleri = selectMediaView.findViewById(R.id.btn_galeri);

                btnKamera.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) ==
                                PackageManager.PERMISSION_DENIED) {
                            String[] permission = {Manifest.permission.CAMERA};
                            requestPermissions(permission, PERMISSION_REQUEST_CAMERA);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_REQUEST);
                        }

                        alertDialog.dismiss();
                    }
                });

                btnGaleri.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALLERY_PICTURE);

                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(selectMediaView);
                alertDialog.show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString().trim();
                String Pass = password.getText().toString().trim();
                String Nama = nama.getText().toString();
                String Hp = hp.getText().toString();
                String Address = alamat.getText().toString();

                if(TextUtils.isEmpty(Email)){
                    email.setError("Email harus diisi");
                    return;
                }

                if(TextUtils.isEmpty(Pass)){
                    password.setError("Password harus diisi");
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Berhasil Daftar.", Toast.LENGTH_SHORT).show();
                            userID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fullName",Nama);
                            user.put("phone",Hp);
                            user.put("address",Address);
                            user.put("email",Email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }else{
                            Toast.makeText(RegisterActivity.this, "Error ! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            } else {
                Toast.makeText(RegisterActivity.this, "Permission denied.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
            return;

        if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            Uri selectedImage = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            bitmap = (Bitmap) data.getExtras().get("data");
        }

        bitmap = getResizedBitmap(bitmap, 512);
        picture.setImageBitmap(bitmap);
    }

    private Bitmap getResizedBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public void sendOnChannel1(View view) {
            String title = "Register Berhasil";
            String message = "Selamat Akun anda berhasil dibuat";

            Notification notification = new NotificationCompat.Builder(this,CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_baseline_looks_two_24)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();
            notificationManager.notify(1,notification);
        }
}