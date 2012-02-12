package me.simplex.hawk;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class Hawk extends JavaPlugin {
	protected static final String PREFIX = "[HAWK] ";
	protected enum Flystate{FLY, HOVER}
	protected LinkedList<String> dmgImunePlayers = new LinkedList<String>();
	protected HashMap<String, Flystate> flyingPlayers = new HashMap<String, Flystate>();
	private Logger log = Logger.getLogger("Minecraft");
	
	public void onDisable() {
		toLogger("disabled  "+getDescription().getFullName());
	}
	
	public void onEnable() {
		toLogger("loading  "+getDescription().getFullName()+" ...");
		
		HawkPlayerListener player_listener = new HawkPlayerListener(this);
		HawkEntityListener entity_listener = new HawkEntityListener(this);
		
		getServer().getPluginManager().registerEvents(player_listener, this);
		getServer().getPluginManager().registerEvents(entity_listener, this);
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new HawkTask(this), 1, 1);
		
		toLogger("loaded");
	}
	
	public void toLogger(String msg){
		log.info(PREFIX + msg);
	}
}