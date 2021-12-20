package com.databinding.elearning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class EditActivity extends AppCompatActivity {
    EditText email, nama, alamat, noHp;
    Button edit;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID, DISPLAY_NAME = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        email = findViewById(R.id.email);
        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);
        noHp = findViewById(R.id.noHp);

        edit = findViewById(R.id.updateAkun);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                email.setText(value.getString("email"));
                nama.setText(value.getString("fullName"));
                alamat.setText(value.getString("address"));
                noHp.setText(value.getString("phone"));
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DISPLAY_NAME = nama.getText().toString();

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                        .setDisplayName(DISPLAY_NAME)
                        .build();

                firebaseUser.updateProfile(request)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditActivity.this, "Update Berhasil", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditActivity.this, "Update Gagal", Toast.LENGTH_SHORT).show();
                            }
                        });
                firebaseUser.updateEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditActivity.this, "Email berhasil diUpdate.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}