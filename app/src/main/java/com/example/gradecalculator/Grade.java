package com.example.gradecalculator;

/**
 * Grade class to store grade parameters
 * 1. Subject's name
 * 2. Grade
 * 3. Points
 */
public class Grade {
    private String subject;
    private int grade;
    private double points;

    public Grade() {
        this.subject = "";
        this.grade = 0;
        this.points = 0;
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

}
