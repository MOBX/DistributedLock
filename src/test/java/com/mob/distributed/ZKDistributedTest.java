/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.distributed;

import java.util.Properties;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.lamfire.logger.Logger;
import com.lamfire.utils.PropertiesUtils;
import com.mob.distributed.lock.zk.ZkDistributedLockTemplate;
import com.mob.distributed.support.ThreadUtil;

/**
 * zk分布式锁测试
 * 
 * @author zxc Jun 7, 2017 3:29:47 PM
 */
public class ZKDistributedTest {

    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            ThreadUtil.submitTask(new CronWorker());
        }
    }

    static class CronWorker implements Runnable {

        private static Logger       logger = Logger.getLogger(CronWorker.class);

        private static Properties   pro;
        private static final String ZK_ADDRESS;

        static {
            pro = PropertiesUtils.load("zk.properties", CronWorker.class);
            ZK_ADDRESS = pro.getProperty("zookeeper.connect");
            pro = null;
        }

        @Override
        public void run() {
            logger.info("CronWorker start doCrontab!");
            try {
                RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
                CuratorFramework client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, retryPolicy);
                client.start();
                final ZkDistributedLockTemplate template = new ZkDistributedLockTemplate(client);
                template.execute("异步任务", 10, new Callback() {

                    @Override
                    public Object onGetLock() throws InterruptedException {
                        logger.info("获得分布式锁");
                        try {
                            doCrontab();
                        } catch (Exception e) {
                            logger.info("doCrontab error!", e);
                        }
                        Thread.sleep(10000);
                        return null;
                    }

                    @Override
                    public Object onTimeout() throws InterruptedException {
                        logger.info("未获得分布式锁,放弃执行任务");
                        return null;
                    }
                });
            } catch (Exception e) {
                logger.info("CronWorker do work exception!", e);
            } catch (Throwable t) {
                logger.info("CronWorker do work error!", t);
            }
        }

        protected void doCrontab() {
            logger.info("now to do,time=" + System.currentTimeMillis());
        }

        public synchronized void close() {

        }
    }
}
