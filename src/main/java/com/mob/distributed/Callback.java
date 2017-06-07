/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.distributed;

/**
 * @author zxc Jun 27, 2016 6:06:30 PM
 */
public interface Callback {

    public Object onGetLock() throws InterruptedException;

    public Object onTimeout() throws InterruptedException;
}
