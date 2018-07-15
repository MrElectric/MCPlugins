package net.mythryl.welcome;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

public class SettingsManager
{
static SettingsManager instance = new SettingsManager();
  Plugin p;
  
public static SettingsManager getInstance() { return instance; }
  

  FileConfiguration config;
  File cfile;
  
  FileConfiguration data;
  File dfile;
  
  public void setup(Plugin p)
  {
  this.config = p.getConfig();
  this.config.options().copyDefaults(true);
  this.cfile = new File(p.getDataFolder(), "config.yml");
  saveConfig();
  dfile = new File(p.getDataFolder(), "data.yml");
  if (!dfile.exists()) {
      try {
              dfile.createNewFile();
      }
      catch (IOException e) {
              Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create data.yml!");
      }
}

data = YamlConfiguration.loadConfiguration(dfile);
}
  public FileConfiguration getData() {
      return data;
}

public void saveData() {
      try {
              data.save(dfile);
      }
      catch (IOException e) {
              Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save data.yml!");
      }
}

public void reloadData() {
      data = YamlConfiguration.loadConfiguration(dfile);
}
  
 

  
  public FileConfiguration getConfig() {
  return this.config;
  }
  
  public void reloadConfig() {
 this.config = YamlConfiguration.loadConfiguration(this.cfile);
  }
  
  public void saveConfig() {
    try {
   this.config.save(this.cfile);
    } catch (IOException e) {
   Bukkit.getServer().getLogger().severe(ChatColor.RED + "Error>" + ChatColor.GRAY + " Could Not Save config.yml!");
    }
  }
  
public PluginDescriptionFile getDesc() { return this.p.getDescription(); }
}
