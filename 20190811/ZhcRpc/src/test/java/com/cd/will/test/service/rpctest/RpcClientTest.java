package com.cd.will.test.service.rpctest;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cd.will.rpc.spring.ZhcRpcRegistry;
import com.cd.will.test.service.IShowInfoService;

/** rpc-client 测试 */
public class RpcClientTest {

    private static Logger logger = Logger.getLogger(RpcClientTest.class);

    public static void main(String[] args) {

        ApplicationContext beanFactory = new ClassPathXmlApplicationContext(
                new String[] { "rpctest/test01/beans-client.xml" },
                true);

        // registry 测试
        ZhcRpcRegistry rpcRegistry = beanFactory.getBean("tzlRegistry", ZhcRpcRegistry.class);
        System.out.println("rpcRegistry -- " + rpcRegistry);

        IShowInfoService showInfoService = beanFactory.getBean("showInfoService", IShowInfoService.class);
        logger.info("showInfoService -- " + showInfoService); // 打印为null，但是并不是null，是因为它没实现toString 方法
        logger.info("reference:whoIAm -- " + showInfoService.whoIAm());
        logger.info("reference:sayHello -- " + showInfoService.sayHello("Will"));

    }

}
