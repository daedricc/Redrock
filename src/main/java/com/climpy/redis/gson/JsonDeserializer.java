package com.climpy.redis.gson;

import com.google.gson.JsonObject;

public interface JsonDeserializer<T> {
  T deserialize(JsonObject paramJsonObject);
}
