package com.dili.tcc.common;

import com.dili.tcc.common.TccRemoteInfo;
import com.dili.tcc.common.TccStatus;
import com.dili.tcc.common.TransactionId;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/6 16:47
 * @Description:
 */
public class TccContext {
    /**阶段重试次数，cancel和confirm阶段重试*/
    private int retryNum;

    private TccStatus tccStatus;
    /**请求参数*/
    private Object requestDto;

    private TransactionId transactionId;
    /**额外的传递属性*/
    private Map<String, Object> attributes = new HashMap<>();
    /**每次执行到下一个阶段的时候需要清除该参数*/
    private Map<String, TccRemoteInfo> remoteOfSucceed = new HashMap<>();

    public void addRemote(TccRemoteInfo info) {
        remoteOfSucceed.put(getKey(info), info);
    }

    public TccRemoteInfo getRemote(TccRemoteInfo info) {
        return remoteOfSucceed.get(getKey(info));
    }

    public void clearRemote() {
        remoteOfSucceed.clear();
    }

    /**
     * 清理上一个阶段的数据
     * @author miaoguoxin
     * @date 2020/7/14
     */
    public void clearPrevious() {
        this.remoteOfSucceed.clear();
        this.retryNum = 0;
    }

    public void addAttr(String key, Object val) {
        attributes.put(key, val);
    }

    public <T> T getAttr(String key, Class<T> clazz) {
        Object o = attributes.get(key);
        if (o == null) {
            return null;
        }
        if (clazz.isAssignableFrom(o.getClass())) {
            return (T) o;
        }
        throw new ClassCastException(String.format("can not cast class %s to %s", o.getClass().getName(), clazz.getName()));
    }

    /**
     * 加重试次数+1
     * @author miaoguoxin
     * @date 2020/7/14
     */
    public void incRetryNum() {
        this.retryNum++;
    }

    public void changeStatus(TccStatus tccStatus) {
        this.tccStatus = tccStatus;
    }

    public Object getRequestDto() {
        return requestDto;
    }

    public void setRequestDto(Object requestDto) {
        this.requestDto = requestDto;
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(TransactionId transactionId) {
        this.transactionId = transactionId;
    }

    public TccStatus getTccStatus() {
        return tccStatus;
    }


    public int getRetryNum() {
        return retryNum;
    }

    /**
     * 如果相同方法相同请求参数重复调用，这里暂时判断不了
     * @author miaoguoxin
     * @date 2020/7/14
     */
    private static String getKey(TccRemoteInfo info) {
        String className = info.getMethod().getDeclaringClass().getSimpleName();
        String methodName = info.getMethod().getName();
        StringBuilder sb = new StringBuilder();
        for (Object arg : info.getArgs()) {
            sb.append(arg.getClass().getName());
        }
        return className + "#" + methodName + "#" + sb.toString();
    }

}
