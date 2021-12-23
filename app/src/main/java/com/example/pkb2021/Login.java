package com.example.pkb2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Login extends AppCompatActivity {
    TextView linkTextView, register, fp;

    private EditText username1,password1;
    private String username, user_full_name, usr, pwd;
    private Boolean login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        linkTextView = findViewById(R.id.register);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        register = findViewById(R.id.register);
        fp = findViewById(R.id.forgetpassword);

        TextView button = findViewById(R.id.btnLogin);

        login = false;

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, forgetPassword.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loginPost(v);
                Toast toast = Toast.makeText(v.getContext(), "Tunggu Sebentar", Toast.LENGTH_SHORT);
                toast.show();

                username1 = findViewById(R.id.inputUsername);
                password1 = findViewById(R.id.inputpw);

                if(username1.getText().toString().matches("") || password1.getText().toString().matches("")) {
                    Toast toast1 = Toast.makeText(v.getContext(), "Data Tidak Boleh Kosong!", Toast.LENGTH_SHORT);
                    toast1.show();
                }else {
                    getJSON(DbContract.SERVER_GET_URL_USER);
                }
            }
        });
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
                    checkData(s);
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

    private void checkData(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        usr = username1.getText().toString();
        pwd = password1.getText().toString();

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = jsonArray.getJSONObject(i);

            if(usr.equals(obj.getString("username")) && pwd.equals(obj.getString("password"))) {
                    login = true;
                    username = obj.getString("username");
                    user_full_name = obj.getString("user_full_name");
            }
        };

        if(login == true) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Login.this, home.class);
                    intent.putExtra("username", username);
                    intent.putExtra("user_full_name", user_full_name);
                    startActivity(intent);
                    login = false;
                    Toast toast1 = Toast.makeText(Login.this, "Login Berhasil", Toast.LENGTH_SHORT);
                    toast1.show();
                    finish();
                }
            }, 1000L);
        }else {
            Toast toast1 = Toast.makeText(Login.this, "Username Atau Password Salah!", Toast.LENGTH_SHORT);
            toast1.show();
        }
    }
}