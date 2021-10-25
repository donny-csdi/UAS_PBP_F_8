package com.databinding.elearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.databinding.elearning.list.BindoActivity;
import com.databinding.elearning.list.BingActivity;
import com.databinding.elearning.list.MtkActivity;
import com.databinding.elearning.list.PpknActivity;
import com.databinding.elearning.list.SejarahActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MateriActivity extends AppCompatActivity {
    private Button buttonMath;
    private Button buttonBing;
    private Button buttonBindo;
    private Button buttonSejarah;
    private Button buttonPpkn;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);

        buttonMath = findViewById(R.id.matematika);
        buttonBing = findViewById(R.id.english);
        buttonBindo = findViewById(R.id.indonesia);
        buttonSejarah= findViewById(R.id.sejarah);
        buttonPpkn = findViewById(R.id.ppkn);
        bottomNavigationView = findViewById(R.id.bottom_navigator);

        bottomNavigationView.setSelectedItemId(R.id.materi);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfilActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.materi:
                        return true;
                }
                return false;
            }
        });

        buttonMath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MateriActivity.this, MtkActivity.class);
                startActivity(intent);
            }
        });

        buttonBing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MateriActivity.this, BingActivity.class);
                startActivity(intent);
            }
        });

        buttonBindo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MateriActivity.this, BindoActivity.class);
                startActivity(intent);
            }
        });

        buttonSejarah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MateriActivity.this, SejarahActivity.class);
                startActivity(intent);
            }
        });

        buttonPpkn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MateriActivity.this, PpknActivity.class);
                startActivity(intent);
            }
        });
    }

}