package com.dtusystem.nettychatclient.network.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ProtocolLengthFieldDecoder extends LengthFieldBasedFrameDecoder {
    public ProtocolLengthFieldDecoder() {
        this(1024, 12, 4);
    }

    private ProtocolLengthFieldDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }
}
