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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class HawkEntityListener implements Listener {
	
	/**
	 * Hawk uses this event to cancel fall damage for flying/was flying players
	 * 
	 * @param event the {@link EntityDamageEvent}
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDmg(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player && event.getCause().equals(DamageCause.FALL)) {
			Player player = (Player) event.getEntity();
			if (Hawk.isFlyingOrHovering(player)) {
				event.setCancelled(true);
				
			} else if (Hawk.isDmgImmune(player)) {
				event.setCancelled(true);
				Hawk.removeFromImmunity(player);
			}
		}
	}
}