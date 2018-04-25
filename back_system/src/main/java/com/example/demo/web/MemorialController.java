package com.example.demo.web;

import com.example.demo.Entity.Memorial;
import com.example.demo.service.MemorialService;
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
@RequestMapping("/Memorial")
public class MemorialController {
    @Autowired
    MemorialService memorialService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Memorial> getMemorials() {
        return memorialService.findMemorialList();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Memorial getMemorialById(@PathVariable("id") int id) {
        return memorialService.findMemorial(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String updateMemorial(@PathVariable("id") int id, @RequestParam(value = "name", required = true) String name,
                                @RequestParam(value = "avatar", required = true) String avatar) {
        int t = memorialService.update(name, avatar, id);
        if (t == 1) {
            return "success";
        } else {
            return "fail";
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable(value = "id") int id) {
        int t = memorialService.delete(id);
        if (t == 1) {
            return "success";
        } else {
            return "fail";
        }

    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postMemorial(@RequestParam(value = "name") String name,
                              @RequestParam(value = "avatar") String avatar) {

        int t = memorialService.add(name, avatar);
        if (t == 1) {
            return "success";
        } else {
            return "fail";
        }

    }

}