package com.example.vinamra.anganwadi_supervisor;

public class child_reg_list_class {
    String id,name,fname,mname,dob,weight;

    public child_reg_list_class(String id, String name, String fname, String mname, String dob, String weight) {
        this.id = id;
        this.name = name;
        this.fname = fname;
        this.mname = mname;
        this.dob = dob;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
