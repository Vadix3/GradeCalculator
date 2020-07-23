package com.example.gradecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;

public class MainActivity extends AppCompatActivity {

    ListView gradesList;
    Button addButton;
    String[] subjects = {"Linear Algebra", "Calculus1", "Calculus2", "Physics1"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton = findViewById((R.id.main_BTN_addButton));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gradesList = findViewById(R.id.main_LST_gradesList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.activityListview_LBL_textView);
        gradesList.setAdapter(adapter);
    }
}