/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.distributed.zk;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import com.lamfire.logger.Logger;
import com.lamfire.utils.StringUtils;

/**
 * @author zxc Nov 25, 2015 1:29:03 PM
 */
public class ZkUtils {

    private static final Logger logger = Logger.getLogger(ZkUtils.class);

    public static String getLocalIp() {
        InetAddress addr;
        try {
            addr = InetAddress.getLocalHost();
            return addr.getHostAddress().toString();
        } catch (UnknownHostException e) {
            logger.error("[ZkUtils getLocalIp error]", e);
            System.exit(1);
        }
        return StringUtils.EMPTY;
    }

    public static String getZkPath(String rootNode, String key) {
        if (!StringUtils.isEmpty(rootNode)) {
            if (key.startsWith("/")) {
                key = key.substring(1);
            }
            if (rootNode.endsWith("/")) {
                return rootNode + key;
            }
            return rootNode + "/" + key;
        }
        return key;
    }

    public static void mkPaths(ZkClient client, String path) {
        String[] subs = path.split("\\/");
        if (subs.length < 2) {
            return;
        }
        String curPath = "";
        for (int i = 1; i < subs.length; i++) {
            curPath = curPath + "/" + subs[i];
            if (!client.exists(curPath)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Trying to create zk node: " + curPath);
                }
                client.createPersistent(curPath);
                if (logger.isDebugEnabled()) logger.debug("Zk node created successfully: " + curPath);
            }
        }
    }

    public static void rmPaths(ZkClient client, String path) {
        String[] subs = path.split("\\/");
        if (subs.length < 2) {
            return;
        }
        String curPath = "";
        for (int i = 1; i < subs.length; i++) {
            curPath = curPath + "/" + subs[i];
            if (client.exists(curPath)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Trying to remove zk node: " + curPath);
                }
                client.deleteRecursive(curPath);
                if (logger.isDebugEnabled()) logger.debug("Zk node remove successfully: " + curPath);
            }
        }
    }

    public static String formatAsMonthDate(Date requestTime) {
        return new SimpleDateFormat("MMdd").format(requestTime);
    }

    public static class StringSerializer implements ZkSerializer {

        private String encoding;

        public StringSerializer(String encoding) {
            this.encoding = encoding;
        }

        public Object deserialize(byte[] abyte0) throws ZkMarshallingError {
            try {
                return new String(abyte0, this.encoding);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        public byte[] serialize(Object obj) throws ZkMarshallingError {
            if (obj == null) {
                return null;
            }
            if (!(obj instanceof String)) {
                throw new ZkMarshallingError("The input obj must be an instance of String.");
            }
            try {
                return ((String) obj).getBytes(this.encoding);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
