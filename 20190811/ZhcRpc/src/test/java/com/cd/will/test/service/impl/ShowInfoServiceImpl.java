package com.cd.will.test.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cd.will.test.service.IShowInfoService;

public class ShowInfoServiceImpl implements IShowInfoService {

    private static SimpleDateFormat NOW_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String whoIAm() {
        return this.getClass().getName();
    }

    @Override
    public String showTodayInfo() {
        return "今天是【" + DATE_FORMAT.format(new Date()) + "】，又是奋斗的一天！";
    }

    @Override
    public String sayHello(String yourName) {
        return "hello world," + yourName;
    }
}
