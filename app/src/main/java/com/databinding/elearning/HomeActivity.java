package com.databinding.elearning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.databinding.elearning.list.BindoActivity;
import com.databinding.elearning.list.BingActivity;
import com.databinding.elearning.list.MtkActivity;
import com.databinding.elearning.list.PpknActivity;
import com.databinding.elearning.list.SejarahActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private ProgressBar mProgressBar1;
    private ProgressBar mProgressBar2;
    private ProgressBar mProgressBar3;
    private ProgressBar mProgressBar4;
    private ProgressBar mProgressBar5;

    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;
    private TextView mTextView5;
    private TextView namaUser;

    private ImageView math;
    private ImageView english;
    private ImageView indo;
    private ImageView sejarah;
    private ImageView ppkn;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;


    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mProgressBar1 = (ProgressBar) findViewById(R.id.barmatematika);
        mProgressBar2 = (ProgressBar) findViewById(R.id.baringgris);
        mProgressBar3 = (ProgressBar) findViewById(R.id.barindonesia);
        mProgressBar4 = (ProgressBar) findViewById(R.id.barsejarah);
        mProgressBar5 = (ProgressBar) findViewById(R.id.barppkn);

        mTextView1 = (TextView) findViewById(R.id.persenmatematika);
        mTextView2 = (TextView) findViewById(R.id.perseninggris);
        mTextView3 = (TextView) findViewById(R.id.persenindonesia);
        mTextView4 = (TextView) findViewById(R.id.persensejarah);
        mTextView5 = (TextView) findViewById(R.id.persenppkn);
        namaUser = findViewById(R.id.bannerNama);

        math = findViewById(R.id.mtk);
        english = findViewById(R.id.eng);
        indo = findViewById(R.id.bindo);
        sejarah = findViewById(R.id.history);
        ppkn = findViewById(R.id.pkn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                namaUser.setText("Hai " + value.getString("fullName"));
            }
        });

        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MtkActivity.class);
                startActivity(intent);
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BingActivity.class);
                startActivity(intent);
            }
        });

        indo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BindoActivity.class);
                startActivity(intent);
            }
        });

        sejarah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SejarahActivity.class);
                startActivity(intent);
            }
        });

        ppkn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PpknActivity.class);
                startActivity(intent);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mProgressStatus <100){
                    mProgressStatus = 50;
                    android.os.SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar1.setProgress(mProgressStatus);
                            mProgressBar2.setProgress(mProgressStatus);
                            mProgressBar3.setProgress(mProgressStatus);
                            mProgressBar4.setProgress(mProgressStatus);
                            mProgressBar5.setProgress(mProgressStatus);
                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTextView1.setVisibility(View.VISIBLE);
                        mTextView2.setVisibility(View.VISIBLE);
                        mTextView3.setVisibility(View.VISIBLE);
                        mTextView4.setVisibility(View.VISIBLE);
                        mTextView5.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.home);
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