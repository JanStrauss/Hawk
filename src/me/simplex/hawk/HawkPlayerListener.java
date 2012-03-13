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

import me.simplex.hawk.HawkPlayerStatus.Flystate;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class HawkPlayerListener implements Listener{
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack stack = event.getItem();
		if (stack == null) {
			return;
		}
		if (stack.getType().equals(HawkConfiguration.getItem()) && checkAction(event.getAction())) {
			Player player = event.getPlayer();
			if (Hawk.flyingPlayers.containsKey(player.getName())) {
				HawkPlayerStatus status = Hawk.flyingPlayers.get(player.getName());
				switch (status.getState()) {
					case FLY: 														// Fly
						status.setState(Flystate.HOVER);
						Hawk.dmgImunePlayers.remove(player.getName());
						player.sendMessage(ChatColor.BLUE + Hawk.PREFIX + ChatColor.WHITE + HawkConfiguration.getMessage_Hover());
						break;
					case HOVER: 													// Hover
						Hawk.flyingPlayers.remove(player.getName()); 
						Hawk.dmgImunePlayers.add(player.getName());
						player.sendMessage(ChatColor.BLUE + Hawk.PREFIX + ChatColor.WHITE + HawkConfiguration.getMessage_Land());
						break;
				}
			}																		// Land
			else {
				if (player.hasPermission("hawk.fly")) {
					if (HawkConfiguration.consume_item()) {
						if (!player.getInventory().contains(HawkConfiguration.getConsume_Item())) {
							return;
						}
					}
					Hawk.flyingPlayers.put(player.getName(), new HawkPlayerStatus());
					player.sendMessage(ChatColor.BLUE + Hawk.PREFIX + ChatColor.WHITE + HawkConfiguration.getMessage_Fly());
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Hawk.flyingPlayers.remove(event.getPlayer().getName());
		Hawk.dmgImunePlayers.remove(event.getPlayer().getName());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerKick(PlayerKickEvent event) {
		if (event.getReason().startsWith("Flying") && (Hawk.flyingPlayers.containsKey(event.getPlayer().getName()) || Hawk.dmgImunePlayers.contains(event.getPlayer().getName()))) {
			event.setCancelled(true);
		}
	}
	
	private static boolean checkAction(Action action){
		return action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);
	}
}