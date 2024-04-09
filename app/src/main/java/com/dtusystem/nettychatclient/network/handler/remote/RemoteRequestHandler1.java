package com.dtusystem.nettychatclient.network.handler.remote;

import com.dtusystem.nettychatclient.network.message.Formula;
import com.dtusystem.nettychatclient.network.message.Technology;

import java.util.Arrays;
import java.util.List;

public class RemoteRequestHandler1 extends RemoteRequestHandler {

    public void handleFormula(Formula formula) {

    }

    public void handleTechnology(Technology technology) {

    }

    @Override
    public List<Integer> getMessageCodes() {
        return Arrays.asList(
                Formula.FORMULA,
                Technology.TECHNOLOGY
        );
    }
}
