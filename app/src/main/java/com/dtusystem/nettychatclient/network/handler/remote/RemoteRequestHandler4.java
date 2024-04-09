package com.dtusystem.nettychatclient.network.handler.remote;

import com.dtusystem.nettychatclient.network.message.Technology;

import java.util.Collections;
import java.util.List;

public class RemoteRequestHandler4 extends RemoteRequestHandler {

    public void handleTechnology(Technology technology) {

    }

    @Override
    public List<Integer> getMessageCodes() {
        return Collections.singletonList(
                Technology.TECHNOLOGY
        );
    }
}