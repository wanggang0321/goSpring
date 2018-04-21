package com.luwak.demo.action;

import com.luwak.demo.service.IDemoService;
import com.luwak.gospring.annotation.Autowired;
import com.luwak.gospring.annotation.Controller;
import com.luwak.gospring.annotation.RequestMapping;
import com.luwak.gospring.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/demo")
public class DemoAction {

    @Autowired
    private IDemoService demoService;

    @RequestMapping("/queryName")
    public void queryName(HttpServletRequest request, HttpServletResponse response, @RequestParam("name") String name) {
        String result = demoService.getName(name);
        System.out.println(result);
    }

}
