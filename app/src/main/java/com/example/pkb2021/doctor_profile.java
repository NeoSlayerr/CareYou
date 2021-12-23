package com.example.pkb2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class doctor_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        String name = getIntent().getExtras().getString("name");
        String speciality = getIntent().getExtras().getString("occ");
        String about = getIntent().getExtras().getString("about");
        String profile_pic = getIntent().getExtras().getString("profile");
        String path = getIntent().getExtras().getString("path");
        String title = getIntent().getExtras().getString("title");
        String username = getIntent().getExtras().getString("username");
        String userFullName = getIntent().getExtras().getString("user_full_name");

        setContentView(R.layout.activity_doctor_profile);

        TextView appoint = findViewById(R.id.appoint);
        ImageView back = findViewById(R.id.imageView2);

        appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doctor_profile.this, Appointment.class);
                intent.putExtra("username", username);
                intent.putExtra("user_full_name", userFullName);
                intent.putExtra("doctor_name", name);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doctor_profile.this, doctors.class);
                intent.putExtra("path", path);
                intent.putExtra("title", title);
                intent.putExtra("username", username);
                intent.putExtra("user_full_name", userFullName);
//                Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        TextView doctorName = findViewById(R.id.textView2);
        TextView special = findViewById(R.id.textView3);
        TextView abt = findViewById(R.id.textView5);
        ImageView profile = findViewById(R.id.imageView3);

        doctorName.setText(name);
        special.setText(speciality);
        abt.setText(about);
        profile.setImageResource(getImageId(this, profile_pic));
//        Toast.makeText(getApplicationContext(), profile_pic, Toast.LENGTH_SHORT).show();
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }
}