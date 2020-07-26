package com.example.gradecalculator;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;


/**
 * TODO: Add options button
 * 1. Sort by name, grade, points (Asc/Desc)
 * 2. Statistics (Highest, Lowest)
 * 3. Save data (Firebase?)
 */

public class MainActivity extends Activity implements MenuItem.OnMenuItemClickListener {
    //Buttons
    private Button subjectButton;
    private Button gradeButton;
    private Button pointsButton;

    //Texts
    private TextView feedBackLabel;
    private TextView gradeLabel;
    private TextView avgTitleLabel;
    private TextView emptyListLabel;
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


    //enums
    public enum saveType { //
        Device, Server
    }

    public enum sortType {
        byName, byGrade, byPoints
    }

    //TODO: Check what is the right type of implementation
    public enum inputType {
        firstInput, nameEdit, gradeEdit, pointsEdit
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /** Initialize elements **/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**Buttons*/
        subjectButton = (Button) findViewById(R.id.main_BTN_subjectButton);
        gradeButton = (Button) findViewById(R.id.main_BTN_gradeButton);
        pointsButton = (Button) findViewById(R.id.main_BTN_pointsButton);

        /**Labels*/
        feedBackLabel = (TextView) findViewById(R.id.main_LBL_feedbackLabel);
        feedBackLabel.setVisibility(View.INVISIBLE);
        gradeLabel = (TextView) findViewById(R.id.main_LBL_gradeLabel);
        gradeLabel.setVisibility(View.INVISIBLE);
        avgTitleLabel = (TextView) findViewById(R.id.main_LBL_memutzaLabel);
        avgTitleLabel.setVisibility(View.INVISIBLE);
        emptyListLabel = (TextView) findViewById(R.id.main_LBL_nothingToShow);
        emptyListLabel.setVisibility(View.INVISIBLE);
        inputLabel = (EditText) findViewById(R.id.main_EDT_input);

        /**Grade List*/
        gradesList = (ListView) findViewById(R.id.main_LST_gradesList);

        /**Arrays*/
        grades = new ArrayList<Grade>();
        stringGrades = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringGrades);
        gradesList.setAdapter(adapter);
        registerForContextMenu(gradesList); // Attach context menu to list

        /**TODO: Complete edit methods
         * maybe add parameter to methods to prevent duplicate
         * */
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
     * Context menu methods:
     * 1. Inflate context menu on press
     * 2. On item selection, perform 2 actions: Delete / Edit
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_context_menu, menu);
    }

    /**
     * A method to perform ContextMenu selection
     */

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) this);
        popup.inflate(R.menu.options_menu);
        popup.show();
    }

    /**
     * TODO:Fix it
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionsMenu_saveListDevice:
                save(saveType.Device);
            case R.id.optionsMenu_saveListServer:
                save(saveType.Server);
            case R.id.optionsMenu_about:
                showAbout();
            case R.id.optionsMenu_sortByGrade:
                sortBy(sortType.byGrade);
            case R.id.optionsMenu_sortByName:
                sortBy(sortType.byName);
            case R.id.optionsMenu_sortByPoints:
                sortBy(sortType.byPoints);
            case R.id.optionsMenu_statistics:
                showStatistics();
            default:
                return false;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.editButton) // Edit list row
            editListLine(info.position);

        if (item.getItemId() == R.id.context_edit_editName)
            System.err.println("Edit by name");

        if (item.getItemId() == R.id.context_edit_editGrade)
            System.err.println("Edit by grade");

        if (item.getItemId() == R.id.context_edit_editPoints)
            System.err.println("Edit by points");

        if (item.getItemId() == R.id.removeButton)
            removeListLine(info.position);

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }


    /**
     * Get sort parameter and sort accordingly
     */

    private void sortBy(sortType type) {
        switch (type) {
            case byGrade:
                return;
            case byName:
                return;
            case byPoints:
                return;
        }
    }

    /**
     * Get save parameter and save accordingly
     */
    private void save(saveType type) {
        switch (type) {
            case Device:
                return;
            case Server:
                return;
        }
    }

    private void showAbout() {
    }

    private void showStatistics() {
    }


    /**
     * Sequence of methods to read user input
     * and update the list.
     */

    /**
     * * 1. readSubjectName - Enter subject name reading state (Change hint and button, add name to object)
     */
    public void readSubjectName() {
        String tempSubject = inputLabel.getText().toString();
        if (emptyListLabel.getVisibility() == View.VISIBLE)
            emptyListLabel.setVisibility(View.INVISIBLE);
        //check if the EditText have values or not
        if (!(tempSubject.matches(".*[a-z].*"))) {
            inputLabel.setError("Please enter a valid name!");
            inputLabel.requestFocus();
        } else {
            grades.add(new Grade()); // Create new grade
            grades.get(grades.size() - 1).setSubject(tempSubject); //Add subject
            inputLabel.setText(null);
            inputLabel.setHint("Enter Grade");
            subjectButton.setVisibility(View.INVISIBLE);
            gradeButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 2. readGrade - Enter grade reading state (Change hint and button, add grade to object)
     */
    public void readGrade() {
        String tempGrade = inputLabel.getText().toString();
        //Raise error if input is not numeric / not in range
        if (!tempGrade.matches("\\d+") || Integer.parseInt(tempGrade) > 100 || Integer.parseInt(tempGrade) < 0) {
            inputLabel.setError("Please enter a valid grade!");
            inputLabel.requestFocus();
        } else {
            grades.get(grades.size() - 1).setGrade(Integer.parseInt(tempGrade)); //Add grade
            inputLabel.setText(null);
            inputLabel.setHint("Enter Number of Points");
            gradeButton.setVisibility(View.INVISIBLE);
            pointsButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 3. readPoints - Enter points reading state (Change hint and button, add points to object)
     */
    public void readPoints() {
        String tempPoints = inputLabel.getText().toString();
        if (!tempPoints.matches("\\d+") || Double.parseDouble(tempPoints) > 5 || Double.parseDouble(tempPoints) < 0) {
            inputLabel.setError("Please enter a valid number of points!");
            inputLabel.requestFocus();
        } else {
            grades.get(grades.size() - 1).setPoints(Double.parseDouble(inputLabel.getText().toString())); //Add points
            inputLabel.setText(null);
            inputLabel.setHint("Add Subject Name");
            pointsButton.setVisibility(View.INVISIBLE);
            subjectButton.setVisibility(View.VISIBLE);
            addGradeToList();
        }
    }

    /**
     * * 4. addGradeToList - Put the whole input from the grades array into the String array,
     */
    public void addGradeToList() {
        Grade temp = grades.get(numOfInputs);
        numOfInputs++; // increase total number of inputs
        String tempSubjectName = temp.getSubject();
        int tempGrade = temp.getGrade();
        double tempPoints = temp.getPoints();
        String entry = "Subject: " + tempSubjectName + "   Grade: " + tempGrade + "   Points: " + tempPoints;
        stringGrades.add(entry);
        adapter.notifyDataSetChanged();
        updateStats();
        Toast.makeText(getApplicationContext(), tempSubjectName + " Added Successfully!", Toast.LENGTH_SHORT).show();
    }

    /** -------------  Finish user input reading  ----------------- */


    /**
     * A method to update the total avg and label
     */

    public void updateStats() {
        //Set labels visibility
        if (gradeLabel.getVisibility() != View.VISIBLE)
            gradeLabel.setVisibility(View.VISIBLE);
        if (avgTitleLabel.getVisibility() != View.VISIBLE)
            avgTitleLabel.setVisibility(View.VISIBLE);

        //temp grade
        Grade temp = grades.get(numOfInputs - 1);
        double tempPoints = temp.getPoints();
        int tempGrade = temp.getGrade();

        //Update total number of points
        numOfPoints += temp.getPoints();
        gradesSum += tempGrade;

        // In case of first entry
        if (totalAvg == 0)
            totalAvg = tempGrade;

            // Update total average
        else {
            totalAvg = totalAvg + (tempGrade - totalAvg) * tempPoints / numOfPoints;
        }
        gradeLabel.setText(String.format("%.1f", totalAvg)); // Update label
        setColor(); // Set feedback according to grade
    }


    public void editListLine(int position) {
        System.out.println("Hi! I would like to edit the following row: " + stringGrades.get(position));
    }

    /**
     * A method to remove selected line
     */
    public void removeListLine(int position) {
        Grade temp = grades.get(position); // temp variable

        //Remove grade and update list
        grades.remove(position);
        stringGrades.remove(position);
        adapter.notifyDataSetChanged();
        numOfInputs--;

        //Update label
        Toast.makeText(getApplicationContext(), temp.getSubject() + " Removed Successfully!", Toast.LENGTH_SHORT).show();

        if (numOfInputs == 0) { // if last input
            avgTitleLabel.setVisibility(View.INVISIBLE);
            gradeLabel.setVisibility(View.INVISIBLE);
            feedBackLabel.setVisibility(View.INVISIBLE);
            totalAvg = 0;
            numOfPoints = 0;
            gradesSum = 0;
            emptyListLabel.setVisibility(View.VISIBLE);
        } else {
            //Update average
            totalAvg = ((totalAvg * numOfPoints) - (temp.getGrade() * temp.getPoints())) / (numOfPoints - temp.getPoints());
            numOfPoints -= temp.getPoints();
            gradesSum -= temp.getGrade();
            gradeLabel.setText(String.format("%.1f", totalAvg));

        }
    }

    /**
     * A method to set and color the feedback to the grade according to:
     * 0-60 Unacceptable! (Red)
     * 60-70 Bad! (Less red)
     * 70-80 Reasonable! (Orange)
     * 80-90 Good! (Blue)
     * 90-100 Excellent! (Green)
     */
    public void setColor() {
        feedBackLabel.setVisibility(View.VISIBLE); // How to add condition to check if visible?
        if (totalAvg < 60) {
            feedBackLabel.setText("Unacceptable! >:(");
            feedBackLabel.setTextColor(ContextCompat.getColor(this, R.color.unacceptableColor));
        }
        if (totalAvg >= 60 && totalAvg < 70) {
            feedBackLabel.setText("Bad! :(");
            feedBackLabel.setTextColor(ContextCompat.getColor(this, R.color.badColor));
        }
        if (totalAvg >= 70 && totalAvg < 80) {
            feedBackLabel.setText("Reasonable! :|");
            feedBackLabel.setTextColor(ContextCompat.getColor(this, R.color.reasonableColor));
        }
        if (totalAvg >= 80 && totalAvg < 90) {
            feedBackLabel.setText("Good! :)");
            feedBackLabel.setTextColor(ContextCompat.getColor(this, R.color.goodColor));
        }
        if (totalAvg >= 90) {
            feedBackLabel.setText("Excellent! :D");
            feedBackLabel.setTextColor(ContextCompat.getColor(this, R.color.goodJobColor));
        }
    }
}