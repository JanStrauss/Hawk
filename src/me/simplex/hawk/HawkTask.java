package me.simplex.hawk;

import me.simplex.hawk.Hawk.Flystate;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class HawkTask implements Runnable {
	private Hawk main;
	private static final Vector HOVER = new Vector(0, 0.1, 0);
	private static final Vector HOVER_BOOST = new Vector(0, 1, 0);
	
	public HawkTask(Hawk main) {
		this.main = main;
	}

	public void run() {
		for (final String pn : main.flyingPlayer.keySet()) {
			Player p = main.getServer().getPlayer(pn);
			Flystate s = main.flyingPlayer.get(pn);
			
			if (p != null) {
				switch (s) {
				case FLY: doFly(p); break;
				case HOVER: doHover(p); break;
				}
			}
			else {
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
					public void run() {
						main.flyingPlayer.remove(pn);
					}
				});
				
			}
		}
	}
	
	private static void doFly(Player player){
		Vector view = player.getLocation().getDirection();
		player.setVelocity(view.multiply(1.5));
	}
	
	private static void doHover(Player player){
		if (player.isSneaking()) {
			player.setVelocity(HOVER_BOOST);
		}
		else {
			player.setVelocity(HOVER);
		}
	}
}