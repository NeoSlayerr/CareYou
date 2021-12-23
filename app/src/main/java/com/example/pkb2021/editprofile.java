package com.example.pkb2021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class editprofile extends AppCompatActivity {

    EditText inputName, inputGender, inputAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);



        String username = getIntent().getExtras().getString("username");
        String userFullName = getIntent().getExtras().getString("user_full_name");

        ImageView back = findViewById(R.id.back);
        TextView edit = findViewById(R.id.save);

        TextView full1 = findViewById(R.id.full);
        TextView userName = findViewById(R.id.username);
        userName.setText(username);
        full1.setText(userFullName);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(editprofile.this, profile.class);
                intent.putExtra("username", username);
                intent.putExtra("user_full_name", userFullName);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputName = findViewById(R.id.input_name);
                inputGender = findViewById(R.id.input_gender);
                inputAge = findViewById(R.id.input_age);

                String inName = inputName.getText().toString();
                String inGender = inputGender.getText().toString();
                String inAge = inputAge.getText().toString();

                if(inName.matches("")|| inGender.matches("") || inAge.matches("")) {
                    Toast toast1 = Toast.makeText(editprofile.this, "Data Tidak Boleh Kosong!", Toast.LENGTH_SHORT);
                    toast1.show();
                }else {
                    //INSERT INTO TABLE
                    //inDate, inTime, inName, inAge, inGender, inProblem, username, doctor_name;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_GET_URL_UPDATE_PROFILE,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast toast1 = Toast.makeText(editprofile.this, "Data berhasil diupdate!!", Toast.LENGTH_SHORT);
                                    toast1.show();

                                    Intent intent = new Intent(editprofile.this, profile.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("user_full_name", inName);
                                    startActivity(intent);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast1 = Toast.makeText(editprofile.this, "Data Gagal Disimpan!", Toast.LENGTH_SHORT);
                            toast1.show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("inName", inName);
                            params.put("inGender", inGender);
                            params.put("inAge", inAge);
                            params.put("username", username);
                            return params;

                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(editprofile.this);
                    queue.add(stringRequest);
//                  pindah ke halaman sukses
                }
            }
        });
    }
}