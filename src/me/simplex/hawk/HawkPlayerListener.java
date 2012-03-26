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
	
	/**
	 * Hawk uses this event to allow players to switch between the different modes, flying, hovering and walking
	 * 
	 * @param event the {@link PlayerInteractEvent} 
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack stack = event.getItem();
		if (stack == null) {
			return;
		}
		if (stack.getType().equals(HawkConfiguration.getItem()) && checkAction(event.getAction())) {
			Player player = event.getPlayer();
			if (Hawk.isFlyingOrHovering(player)) {
				HawkPlayerStatus status = Hawk.getPlayerStatus(player);
				switch (status.getState()) {
					case FLY: // Fly > Hover
						Hawk.performSwitchToHover(player, status);
						break;
					case HOVER: // Hover > Land
						Hawk.performSwitchToLand(player);
						break;
				}
			}	// Land > Fly
			else {
				Hawk.performSwitchToFly(player);
			}
		}
	}
	
	/**
	 * Remove the quitting player from the data model
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Hawk.handlePlayerQuit(event.getPlayer());
	}
	
	/**
	 * Cancel a kick event if the player is flying or was flying 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerKick(PlayerKickEvent event) {
		if (event.getReason().startsWith("Flying") && (Hawk.isFlyingOrHovering(event.getPlayer()) || Hawk.isDmgImmune(event.getPlayer()))) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Checks if the {@link PlayerInteractEvent} fits for Hawk
	 * 
	 * @param action the {@link Action} to check
	 * @return true if the player right clicked on air or a block, false otherwise
	 */
	private static boolean checkAction(Action action){
		return action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);
	}
}