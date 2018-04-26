package com.example.demo.service;

///**
// * \* Created with IntelliJ IDEA.
// * \* User: llf
// * \* Date: 2018/4/25
// * \* Time: 23:22
// * \* Description:
// * \
// */
//
//import com.example.demo.Dao.MemorialMapper;
//import com.example.demo.Entity.Memorial;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class StarService {
//    @Autowired
//    private MemorialMapper memorialMapper;
//
//    public int add(String name, String avatar) {
//        return memorialMapper.add(name, avatar);
//    }
//
//    public int update(String name, String avatar, int id) {
//        return memorialMapper.update(name, avatar, id);
//    }
//
//    public int delete(int id) {
//        return memorialMapper.delete(id);
//    }
//
//    public Memorial findMemorial(int id) {
//        return memorialMapper.findMemorial(id);
//    }
//
//    public List<Memorial> findMemorialList() {
//        return memorialMapper.findMemorialList();
//    }
//}