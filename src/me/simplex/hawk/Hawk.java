package me.simplex.hawk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class Hawk extends JavaPlugin {
	protected static final String PREFIX = "[HAWK] ";

	protected static HashSet<String> dmgImunePlayers 					= new HashSet<String>();
	protected static HashMap<String, HawkPlayerStatus> flyingPlayers 	= new HashMap<String, HawkPlayerStatus>();
	
	private static Logger log;
	
	public void onDisable() {}
	
	public void onEnable() {
		log = getLogger();

		HawkConfiguration.loadConfiguration(this);

		HawkPlayerListener player_listener = new HawkPlayerListener();
		HawkEntityListener entity_listener = new HawkEntityListener();
		
		getServer().getPluginManager().registerEvents(player_listener, this);
		getServer().getPluginManager().registerEvents(entity_listener, this);
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new HawkTask(this), 1, 1);
		
		toLogger("loaded " + getDescription().getFullName());
	}
	
	public static void toLogger(String msg){
		log.info(msg);
	}
}