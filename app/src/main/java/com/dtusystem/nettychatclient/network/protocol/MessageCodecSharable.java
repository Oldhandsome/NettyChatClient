package com.dtusystem.nettychatclient.network.protocol;


import static com.dtusystem.nettychatclient.network.Utils.transformByteBuf2String;

import android.util.Log;

import com.dtusystem.nettychatclient.network.handler.NetworkMsg;
import com.dtusystem.nettychatclient.network.message.Message;
import com.dtusystem.nettychatclient.network.serializer.JsonSerializer;
import com.dtusystem.nettychatclient.network.serializer.Serializer;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;


@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, NetworkMsg> {

    private final Serializer serializer = new JsonSerializer();

    @Override
    protected void encode(ChannelHandlerContext ctx, NetworkMsg networkMsg, List<Object> outList) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        // 1. 4 字节的魔数
        out.writeBytes(new byte[]{'b', 'a', 'b', 'y'});
        // 2. 1 字节的版本,
        out.writeByte(1);
        // 3. 1 字节的序列化方式 jdk 0 , json 1
        out.writeByte(1);
        // 4. 1 字节的指令类型
        out.writeByte(networkMsg.getMessage().getMessageCode());
        // 5. 4 个字节
        out.writeInt(networkMsg.getId());
        // 无意义，对齐填充
        out.writeByte(0xff);
        // 6. 获取内容的字节数组
        byte[] bytes = serializer.serialize(networkMsg.getMessage());
        // 7. 长度
        out.writeInt(bytes.length);
        // 8. 写入内容
        out.writeBytes(bytes);
        outList.add(out);

        Log.d(MessageCodecSharable.class.toString(), transformByteBuf2String(out));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerType = in.readByte();
        int messageType = in.readByte();
        int sequenceId = in.readInt();
        in.readByte();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);

        Log.d(MessageCodecSharable.class.toString(), String.format("%d, %d, %d", messageType, sequenceId, length));
        Log.d(MessageCodecSharable.class.toString(), Message.Companion.getMessageClass(messageType).toString());

        Class<? extends Message> msgType = Message.Companion.getMessageClass(messageType);
        Message msg = serializer.deserialize(msgType, bytes);
        NetworkMsg networkMsg = new NetworkMsg(sequenceId, msg);
        out.add(networkMsg);
    }
}
