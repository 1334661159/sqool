package com.abuqool.sqool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author michael
 * @date 2019-06-12 21:25
 */

@Controller
@RequestMapping("/")
public class BaseWebController {

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(){
        return "index";
    }
    public static void main(String[] args) {

    }
}
