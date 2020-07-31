package com.dili.tcc.repository;

import com.dili.tcc.bean.TccTransaction;
import com.dili.tcc.serializer.ObjectSerializer;
import com.dili.tcc.util.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/29 16:27
 * @Description:
 */
public class RedisTccTransactionRepository implements TccTransactionRepository {

    private String keyPrefix;

    private ObjectSerializer serializer;

    @Autowired
    private RedisTemplate<String,Byte[]> redisTemplate;


    @PostConstruct
    public void init() {
        String name = SpringContext.getPropertyVal("spring.application.name");
        this.keyPrefix = String.format("tcc:%s:", name);
    }

    @Override
    public int create(TccTransaction transaction) {
        final String redisKey = this.keyPrefix + transaction.getTransId();
     //   redisTemplate.opsForValue().set(redisKey,);
       // jedisClient.set(redisKey, RepositoryConvertUtils.convert(hmilyTransaction, objectSerializer));
        return 1;
    }

    @Override
    public void setSerializer(ObjectSerializer serializer) {
        this.serializer = serializer;
    }

}
