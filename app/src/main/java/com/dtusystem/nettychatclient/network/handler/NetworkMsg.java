package com.dtusystem.nettychatclient.network.handler;

import com.dtusystem.nettychatclient.network.message.Message;


public class NetworkMsg {
    private int id;
    private Message message;

    public NetworkMsg(int id, Message message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
