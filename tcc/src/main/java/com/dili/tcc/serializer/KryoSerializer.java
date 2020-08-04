package com.dili.tcc.serializer;

import com.dili.tcc.bean.TccParticipant;
import com.dili.tcc.bean.TccTransaction;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/30 15:21
 */
public class KryoSerializer implements ObjectSerializer {
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); Output output = new Output(outputStream)) {
            //获取kryo对象
            Kryo kryo = new Kryo();
            kryo.writeObject(output, obj);
            bytes = output.toBytes();
            output.flush();
        } catch (IOException ex) {
            throw new RuntimeException("kryo serialize error", ex);
        }
        return bytes;
    }

    @Override
    public <T> T deSerialize(byte[] param, Class<T> clazz) {
        T object;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(param)) {
            Kryo kryo = new Kryo();
            Input input = new Input(inputStream);
            object = kryo.readObject(input, clazz);
            input.close();
        } catch (IOException ex) {
            throw new RuntimeException("kryo deSerialize error", ex);
        }
        return object;
    }

}
