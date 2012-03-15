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

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class Hawk extends JavaPlugin implements Filter {
	protected static final String PREFIX = "[HAWK] ";

	protected static HashSet<String> dmgImunePlayers 					= new HashSet<String>();
	protected static HashMap<String, HawkPlayerStatus> flyingPlayers 	= new HashMap<String, HawkPlayerStatus>();
	
	private static Logger log;
	
	public void onDisable() {}
	
	public void onEnable() {
		log = getLogger();
		log.setFilter(this);

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

	public boolean isLoggable(LogRecord record) {
		return !record.getMessage().contains("was kicked for floating too long!");
	}
}