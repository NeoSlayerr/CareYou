package com.example.pkb2021;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);


        String username = getIntent().getExtras().getString("username");
        String userFullName = getIntent().getExtras().getString("user_full_name");

        TextView welcome = findViewById(R.id.welcome);
        welcome.setText("Welcome "+userFullName);

        TextView profil = findViewById(R.id.profil);
        TextView schedule = findViewById(R.id.schedule);
        CardView doctor = findViewById(R.id.problem);
        CardView neurology = findViewById(R.id.neurology);
        CardView dentistry = findViewById(R.id.dentistry);
        CardView genetics = findViewById(R.id.genetics);
        CardView surgery = findViewById(R.id.surgery);



        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, doctors.class);
                intent.putExtra("title", "Doctors");
                intent.putExtra("path", DbContract.SERVER_GET_URL);
                intent.putExtra("username", username);
                intent.putExtra("user_full_name", userFullName);
                startActivity(intent);
            }
        });

        neurology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, doctors.class);
                intent.putExtra("title", "Neurology");
                intent.putExtra("path", DbContract.SERVER_GET_URL_NEUROLOGY);
                intent.putExtra("username", username);
                intent.putExtra("user_full_name", userFullName);
                startActivity(intent);
            }
        });

        dentistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, doctors.class);
                intent.putExtra("title", "Dentistry");
                intent.putExtra("path", DbContract.SERVER_GET_URL_DENTISTRY);
                intent.putExtra("username", username);
                intent.putExtra("user_full_name", userFullName);
                startActivity(intent);
            }
        });

        genetics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, doctors.class);
                intent.putExtra("title", "Genetics");
                intent.putExtra("path", DbContract.SERVER_GET_URL_GENETICS);
                intent.putExtra("username", username);
                intent.putExtra("user_full_name", userFullName);
                startActivity(intent);
            }
        });

        surgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, doctors.class);
                intent.putExtra("title", "Surgery");
                intent.putExtra("path", DbContract.SERVER_GET_URL_SURGERY);
                intent.putExtra("username", username);
                intent.putExtra("user_full_name", userFullName);
                startActivity(intent);
            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, profile.class);
                intent.putExtra("username", username);
                intent.putExtra("user_full_name", userFullName);
                startActivity(intent);
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, Schedule.class);
                intent.putExtra("username", username);
                intent.putExtra("user_full_name", userFullName);
                startActivity(intent);
            }
        });
    }
}