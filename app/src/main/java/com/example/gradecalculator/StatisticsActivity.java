package com.example.gradecalculator;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Statistics class to display statistics
 * TODO: 1. receive array and put into "grades"
 * 2. implement compareTo methods in Grade class
 */
public class StatisticsActivity extends AppCompatActivity {

    //Widgets
    private ListView statList; // Statistics list

    //Array things
    private ArrayList<Grade> grades; // Grades array to receive from main activity
    private ArrayList<String> toPrint; // To print list - maybe not needed
    private ArrayAdapter<String> adapter; // Array adapter


    /**
     * Statistics stuff:
     * 1.Highest Grade
     * 2.Num Of Points
     * 3.Grades Avg
     * 4.Num Of Subjects
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);

        if (getIntent().hasExtra("GradesArray")) {
            grades = (ArrayList<Grade>) getIntent().getSerializableExtra("GradesArray");
            toPrint = new ArrayList<String>();
            Collections.sort(grades); // Sort grades (highest first)
            int numOfSubjects = grades.size(); // Get the number of subjects in list
            double gradesAvg = getGradeAvg(); // Get the grades average
            double numOfPoints = getNumOfPoints(); // Get the total number of points

            statList = (ListView) findViewById(R.id.statistics_LST_statList);
            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, toPrint);
            statList.setAdapter(adapter);

            String highestGrade = "Your highest grade: " + grades.get(0).getSubject() + ". Grade: " + grades.get(0).getGrade();
            String gradesAverage = "Your grade average is: " + String.format("%.1f", gradesAvg / numOfSubjects);
            String numberOfPoints = "Your total number of points: " + numOfPoints;
            String numberOfSubjects = "Your number of courses: " + numOfSubjects;

            //Add stats to list
            toPrint.add(highestGrade);
            toPrint.add(gradesAverage);
            toPrint.add(numberOfPoints);
            toPrint.add(numberOfSubjects);

            //Update list
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), this + "NO DATA!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * A method to get all the points
     */
    private double getNumOfPoints() {
        double points = 0;
        for (Grade grade : grades)
            points += grade.getPoints();
        return points;
    }

    /**
     * A method to get the average of all grades
     */
    private double getGradeAvg() {
        double avg = 0;
        for (Grade grade : grades)
            avg += grade.getGrade();
        return avg;
    }
}
