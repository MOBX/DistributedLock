/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.distributed.lock.redis;

import com.mob.distributed.Callback;
import com.mob.distributed.DistributedLockTemplate;

/**
 * @author zxc Jun 7, 2017 3:33:02 PM
 */
public class RedisDistributedLockTemplate implements DistributedLockTemplate {

    @Override
    public Object execute(String lockId, int timeout, Callback callback) {
        return null;
    }
}
