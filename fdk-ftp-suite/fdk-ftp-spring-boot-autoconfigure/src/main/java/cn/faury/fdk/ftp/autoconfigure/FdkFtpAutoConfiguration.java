package cn.faury.fdk.ftp.autoconfigure;

import cn.faury.fdk.ftp.FTPUtil;
import cn.faury.fdk.ftp.pool.FTPPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FdkFtpProperties.class)
public class FdkFtpAutoConfiguration {

    @Autowired(required = false)
    private FdkFtpProperties fdkFtpProperties;

    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        // 能从池中借出的对象的最大数目。如果这个值不是正数，表示没有限制。
        config.setMaxTotal(fdkFtpProperties.getMaxActive());
        /*
         * 在池中借出对象的数目已达极限的情况下，调用它的borrowObject方法时的行为。 可以选用的值有：
         * GenericObjectPool.WHEN_EXHAUSTED_BLOCK，表示等待
         * GenericObjectPool
         * .WHEN_EXHAUSTED_GROW，表示创建新的实例（maxActive参数失去意义）；
         * GenericObjectPool
         * .WHEN_EXHAUSTED_FAIL，表示抛出一个java.util.NoSuchElementException异常
         * 。
         */
        config.setBlockWhenExhausted(true);
        // 若在对象池空时调用borrowObject方法的行为被设定成等待，最多等待多少毫秒。
        config.setMaxWaitMillis(fdkFtpProperties.getMaxWait());
        config.setMaxIdle(fdkFtpProperties.getMaxIdle());
        config.setMinIdle(fdkFtpProperties.getMinIdle());
        // 设定在借出对象时是否进行有效性检查。
        config.setTestOnBorrow(fdkFtpProperties.isTestOnBorrow());
        // 设定在还回对象时是否进行有效性检查。
        config.setTestOnReturn(fdkFtpProperties.isTestOnReturn());
        // 设定间隔每过多少毫秒进行一次后台对象清理的行动。如果不是正数，则不进行清理。
        config.setTimeBetweenEvictionRunsMillis(fdkFtpProperties.getTimeBetweenEvictionRunsMillis());
        // 设定在进行后台对象清理时，每次检查几个对象。
        config.setNumTestsPerEvictionRun(fdkFtpProperties.getNumTestsPerEvictionRun());
        // 设定在进行后台对象清理时，视休眠时间超过了多少毫秒的对象为过期。过期的对象将被回收。
        config.setMinEvictableIdleTimeMillis(fdkFtpProperties.getMinEvictableIdleTimeMillis());
        // 设定在进行后台对象清理时，是否还对没有过期的池内对象进行有效性检查。
        config.setTestWhileIdle(fdkFtpProperties.isTestWhileIdle());
        config.setSoftMinEvictableIdleTimeMillis(fdkFtpProperties.getSoftMinEvictableIdleTimeMillis());
        // 先进先出
        config.setLifo(fdkFtpProperties.isLifo());
        return config;
    }

    @Bean
    public FTPPool ftpPool(GenericObjectPoolConfig genericObjectPoolConfig){
        FTPPool pool = new FTPPool(genericObjectPoolConfig, fdkFtpProperties.getHostname(), fdkFtpProperties.getPort()
                , fdkFtpProperties.getUsername(), fdkFtpProperties.getPassword(), fdkFtpProperties.isPassiveModeConf());
        FTPUtil.init(pool);
        return pool;
    }
}
