package com.dtusystem.nettychatclient.network.handler.remote;

import com.dtusystem.nettychatclient.network.message.Formula;

import java.util.Collections;
import java.util.List;

public class RemoteRequestHandler2 extends RemoteRequestHandler {

    public void handleFormula(Formula formula) {

    }

    @Override
    public List<Integer> getMessageCodes() {
        return Collections.singletonList(
                Formula.FORMULA
        );
    }
}