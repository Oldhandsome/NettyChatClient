package com.dtusystem.nettychatclient.network.serializer;


import com.dtusystem.nettychatclient.network.message.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.StandardCharsets;

public class JsonSerializer implements Serializer {
	private static final Gson gson;

	static {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gson = gsonBuilder.create();
	}

	@Override
	public <T> T deserialize(Class<T> clazz, byte[] bytes) {
		String json = new String(bytes, StandardCharsets.UTF_8);
//		Log.d("MessageCodecSharable", "deserialize: " + json);
		Object object = gson.fromJson(json, clazz);
//		Log.d("MessageCodecSharable", "deserialize: " + object);
		return (T) object;
	}

	@Override
	public <T> byte[] serialize(T object) {
		String str = gson.toJson(object);
//		Log.d("MessageCodecSharable", "serialize: " + str);
		return str.getBytes(StandardCharsets.UTF_8);
	}
}