package com.example.vinamra.anganwadi_supervisor;

public class class_for_dr_list {
    String name,org,date,childdata;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChilddata() {
        return childdata;
    }

    public void setChilddata(String childdata) {
        this.childdata = childdata;
    }

    public class_for_dr_list(String name, String org, String date, String childdata) {
        this.name = name;
        this.org = org;
        this.date = date;
        this.childdata = childdata;
    }
}
