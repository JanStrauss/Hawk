package me.simplex.hawk;

import me.simplex.hawk.Hawk.Flystate;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class HawkPlayerListener implements Listener{
	private Hawk main;
	
	public HawkPlayerListener(Hawk main) {
		this.main = main;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack is = event.getItem();
		if (is == null) {
			return;
		}
		if (is.getType().equals(Material.FEATHER) && checkAction(event.getAction())) {
			Player player = event.getPlayer();
			if (main.flyingPlayers.containsKey(player.getName())) {
				switch (main.flyingPlayers.get(player.getName())) {
					case FLY: 														// Fly
						main.flyingPlayers.put(player.getName(), Flystate.HOVER); 
						main.dmgImunePlayers.remove(player.getName());
						player.sendMessage(ChatColor.BLUE+Hawk.PREFIX+ChatColor.WHITE+"you are now hovering.");
						break;
					case HOVER: 													// Hover
						main.flyingPlayers.remove(player.getName()); 
						main.dmgImunePlayers.add(player.getName());
						player.sendMessage(ChatColor.BLUE+Hawk.PREFIX+ChatColor.WHITE+"you are now walking the dinosaur.");
						break;
				}
			}																		// Land
			else {
				if (player.hasPermission("hawk.fly")) {
					main.flyingPlayers.put(player.getName(), Flystate.FLY);
					player.sendMessage(ChatColor.BLUE+Hawk.PREFIX+ChatColor.WHITE+"you are now flying.");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		main.flyingPlayers.remove(event.getPlayer().getName());
		main.dmgImunePlayers.remove(event.getPlayer().getName());
	}
	
	private static boolean checkAction(Action a){
		return a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK);
	}
}