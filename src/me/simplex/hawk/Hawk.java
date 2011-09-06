package me.simplex.hawk;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;

public class Hawk extends JavaPlugin {
	protected static final String PREFIX = "[HAWK] ";
	protected enum Flystate{FLY, HOVER}
	protected HashMap<String, Flystate> flyingPlayers = new HashMap<String, Flystate>();
	private Logger log = Logger.getLogger("Minecraft");
	
	public void onDisable() {
		log.info(PREFIX+"disabled  "+getDescription().getFullName());
	}
	
	public void onEnable() {
		log.info(PREFIX+"loading  "+getDescription().getFullName()+" ...");
		
		HawkPlayerListener listener = new HawkPlayerListener(this);
		getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT	, listener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Type.PLAYER_QUIT		, listener, Priority.Normal, this);
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new HawkTask(this), 1, 1);
		
		log.info(PREFIX+"loaded");
	}
}