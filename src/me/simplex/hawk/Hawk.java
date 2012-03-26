/*
 * Copyright 2012 s1mpl3x
 * 
 * This file is part of Hawk.
 * 
 * Hawk is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Hawk is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Hawk If not, see <http://www.gnu.org/licenses/>.
 */
package me.simplex.hawk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import me.simplex.hawk.HawkPlayerStatus.Flystate;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class of Hawk
 * 
 * @author s1mpl3x
 *
 */
public class Hawk extends JavaPlugin implements Filter {
	/**
	 * Prefix used for all messages that are sent to the player
	 */
	protected static final String PREFIX = "[HAWK] ";

	/**
	 * set containing the names of the players that are immune to fall damage
	 */
	private static HashSet<String> dmgImmunePlayers = new HashSet<String>();
	
	/**
	 * {@link HashMap} containing all player names that are flying/hovering mapped to a {@link HawkPlayerStatus} object.
	 */
	private static HashMap<String, HawkPlayerStatus> flyingPlayers = new HashMap<String, HawkPlayerStatus>();
	
	private static Logger log;
	
	public void onDisable() {}
	
	public void onEnable() {
		log = getLogger();
		log.setFilter(this);

		HawkConfiguration.loadConfiguration(this);

		HawkPlayerListener player_listener = new HawkPlayerListener();
		HawkEntityListener entity_listener = new HawkEntityListener();
		
		getServer().getPluginManager().registerEvents(player_listener, this);
		getServer().getPluginManager().registerEvents(entity_listener, this);
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new HawkTask(this), 1, 1);
		
		toLogger("loaded " + getDescription().getFullName());
	}
	
	/**
	 * Prints the given msg to the server log/console
	 * @param msg
	 */
	public static void toLogger(String msg){
		log.info(msg);
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Filter#isLoggable(java.util.logging.LogRecord)
	 */
	public boolean isLoggable(LogRecord record) {
		return !record.getMessage().contains("was kicked for floating too long!");
	}
	
	/**
	 * Called when a {@link Player} switches to flying
	 * @param player the {@link Player} to switch
	 */
	public static void performSwitchToFly(Player player){
		if (player.hasPermission("hawk.fly")) {
			if (HawkConfiguration.consume_item()) {
				if (!player.getInventory().contains(HawkConfiguration.getConsume_Item())) {
					return;
				}
			}
			flyingPlayers.put(player.getName(), new HawkPlayerStatus());
			player.setAllowFlight(true);
			player.sendMessage(ChatColor.BLUE + PREFIX + ChatColor.WHITE + HawkConfiguration.getMessage_Fly());
		}
	}
	
	/**
	 * Called when a {@link Player} switches to hovering
	 * 
	 * @param player the {@link Player} to switch
	 * @param status the {@link HawkPlayerStatus} of this player
	 */
	public static void performSwitchToHover(Player player, HawkPlayerStatus status){
		status.setState(Flystate.HOVER);
		dmgImmunePlayers.remove(player.getName());
		player.sendMessage(ChatColor.BLUE + PREFIX + ChatColor.WHITE + HawkConfiguration.getMessage_Hover());
	}
	
	/**
	 * Called when a {@link Player} lands
	 * 
	 * @param player to force to walk again
	 */
	public static void performSwitchToLand(Player player){
		flyingPlayers.remove(player.getName());
		dmgImmunePlayers.add(player.getName());
		player.setAllowFlight(false);
		player.sendMessage(ChatColor.BLUE + Hawk.PREFIX + ChatColor.WHITE + HawkConfiguration.getMessage_Land());
	}
	
	/**
	 * Checks if a player is currently flying
	 * 
	 * @param player the {@link Player} to check
	 * @return true/false whether the {@link Player} is flying/hovering or not
	 */
	public static boolean isFlyingOrHovering(Player player){
		return flyingPlayers.containsKey(player.getName());
	}
	
	/**
	 * Checks if a player is immune to fall damage
	 * 
	 * @param player the {@link Player} to check
	 * @return true/false whether the {@link Player} is immune to fall dmg
	 */
	public static boolean isDmgImmune(Player player){
		return dmgImmunePlayers.contains(player.getName());
	}
	
	/**
	 * Removes a {@link Player} from fall dmg immunity
	 * 
	 * @param player to remove
	 */
	public static void removeFromImmunity(Player player){
		dmgImmunePlayers.remove(player.getName());
	}
	
	/**
	 * Gets the {@link HawkPlayerStatus} for the given player
	 * 
	 * @param player to get the {@link HawkPlayerStatus} for
	 * @return the {@link HawkPlayerStatus} for this {@link Player}, null if there is none
	 */
	public static HawkPlayerStatus getPlayerStatus(Player player){
		return flyingPlayers.get(player.getName());
	}
	
	/**
	 * Returns a {@link Set}<String> with all player names that are currently flying/hovering
	 * 
	 * @return KeySet of the {@link Hawk#flyingPlayers} {@link HashMap}
	 */
	public static Set<String> getFlyingPlayersNames(){
		return flyingPlayers.keySet();
	}
	
	/**
	 * Called when a player quits
	 * 
	 * @param player the {@link Player} that quits
	 */
	public static void handlePlayerQuit(Player player){
		flyingPlayers.remove(player.getName());
		dmgImmunePlayers.remove(player.getName());
		player.setAllowFlight(false);
	}
}