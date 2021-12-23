package com.example.pkb2021;

public class DataAppointment {
    private String inDate, inTime, inAge, inGender, inProblem, username, doctor_name, inName;

    DataAppointment(String inDate, String inTime, String inAge, String inGender, String inProblem, String username, String doctor_name, String inName) {
        this.inDate = inDate;
        this.inTime = inTime;
        this.inAge = inAge;
        this.inGender = inGender;
        this.inProblem = inProblem;
        this.username = username;
        this.doctor_name = doctor_name;
        this.inName = inName;

    }

    public String getInDate() {
        return inDate;
    }

    public String getInTime() {
        return inTime;
    }

    public String getInAge() {
        return inAge;
    }

    public String getInGender() {
        return inGender;
    }

    public String getInProblem() {
        return inProblem;
    }

    public String getUsername() {
        return username;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getInName() {
        return inName;
    }
}
