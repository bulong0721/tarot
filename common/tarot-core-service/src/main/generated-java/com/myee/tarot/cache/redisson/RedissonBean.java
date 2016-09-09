package com.myee.tarot.cache.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 2016/9/5.
 */
public class RedissonBean implements FactoryBean<RedissonClient>, InitializingBean, DisposableBean {
    private RedissonClient redisson;

    private int      idleConnectionTimeout      = 10000;
    private int      pingTimeout                = 1000;
    private int      connectTimeout             = 1000;
    private int      timeout                    = 1000;
    private int      retryAttempts              = 3;
    private int      retryInterval              = 1000;
    private int      reconnectionTimeout        = 3000;
    private int      failedAttempts             = 3;
    private String   password                   = null;
    private int      subscriptionsPerConnection = 5;
    private String   clientName                 = null;
    private String[] nodeAddresses              = new String[0];
    private Codec    codec                      = new FastJsonCodec();
    private ReadMode readMode                   = ReadMode.MASTER;
    private String   masterAddress              = "127.0.0.1:6379";

    @Override
    public RedissonClient getObject() throws Exception {
        return redisson;
    }

    @Override
    public Class<?> getObjectType() {
        return RedissonClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Config config = new Config();
        config.setCodec(codec)
                .useMasterSlaveServers()
//                .setScanInterval(scanInterval)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setPingTimeout(pingTimeout)
                .setConnectTimeout(connectTimeout)
                .setTimeout(timeout)
                .setRetryAttempts(retryAttempts)
                .setRetryInterval(retryInterval)
                .setReconnectionTimeout(reconnectionTimeout)
                .setFailedAttempts(failedAttempts)
                .setPassword(password)
                .setSubscriptionsPerConnection(subscriptionsPerConnection)
                .setClientName(clientName)
                .setMasterAddress(masterAddress);
        redisson = Redisson.create(config);
    }

    @Override
    public void destroy() throws Exception {
        if (null != redisson) {
            redisson.shutdown();
        }
    }

    public RedissonClient getRedisson() {
        return redisson;
    }

    public void setRedisson(RedissonClient redisson) {
        this.redisson = redisson;
    }

    public int getIdleConnectionTimeout() {
        return idleConnectionTimeout;
    }

    public void setIdleConnectionTimeout(int idleConnectionTimeout) {
        this.idleConnectionTimeout = idleConnectionTimeout;
    }

    public int getPingTimeout() {
        return pingTimeout;
    }

    public void setPingTimeout(int pingTimeout) {
        this.pingTimeout = pingTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getRetryAttempts() {
        return retryAttempts;
    }

    public void setRetryAttempts(int retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    public int getReconnectionTimeout() {
        return reconnectionTimeout;
    }

    public void setReconnectionTimeout(int reconnectionTimeout) {
        this.reconnectionTimeout = reconnectionTimeout;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSubscriptionsPerConnection() {
        return subscriptionsPerConnection;
    }

    public void setSubscriptionsPerConnection(int subscriptionsPerConnection) {
        this.subscriptionsPerConnection = subscriptionsPerConnection;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String[] getNodeAddresses() {
        return nodeAddresses;
    }

    public void setNodeAddresses(String[] nodeAddresses) {
        this.nodeAddresses = nodeAddresses;
    }

    public Codec getCodec() {
        return codec;
    }

    public void setCodec(Codec codec) {
        this.codec = codec;
    }

    public ReadMode getReadMode() {
        return readMode;
    }

    public void setReadMode(ReadMode readMode) {
        this.readMode = readMode;
    }

    public String getMasterAddress() {
        return masterAddress;
    }

    public void setMasterAddress(String masterAddress) {
        this.masterAddress = masterAddress;
    }
}
