package com.example.ebihartourism.Model;

public class Places {


    private String pid,Category,Name,Speciality,Specification,date,time,Image,Nearest_Railway;

    public Places(){   }

    public Places(String nearest_Railway,String pid, String category, String name, String speciality, String specification, String date, String time, String image) {
        this.pid = pid;
        Category = category;
        Name = name;
        this.Nearest_Railway = nearest_Railway;
        Speciality = speciality;
        Specification = specification;
        this.date = date;
        this.time = time;
        Image = image;
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSpeciality() {
        return Speciality;
    }

    public void setSpeciality(String speciality) {
        Speciality = speciality;
    }

    public String getSpecification() {
        return Specification;
    }

    public void setSpecification(String specification) {
        Specification = specification;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getNearest_Railway() {
        return Nearest_Railway;
    }

    public void setNearest_Railway(String nearest_Railway) {
        Nearest_Railway = nearest_Railway;
    }
}
