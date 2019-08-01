package com.cd.will.rpc.util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperUtil {

    private static Logger logger = Logger.getLogger(ZookeeperUtil.class);

    public static ZooKeeper getZooKeeperCli(String connectAddr, int sessionOuttime)
            throws IOException, InterruptedException {
        final CountDownLatch connectedSemaphore = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper(connectAddr, sessionOuttime, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 获取事件的状态
                KeeperState keeperState = event.getState();
                EventType eventType = event.getType();
                // 如果是建立连接
                if (KeeperState.SyncConnected == keeperState) {
                    if (EventType.None == eventType) {
                        // 如果建立连接成功，则发送信号量，让后续阻塞程序向下执行
                        logger.info("zk[addr=" + connectAddr + "] 建立连接");
                        connectedSemaphore.countDown();
                    }
                }
            }
        });
        connectedSemaphore.await();
        return zk;
    }

    /**
     * 清空子节点
     * @throws InterruptedException 
     * @throws KeeperException 
     */
    public static void clearChildNode(String znodePath, ZooKeeper zk) throws KeeperException, InterruptedException {

        List<String> children = zk.getChildren(znodePath, false);

        for (String child : children) {
            String childNode = znodePath + "/" + child;
            if (zk.getChildren(childNode, null).size() != 0) {
                clearChildNode(childNode, zk);
            }
            zk.delete(childNode, -1);
        }
    }

    /**
     * 删除一个节点，不管有有没有任何子节点
     */
    public static boolean deleteNode(String path, ZooKeeper zk) throws Exception {
        //看看传入的节点是否存在
        if ((zk.exists(path, false)) != null) {
            //查看该节点下是否还有子节点
            List<String> children = zk.getChildren(path, false);
            //如果没有子节点，直接删除当前节点
            if (children.size() == 0) {
                zk.delete(path, -1);
            } else {
                //如果有子节点，则先遍历删除子节点
                for (String child : children) {
                    deleteNode(path + "/" + child, zk);
                }
                //删除子节点之后再删除之前子节点的父节点
                deleteNode(path, zk);
            }
            return true;
        } else {
            //如果传入的路径不存在直接返回不存在
            System.out.println(path + " not exist");
            return false;
        }
    }
}
