package com.climpy.mongo;

import com.climpy.RedrockPlugin;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

@Getter
@Setter
public class CollectionManager {
    private Set<String> collections;
    private MongoClient mongoClient;

    public CollectionManager(MongoClient mongoClient) {
        this.collections = new HashSet<>();
        this.mongoClient = mongoClient;

        this.collections.add("user");
        this.collections.add("report");
    }

    public void createDataConnection() {

        try {
            RedrockPlugin.getInstance().getLogger().log(Level.INFO, "[MongoDB] Collection işlemde şu anda kuruluyor..");

            try {
                MongoDatabase mongoDatabase = this.mongoClient.getDatabase("redrock");

                String[] names = new String[this.collections.size()];
                for (int index = 0; index < names.length; ) {
                    for (String collection : this.collections) {
                        RedrockPlugin.getInstance().getLogger().log(Level.INFO, "[MongoDB] [" + (index + 1) + "] - " + collection + " collections sisteme işleniyor.");
                        names[index++] = collection;
                    }
                }

                try {
                    Thread collectionThread = new Thread(() -> {
                        Arrays.stream(names).forEach(name -> {
                            if (!mongoDatabase.listCollectionNames().into(new ArrayList<>()).contains(name)) {
                                mongoDatabase.createCollection(name);
                            }
                        });
                    });

                    collectionThread.setName("Collection-Create-Task");
                    collectionThread.start();
                } catch (Exception ex) {
                    RedrockPlugin.getInstance().getLogger().log(Level.WARNING, ex.getMessage());
                }

                RedrockPlugin.getInstance().getLogger().log(Level.INFO, "[MongoDB] Collection bağlantıları kuruldu, bağlantı bekleniyor!");
            } catch (IndexOutOfBoundsException | NullPointerException ex) {
                ex.printStackTrace();

                RedrockPlugin.getInstance().getLogger().log(Level.SEVERE, "[MongoDB] Collection hatası, 5 saniye içinde kurtarılmaya çalışılıyor.");
                Bukkit.getScheduler().runTaskLater(RedrockPlugin.getInstance(), this::createDataConnection, 5 * 20L);
            }

        } catch (Exception ex) {
            RedrockPlugin.getInstance().getLogger().log(Level.SEVERE, "[MongoDB] Collection hatası, 5 saniye içinde kurtarılmaya çalışılıyor.");
            Bukkit.getScheduler().runTaskLater(RedrockPlugin.getInstance(), this::createDataConnection, 5 * 20L);
        }
    }

    public MongoCollection getMongoCollection(String registeredName) {
        MongoConnection mongoConnection = RedrockPlugin.getInstance().getMongoConnection();
        MongoClient mongoClient = mongoConnection.getMongoClient();

        MongoDatabase mongoDatabase = mongoClient.getDatabase("redrock");
        for (String dataCollection : this.collections) {
            if (dataCollection.equals(registeredName)) {
                return mongoDatabase.getCollection(registeredName);
            }
        }
        return null;
    }
}
