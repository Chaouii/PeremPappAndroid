package com.root.perempapp;

public class CredentialsBO {
    private String id;
    private String mail;
    private String password;
    private String message;
    private String fName;
    private String lName;
    private String bDate;

    public void setId(String id) {
        this.id = id;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setbDate(String bDate) {
        this.bDate = bDate;
    }

    public String getId() {
        return id;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getbDate() {
        return bDate;
    }
    public void clearAll(){
        id="";
        mail="";
        password="";
        message="";
        fName="";
        lName="";
        bDate="";
    }
}
