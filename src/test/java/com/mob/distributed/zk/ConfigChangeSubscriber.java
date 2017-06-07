/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.distributed.zk;

import java.util.List;

/**
 * 配置改变的订阅者,在每一个zk文件上订阅一个监听器
 * 
 * @author zxc Nov 25, 2015 1:44:55 PM
 */
public interface ConfigChangeSubscriber {

    public abstract String getInitValue(String paramStr);

    public abstract void subscribe(String paramStr, ConfigChangeListener paramConfigChangeListener);

    public abstract List<String> listKeys();
}
