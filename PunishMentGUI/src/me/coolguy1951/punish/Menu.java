package me.coolguy1951.punish;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import org.bukkit.plugin.Plugin;

public class Menu implements Listener {

	private Inventory inv;
	private ItemStack c, s, a, sp;
	private String[] args;
	
	 ArrayList<Player> mute = new ArrayList();
	  @EventHandler
	   public void PlayerChatEvent(AsyncPlayerChatEvent e){
		  Player p = e.getPlayer();
	 if (this.mute.contains(p.getName())) {
	      e.setCancelled(true);
	      }
	        }
	
	public Menu(Plugin p){
		inv = Bukkit.getServer().createInventory(null, 9, "Punishment GUI");
		
		c = createItem(DyeColor.RED, ChatColor.RED + "Ban");
		s = createItem(DyeColor.ORANGE, ChatColor.GOLD + "Kick");
		a = createItem(DyeColor.YELLOW, ChatColor.YELLOW + "Mute");
		
		inv.setItem(3, c);
		inv.setItem(5, s);
		inv.setItem(7, a);
		
		Bukkit.getServer().getPluginManager().registerEvents(this, p);
	}
	
	
	private ItemStack createItem(DyeColor dc, String name){
    	ItemStack i = new Wool(dc).toItemStack(1);
    	ItemMeta im = i.getItemMeta();
    	im.setDisplayName(name);
    	im.setLore(Arrays.asList("Punish " + "this player ", "by " + name.toLowerCase() + "ing them" ));
    	i.setItemMeta(im);
    	return i;
    }
    
    public void show(Player p){
    	p.openInventory(inv);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
		Player target = Bukkit.getServer().getPlayer(args[0]);
    	  if (!e.getInventory().getName().equalsIgnoreCase(inv.getName())) return;
          if (e.getCurrentItem().getItemMeta() == null) return;
          if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Ban")) {
                  e.setCancelled(true);
                  target.kickPlayer(args[1]);
                  target.setBanned(true);
                  e.getWhoClicked().closeInventory();
          }
          if (e.getCurrentItem().getItemMeta().getDisplayName().contains("kick")) {
                  e.setCancelled(true);
                  target.kickPlayer(args[1]);
                  e.getWhoClicked().closeInventory();
          }
          if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Mute")) {
                  e.setCancelled(true);
                 mute.add(target);
                  e.getWhoClicked().closeInventory();
                  if(mute.contains(target)){
                	  e.setCancelled(true);
                	  mute.remove(target);
                	  e.getWhoClicked().closeInventory();
                  }
          }
    }
}