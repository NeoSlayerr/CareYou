package com.example.pkb2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Schedule extends AppCompatActivity implements DatePickerListener {
    LinearLayout data1, doctor;
    TextView patient_name, age, gender, problem, schedule_list;
    DatePickerDialog picker;
    EditText eText;
    TextView schedule, doctor_name, date, time;
    String schedule1;
    String inDate, username;
    final Calendar cldr = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        if(getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFFFF"));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle(Html.fromHtml("<font color=\"black\">"+"Schedule" + "</font>"));

        username = getIntent().getExtras().getString("username");

        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        // find the picker
        HorizontalPicker picker2 = (HorizontalPicker) findViewById(R.id.datePickerSchedule);

        // initialize it and attach a listener
        picker2
                .setListener(this)
                .showTodayButton(false)
                .setOffset(3)
                .init();

        picker2.setDate(new DateTime().withDate(year, (month+1), day));

        eText=(EditText) findViewById(R.id.editTextSchedule);
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
                picker = new DatePickerDialog(Schedule.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year2, int month2, int day2) {
                                cldr.set(year2, month2, day2);
                                SimpleDateFormat cek = new SimpleDateFormat("yyyy-MM-dd");
                                schedule1 = (cek.format(cldr.getTime())).toString();
                                eText.setText(sdf.format(cldr.getTime()));
                                cldr.set(year2, month2+1, day2);
                                picker2.setDate(new DateTime().withDate(year2, month2+1, day2));
                                inDate = year2+"-"+month2+"-"+day2;
                                getJSON(DbContract.SERVER_GET_URL_APPOINTMENT);
                            }
                        }, year, month-1, day);
                picker.getDatePicker().setMinDate(c.getTimeInMillis());
                picker.show();

            }
        });

        patient_name = findViewById(R.id.patient_name);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        problem = findViewById(R.id.problem);
        schedule_list = findViewById(R.id.schedule_list);
        data1 = findViewById(R.id.data1);
        doctor = findViewById(R.id.doctor);
        doctor_name = findViewById(R.id.doctor_name);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);


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
        SimpleDateFormat cek = new SimpleDateFormat("yyyy-MM-dd");
        schedule1 = (cek.format(cldr.getTime())).toString();
        eText.setText(sdf.format(cldr.getTime()));
        getJSON(DbContract.SERVER_GET_URL_APPOINTMENT);
        cldr.set(year, month+1, day);

        //database ambil date dari database
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

        boolean check = false;
        for (int i = 0; i < jsonArray.length(); i++) {



            JSONObject obj = jsonArray.getJSONObject(i);
            if(obj.getString("inDate").equals(schedule1) && obj.getString("username").equals(username)) {
                check = true;


                patient_name.setText(obj.getString("inName"));
                age.setText(obj.getString("inAge"));
                gender.setText(obj.getString("inGender"));
                problem.setText(obj.getString("inProblem"));
                doctor_name.setText(obj.getString("doctor_name"));
                date.setText(obj.getString("inDate"));
                time.setText(obj.getString("inTime"));

            }
//            dataDoctorArrayList.add(new DataDoctor(obj.getString("doctorName"), obj.getString("speciality"), obj.getString("about"), obj.getString("profile_pic"), obj.getString("pic")));
        }

        if (check) {
            schedule_list.setVisibility(View.GONE);
            data1.setVisibility(View.VISIBLE);
            doctor.setVisibility(View.VISIBLE);
        }else {
            schedule_list.setVisibility(View.VISIBLE);
            data1.setVisibility(View.GONE);
            doctor.setVisibility(View.GONE);
        }

    }
}