package com.luwak.demo.service.impl;

import com.luwak.demo.service.IDemoService;
import com.luwak.gospring.annotation.Service;

@Service
public class DemoService implements IDemoService {

    public String getName(String name) {

        return "My name is " + name;
    }

}
