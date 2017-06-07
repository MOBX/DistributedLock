/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.distributed;

/**
 * 分布式锁模板类
 * 
 * @author zxc Jun 27, 2016 6:06:51 PM
 */
public interface DistributedLockTemplate {

    /**
     * @param lockId 锁id(对应业务唯一ID)
     * @param timeout 单位毫秒
     * @param callback 回调函数
     * @return Object
     */
    public Object execute(String lockId, int timeout, Callback callback);
}
