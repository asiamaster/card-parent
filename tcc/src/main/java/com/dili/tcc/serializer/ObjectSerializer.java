
package com.dili.tcc.serializer;

/**
 * ObjectSerializer.
 *
 * @author xiaoyu
 */
public interface ObjectSerializer {

    byte[] serialize(Object obj);


    <T> T deSerialize(byte[] param, Class<T> clazz);

}
