package com.unclecole.mfpwarps.utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder extends ItemStack {
    public ItemBuilder(Material mat) {
        super(mat);
    }

    public ItemBuilder(ItemStack is) {
        super(is);
    }

    public ItemBuilder amount(int amount) {
        this.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name) {
        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName(name);
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> text) {
        ItemMeta meta = this.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList();
        }

        ((List)lore).addAll(text);
        meta.setLore((List)lore);
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String text) {
        ItemMeta meta = this.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList();
        }

        ((List)lore).add(text);
        meta.setLore((List)lore);
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder durability(int durability) {
        this.setDurability((short)durability);
        return this;
    }

    public ItemBuilder data(int data) {
        this.setData(new MaterialData(this.getType(), (byte)data));
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        this.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment) {
        this.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder type(Material material) {
        this.setType(material);
        return this;
    }

    public ItemBuilder clearLore() {
        ItemMeta meta = this.getItemMeta();
        meta.setLore(new ArrayList());
        this.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearEnchantments() {
        this.getEnchantments().keySet().forEach(this::removeEnchantment);
        return this;
    }

    public ItemBuilder color(Color color) {
        if (this.getType() != Material.LEATHER_BOOTS && this.getType() != Material.LEATHER_CHESTPLATE && this.getType() != Material.LEATHER_HELMET && this.getType() != Material.LEATHER_LEGGINGS) {
            throw new IllegalArgumentException("color() only applicable for leather armor!");
        } else {
            LeatherArmorMeta meta = (LeatherArmorMeta)this.getItemMeta();
            meta.setColor(color);
            this.setItemMeta(meta);
            return this;
        }
    }
}
