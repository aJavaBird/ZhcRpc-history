package com.cd.will.test.service;

/** 
 * 测试-service
 * 展示信息的 service */
public interface IShowInfoService {
    /** 返回当前类路径*/
    public String whoIAm();

    /** 打印今天的日期信息*/
    public String showTodayInfo();

    /** 打印hello */
    public String sayHello(String yourName);
}
