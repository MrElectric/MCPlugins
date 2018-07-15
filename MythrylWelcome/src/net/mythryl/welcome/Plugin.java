package net.mythryl.welcome;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.ChatColor.*;



public class Plugin
  extends JavaPlugin
  implements Listener
{
 SettingsManager settings = SettingsManager.getInstance();
  

 ArrayList<String> frozen = new ArrayList();
 ArrayList<Player> mute = new ArrayList();
  FileConfiguration config;
  File cfile;
  
  @EventHandler
 public void onPlayerMove(PlayerMoveEvent e) { Player p = e.getPlayer();
   if (this.frozen.contains(p.getName())) {
    e.setTo(e.getFrom());
     p.sendMessage(ChatColor.RED + "Event>" + ChatColor.GRAY + " You Are Now Frozen!");
    }
  }
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e){
	  
  	 String motd = getConfig().getString("motd");
  	 Player p = e.getPlayer();
  	 motd = motd.replaceAll("&", "§");
  	 motd = motd.replaceAll("\\{player\\}", p.getName());

     p.sendMessage(motd + "");
  }
  

  public void onEnable() {
  this.settings.setup(this);
  
   Bukkit.getServer().getPluginManager().registerEvents(this, this);
  getConfig().options().copyDefaults(true);
 saveConfig();
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
  		Player player = (Player)sender;
  		
   if (cmd.getName().equalsIgnoreCase("motdsyntax")) {
	   sender.sendMessage(ChatColor.RED + "Syntax> " + ChatColor.GRAY + "To choose a color: &(number/leter); To show a player name: {player}");
   }
   if (cmd.getName().equalsIgnoreCase("motd")) {
  	 String motd = getConfig().getString("motd");
  	   motd = motd.replaceAll("&", "§");
  	   
     sender.sendMessage(ChatColor.RED + "MOTD> " + ChatColor.GRAY + motd);
   }
   if (cmd.getName().equalsIgnoreCase("setmotd")) {
     if (!sender.hasPermission("motd.set")) {
       sender.sendMessage(ChatColor.RED + "Error>" + ChatColor.GRAY + " You Do Not Have Permissions!");
      return true;
      }
     if (args.length == 0) {
       sender.sendMessage(ChatColor.RED + "Error> " + ChatColor.GRAY + "Please Specify An MOTD!");
      return true;
      }
    StringBuilder str = new StringBuilder();
     for (int i = 0; i < args.length; i++) {
       str.append(args[i] + " ");
      }
      
     String motd = str.toString();
    getConfig().set("motd", motd);
    String newmotd = getConfig().getString("motd");
    motd = motd.replaceAll("&", "§");
    

     saveConfig();
    sender.sendMessage(ChatColor.RED + "New MOTD> " + ChatColor.GRAY + motd);
    }
   return true;
  }

}