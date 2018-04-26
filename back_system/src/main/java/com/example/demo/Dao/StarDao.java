package com.example.demo.Dao;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: llf
 * \* Date: 2018/4/26
 * \* Time: 21:02
 * \* Description:
 * \
 */
import com.example.demo.Entity.Star;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarDao extends JpaRepository <Star,Integer>{
}

//public interface