  package me.coolguy1951.punish;
  
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
import org.bukkit.plugin.PluginManager;
  import org.bukkit.plugin.java.JavaPlugin;
  import org.bukkit.scheduler.BukkitScheduler;

import me.coolguy1951.punish.EnforcerEvent;
import me.coolguy1951.punish.Menu;

import org.bukkit.ChatColor.*;
  
  
  
  public class Plugin extends JavaPlugin implements Listener  {
	  
	  private Menu menu;
  
   ArrayList<String> frozen = new ArrayList();
   
    @EventHandler
   public void onPlayerMove(PlayerMoveEvent e) { Player p = e.getPlayer();
     if (this.frozen.contains(p.getName())) {
      e.setTo(e.getFrom());
       p.sendMessage(ChatColor.RED + "Event>" + ChatColor.GRAY + " You Are Now Frozen!");
      }
    }
    public void onEnable() {
    	menu = new Menu(this);
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }
    
    private ArrayList<Player> banned = new ArrayList<Player>();
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    		Player player = (Player)sender;
    		
    		if(cmd.getName().equalsIgnoreCase("punish") || cmd.getName().equalsIgnoreCase("p")){
    			if(!sender.hasPermission("admin.si")){
    				sender.sendMessage(ChatColor.RED + "Error> " + ChatColor.GRAY + "You Do Not Have Permissions");
    				return true;
    			}
    			if(args.length > 2){
    				sender.sendMessage(ChatColor.RED + "Syntax> " + ChatColor.GRAY + "/punish <player> <reason>");
    			}
    			  menu.show(player.getPlayer());
 		         return true;
    		}
    		
    		if(cmd.getName().equalsIgnoreCase("warn")){
    			 if (!sender.hasPermission("ee.*")) {
 			        sender.sendMessage(ChatColor.RED + "Error>" + ChatColor.GRAY + " You Do Not Have Permissions!");
 			        return true;
 			        }
    			 if(args.length == 0){
    				 sender.sendMessage(ChatColor.RED + "Error> " + ChatColor.GRAY + "You Must Specify A Player!");
    				 return true;
    			 }
    			 
    			 Player target = Bukkit.getServer().getPlayer(args[0]);
    			 
    			 if (target == null){
    				 sender.sendMessage(ChatColor.RED + "Error> " + ChatColor.GRAY + "Couldn't Find The Player!");
    			 }
    			 
    			 if(args.length < 2){
    				 sender.sendMessage(ChatColor.RED + "Syntax> " + ChatColor.GRAY + "/warn <player> <reason>");
    			 }
    			 
    			 String msg = "";
    			 for (int i = 1; i < args.length; i++){
    				 msg += args[1];
    			 }
    			 
    			 Object level = this.getConfig().get(target.getName());
    			 
    			 if(level == null){
    				 target.sendMessage(ChatColor.RED + "Warn>" + ChatColor.GRAY + msg);
    				 this.getConfig().set(target.getName(), 1);
    				 this.saveConfig();
    				 return true;
    			 }
    			 
    			 int l = Integer.parseInt(level.toString());
    			 
    			 if (l == 1){
    				 target.kickPlayer(ChatColor.RED + "You've Been Warned! Reason:" + ChatColor.RED.BOLD + msg);
    				 this.getConfig().set(target.getName(), 2);
    				 this.saveConfig();
    				 return true;
    			 }
    			 if (l == 2){
    				 banned.add(target);
    				 target.setBanned(true);
    				 Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
    					public void run(){
    						banned.remove(target);
    					}
    				 });
    				 return true;
    			 }
    		}
    	
    		  if (cmd.getName().equalsIgnoreCase("kick")) {
    			  if (!sender.hasPermission("ee.*")) {
    			        sender.sendMessage(ChatColor.RED + "Error>" + ChatColor.GRAY + " You Do Not Have Permissions!");
    			        return true;
    			        }
                  if (args.length == 0) {
                          sender.sendMessage(ChatColor.RED + "Error> " + ChatColor.GRAY + "Please specify a player!");
                          return true;
                  }
                  Player target = Bukkit.getServer().getPlayer(args[0]);
                  if (target == null) {
                          sender.sendMessage(ChatColor.RED + "Could not find player " + args[0] + "!");
                          return true;
                  }
                  target.kickPlayer(ChatColor.RED + "You have been Kicked By " + ChatColor.GRAY.BOLD + sender.getName() + "!" + ChatColor.RED.BOLD + " Reason: " + ChatColor.GRAY.BOLD + args[1]);
                  Bukkit.getServer().getPluginManager().callEvent(new EnforcerEvent(target, Type.KICK));
                  Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Player " + target.getName() + " has been kicked by " + sender.getName() + "!");
          }
          if (cmd.getName().equalsIgnoreCase("ban")) {
        	  if (!sender.hasPermission("ee.*")) {
        	        sender.sendMessage(ChatColor.RED + "Error>" + ChatColor.GRAY + " You Do Not Have Permissions!");
        	        return true;
        	        }
                  if (args.length == 0) {
                          sender.sendMessage(ChatColor.RED + "Error> " + ChatColor.GRAY + "Please specify a player!");
                          return true;
                  }
                  Player target = Bukkit.getServer().getPlayer(args[0]);
                  if (target == null) {
                          sender.sendMessage(ChatColor.RED + "Error> " + ChatColor.GRAY + "Could not find player " + args[0] + "!");
                          return true;
                  }
                  target.kickPlayer(ChatColor.GRAY + "You have been Banned By " + ChatColor.GRAY.BOLD + sender.getName() + "Reason: " + args[1]);
                  Bukkit.getServer().getPluginManager().callEvent(new EnforcerEvent(target, Type.BAN));
                  Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Player " + target.getName() + " has been banned by " + sender.getName() + "!");
          }
				if (cmd.getName().equalsIgnoreCase("freeze")) {
     if (!sender.hasPermission("freeze.set")) {
       sender.sendMessage(ChatColor.RED + "Error>" + ChatColor.GRAY + " You Do Not Have Permissions!");
       return true;
        }
       if (args.length == 0) {
         sender.sendMessage(ChatColor.RED + "Error>" + ChatColor.GRAY + " No Player Was Specified!");
        return true;
        }
       Player target = Bukkit.getServer().getPlayer(args[0]);
      if (target == null) {
        sender.sendMessage(ChatColor.RED + "Error>" + ChatColor.GRAY + " Player Couldn't Be Found!");
        return true;
        }
     if (this.frozen.contains(target.getName())) {
         this.frozen.remove(target.getName());
         sender.sendMessage(ChatColor.GREEN + target.getName() + " Has Been Unfrozen!");
        return true;
        }
     this.frozen.add(target.getName());
       sender.sendMessage(ChatColor.GREEN + target.getName() + " Has Been Frozen!");
      }
  
				
				
     return true;
    }
  }