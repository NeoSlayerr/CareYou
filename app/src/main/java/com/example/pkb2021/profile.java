package com.example.pkb2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class profile extends AppCompatActivity {
    String gender, profile_pic, age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String username = getIntent().getExtras().getString("username");
        String userFullName = getIntent().getExtras().getString("user_full_name");

        ImageView back = findViewById(R.id.back);

        ImageView setting = findViewById(R.id.setting);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile.this, home.class);
                intent.putExtra("username", username);
                intent.putExtra("user_full_name", userFullName);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile.this, editprofile.class);
                intent.putExtra("username", username);
                intent.putExtra("user_full_name", userFullName);
                startActivity(intent);
            }
        });

        getJSON(DbContract.SERVER_GET_URL_USER);
    }

    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    loadData(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {
                    URL url = new URL(urlWebService);

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    StringBuilder sb = new StringBuilder();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;

                    while ((json = bufferedReader.readLine()) != null) {

                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }

        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadData(String json) throws JSONException {
        String username = getIntent().getExtras().getString("username");
        String userFullName = getIntent().getExtras().getString("user_full_name");
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = jsonArray.getJSONObject(i);
            if(obj.getString("username").equals(username)) {
                age = obj.getString("age");
                gender = obj.getString("gender");
                profile_pic = obj.getString("profile_pic");
            }

        };

        ImageView profile = findViewById(R.id.profil);
        TextView userName = findViewById(R.id.username);
        TextView full1 = findViewById(R.id.full);
        TextView full = findViewById(R.id.Input_name);
        TextView genderr = findViewById(R.id.input_gender);
        TextView agee = findViewById(R.id.input_age);

        profile.setImageResource(getImageId(this, profile_pic));
        userName.setText(username);
        full1.setText(userFullName);
        full.setText(userFullName);
        genderr.setText(gender);
        agee.setText(age);
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }
}