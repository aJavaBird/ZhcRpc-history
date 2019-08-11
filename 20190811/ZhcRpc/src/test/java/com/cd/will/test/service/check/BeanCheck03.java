package com.cd.will.test.service.check;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cd.will.rpc.spring.ZhcRpcReference;
import com.cd.will.rpc.spring.ZhcRpcRegistry;
import com.cd.will.rpc.spring.ZhcRpcService;
import com.cd.will.test.service.IShowInfoService;

/** 自定义标签获取bean 测试 */
public class BeanCheck03 {

    private static Logger logger = Logger.getLogger(BeanCheck03.class);

    public static void main(String[] args) {

        ApplicationContext beanFactory = new ClassPathXmlApplicationContext(new String[] { "check/test03/beans.xml" },
                true);

        ZhcRpcRegistry rpcRegistry = beanFactory.getBean("tzlRegistry", ZhcRpcRegistry.class);

        // rpcShowInfoService 为自定义标签  zhcrpc:service 的 id
        // 将 IShowInfoService 对象放入 ZhcRpcService 中，为后续  RPC 做准备
        IShowInfoService showInfoService = (IShowInfoService) beanFactory
                .getBean("rpcShowInfoService", ZhcRpcService.class).getServiceBean();

        logger.info(showInfoService.whoIAm());
        logger.info(showInfoService.showTodayInfo());

        IShowInfoService showInfoService01 = beanFactory.getBean("showInfoService", IShowInfoService.class);

        logger.info(showInfoService == showInfoService01); // 查看两个 bean 对象是否是同一个对象  true

        ZhcRpcReference rpcReference = beanFactory.getBean("showInfoServiceRef", ZhcRpcReference.class);
        logger.info("end");

    }

}
