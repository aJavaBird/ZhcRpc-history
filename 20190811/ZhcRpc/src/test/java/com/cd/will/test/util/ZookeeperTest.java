package com.cd.will.test.util;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import com.cd.will.rpc.util.ZookeeperUtil;

/**
 * Zookeeper Test
 */
public class ZookeeperTest {

    /** zookeeper地址 */
    static final String CONNECT_ADDR = "127.0.0.1:2181";

    /** session超时时间 */
    static final int SESSION_OUTTIME = 2000;// ms

    /** 信号量，阻塞程序执行，用于等待zookeeper连接成功，发送成功信号 */
    static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

    private static Logger logger = Logger.getLogger(ZookeeperTest.class);

    public static void main(String[] args) throws Exception {

        ZooKeeper zk = ZookeeperUtil.getZooKeeperCli(CONNECT_ADDR, SESSION_OUTTIME);

        logger.info("test start...");

        // 删除节点
        ZookeeperUtil.deleteNode("/zhcrpc", zk);

        // 创建父节点
        zk.create("/zhcrpc", null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        // 创建子节点
        zk.create("/zhcrpc/com.cd.will.test.service.IShowInfoService", null, Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        // 创建子节点
        zk.create("/zhcrpc/com.cd.will.test.service.IShowInfoService/providers", null, Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        zk.create("/zhcrpc/com.cd.will.test.service.IShowInfoService/consumers", null, Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);

        zk.create(
                "/zhcrpc/com.cd.will.test.service.IShowInfoService/providers"
                        + "/zhcrpc%3a%2f%2f127.0.0.1%3a5167%2fcom.cd.will.test.service.IShowInfoService%3fapplication%3dmyApplication%26zhcrpc%3d0.0.1-SNAPSHOT%26group%3dmq%26methods%3dwhoIAm%2cshowTodayInfo%26pid%3d39333%26timeout%3d60000",
                null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zk.create("/zhcrpc/com.cd.will.test.service.IShowInfoService/consumers"
                        + "/zhcrpc%3a%2f%2f127.0.0.1%3a5167%2fcom.cd.will.test.service.IShowInfoService%3fapplication%3dmyApplication%26zhcrpc%3d0.0.1-SNAPSHOT%26group%3dmq%26methods%3dwhoIAm%2cshowTodayInfo%26pid%3d39333%26timeout%3d60000",
                null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        // 获取节点洗信息
        byte[] data = zk.getData("/zhcrpc/com.cd.will.test.service.IShowInfoService", false, null);
        logger.info("/zhcrpc/com.cd.will.test.service.IShowInfoService.value == null --- " + (data == null));
        System.out.println(new String(data));
        System.out.println(zk.getChildren("/zhcrpc/com.cd.will.test.service.IShowInfoService", false));

        // 创建子节点
        // zk.create("/testRoot/children", "children data".getBytes(),
        // Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        // 获取节点洗信息
        // byte[] data = zk.getData("/testRoot", false, null);
        // System.out.println(new String(data));
        // System.out.println(zk.getChildren("/testRoot", false));

        // 修改节点的值
        // zk.setData("/testRoot", "modify data root".getBytes(), -1);
        // byte[] data = zk.getData("/testRoot", false, null);
        // System.out.println(new String(data));

        // 判断节点是否存在
        // System.out.println(zk.exists("/testRoot/children", false));
        // 删除节点
        // zk.delete("/testRoot/children", -1);
        // System.out.println(zk.exists("/testRoot/children", false));

        logger.info("test end...");

        zk.close();

    }

}

