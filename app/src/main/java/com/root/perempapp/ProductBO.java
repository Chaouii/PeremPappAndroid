package com.root.perempapp;

public class ProductBO {
    private String id;
    private String userId;
    private String category;
    private String name;
    private String perempDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerempDate() {
        return perempDate;
    }

    public void setPerempDate(String perempDate) {
        this.perempDate = perempDate;
    }
    public void clearAll(){
        id="";
        userId="";
        name="";
        category="";
        perempDate="";
    }
}
