package cn.faury.fdk.shiro.core;

import java.io.Serializable;
import java.util.Date;

/**
 * Session实体
 */
public class SessionBean implements Serializable {

    /**
     * 唯一标示键
     */
    private String key;

    /**
     * 值
     */
    private byte[] value;

    /**
     * 用户名
     */
    private String principal;

    /**
     * 主机
     */
    private String host;

    /**
     * 系统编码
     */
    private String sysCode;

    /**
     * 开始时间
     */
    private Date startTimestamp;

    /**
     * 最后进入时间
     */
    private Date lastAccessTime;

    /**
     * 超时
     */
    private Date timeoutTime;

    public String getKey() {
        return key;
    }

    public SessionBean setKey(String key) {
        this.key = key;
        return this;
    }

    public byte[] getValue() {
        return value;
    }

    public SessionBean setValue(byte[] value) {
        this.value = value;
        return this;
    }

    public String getPrincipal() {
        return principal;
    }

    public SessionBean setPrincipal(String principal) {
        this.principal = principal;
        return this;
    }

    public String getHost() {
        return host;
    }

    public SessionBean setHost(String host) {
        this.host = host;
        return this;
    }

    public String getSysCode() {
        return sysCode;
    }

    public SessionBean setSysCode(String sysCode) {
        this.sysCode = sysCode;
        return this;
    }

    public Date getStartTimestamp() {
        return startTimestamp;
    }

    public SessionBean setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
        return this;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public SessionBean setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
        return this;
    }

    public Date getTimeoutTime() {
        return timeoutTime;
    }

    public SessionBean setTimeoutTime(Date timeoutTime) {
        this.timeoutTime = timeoutTime;
        return this;
    }
}
