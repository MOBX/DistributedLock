/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.distributed.lock.zk;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;

import com.lamfire.logger.Logger;
import com.lamfire.logger.LoggerFactory;
import com.mob.distributed.Callback;
import com.mob.distributed.DistributedLockTemplate;

/**
 * @author zxc Jun 27, 2016 6:08:03 PM
 */
public class ZkDistributedLockTemplate implements DistributedLockTemplate {

    private static final Logger log = LoggerFactory.getLogger(ZkDistributedLockTemplate.class);

    private CuratorFramework    client;

    public ZkDistributedLockTemplate(CuratorFramework client) {
        this.client = client;
    }

    private boolean tryLock(ZkReentrantLock distributedReentrantLock, Long timeout) throws Exception {
        return distributedReentrantLock.tryLock(timeout, TimeUnit.MILLISECONDS);
    }

    public Object execute(String lockId, int timeout, Callback callback) {
        ZkReentrantLock distributedReentrantLock = null;
        boolean getLock = false;
        try {
            distributedReentrantLock = new ZkReentrantLock(client, lockId);
            if (tryLock(distributedReentrantLock, new Long(timeout))) {
                getLock = true;
                return callback.onGetLock();
            } else {
                return callback.onTimeout();
            }
        } catch (InterruptedException ex) {
            log.error(ex.getMessage(), ex);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (getLock) distributedReentrantLock.unlock();
        }
        return null;
    }
}
