package com.dtusystem.nettychatclient.network.serializer;


import java.lang.reflect.Type;

public interface Serializer {
    /**
     * 反序列化
     * */
    <T> T deserialize(Class<T> clazz, byte[] objectByte);

    /**
     * 序列化
     * */
    <T> byte[] serialize(T obj);
}