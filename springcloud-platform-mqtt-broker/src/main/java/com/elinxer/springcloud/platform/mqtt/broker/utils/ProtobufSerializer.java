/**
 * @description
 * @author caoxiaoguang
 * @create 2021-09-07 16:48
 **/
package com.elinxer.springcloud.platform.mqtt.broker.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ProtobufSerializer implements Serializer {
    private  Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();

    private  <T> Schema<T> getSchema(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.getSchema(clazz);
            if (schema != null) {
                cachedSchema.put(clazz, schema);
            }
        }
        return schema;
    }


    @Override
    public  byte[] serialize(Object obj) {
        @SuppressWarnings("unchecked")
        Class clazz = obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema schema = getSchema(clazz);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            T obj = clazz.newInstance();
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
            return obj;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
      /*  InternalMessage<Cloud.Message> internalMessage =new InternalMessage<>();
        Cloud.Message.Builder builder = Cloud.Message.newBuilder();
        Cloud.Command.Builder commandBuild = Cloud.Command.newBuilder();
        commandBuild.setWorkMode(Cloud.WorkMode.valueOf("WORK_MODE_STANDALONE"));
        builder.setCommand(commandBuild);
        internalMessage.setData(builder.build());
        internalMessage.setBrokerId(12323);
        internalMessage.setTimestamp(System.currentTimeMillis());

        ProtobufSerializer serializer =new ProtobufSerializer();

        byte[] bytes =serializer.serialize(internalMessage);

        InternalMessage internalMessage1 =serializer.deserialize(bytes,InternalMessage.class);
        System.out.println("-----------------");
        Cloud.Message message =(Cloud.Message) internalMessage.getData();
        System.out.println(message.getCommand().getWorkMode());*/

    }
}