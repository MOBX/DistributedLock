/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.distributed.lock.redis;

import java.util.concurrent.TimeUnit;

import com.mob.distributed.DistributedReentrantLock;

/**
 * @author zxc Jun 7, 2017 3:32:06 PM
 */
public class RedisReentrantLock implements DistributedReentrantLock {

    @Override
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

    }
}
