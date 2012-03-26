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

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

/**
 * This class is scheduled using the {@link BukkitScheduler}.
 * It gets called on every server tick performing the fly/hover velocity changes to a {@link Player}
 * 
 * @author s1mpl3x
 *
 */
public class HawkTask implements Runnable {
	private Hawk main;
	
	public HawkTask(Hawk main) {
		this.main = main;
	}
	
	private static final Vector HOVER 		= new Vector(0, HawkConfiguration.getHover_boost(), 0);
	private static final Vector HOVER_BOOST = new Vector(0,   1, 0);
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		for (final String playername : Hawk.getFlyingPlayersNames()) {
			Player player = main.getServer().getPlayer(playername);
			HawkPlayerStatus status = Hawk.getPlayerStatus(player);
			
			if (player != null) {
				switch (status.getState()) {
					case FLY: 	doFly(player); 	break;
					case HOVER: doHover(player); break;
				}
				performConsume(player, status);
			}
		}
	}
	
	/**
	 * Give the player the fly boost
	 * @param player
	 */
	private static void doFly(Player player){
		Vector view = player.getLocation().getDirection();
		player.setVelocity(view.multiply(HawkConfiguration.getFly_boost()));
	}
	
	/**
	 * Give the player the hover boost
	 * @param player
	 */
	private static void doHover(Player player){
		if (player.isSneaking()) {
			player.setVelocity(HOVER_BOOST);
		}
		else {
			player.setVelocity(HOVER);
		}
	}
	
	/**
	 * Only called if enabled in the configuration. This method checks if a player has the configured item in the inventory and removes it every
	 * configured seconds
	 * @param player to handle 
	 * @param status of the given {@link Player}
	 */
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
						Hawk.performSwitchToLand(player);
						return;
					}
				} else {
					Hawk.performSwitchToLand(player);
				}
			}
			status.increaseTime();
		}
	}
}