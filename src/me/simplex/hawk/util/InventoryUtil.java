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
package me.simplex.hawk.util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Utility class to provide {@link Inventory} related methods
 * 
 * @author s1mpl3x
 *
 */
public class InventoryUtil {
	/**
	 * removes 1 item from the given {@link PlayerInventory}
	 * 
	 * @param inv the inventory to remove the given type from
	 * @param type the {@link Material} to remove
	 */
	public static void removeOneFromPlayerInventory(PlayerInventory inv, Material type){
		for (int index = 0; index < inv.getSize(); index++) {
			ItemStack stack = inv.getItem(index);
			if (stack != null) {
				if (stack.getType() == type) {
					stack.setAmount(stack.getAmount()-1);
					inv.setItem(index, stack);
					return;
				}
			}
		}
	}
}
