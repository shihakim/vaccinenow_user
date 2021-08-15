package com.example.data33;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class search extends AppCompatActivity {
    private FirebaseDatabase database;
    Spinner spinner;
    Spinner spinner1;
    String disease_name;
    String vaccine_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findViewById(R.id.btn_search);
        database = FirebaseDatabase.getInstance();
        spinner = findViewById(R.id.disease_spinner);
        spinner1 = findViewById(R.id.vaccine_spinner);


        ArrayAdapter diseaseAdapter = ArrayAdapter.createFromResource(this, R.array.array_disease, android.R.layout.simple_spinner_dropdown_item);
        diseaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(diseaseAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                disease_name = parent.getSelectedItem().toString();
                if (disease_name.equals("코로나19")){
                    ArrayAdapter vaccineAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_covid, android.R.layout.simple_spinner_dropdown_item);
                    vaccineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(vaccineAdapter);

                }
                else if (disease_name.equals("인플루엔자(3가)")){
                    ArrayAdapter vaccineAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_3type, android.R.layout.simple_spinner_dropdown_item);
                    vaccineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(vaccineAdapter);
                }
                else if (disease_name.equals("인플루엔자(4가)")){
                    ArrayAdapter vaccineAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.array_4type, android.R.layout.simple_spinner_dropdown_item);
                    vaccineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(vaccineAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vaccine_name = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        ImageButton search_btn = (ImageButton) findViewById(R.id.search);
        search_btn.setVisibility(View.INVISIBLE);
        ImageButton nav_btn = (ImageButton) findViewById(R.id.nav_btn);
        nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(search.this,navigation.class);
                startActivity(intent);
            }
        });

        Button btn = (Button) findViewById(R.id.btn_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(search.this,search_result.class);
                intent.putExtra("vaccine_name",vaccine_name);
                startActivity(intent);
            }
        });

    }

}
