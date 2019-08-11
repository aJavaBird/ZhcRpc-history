package com.cd.will.test.service.check;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cd.will.rpc.spring.ZhcRpcReference;
import com.cd.will.rpc.spring.ZhcRpcRegistry;
import com.cd.will.rpc.spring.ZhcRpcService;
import com.cd.will.test.service.IShowInfoService;

/** 自定义标签获取bean 测试 */
public class BeanCheck04 {

    private static Logger logger = Logger.getLogger(BeanCheck04.class);

    public static void main(String[] args) {

        ApplicationContext beanFactory = new ClassPathXmlApplicationContext(new String[] { "check/test04/beans.xml" },
                true);

        // registry 测试
        ZhcRpcRegistry rpcRegistry = beanFactory.getBean("tzlRegistry", ZhcRpcRegistry.class);
        System.out.println("rpcRegistry -- " + rpcRegistry);

        // service 测试
        ZhcRpcService zhcRpcService = beanFactory.getBean("com.cd.will.test.service.IShowInfoService",
                ZhcRpcService.class);
        IShowInfoService showInfoService = (IShowInfoService) zhcRpcService.getServiceBean();

        logger.info(showInfoService.whoIAm());
        logger.info(showInfoService.showTodayInfo());

        IShowInfoService showInfoService01 = beanFactory.getBean("showInfoService", IShowInfoService.class);

        logger.info(showInfoService == showInfoService01); // 查看两个 bean 对象是否是同一个对象  true

        // reference 测试
        IShowInfoService showInfoServiceRef = beanFactory.getBean("showInfoServiceRef", IShowInfoService.class);
        logger.info("showInfoServiceRef -- " + showInfoServiceRef); // 打印为null，但是并不是null，是因为它没实现toString 方法
        logger.info("reference:whoIAm -- " + showInfoServiceRef.whoIAm());

        logger.info(ZhcRpcReference.referenceInfoMap);

        logger.info("end");

    }

}
