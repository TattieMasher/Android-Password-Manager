package com.example.passwordmanager;

public class Account {
    private String service;
    private String name;
    private String password;
    private int id;

    public Account(String service, String name, String password, int id) {
        this.service = service;
        this.name = name;
        this.password = password;
        this.id = id;
    }

    public Account(){    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
