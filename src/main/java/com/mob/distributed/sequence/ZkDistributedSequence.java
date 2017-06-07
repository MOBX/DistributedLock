/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.distributed.sequence;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.lamfire.logger.Logger;
import com.lamfire.logger.LoggerFactory;
import com.mob.distributed.DistributedSequence;
import com.mob.distributed.lock.zk.ZkReentrantLockCleanerTask;

/**
 * @author zxc Jun 27, 2016 6:09:12 PM
 */
public class ZkDistributedSequence implements DistributedSequence {

    private static final Logger log             = LoggerFactory.getLogger(ZkReentrantLockCleanerTask.class);

    private CuratorFramework    client;
    private int                 maxRetries      = 3;
    private final int           baseSleepTimeMs = 1000;

    public ZkDistributedSequence(String zookeeperAddress) {
        try {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
            client = CuratorFrameworkFactory.newClient(zookeeperAddress, retryPolicy);
            client.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } catch (Throwable ex) {
            ex.printStackTrace();
            log.error(ex.getMessage(), ex);
        }
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public Long sequence(String sequenceName) {
        try {
            int value = client.setData().withVersion(-1).forPath("/" + sequenceName, "".getBytes()).getVersion();
            return new Long(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
