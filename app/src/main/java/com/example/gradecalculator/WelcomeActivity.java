package com.example.gradecalculator;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import org.w3c.dom.Text;

public class WelcomeActivity extends AppCompatActivity {
    /**
     * Widgets
     */
    //Buttons
    private Button welcomeButton;
    private Button loadButton;

    private TextView version;

    private ImageView mainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        version = (TextView) findViewById(R.id.welcome_LBL_versionLabel);
        version.setText(MainActivity.versionNum);
        mainImage = findViewById(R.id.welcome_img_mainImage);

        //Inits
        loadButton = (Button) findViewById(R.id.welcome_BTN_loadCourses);
        welcomeButton = (Button) findViewById(R.id.welcome_BTN_startButton);

        Glide.with(this)
                .load(getResources()
                        .getIdentifier("grade", "drawable", this.getPackageName()))
                .into(mainImage);
        //Listeners
        welcomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
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
