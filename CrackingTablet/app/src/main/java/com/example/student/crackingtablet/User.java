package com.example.student.crackingtablet;

public class User {
    public static final int LOGIN = 1 << 0;
    public static final int CONNECTION = 1 << 1;
    public static final int MOTION = 1 << 2;

    private String id;
    private String pwd;
    private String name;
    private int manager;
    private String rdate;
    private int img;
    private int state;

    public User() {

    }

    public User(String id, String pwd, String name) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
    }

    public User(String id, String pwd, String name, String rdate, int img, int state) {
        this.id = id;
        this.pwd = pwd;
        this.name = name;
        this.rdate = rdate;
        this.img = img;
        this.state = state;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int optionEnable(int option) {
        return state = state | option;
    }

    public int optionDisable(int option) {
        return state = state & ~option;
    }

    public boolean isOptionEnabled(int option) {
        return (state & option) != 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", manager=" + manager +
                ", rdate='" + rdate + '\'' +
                ", img=" + img +
                '}';
    }
}
