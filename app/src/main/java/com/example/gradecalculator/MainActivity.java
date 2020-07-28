package com.example.gradecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
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


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    /**
     * Widgets
     */

    //Buttons
    private Button inputButton;
    private Button editButton;

    //Texts
    private TextView feedBackLabel;
    private TextView gradeLabel;
    private TextView avgTitleLabel;
    private TextView emptyListLabel;
    private EditText inputLabel;

    //List
    private ListView gradesList;

    /**
     * Variables
     */
    //Boolean
    public static boolean loadStart = false; // Boolean to load from start

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

    public enum editType {
        nameEdit, gradeEdit, pointsEdit
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /** Initialize elements **/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**Buttons*/
        inputButton = (Button) findViewById(R.id.main_BTN_subjectButton);
        inputButton.setText("Submit Subject");
        editButton = (Button) findViewById(R.id.main_BTN_editButton);
        editButton.setVisibility(View.INVISIBLE);

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
        if (loadStart) {
            loadFromDevice(); //  -> In case I would like to load from the start and not with a button press
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringGrades);
        gradesList.setAdapter(adapter);
        registerForContextMenu(gradesList); // Attach context menu to list


        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (inputButton.getText().toString()) {
                    case "Submit Subject":
                        readSubjectName();
                        break;
                    case "Submit Grade":
                        readGrade();
                        break;
                    case "Submit Points":
                        readPoints();
                        break;
                }
            }
        });
    }

    /**
     * Check if a number is double
     */
    public boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
     * A function to edit or remove list entry
     * 1. Edit name
     * 2. Edit grade
     * 3. Edit points
     * 4. Remove entry
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int tempPosition = info.position;
        switch (item.getItemId()) {
            case R.id.context_edit_editName:
                editEntry(tempPosition, editType.nameEdit);
                return true;
            case R.id.context_edit_editGrade:
                editEntry(tempPosition, editType.gradeEdit);
                return true;
            case R.id.context_edit_editPoints:
                editEntry(tempPosition, editType.pointsEdit);
                return true;
            case R.id.context_edit_remove:
                removeListLine(tempPosition);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Show popup menu
     */

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) this);
        popup.inflate(R.menu.options_menu);
        popup.show();
    }

    /**
     * TODO: Add Button Click Effect
     * Menu button options:
     * 1. Sort
     * 1.1 Sort by name - Sort grades array by name
     * 1.2 Sort by grade - Sort grades array by grade
     * 1.3 Sort by points - Sort grades array by points
     * 2. Save
     * 2.1 Save locally - Save on device
     * 2.2 Save FireBase - Save on Server
     * 3. Statistics - Display statistics (Top grade, Total number of points etc..)
     * 4. About - Credits and such
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.optionsMenu_saveListDevice:
                save(saveType.Device);
                return true;
            case R.id.optionsMenu_saveListServer:
                save(saveType.Server);
                return true;
            case R.id.optionsMenu_loadListDevice:
                load(saveType.Device);
                return true;
            case R.id.optionsMenu_loadListServer:
                load(saveType.Server);
                return true;
            case R.id.optionsMenu_about:
                showAbout();
                return true;
            case R.id.optionsMenu_sortByGrade:
                sortBy(sortType.byGrade);
                return true;
            case R.id.optionsMenu_sortByName:
                sortBy(sortType.byName);
                return true;
            case R.id.optionsMenu_sortByPoints:
                sortBy(sortType.byPoints);
                return true;
            case R.id.optionsMenu_statistics:
                showStatistics();
                return true;
            default:
                return false;
        }
    }


    /**
     * A method to edit selected entry by criteria
     */
    private void editEntry(int position, editType type) {
        inputButton.setText("Submit Edit");

        switch (type) {
            case nameEdit: // Edit name
                editEntryName(position);
                break;
            case gradeEdit: // Edit grade
                Toast.makeText(this, "Edit grade *In Construction*", Toast.LENGTH_SHORT).show();
                break;
            case pointsEdit: // Edit points
                Toast.makeText(this, "Edit points *In Construction*", Toast.LENGTH_SHORT).show();
                break;
        }
        inputLabel.setText(null);
        inputLabel.setHint("Add subject name"); // Reset hint

    }


    /**
     * Edit the name of the entry
     */
    public void editEntryName(int position) {
        String tempName = inputLabel.getText().toString();
        if (!(tempName.matches(".*[a-z].*"))) {
            inputLabel.setError("Please enter a valid name!");
            inputLabel.requestFocus();
        } else {
            tempName = tempName.substring(0, 1).toUpperCase() + tempName.substring(1); // Capitalize
            grades.get(position).setSubject(tempName); // Edit Name in objects array
            stringGrades.set(position, "Subject: " + tempName + "   Grade: " + grades.get(position).getGrade() + "   Points: " + grades.get(position).getPoints()); // Edit name in strings array
            adapter.notifyDataSetChanged(); // update list
            Toast.makeText(getApplicationContext(), tempName + "edited successfully!", Toast.LENGTH_SHORT).show();
            inputLabel.setHint("Enter Course Name");
            inputLabel.setText(null);
            inputButton.setText("Submit Subject");
        }
    }

    /**
     * Get sort parameter and sort accordingly
     * TODO: Add sorts (write manually, learn sorts)
     */

    private void sortBy(sortType type) {
        switch (type) {
            case byGrade:
                Toast.makeText(this, "Sort by grade *In Construction*", Toast.LENGTH_SHORT).show();
                return;
            case byName:
                Toast.makeText(this, "Sort by name *In Construction*", Toast.LENGTH_SHORT).show();
                return;
            case byPoints:
                Toast.makeText(this, "Sort by points *In Construction*", Toast.LENGTH_SHORT).show();
                return;
        }
    }


    /**
     * Get save parameter and save accordingly
     * TODO:Add save in server
     */
    private void save(saveType type) {
        switch (type) {
            case Device:
                saveOnDevice();
                break;
            case Server:
                Toast.makeText(this, "Save to server *In Construction*", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Get save parameter and load accordingly
     * TODO:Add load from server
     */
    private void load(saveType type) {
        switch (type) {
            case Device:
                loadFromDevice();
                break;
            case Server:
                Toast.makeText(this, "Load from server *In Construction*", Toast.LENGTH_SHORT).show();
                break;
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringGrades);
        gradesList.setAdapter(adapter);
        registerForContextMenu(gradesList); // Attach context menu to list
        adapter.notifyDataSetChanged(); // Notify list
        updateStats();
        Toast.makeText(this, "Data loaded successfully!", Toast.LENGTH_SHORT).show();
    }

    /**
     * A method to save grades array on the device
     * save grades array? string array?
     */
    private void saveOnDevice() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        sharedPreferences.edit().clear(); // Clear previous
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String gradeJson = gson.toJson(grades);
        String stringJson = gson.toJson(stringGrades); //Save stringGrades array
        editor.putString("Grades Object List", gradeJson);
        editor.putString("Grades String List", stringJson);
        editor.apply();
        Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
    }

    //Maybe try to load anyway? worst case we wont get anything
    private void loadFromDevice() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();

        String stringJson = sharedPreferences.getString("Grades String List", null); // get string json
        String gradeJson = sharedPreferences.getString("Grades Object List", null); // get object json

        Type stringType = new TypeToken<ArrayList<String>>() {
        }.getType();
        stringGrades = gson.fromJson(stringJson, stringType); //get string grades array from json

        Type gradeType = new TypeToken<ArrayList<Grade>>() {
        }.getType();
        grades = gson.fromJson(gradeJson, gradeType); // get grades array from json
        updateStats();
    }

    /**
     * Show about_activity
     */

    private void showAbout() {
        AboutDialog dialog = new AboutDialog();
        dialog.show(getSupportFragmentManager(), "AboutDialog");
    }

    /**
     * Show statistics
     * TODO: Add statistics. Fragment window? Activity?
     */
    private void showStatistics() {
        Toast.makeText(this, "Show statistics *In Construction*", Toast.LENGTH_SHORT).show();
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
            tempSubject = tempSubject.substring(0, 1).toUpperCase() + tempSubject.substring(1); // Capitalize
            grades.add(new Grade()); // Create new grade
            grades.get(grades.size() - 1).setSubject(tempSubject); //Add subject
            inputLabel.setText(null);
            inputLabel.setHint("Enter Grade");
            inputButton.setText("Submit Grade");
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
            inputButton.setText("Submit Points");
        }
    }

    /**
     * 3. readPoints - Enter points reading state (Change hint and button, add points to object)
     */
    public void readPoints() {
        String tempPoints = inputLabel.getText().toString();
        if (!(isDouble(tempPoints)) || Double.parseDouble(tempPoints) > 5 || Double.parseDouble(tempPoints) <= 0) {
            inputLabel.setError("Please enter a valid number of points!");
            inputLabel.requestFocus();
        } else {
            grades.get(grades.size() - 1).setPoints(Double.parseDouble(inputLabel.getText().toString())); //Add points
            inputLabel.setText(null);
            inputLabel.setHint("Add Subject Name");
            inputButton.setText("Submit Subject");
            addGradeToList();
        }
    }

    /**
     * * 4. addGradeToList - Put the whole input from the grades array into the String array,
     */
    public void addGradeToList() {
        Grade temp = grades.get(numOfInputs);
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
     * A method to update the total avg and labels (after load & changes to the arrays)
     */

    public void updateStats() {
        gradesSum = 0;
        numOfPoints = 0;
        //Set labels visibility
        if (gradeLabel.getVisibility() != View.VISIBLE)
            gradeLabel.setVisibility(View.VISIBLE);
        if (avgTitleLabel.getVisibility() != View.VISIBLE)
            avgTitleLabel.setVisibility(View.VISIBLE);

        numOfInputs = grades.size();
        for (Grade grade : grades) {
            numOfPoints += grade.getPoints();
            gradesSum += (grade.getPoints() * grade.getGrade());
        }
        totalAvg = gradesSum / numOfPoints;
        gradeLabel.setText(String.format("%.1f", totalAvg)); // Update label
        setColor(); // Set feedback according to grade
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

        //Update label
        Toast.makeText(getApplicationContext(), temp.getSubject() + " Removed Successfully!", Toast.LENGTH_SHORT).show();

        if (numOfInputs - 1 == 0) { // if last input
            avgTitleLabel.setVisibility(View.INVISIBLE);
            gradeLabel.setVisibility(View.INVISIBLE);
            feedBackLabel.setVisibility(View.INVISIBLE);
            totalAvg = 0;
            numOfPoints = 0;
            gradesSum = 0;
            emptyListLabel.setVisibility(View.VISIBLE);
        } else {
            //Update average
            updateStats();
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

    /**
     * Method to ask for exit when backPress
     */
    @Override
    public void onBackPressed() {
        QuitDialog dialog = new QuitDialog();
        dialog.show(getSupportFragmentManager(), "QuitDialog");
    }
}