/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.distributed;

import java.util.concurrent.TimeUnit;

/**
 * @author zxc Jun 27, 2016 6:07:25 PM
 */
public interface DistributedReentrantLock {

    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException;

    public void unlock();
}
