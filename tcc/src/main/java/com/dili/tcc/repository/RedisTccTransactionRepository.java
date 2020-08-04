package com.dili.tcc.repository;

import com.dili.tcc.bean.TccTransaction;
import com.dili.tcc.serializer.ObjectSerializer;
import com.dili.tcc.util.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/29 16:27
 * @Description:
 */
public class RedisTccTransactionRepository implements TccTransactionRepository {
    private String keyPrefix;
    @Autowired
    private ObjectSerializer serializer;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @PostConstruct
    public void init() {
        String name = SpringContext.getPropertyVal("spring.application.name");
        this.keyPrefix = String.format("tcc:%s:", name);
    }

    @Override
    public int create(TccTransaction transaction) {
        final String redisKey = this.keyPrefix + transaction.getTransId();
        byte[] contents = serializer.serialize(transaction);
        Boolean execute = redisTemplate.execute((RedisCallback<Boolean>) connection ->
                connection.set(redisKey.getBytes(StandardCharsets.UTF_8), contents));
        return execute != null && execute ? 1 : 0;
    }

    @Override
    public TccTransaction findById(String id) {
        try {
            final String redisKey = this.keyPrefix + id;
            byte[] contents = redisTemplate.execute((RedisCallback<byte[]>) connection ->
                    connection.get(redisKey.getBytes(StandardCharsets.UTF_8)));
            return serializer.deSerialize(contents, TccTransaction.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int update(TccTransaction transaction) {
        final String redisKey = this.keyPrefix + transaction.getTransId();
        transaction.setLastTime(new Date());
        byte[] contents = serializer.serialize(transaction);
        Boolean execute = redisTemplate.execute((RedisCallback<Boolean>) connection ->
                connection.set(redisKey.getBytes(StandardCharsets.UTF_8), contents));
        return execute != null && execute ? 1 : 0;
    }

    @Override
    public int remove(String id) {
        final String redisKey = this.keyPrefix + id;
        if (redisTemplate.hasKey(redisKey)) {
            redisTemplate.delete(id);
        }
        return 1;
    }


}
