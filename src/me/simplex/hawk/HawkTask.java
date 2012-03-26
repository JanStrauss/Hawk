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
import me.simplex.hawk.util.InventoryUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class HawkTask implements Runnable {
	private Hawk main;
	
	public HawkTask(Hawk main) {
		this.main = main;
	}
	
	private static final Vector HOVER 		= new Vector(0, HawkConfiguration.getHover_boost(), 0);
	private static final Vector HOVER_BOOST = new Vector(0,   1, 0);
	
	public void run() {
		for (final String playername : Hawk.flyingPlayers.keySet()) {
			Player player = main.getServer().getPlayer(playername);
			HawkPlayerStatus status = Hawk.flyingPlayers.get(playername);
			
			if (player != null) {
				switch (status.getState()) {
					case FLY: 	doFly(player); 	break;
					case HOVER: doHover(player); break;
				}
				performConsume(player, status);
			}
			else {
				Hawk.flyingPlayers.remove(playername);
			}
		}
	}
	
	private static void doFly(Player player){
		Vector view = player.getLocation().getDirection();
		player.setVelocity(view.multiply(HawkConfiguration.getFly_boost()));
	}
	
	private static void doHover(Player player){
		if (player.isSneaking()) {
			player.setVelocity(HOVER_BOOST);
		}
		else {
			player.setVelocity(HOVER);
		}
	}
	
	private static void performConsume(Player player, HawkPlayerStatus status){
		if (HawkConfiguration.consume_item()) {
			int limit = 20 * (status.getState() == Flystate.FLY ?	HawkConfiguration.getConsume_Seconds_Fly() : 
																	HawkConfiguration.getConsume_Seconds_Hover());
			int current = status.getTime_since_consume();

			if (current >= limit) {
				PlayerInventory inv = player.getInventory();
				Material type = HawkConfiguration.getConsume_Item();
				if (inv.contains(type)) {
					InventoryUtil.removeOneFromPlayerInventory(inv, type);
					status.resetTime();
					if (!inv.contains(type)) {
						Hawk.flyingPlayers.remove(player.getName());
						Hawk.dmgImunePlayers.add(player.getName());
						player.setAllowFlight(false);
						player.sendMessage(ChatColor.BLUE + Hawk.PREFIX + ChatColor.WHITE + HawkConfiguration.getMessage_Land());
						return;
					}
				} else {
					Hawk.flyingPlayers.remove(player.getName());
					Hawk.dmgImunePlayers.add(player.getName());
					player.setAllowFlight(false);
					player.sendMessage(ChatColor.BLUE + Hawk.PREFIX + ChatColor.WHITE + HawkConfiguration.getMessage_Land());
				}
			}
			status.increaseTime();
		}
	}
}