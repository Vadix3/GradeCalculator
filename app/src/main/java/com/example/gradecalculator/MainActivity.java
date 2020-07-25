package com.example.gradecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Buttons
    private Button initButton;
    private Button subjectButton;
    private Button gradeButton;
    private Button pointsButton;

    //Texts
    private TextView feedBackLabel;
    private TextView gradeLabel;
    private TextView avgTitleLabel;
    private TextView welcomeLabel;
    private EditText inputLabel;


    //List
    private ListView gradesList;

    //Arrays
    private ArrayList<Grade> grades; //from user input
    private ArrayList<String> stringGrades;
    private ArrayAdapter<String> adapter;

    //Numbers
    int numOfInputs = 0; // Number of grades
    int gradesSum = 0; // Sum of grades
    double numOfPoints = 0; // Total points
    double totalAvg = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //---------------------- Inits ---------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Buttons
        initButton = (Button) findViewById(R.id.main_BTN_addButton);
        subjectButton = (Button) findViewById(R.id.main_BTN_subjectButton);
        gradeButton = (Button) findViewById(R.id.main_BTN_gradeButton);
        pointsButton = (Button) findViewById(R.id.main_BTN_pointsButton);

        //Labels
        feedBackLabel = (TextView) findViewById(R.id.main_LBL_feedbackLabel);
        feedBackLabel.setVisibility(View.INVISIBLE);
        gradeLabel = (TextView) findViewById(R.id.main_LBL_gradeLabel);
        gradeLabel.setVisibility(View.INVISIBLE);
        avgTitleLabel = (TextView) findViewById(R.id.main_LBL_memutzaLabel);
        avgTitleLabel.setVisibility(View.INVISIBLE);
        welcomeLabel = (TextView) findViewById(R.id.main_LBL_welcomeLabel);
        inputLabel = (EditText) findViewById(R.id.main_EDT_input);

        //List
        gradesList = (ListView) findViewById(R.id.main_LST_gradesList);

        //Arrays
        grades = new ArrayList<Grade>();
        stringGrades = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringGrades);
        gradesList.setAdapter(adapter);

        //Listeners
        initButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initReading();
            }
        });

        subjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readSubjectName();
            }
        });

        gradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readGrade();
            }
        });

        pointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readPoints();
            }
        });
    }

    /**
     * Sequence of methods to read user input
     * 1. initReading - User pressed Add Course, change button to add subject and add EditText
     * 2. readSubjectName - Enter subject name reading state (Change hint and button, add name to object)
     * 3. readGrade - Enter grade reading state (Change hint and button, add grade to object)
     * 4. readPoints - Enter points reading state (Change hint and button, add points to object)
     * 5. addGradeToList - Put the whole input from the grades array into the String array,
     * and update the list.
     */

    // 1
    public void initReading() {
        welcomeLabel.setVisibility(View.INVISIBLE);
        inputLabel.setVisibility(View.VISIBLE);
        inputLabel.setHint("Enter Course Name");
        initButton.setVisibility(View.INVISIBLE);
        subjectButton.setVisibility(View.VISIBLE);
    }

    // 2
    public void readSubjectName() {
        grades.add(new Grade()); // Create new grade
        grades.get(grades.size() - 1).setSubject(inputLabel.getText().toString()); //Add subject
        inputLabel.setText(null);
        inputLabel.setHint("Enter Grade");
        subjectButton.setVisibility(View.INVISIBLE);
        gradeButton.setVisibility(View.VISIBLE);
    }

    // 3
    public void readGrade() {
        grades.get(grades.size() - 1).setGrade(Integer.parseInt(inputLabel.getText().toString())); //Add grade
        inputLabel.setText(null);
        inputLabel.setHint("Enter Number of Points");
        gradeButton.setVisibility(View.INVISIBLE);
        pointsButton.setVisibility(View.VISIBLE);
    }

    //4
    public void readPoints() {
        grades.get(grades.size() - 1).setPoints(Double.parseDouble(inputLabel.getText().toString())); //Add points
        inputLabel.setText(null);
        inputLabel.setHint("Add Subject Name");
        pointsButton.setVisibility(View.INVISIBLE);
        subjectButton.setVisibility(View.VISIBLE);
        addGradeToList();
    }

    // 5
    public void addGradeToList() {
        Grade temp = grades.get(numOfInputs);
        numOfInputs++;
        String tempSubjectName = temp.getSubject();
        int tempGrade = temp.getGrade();
        double tempPoints = temp.getPoints();
        numOfPoints += tempPoints;
        String entry = "Subject: " + tempSubjectName + "    Grade: " + tempGrade + "    Points: " + tempPoints;
        stringGrades.add(entry);
        adapter.notifyDataSetChanged();
        updateStats();
    }

    /**
     * Update the Average and display message accordingly
     * 0-60 Unacceptable! (Red)
     * 60-70 Bad! (Less red)
     * 70-80 Reasonable! (Orange)
     * 80-90 Good! (Blue)
     * 90-100 Excellent! (Green)
     */
    public void updateStats() {
        gradeLabel.setVisibility(View.VISIBLE); // How to add condition to check if visible?
        avgTitleLabel.setVisibility(View.VISIBLE); // Same here
        Grade temp = grades.get(numOfInputs - 1);
        double tempPoints = temp.getPoints();
        int tempGrade = temp.getGrade();
        System.out.println("TotalAvg: " + totalAvg + " TotalPoints: " + numOfPoints + " TempGrade: " + tempGrade + " TempPoints: " + tempPoints);
        if (totalAvg == 0) // In case of first entry
            totalAvg = tempGrade;
        else {
            double tempAvg = ((tempGrade * tempPoints) + (totalAvg * numOfPoints)) / (numOfPoints + tempPoints);
            totalAvg = tempAvg;
        }
//        String.format("%.2f", i2)
        gradeLabel.setText(String.format("%.1f", totalAvg));
        setColor(); // Set feedback label color according to the grade
    }

    /**
     * Add colors!
     **/
    public void setColor() {
        feedBackLabel.setVisibility(View.VISIBLE); // How to add condition to check if visible?
        if (totalAvg < 60) {
            feedBackLabel.setText("Unacceptable! >:(");
        }
        if (totalAvg >= 60 && totalAvg < 70) {
            feedBackLabel.setText("Bad! :(");
        }
        if (totalAvg >= 70 && totalAvg < 80) {
            feedBackLabel.setText("Reasonable! :|");
        }
        if (totalAvg >= 80 && totalAvg < 90) {
            feedBackLabel.setText("Good! :)");
        }
        if (totalAvg >= 90) {
            feedBackLabel.setText("Excellent! :D");
        }
    }
}