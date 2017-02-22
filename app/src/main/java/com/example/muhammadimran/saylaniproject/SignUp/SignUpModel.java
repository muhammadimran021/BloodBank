package com.example.muhammadimran.saylaniproject.SignUp;

/**
 * Created by muhammad imran on 2/22/2017.
 */

public class SignUpModel {
    private String imageurl;
    private String fname;
    private String lname;
    private String email;
    private String bloodGroup;
    private String membership;
    private String password;
    private String conferm_password;

    public SignUpModel() {
    }

    public SignUpModel(String imageurl, String fname, String lname, String email, String bloodGroup, String membership, String password, String conferm_password) {
        this.imageurl = imageurl;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.membership = membership;
        this.password = password;
        this.conferm_password = conferm_password;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConferm_password() {
        return conferm_password;
    }

    public void setConferm_password(String conferm_password) {
        this.conferm_password = conferm_password;
    }
}
