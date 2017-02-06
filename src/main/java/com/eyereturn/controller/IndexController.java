package com.eyereturn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Chemcee. M. C on 05-02-2017.
 */

@Controller
@RequestMapping(value={"/index","/"})
public class IndexController {

    @RequestMapping(method = RequestMethod.GET)
    public String returnIndex(Model model)
    {
        return "index";
    }

    @RequestMapping(value = "/allusers",method = RequestMethod.GET)
    public String returnAllUsers(Model model)
    {
        return "allUsers";
    }

    @RequestMapping(value = "/newuser",method = RequestMethod.GET)
    public String addNewUser(Model model)
    {
        return "newUser";
    }
}
