package com.example.pkb2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Appointment extends AppCompatActivity implements DatePickerListener{
    String[] time = {"09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "12:00 AM", "12:30 AM", "02:00 PM", "03:00 PM", "04:00 PM", "05:30 PM"};
    String[] age = {"Toddler", "5-10", "10-20", "20-30", "30-40", "40-50", "50-60", "Elder"};
    String[] gender = {"Male", "Female"};
    DatePickerDialog picker;
    EditText eText, problem, name;
    Button appointment_button;
    String inDate, inTime, inName, inAge, inGender, inProblem;
    final Calendar cldr = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFFFF"));


        String username = getIntent().getExtras().getString("username");
        String userFullName = getIntent().getExtras().getString("user_full_name");
        String doctor_name = getIntent().getExtras().getString("doctor_name");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle(Html.fromHtml("<font color=\"black\">"+"New Appointment" + "</font>"));

        Spinner spin_time = (Spinner) findViewById(R.id.edit_time);
        ArrayAdapter<String> adapter_time = new ArrayAdapter<String>(this, R.layout.spinner_text, time);
        adapter_time.setDropDownViewResource(R.layout.spinner_dropdown);
        spin_time.setAdapter(adapter_time);
        spin_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inTime=time[position].toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spin_age = (Spinner) findViewById(R.id.edit_age);
        ArrayAdapter<String> adapter_age = new ArrayAdapter<String>(this, R.layout.spinner_text, age);
        adapter_age.setDropDownViewResource(R.layout.spinner_dropdown);
        spin_age.setAdapter(adapter_age);
        spin_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inAge=age[position].toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spin_gender = (Spinner) findViewById(R.id.edit_gender);
        ArrayAdapter<String> adapter_gender = new ArrayAdapter<String>(this, R.layout.spinner_text, gender);
        adapter_gender.setDropDownViewResource(R.layout.spinner_dropdown);
        spin_gender.setAdapter(adapter_gender);
        spin_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inGender=gender[position].toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        // find the picker
        HorizontalPicker picker2 = (HorizontalPicker) findViewById(R.id.datePicker);

        // initialize it and attach a listener
        picker2
                .setListener(this)
                .showTodayButton(false)
                .setOffset(3)
                .init();

        picker2.setDate(new DateTime().withDate(year, (month+1), day));

        eText=(EditText) findViewById(R.id.editText1);
        eText.setInputType(InputType.TYPE_NULL);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyyy");
        eText.setText(sdf.format(cldr.getTime()));
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Appointment.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year2, int month2, int day2) {
                                cldr.set(year2, month2, day2);
                                eText.setText(sdf.format(cldr.getTime()));
                                cldr.set(year2, month2+1, day2);
                                picker2.setDate(new DateTime().withDate(year2, month2+1, day2));
                                inDate = year2+"-"+month2+"-"+day2;
                            }
                        }, year, month-1, day);
                picker.getDatePicker().setMinDate(c.getTimeInMillis());
                picker.show();

            }
        });

        appointment_button = findViewById(R.id.appointment_button);

        //masuki database
        appointment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                problem = findViewById(R.id.edit_problem);
                name = findViewById(R.id.edit_name);
                inName = name.getText().toString();
                inProblem = problem.getText().toString();

                if(inName.matches("")|| inProblem.matches("") || inDate.matches("") || inAge.matches("") || inGender.matches("") || inProblem.matches("") || username.matches("") || doctor_name.matches("")) {
                    Toast toast1 = Toast.makeText(Appointment.this, "Data Tidak Boleh Kosong!", Toast.LENGTH_SHORT);
                    toast1.show();
                }else {
                    //INSERT INTO TABLE
                    //inDate, inTime, inName, inAge, inGender, inProblem, username, doctor_name;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_GET_URL_ADD_APPOINTMENT,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Intent intent = new Intent(Appointment.this, Success.class);
                                    Toast toast1 = Toast.makeText(Appointment.this, "Appointment Berhasil Disimpan!", Toast.LENGTH_SHORT);
                                    toast1.show();
                                    intent.putExtra("username", username);
                                    intent.putExtra("user_full_name", userFullName);
                                    startActivity(intent);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast1 = Toast.makeText(Appointment.this, "Appointment Gagal Disimpan!", Toast.LENGTH_SHORT);
                            toast1.show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("inDate",inDate);
                            params.put("inTime",inTime);
                            params.put("inAge",inAge);
                            params.put("inGender",inGender);
                            params.put("inProblem",inProblem);
                            params.put("username",username);
                            params.put("doctor_name",doctor_name);
                            params.put("inName",inName);
                            return params;
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(Appointment.this);
                    queue.add(stringRequest);
//                  pindah ke halaman sukses
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        inDate = dateSelected.toString().substring(0,10);
        int day = Integer.parseInt(inDate.substring(8,10));
        int month = Integer.parseInt(inDate.substring(5,7))-1;
        int year = Integer.parseInt(inDate.substring(0,4));
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyyy");
        cldr.set(year, month, day);
        eText.setText(sdf.format(cldr.getTime()));
        cldr.set(year, month+1, day);
    }

}
