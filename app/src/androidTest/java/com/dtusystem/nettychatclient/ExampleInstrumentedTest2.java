package com.dtusystem.nettychatclient;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dtusystem.nettychatclient.network.handler.EventHandler;
import com.dtusystem.nettychatclient.network.handler.LocalRequestHandler;
import com.dtusystem.nettychatclient.network.handler.RemoteRequestHandlers;
import com.dtusystem.nettychatclient.network.message.Formula;
import com.dtusystem.nettychatclient.network.message.HeartBeat;
import com.dtusystem.nettychatclient.network.message.Message;
import com.dtusystem.nettychatclient.network.message.Response;
import com.dtusystem.nettychatclient.network.protocol.MessageCodecSharable;
import com.dtusystem.nettychatclient.network.serializer.Serializer;
import com.dtusystem.nettychatclient.network.utils.RequestWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest2 {
    private final static Serializer serializer = new JsonSerializer();
    private final EmbeddedChannel channel = new EmbeddedChannel(
            new LoggingHandler(LogLevel.DEBUG),
            new MessageCodecSharable(),
            new EventHandler(),
            new LocalRequestHandler(),
            new RemoteRequestHandlers()
    );

    public static ByteBuf messageToByteBuf(Message msg) {
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        // 1. 4 字节的魔数
        out.writeBytes(new byte[]{'b', 'a', 'b', 'y'});
        // 2. 1 字节的版本,
        out.writeByte(1);
        // 3. 1 字节的序列化方式 jdk 0 , json 1
        out.writeByte(1);
        // 4. 1 字节的指令类型
        out.writeByte(msg.getMessageCode());
        // 5. 4 个字节
        out.writeInt(99);
        // 无意义，对齐填充
        out.writeByte(0xff);
        // 6. 获取内容的字节数组
        byte[] bytes = serializer.serialize(msg);
        // 7. 长度
        out.writeInt(bytes.length);
        // 8. 写入内容
        out.writeBytes(bytes);

        return out;
    }

    @Test
    public void testWriteOut() {
        Formula formula = new Formula(1, 1, 1, 2, 2, 2);
        channel.writeOutbound(new RequestWrapper(formula, null));
        Response<String> exampleMessage = new Response<>(true, "this is a formula", "abc");
        channel.writeOutbound(new RequestWrapper(exampleMessage, null));
        HeartBeat heartBeat = new HeartBeat();
        channel.writeOutbound(new RequestWrapper(heartBeat, null));
    }

    @Test
    public void testWriteIn() {
        Response<String> exampleMessage = new Response<>(true, "this is a formula", "abc");
        channel.writeInbound(messageToByteBuf(exampleMessage));
        Formula formula = new Formula(1, 1, 1, 2, 2, 2);
        channel.writeInbound(messageToByteBuf(formula));
        HeartBeat heartBeat = new HeartBeat();
        channel.writeInbound(messageToByteBuf(heartBeat));
    }

    public static class JsonSerializer implements Serializer {
        private static final Gson gson;

        static {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
        }

        @Override
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            String json = new String(bytes, StandardCharsets.UTF_8);
            Object object = gson.fromJson(json, clazz);

            return (T) object;
        }

        @Override
        public <T> byte[] serialize(T object) {
            String str = gson.toJson(object);
            return str.getBytes(StandardCharsets.UTF_8);
        }
    }
}
