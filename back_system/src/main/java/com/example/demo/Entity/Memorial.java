package com.example.demo.Entity;

public class Memorial {
    private int id;
    private String name;
    private String avatar;


    private int valid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }


    @Override
    public String toString() {
        return "Memorial{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", money=" + valid +
                '}';
    }
}
