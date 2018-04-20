package com.example.student.crackingtablet;

public class User {

    String id;
    String pwd;
    String name;
    int manager;
    String rdate;
    int img;
    int conn;

    public User() {
    }

    public User(String id, String pwd, String name) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
    }

    public User(String id, String pwd, String name, String rdate, int img, int conn) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.rdate = rdate;
        this.img = img;
        this.conn = conn;
    }

    //img 나중에 만들기
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getManager() {
        return manager;
    }

    public void setManager(int manager) {
        this.manager = manager;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getConn() {
        return conn;
    }

    public void setConn(int conn) {
        this.conn = conn;
    }
}
