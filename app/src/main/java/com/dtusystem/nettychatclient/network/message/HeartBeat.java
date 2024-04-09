package com.dtusystem.nettychatclient.network.message;

public class HeartBeat extends Message {
	@Override
	public int getMessageCode() {
		return Message.HEART;
	}
}
