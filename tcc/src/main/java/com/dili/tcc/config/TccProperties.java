package com.dili.tcc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/8/1 14:54
 * @Description:
 */
@ConfigurationProperties(prefix = "tcc")
public class TccProperties {
    private int bufferSize = 4096 * 2 * 2;

    private int consumerThreads = Runtime.getRuntime().availableProcessors() << 1;

    private int asyncThreads = Runtime.getRuntime().availableProcessors() << 1;

    private int scheduledThreadMax = Runtime.getRuntime().availableProcessors() << 1;

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getConsumerThreads() {
        return consumerThreads;
    }

    public void setConsumerThreads(int consumerThreads) {
        this.consumerThreads = consumerThreads;
    }

    public int getAsyncThreads() {
        return asyncThreads;
    }

    public void setAsyncThreads(int asyncThreads) {
        this.asyncThreads = asyncThreads;
    }

    public int getScheduledThreadMax() {
        return scheduledThreadMax;
    }

    public void setScheduledThreadMax(int scheduledThreadMax) {
        this.scheduledThreadMax = scheduledThreadMax;
    }
}
