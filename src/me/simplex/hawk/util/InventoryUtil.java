package me.simplex.hawk.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryUtil {
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
