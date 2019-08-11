package com.cd.will.test.service.check;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cd.will.test.service.IShowInfoService;

/** 纯 spring bean 测试 */
public class BeanCheck01 {

    private static Logger logger = Logger.getLogger(BeanCheck01.class);

    public static void main(String[] args) {

        ApplicationContext beanFactory = new ClassPathXmlApplicationContext(new String[] { "check/test01/beans.xml" },
                true);
        IShowInfoService showInfoService = beanFactory.getBean("showInfoService", IShowInfoService.class);
        logger.info(showInfoService.whoIAm());
        logger.info(showInfoService.showTodayInfo());

    }

}
