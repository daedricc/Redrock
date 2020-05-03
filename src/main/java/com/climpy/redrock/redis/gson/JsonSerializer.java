package com.climpy.redrock.redis.gson;

import com.google.gson.JsonObject;

public interface JsonSerializer<T> {
    JsonObject serialize(T paramT);
}
