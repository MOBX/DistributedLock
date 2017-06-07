/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.distributed.zk;

import java.util.List;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import com.lamfire.utils.StringUtils;

/**
 * 订阅者实现类,当订阅到zk数据改变时,会触发ConfigChangeListener
 * 
 * @author zxc Nov 25, 2015 1:54:08 PM
 */
public class ZkConfigChangeSubscriberImpl implements ConfigChangeSubscriber {

    private ZkClient zkClient;
    private String   rootNode;

    public ZkConfigChangeSubscriberImpl(ZkClient zkClient, String rootNode) {
        this.rootNode = rootNode;
        this.zkClient = zkClient;
    }

    public void subscribe(String key, ConfigChangeListener listener) {
        String path = ZkUtils.getZkPath(this.rootNode, key);
        if (!this.zkClient.exists(path)) {
            throw new RuntimeException(
                                       "Configuration ("
                                               + path
                                               + ") does not exist, you must first define the configuration to monitor configuration changes, please check the configuration of the key is correct, if the key to confirm the configuration is correct, then you need to ensure that an order to release the configuration using the configuration!");
        }
        this.zkClient.subscribeDataChanges(path, new DataListenerAdapter(listener));
    }

    /**
     * 触发ConfigChangeListener
     * 
     * @param path
     * @param value
     * @param configListener
     */
    private void fireConfigChanged(String path, String value, ConfigChangeListener configListener) {
        configListener.configChanged(getKey(path), value);
    }

    private String getKey(String path) {
        String key = path;
        if (!StringUtils.isEmpty(this.rootNode)) {
            key = path.replaceFirst(this.rootNode, "");
            if (key.startsWith("/")) {
                key = key.substring(1);
            }
        }
        return key;
    }

    public String getInitValue(String key) {
        String path = ZkUtils.getZkPath(this.rootNode, key);
        return (String) this.zkClient.readData(path);
    }

    public List<String> listKeys() {
        return this.zkClient.getChildren(this.rootNode);
    }

    /**
     * 数据监听器适配类，当zk数据变化时，触发ConfigChangeListener
     */
    private class DataListenerAdapter implements IZkDataListener {

        private ConfigChangeListener configListener;

        public DataListenerAdapter(ConfigChangeListener configListener) {
            this.configListener = configListener;
        }

        public void handleDataChange(String s, Object obj) throws Exception {
            ZkConfigChangeSubscriberImpl.this.fireConfigChanged(s, (String) obj, this.configListener);
        }

        public void handleDataDeleted(String s) throws Exception {

        }
    }
}
