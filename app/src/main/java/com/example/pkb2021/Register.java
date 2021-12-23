package com.example.pkb2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Register extends AppCompatActivity {
    EditText fullname, username, password, password2;
    TextView button, login;
    String inName, inuser, p, p2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        button = findViewById(R.id.btnRegis);
        login = findViewById(R.id.login);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loginPost(v);
                Toast toast = Toast.makeText(v.getContext(), "Tunggu Sebentar", Toast.LENGTH_SHORT);
                toast.show();

                getJSON(DbContract.SERVER_GET_URL_USER);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
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

        boolean checkUsr = false;

        fullname = findViewById(R.id.inputFN);
        username = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputpw);
        password2 = findViewById(R.id.inputpw2);

        inName = fullname.getText().toString();
        inuser = username.getText().toString();
        p = password.getText().toString();
        p2 = password2.getText().toString();

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = jsonArray.getJSONObject(i);
            if (inuser.equals(obj.getString("username"))){
                checkUsr = true;
            }
        }

        if (checkUsr == true) {
            Toast toast1 = Toast.makeText(Register.this, "Username Telah Ada", Toast.LENGTH_SHORT);
            toast1.show();
        }else {
            if(inName.matches("")|| inuser.matches("") || p.matches("")|| p2.matches("")) {
                Toast toast1 = Toast.makeText(Register.this, "Data Tidak Boleh Kosong!", Toast.LENGTH_SHORT);
                toast1.show();
            }else {
                if (p.equals(p2)) {
                    //INSERT INTO TABLE
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_GET_URL_ADD_ACCOUNT,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Intent intent = new Intent(Register.this, Login.class);
                                    Toast toast1 = Toast.makeText(Register.this, "Register Berhasil", Toast.LENGTH_SHORT);
                                    toast1.show();
                                    startActivity(intent);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast1 = Toast.makeText(Register.this, "Register Gagal!", Toast.LENGTH_SHORT);
                            toast1.show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("inName", inName);
                            params.put("inuser", inuser);
                            params.put("p", p);
                            params.put("p2", p2);
                            return params;

                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(Register.this);
                    queue.add(stringRequest);
                }else {
                    Toast toast1 = Toast.makeText(Register.this, "Password Tidak Sama!", Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }
        }
    }


    public void moveLogin(View view) {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
    }

    public void moveFP(View view) {
        Intent intent = new Intent(Register.this, forgetPassword.class);
        startActivity(intent);
    }
}