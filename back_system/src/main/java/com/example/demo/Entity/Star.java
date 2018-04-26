package com.example.demo.Entity;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: llf
 * \* Date: 2018/4/26
 * \* Time: 21:06
 * \* Description:
 * \
 */
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="d_star")
public class Star {
    @Id
    @GeneratedValue
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
}