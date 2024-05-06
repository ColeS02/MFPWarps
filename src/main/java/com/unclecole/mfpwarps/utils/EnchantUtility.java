package com.unclecole.mfpwarps.utils;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EnchantUtility {

    public static ItemStack applyEnchantment(ItemStack itemStack, String enchantment, int level, ChatColor chatColor, int previousLevel) {

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (!itemMeta.getLore().contains(chatColor + enchantment + " " + previousLevel)) {
            List<String> currentLore = new ArrayList<>(itemMeta.getLore());
            currentLore.add(chatColor + enchantment + " " + level);
            itemMeta.setLore(currentLore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        } else {
            List<String> newLore = new ArrayList<>();
            for (String data : itemMeta.getLore()) {
                if (data.contains(chatColor + enchantment)) {
                    newLore.add(chatColor + enchantment + " " + level);
                    continue;
                }
                newLore.add(data);
            }
            itemMeta.setLore(newLore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
    }

}
