package com.example.gradecalculator;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class GradeContainer {
    private ArrayList<Grade> grades;
    private ArrayList<String> stringGrades;


    public GradeContainer() {
    }

    public GradeContainer(ArrayList<Grade> grades, ArrayList<String> stringGrades) {
        this.grades = grades;
        this.stringGrades = stringGrades;
    }

    public void setGrades(ArrayList<Grade> grades) {
        this.grades = grades;
    }

    public void setStringGrades(ArrayList<String> stringGrades) {
        this.stringGrades = stringGrades;
    }

    public ArrayList<Grade> getGrades() {
        return grades;
    }

    public ArrayList<String> getStringGrades() {
        return stringGrades;
    }
}