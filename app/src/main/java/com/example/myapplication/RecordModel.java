package com.example.myapplication;

// RecordModel.java
public class RecordModel {

    private String Breakdown_ID;
    private String cause_of_bd;
    private String spares;
    private String timetext;
    private String Date;
    private String nature_of_problem;
    private String User_ID;
    private String TotalHrOfBreakdown;

    public RecordModel() {
        // Default constructor required for Firestore to be able to convert documents to objects
    }

    public RecordModel(String breakdown_ID, String cause_of_bd, String spares, String timetext, String date, String nature_of_problem, String user_ID, String totalHrOfBreakdown) {
        Breakdown_ID = breakdown_ID;
        this.cause_of_bd = cause_of_bd;
        this.spares = spares;
        this.timetext = timetext;
        Date = date;
        this.nature_of_problem = nature_of_problem;
        User_ID = user_ID;
        TotalHrOfBreakdown = totalHrOfBreakdown;
    }


    public String getBreakdown_ID() {
        return Breakdown_ID;
    }

    public void setBreakdown_ID(String breakdown_ID) {
        Breakdown_ID = breakdown_ID;
    }

    public String getNature_of_problem() {
        return nature_of_problem;
    }

    public void setNature_of_problem(String nature_of_problem) {
        this.nature_of_problem = nature_of_problem;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getTotalHrOfBreakdown() {
        return TotalHrOfBreakdown;
    }

    public void setTotalHrOfBreakdown(String totalHrOfBreakdown) {
        TotalHrOfBreakdown = totalHrOfBreakdown;
    }


    public String getCause_of_bd() {
        return cause_of_bd;
    }

    public void setCause_of_bd(String cause_of_bd) {
        this.cause_of_bd = cause_of_bd;
    }

    public String getSpares() {
        return spares;
    }

    public void setSpares(String spares) {
        this.spares = spares;
    }

    public String getTimetext() {
        return timetext;
    }

    public void setTimetext(String timetext) {
        this.timetext = timetext;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }
}
