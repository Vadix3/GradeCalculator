package com.example.gradecalculator;

import java.io.Serializable;

/**
 * Grade class to store grade parameters
 * 1. Subject's name
 * 2. Grade
 * 3. Points
 */
public class Grade implements Comparable, Serializable {

    private String subject;
    private int grade;
    private double points;

    public Grade() {
        this.subject = "";
        this.grade = 0;
        this.points = 0;
    }

    public Grade(String subject, int grade, double points) {
        this.subject = subject;
        this.grade = grade;
        this.points = points;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getSubject() {
        return subject;
    }

    public int getGrade() {
        return grade;
    }

    public double getPoints() {
        return points;
    }

    @Override
    public int compareTo(Object o) {
        Grade temp = (Grade) o;
        return temp.getGrade() - this.grade;
    }

    @Override
    public String toString() {
        return "Name: " + subject + " Grade: " + grade + " Points: " + points;
    }
}
