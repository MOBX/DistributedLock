/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.distributed;

/**
 * @author zxc Jun 27, 2016 6:09:12 PM
 */
public interface DistributedSequence {

    public Long sequence(String sequenceName);
}
