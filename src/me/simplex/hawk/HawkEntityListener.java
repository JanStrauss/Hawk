package me.simplex.hawk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class HawkEntityListener implements Listener{
	private Hawk main;
	
	public HawkEntityListener(Hawk main) {
		this.main = main;
	}
		
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDmg(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (main.flyingPlayers.containsKey(player.getName())) {
				if (event.getCause().equals(DamageCause.FALL)) {
					event.setCancelled(true);
					player.sendMessage("[CASE 1] Fall dmg event cancelled");
				}
			}
			else if (main.dmgImunePlayers.contains(player.getName())) {
				event.setCancelled(true);
				player.sendMessage("[CASE 2] Fall dmg event cancelled");
				main.toLogger("List Length: " + main.dmgImunePlayers.size());
				main.dmgImunePlayers.remove(player.getName());
				main.toLogger("List Length: " + main.dmgImunePlayers.size());
			}
		}
	}
}