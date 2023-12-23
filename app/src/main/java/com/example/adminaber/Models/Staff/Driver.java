package com.example.adminaber.Models.Staff;

public class Driver extends Staff{
    private String name;

    public Driver(){};

    public Driver(String name) {
        this.name = name;
    }

    public Driver(String email, String name) {
        super(email);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
