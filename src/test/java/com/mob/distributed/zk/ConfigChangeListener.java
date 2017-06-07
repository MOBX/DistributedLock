/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.distributed.zk;

/**
 * 监听器,监听配置的改变
 * 
 * @author zxc Nov 25, 2015 1:44:17 PM
 */
public interface ConfigChangeListener {

    public abstract void configChanged(String paramStr1, String paramStr2);
}
