package me.simplex.hawk;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

public class HawkConfiguration {
	
	private static int item_ID 					= 288;
	private static double hover_boost 			= 0.1;
	private static double fly_boost 			= 1.5;
	
	private static boolean consume_item 		= false;
	private static int consume_ID 				= 288;
	private static int consume_seconds_fly 		= 5;
	private static int consume_seconds_hover 	= 15;
	
	private static String message_Fly 			= "you are now flying";
	private static String message_Hover 		= "you are now hovering";
	private static String message_Land 			= "you are now walking the dinosaur";
	
	/**
	 * @return a {@link Configuration} with the default values
	 */
	private static Configuration buildDefaultCfg(){
		Configuration default_cfg = new MemoryConfiguration();
		
		default_cfg.set("Hawk.ItemID", item_ID);
		default_cfg.set("Hawk.FlyBoost", fly_boost);
		default_cfg.set("Hawk.HoverBoost", hover_boost);
		
		default_cfg.set("Hawk.ConsumeItem.Enable", consume_item);
		default_cfg.set("Hawk.ConsumeItem.ItemID", consume_ID);
		default_cfg.set("Hawk.ConsumeItem.SecondsFly", consume_seconds_fly);
		default_cfg.set("Hawk.ConsumeItem.SecondsHover", consume_seconds_hover);
		
		default_cfg.set("Hawk.Messages.Fly", message_Fly);
		default_cfg.set("Hawk.Messages.Hover", message_Hover);
		default_cfg.set("Hawk.Messages.Land", message_Land);
		
		return default_cfg;
	}
	
	/**
	 * Loads the configuration and sets the values
	 * 
	 * @param main the main class of Hawk
	 */
	public static void loadConfiguration(Hawk main) {
		Hawk.toLogger("loading configuration");
		
		FileConfiguration cfg = main.getConfig();
		
		cfg.setDefaults(buildDefaultCfg());
		cfg.options().copyDefaults(true);
		
		item_ID 				= cfg.getInt("Hawk.ItemID");
		fly_boost 				= cfg.getDouble("Hawk.FlyBoost");
		hover_boost 			= cfg.getDouble("Hawk.HoverBoost");
		
		consume_item 			= cfg.getBoolean("Hawk.ConsumeItem.Enable");
		consume_ID				= cfg.getInt("Hawk.ConsumeItem.ItemID");
		consume_seconds_fly		= cfg.getInt("Hawk.ConsumeItem.SecondsFly");
		consume_seconds_hover	= cfg.getInt("Hawk.ConsumeItem.SecondsHover");
	
		message_Fly 			= cfg.getString("Hawk.Messages.Fly");
		message_Hover 			= cfg.getString("Hawk.Messages.Hover");
		message_Land 			= cfg.getString("Hawk.Messages.Land");
		
		String header = main.getDescription().getFullName() + " Configuration | If you need help with this file, check http://www.lemonparty.org";
		cfg.options().header(header);
		
		main.saveConfig();
		
		Hawk.toLogger("configuration loaded");
	}

	// Getter
	public static Material getItem() {
		return Material.getMaterial(item_ID);
	}

	public static double getHover_boost() {
		return hover_boost;
	}

	public static double getFly_boost() {
		return fly_boost;
	}

	public static boolean consume_item() {
		return consume_item;
	}

	public static Material getConsume_Item() {
		return Material.getMaterial(consume_ID);
	}

	public static int getConsume_Seconds_Fly() {
		return consume_seconds_fly;
	}
	
	public static int getConsume_Seconds_Hover() {
		return consume_seconds_hover;
	}

	public static String getMessage_Fly() {
		return message_Fly;
	}

	public static String getMessage_Hover() {
		return message_Hover;
	}

	public static String getMessage_Land() {
		return message_Land;
	}
}
