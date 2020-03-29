package com.climpy.mongo;

import com.climpy.RedrockPlugin;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.logging.Level;

@Getter
public class MongoConnection {
    private MongoCredential credential;
    private MongoClient mongoClient;
    private CollectionManager collectionManager;

    public void setupDatabaseConnection() {
        try {
            this.credential = MongoCredential.createCredential("climpy", "admin", "root".toCharArray());
            this.mongoClient = new MongoClient(new ServerAddress("localhost", 27017), Collections.singletonList(this.credential));

            this.collectionManager = new CollectionManager(this.mongoClient);
            this.collectionManager.createDataConnection();
            RedrockPlugin.getInstance().getLogger().log(Level.INFO, "[MongoDB] Veritabani sunucusuna basariyla baglanildi.");
        } catch (MongoClientException ex) {
            RedrockPlugin.getInstance().getLogger().log(Level.SEVERE, "[MongoDB] Çalisan, yerel Mongo Veritabani sunucusuna baglanilamiyor!");
            Bukkit.getServer().shutdown();
        }
    }

    public void disableDatabaseConnection() {
        if (this.mongoClient != null) {
            RedrockPlugin.getInstance().getLogger().log(Level.INFO, "[MongoDB] Veritabani baglantisi kapatiliyor..");

            try {
                this.mongoClient.close();
                RedrockPlugin.getInstance().getLogger().log(Level.INFO, "[MongoDB] Veritabani baglantisi basariyla kapatildi.");
            } catch (Exception ex) {
                RedrockPlugin.getInstance().getLogger().log(Level.INFO, "[MongoDB] Veritabani verileri kaydedilemedi.");
            }
        }
    }
}