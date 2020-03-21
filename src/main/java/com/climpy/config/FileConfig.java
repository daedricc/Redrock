package com.climpy.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class FileConfig {
  private FileConfiguration fileConfiguration;
  private File file;
  
  public FileConfiguration getFileConfiguration() { return this.fileConfiguration; }
  public File getFile() { return this.file; }
  
  public FileConfig(JavaPlugin plugin, String fileName) {
    this.file = new File(plugin.getDataFolder(), fileName);
    checkFileStatus(plugin, this.file, false);
    
    this.fileConfiguration = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
  }
  
  public static void checkFileStatus(JavaPlugin plugin, File checkedFile, boolean hasDirectory) {
    String fileName = checkedFile.getName();
    
    if (!checkedFile.exists()) {
      if (hasDirectory) {
        if (checkedFile.mkdir()) {
          plugin.getLogger().log(Level.WARNING, "Yapılandandırma dosyası oluşturulamadı!");
        }
      } else if (!checkedFile.getParentFile().mkdir()) {
        plugin.getLogger().log(Level.WARNING, "Yapılandırlma dizini oluşturulamadı!");
      } 
      
      if (plugin.getResource(fileName) == null) {
        try {
          if (!checkedFile.createNewFile())
            plugin.getLogger().log(Level.WARNING, "Yapılandırılma dosyası olusturulamadi!"); 
        } catch (IOException ex) {
          plugin.getLogger().log(Level.WARNING, "Yapilandirilan dosya " + fileName + " olusturulamadi!");
        } 
      } else {
        plugin.saveResource(fileName, false);
      } 
    } 
  }
  
  public void configSave() {
    try {
      this.fileConfiguration.save(this.file);
    } catch (IOException ex) {
      Bukkit.getLogger().log(Level.WARNING, "Yapilandirilma dosyasi " + this.file.getName() + " kayit edilemedi!");
      ex.printStackTrace();
    } 
  }
}
