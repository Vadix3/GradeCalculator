package com.example.gradecalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    /**
     * Widgets
     */
    //Buttons
    Button welcomeButton;
    Button loadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        //Inits
        loadButton = (Button) findViewById(R.id.welcome_BTN_loadCourses);
        welcomeButton = (Button) findViewById(R.id.welcome_BTN_startButton);

        //Listeners
        welcomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Notify main activity to load files from device if requested
                MainActivity.loadStart = true;
                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
