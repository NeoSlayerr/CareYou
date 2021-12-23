package com.example.pkb2021;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import java.util.Locale;

public class doctors extends AppCompatActivity {

    CheckBox one, two, three, four;
    String[] check = {"", "", "", ""};
    GridView doctorsGV;
    TextView btn;
    GridAdapter adapter;
    ArrayList<DataDoctor> dataDoctorArrayList;
    LinearLayout list_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        String username = getIntent().getExtras().getString("username");
        String userFullName = getIntent().getExtras().getString("user_full_name");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        String title = getIntent().getExtras().getString("title");
        String path = getIntent().getExtras().getString("path");

        dataDoctorArrayList = new ArrayList<>();
        doctorsGV = findViewById(R.id.gridView);

        list_filter = findViewById(R.id.list_filter);

        TextView title1 = findViewById(R.id.title);

        btn = findViewById(R.id.btnsend);

        getJSON(path);

        title1.setText(title);

        one = (CheckBox) findViewById(R.id.one);
        two = (CheckBox) findViewById(R.id.two);
        three = (CheckBox) findViewById(R.id.three);
        four = (CheckBox) findViewById(R.id.four);

        if(title.equals("Neurology")){
            one.setText("Neuroanatomist");
            two.setText("Neurochemist");
            three.setText("Oncologist");
            four.setText("Psychiatrist");
        }else if(title.equals("Genetics")){
            one.setText("Bioinformatician");
            two.setText("Genetic Pathologist");
            three.setText("Rheumatologist");
            four.setText("Virologist");
        }else if(title.equals("Dentistry")){
            one.setText("Endodontics");
            two.setText("Orthodontist");
            three.setText("Pediatric Dentist");
            four.setText("Prosthodontics");
        }else if(title.equals("Surgery")){
            one.setText("Gastroenterologist");
            two.setText("Maxillofacial Surgeon");
            three.setText("Ophthalmic Surgeon");
            four.setText("Radiologist");
        }

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(one.isChecked()) {
                    check[0] = one.getText().toString();
                }else {
                    check[0] = "";
                }
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(two.isChecked()) {
                    check[1] = two.getText().toString();
                }else {
                    check[1] = "";
                }
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(three.isChecked()) {
                    check[2] = three.getText().toString();
                }else {
                    check[2] = "";
                }
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(four.isChecked()) {
                    check[3] = four.getText().toString();
                }else {
                    check[3] = "";
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter2(check);
            }
        });

        ImageView filter = findViewById(R.id.filters);

        if(title.equals("Doctors")) {
            filter.setVisibility(View.GONE);
        }

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list_filter.getVisibility()==View.VISIBLE) {
                    list_filter.setVisibility(View.GONE);
                }else {
                    list_filter.setVisibility(View.VISIBLE);
                }
            }
        });

        EditText search = findViewById(R.id.item_search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doctors.this, home.class);
                intent.putExtra("username", username);
                intent.putExtra("user_full_name", userFullName);
//                Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }

    private void filter(String text) {
        ArrayList<DataDoctor> filteredList = new ArrayList<>();

        String username = getIntent().getExtras().getString("username");
        String userFullName = getIntent().getExtras().getString("user_full_name");

        for (DataDoctor item : dataDoctorArrayList){
            if(item.getDoctorName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }

        adapter = new GridAdapter(doctors.this, filteredList);
        doctorsGV.setAdapter(adapter);

        String path = getIntent().getExtras().getString("path");
        String title = getIntent().getExtras().getString("title");

        doctorsGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(doctors.this, doctor_profile.class);
                    intent.putExtra("name", filteredList.get(position).getDoctorName());
                    intent.putExtra("occ", filteredList.get(position).getSpeciality());
                    intent.putExtra("about", filteredList.get(position).getAbout());
                    intent.putExtra("profile", filteredList.get(position).getPic());
                    intent.putExtra("path", path);
                    intent.putExtra("title", title);
                    intent.putExtra("username", username);
                    intent.putExtra("user_full_name", userFullName);
//                    Toast.makeText(getApplicationContext(), filteredList.get(0).toString(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
            }
        });
    }

    private void filter2(String[] check) {
        ArrayList<DataDoctor> filteredList = new ArrayList<>();

        String username = getIntent().getExtras().getString("username");
        String userFullName = getIntent().getExtras().getString("user_full_name");
        boolean test = false;

        for (DataDoctor item : dataDoctorArrayList){
            if(item.getSpeciality().equals(check[0])){
                test = true;
                filteredList.add(item);
            }
        }
        for (DataDoctor item : dataDoctorArrayList){
            if(item.getSpeciality().equals(check[1])){
                test = true;
                filteredList.add(item);
            }
        }
        for (DataDoctor item : dataDoctorArrayList){
            if(item.getSpeciality().equals(check[2])){
                test = true;
                filteredList.add(item);
            }
        }
        for (DataDoctor item : dataDoctorArrayList){
            if(item.getSpeciality().equals(check[3])){
                test = true;
                filteredList.add(item);
            }
        }

        if(test == true) {
            adapter = new GridAdapter(doctors.this, filteredList);
            doctorsGV.setAdapter(adapter);
        }else {
            adapter = new GridAdapter(doctors.this, dataDoctorArrayList);
            doctorsGV.setAdapter(adapter);
        }

        String path = getIntent().getExtras().getString("path");
        String title = getIntent().getExtras().getString("title");

        doctorsGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(doctors.this, doctor_profile.class);
                intent.putExtra("name", filteredList.get(position).getDoctorName());
                intent.putExtra("occ", filteredList.get(position).getSpeciality());
                intent.putExtra("about", filteredList.get(position).getAbout());
                intent.putExtra("profile", filteredList.get(position).getPic());
                intent.putExtra("path", path);
                intent.putExtra("title", title);
                intent.putExtra("username", username);
                intent.putExtra("user_full_name", userFullName);
//                    Toast.makeText(getApplicationContext(), filteredList.get(0).toString(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    //this method is actually fetching the json string
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
                    loadIntoGridView(s);
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

    private void loadIntoGridView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = jsonArray.getJSONObject(i);

            dataDoctorArrayList.add(new DataDoctor(obj.getString("doctorName"), obj.getString("speciality"), obj.getString("about"), obj.getString("profile_pic"), obj.getString("pic")));
        }

        adapter = new GridAdapter(doctors.this, dataDoctorArrayList);

        doctorsGV.setAdapter(adapter);

        String path = getIntent().getExtras().getString("path");
        String title = getIntent().getExtras().getString("title");
        String username = getIntent().getExtras().getString("username");
        String userFullName = getIntent().getExtras().getString("user_full_name");

        doctorsGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject obj1 = jsonArray.getJSONObject(position);
                    Intent intent = new Intent(doctors.this, doctor_profile.class);
                    intent.putExtra("name", obj1.getString("doctorName"));
                    intent.putExtra("occ", obj1.getString("speciality"));
                    intent.putExtra("about", obj1.getString("about"));
                    intent.putExtra("profile", obj1.getString("pic"));
                    intent.putExtra("path", path);
                    intent.putExtra("title", title);
                    intent.putExtra("username", username);
                    intent.putExtra("user_full_name", userFullName);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}