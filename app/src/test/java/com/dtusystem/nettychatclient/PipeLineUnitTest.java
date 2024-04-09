package com.dtusystem.nettychatclient;


import com.dtusystem.nettychatclient.network.handler.EventHandler;
import com.dtusystem.nettychatclient.network.handler.LocalRequestHandler;
import com.dtusystem.nettychatclient.network.handler.RemoteRequestHandlers;
import com.dtusystem.nettychatclient.network.handler.RequestAndPromise;
import com.dtusystem.nettychatclient.network.message.Formula;
import com.dtusystem.nettychatclient.network.protocol.MessageCodecSharable;

import org.junit.Test;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class PipeLineUnitTest {

    private final EmbeddedChannel channel = new EmbeddedChannel(
            new LoggingHandler(LogLevel.DEBUG),
            new MessageCodecSharable(),
            new EventHandler(),
            new LocalRequestHandler(),
            new RemoteRequestHandlers()
    );

    @Test
    public void testWriteOut() {
        Formula formula = new Formula(1, 1, 1, 2, 2, 2);
        channel.writeOutbound(new RequestAndPromise(formula, null));
    }

    @Test
    public void testWriteIn(){
        channel.writeInbound();
    }
}
