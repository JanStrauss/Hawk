/*
 * Copyright 2012 s1mpl3x
 * 
 * This file is part of Nordic.
 * 
 * Nordic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Nordic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nordic If not, see <http://www.gnu.org/licenses/>.
 */
package me.simplex.hawk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class HawkEntityListener implements Listener {
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDmg(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (Hawk.flyingPlayers.containsKey(player.getName())) {
				if (event.getCause().equals(DamageCause.FALL)) {
					event.setCancelled(true);
					// player.sendMessage("[CASE 1] Fall dmg event cancelled");
				}
			} else if (Hawk.dmgImunePlayers.contains(player.getName())) {
				event.setCancelled(true);
				// player.sendMessage("[CASE 2] Fall dmg event cancelled");
				// main.toLogger("List Length: " + main.dmgImunePlayers.size());
				Hawk.dmgImunePlayers.remove(player.getName());
				// main.toLogger("List Length: " + main.dmgImunePlayers.size());
			}
		}
	}
}