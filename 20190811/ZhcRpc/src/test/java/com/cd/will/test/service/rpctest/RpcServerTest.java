package com.cd.will.test.service.rpctest;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cd.will.rpc.spring.ZhcRpcRegistry;
import com.cd.will.rpc.spring.ZhcRpcService;
import com.cd.will.test.service.IShowInfoService;

/** rpc-server 测试 */
public class RpcServerTest {

    private static Logger logger = Logger.getLogger(RpcServerTest.class);

    public static void main(String[] args) {

        ApplicationContext beanFactory = new ClassPathXmlApplicationContext(
                new String[] { "rpctest/test01/beans-server.xml" },
                true);

        // registry 测试
        ZhcRpcRegistry rpcRegistry = beanFactory.getBean("tzlRegistry", ZhcRpcRegistry.class);
        System.out.println("rpcRegistry -- " + rpcRegistry);

        // service 测试
        ZhcRpcService zhcRpcService = beanFactory.getBean("com.cd.will.test.service.IShowInfoService",
                ZhcRpcService.class);
        IShowInfoService showInfoService = (IShowInfoService) zhcRpcService.getServiceBean();
        logger.info("showInfoService--" + showInfoService);

        //        CountDownLatch countDownLatch = new CountDownLatch(1);
        //        try {
        //            countDownLatch.await();
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }

    }

}
