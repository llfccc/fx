package com.example.demo.web;

//import com.example.demo.service.StarService;

import com.example.demo.Dao.StarDao;
import com.example.demo.Entity.Star;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: llf
 * \* Date: 2018/4/26
 * \* Time: 0:26
 * \* Description:
 * \
 */
@RestController
@RequestMapping("/star")
public class StarController {

    @Autowired
    StarDao starDao;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Star> getStars() {
        return starDao.findAll();
    }

//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public Star getStarById(@PathVariable("id") int id) {
//        return starDao.findAllById(id);
//    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String updateStar(@PathVariable("id") int id, @RequestParam(value = "name", required = true) String name,
                             @RequestParam(value = "avatar", required = true) String avatar) {
        Star star = new Star();
        star.setAvatar(avatar);
        star.setName(name);
        star.setId(id);
        Star star1 = starDao.saveAndFlush(star);

        return star1.toString();

    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String postStar(@RequestParam(value = "name") String name,
                           @RequestParam(value = "avatar") String avatar) {
        Star star = new Star();
        star.setAvatar(avatar);
        star.setName(name);
        Star star1 = starDao.save(star);
        return star1.toString();

    }


}