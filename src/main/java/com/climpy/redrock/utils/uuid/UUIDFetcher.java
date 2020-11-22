package com.climpy.redrock.utils.uuid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Getter
public class UUIDFetcher {
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    private static final Map<String, UUID> uuidCache = new HashMap<>();
    private static final Map<UUID, String> nameCache = new HashMap<>();

    private String name;
    private UUID id;

    public static String SERVICE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
    public static String PROFILE_URL = "https://api.mojang.com/users/profiles/minecraft/%s";
    public static String NAME_URL = "https://api.mojang.com/user/profiles/%s/names";

    public static void getUUID(String name, Consumer<UUID> action) {
        UUIDFetcher.executorService.execute(() -> action.accept(getUUID(name)));
    }

    public static UUID getUUID(String name) {
        return UUIDFetcher.getUUIDAt(name, System.currentTimeMillis());
    }

    public static void getUUIDAt(String name, long timestamp, Consumer<UUID> action) {
        UUIDFetcher.executorService.execute(() -> action.accept(getUUIDAt(name, timestamp)));
    }

    public static UUID getUUIDAt(String name, long timestamp) {
        name = name.toLowerCase();
        if (UUIDFetcher.uuidCache.containsKey(name)) {
            return UUIDFetcher.uuidCache.get(name);
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format(PROFILE_URL, name, timestamp / 1000)).openConnection();
            connection.setReadTimeout(5000);

            UUIDFetcher uuidFetcher = UUIDFetcher.GSON.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcher.class);
            UUIDFetcher.uuidCache.put(name, uuidFetcher.id);
            UUIDFetcher.nameCache.put(uuidFetcher.id, uuidFetcher.name);

            return uuidFetcher.id;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void getName(UUID uniqueUUID, Consumer<String> action) {
        UUIDFetcher.executorService.execute(() -> {
            action.accept(getName(uniqueUUID));
        });
    }

    public static String getName(UUID uniqueUUID) {
        if (UUIDFetcher.nameCache.containsKey(uniqueUUID)) {
            return UUIDFetcher.nameCache.get(uniqueUUID);
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format(NAME_URL, UUIDTypeAdapter.fromUUID(uniqueUUID))).openConnection();
            connection.setReadTimeout(5000);

            UUIDFetcher[] nameHistory = GSON.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcher[].class);
            UUIDFetcher currentNameData = nameHistory[nameHistory.length - 1];

            UUIDFetcher.uuidCache.put(currentNameData.name.toLowerCase(), uniqueUUID);
            UUIDFetcher.nameCache.put(uniqueUUID, currentNameData.name);

            return currentNameData.name;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static Map<UUID, String> getNameList(List<UUID> uuidList) {
        Map<UUID, String> uuidStringMap = new HashMap<>();
        for (UUID uniqueUUID : uuidList) {
            uuidStringMap.put(uniqueUUID, getName(uniqueUUID));
        }

        return uuidStringMap;
    }
}
