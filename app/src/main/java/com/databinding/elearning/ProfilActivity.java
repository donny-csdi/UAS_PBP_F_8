package com.databinding.elearning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class ProfilActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView profil;
    TextView nama, alamat, nohp, email;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Button logout;
    Button edit;
    String userID,pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        nama = findViewById(R.id.namaProfil);
        alamat = findViewById(R.id.alamatProfil);
        nohp = findViewById(R.id.noProfil);
        email = findViewById(R.id.emailProfil);
        logout = findViewById(R.id.logout);
        edit = findViewById(R.id.change);
        profil = findViewById(R.id.imageView2);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        ListenerRegistration registration = documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                nama.setText(value.getString("fullName"));
                alamat.setText(value.getString("address"));
                nohp.setText(value.getString("phone"));
                email.setText(value.getString("email"));
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registration.remove();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.profile:
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.materi:
                        startActivity(new Intent(getApplicationContext(), MateriActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

}


