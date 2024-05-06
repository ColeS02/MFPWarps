package com.unclecole.mfpwarps.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MenuUtility {

    private Inventory inv;
    public String name;
    public static Map<UUID, MenuUtility> inventories = new HashMap<>();
    private Map<Integer, ClickRunnable> runnables = new HashMap<>();
    private UUID uuid;

    public MenuUtility(String name, int size, UUID uuid) {
        this(name, size, uuid, null);
    }

    public MenuUtility(String name, int size, UUID uuid, ItemStack placeholder) {
        this.name = C.color(name);
        this.uuid = uuid;
        if (size == 0) {
            return;
        }
        this.inv = Bukkit.createInventory(null, size, C.color(name));
        if (placeholder != null) {
            ItemMeta itemMeta = placeholder.getItemMeta();
            itemMeta.setDisplayName(" ");
            placeholder.setItemMeta(itemMeta);
            for (int i = 0; i < size; i++) {
                inv.setItem(i, placeholder);
            }
        }
        this.register();
    }

    public Inventory getInventory() {
        return inv;
    }

    public int getSize() {
        return inv.getSize();
    }

    public String getName() {
        return name;
    }

    public void setItem(ItemStack itemstack, Integer slot, ClickRunnable executeOnClick) {
        setItem(itemstack, null, null, slot, executeOnClick);
    }

    public void setItem(ItemStack itemstack, String displayname, List<String> description, Integer slot, ClickRunnable executeOnClick) {
        ItemStack is = itemstack;
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS,
                ItemFlag.HIDE_UNBREAKABLE);
        if (displayname != null) {
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayname));
        }
        if (description != null) {
            List<String> lore = new ArrayList<String>();
            for (String message : description) {
                lore.add(ChatColor.translateAlternateColorCodes('&', message));
            }
            im.setLore(lore);
        }
        is.setItemMeta(im);
        inv.setItem(slot, is);
        runnables.put(slot, executeOnClick);
    }

    public void removeItem(int slot) {
        inv.setItem(slot, new ItemStack(Material.AIR));
    }

    public void setItem(ItemStack itemstack, Integer slot) {
        inv.setItem(slot, itemstack);
    }

    public void setItem(ItemStack itemStack, Integer... slots) {
        for(Integer slot : slots) {
            inv.setItem(slot, itemStack);
        }
    }

    public static Listener getListener() {
        return new Listener() {
            @EventHandler
            public void onClick(InventoryClickEvent e) {
                HumanEntity clicker = e.getWhoClicked();
                if (clicker instanceof Player) {
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    Player p = (Player) clicker;
                    if (p != null) {
                        UUID uuid = p.getUniqueId();
                        if (inventories.containsKey(uuid)) {
                            MenuUtility current = inventories.get(uuid);
                            if (!e.getView().getTitle()
                                    .equalsIgnoreCase(current.getName())) {
                                return;
                            }
                            e.setCancelled(true);
                            if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
                                if(!e.getView().getTitle().equalsIgnoreCase("Warp Editor")) return;
                                e.setCancelled(false);
                                return;
                            }
                            int slot = e.getSlot();
                            if (current.runnables.get(slot) != null) {
                                current.runnables.get(slot).run(e);
                            }
                        }
                    }
                }
            }

            @EventHandler
            public void onClose(InventoryCloseEvent e) {
                if (e.getPlayer() instanceof Player) {
                    if (e.getInventory() == null) {
                        return;
                    }
                    Player p = (Player) e.getPlayer();
                    UUID uuid = p.getUniqueId();
                    if (inventories.containsKey(uuid)) {
                        inventories.get(uuid).unRegister();
                    }
                }
            }
        };
    }

     public void openInventory(Player player) {
        Inventory inv = getInventory();
        InventoryView openInv = player.getOpenInventory();
        if (openInv != null) {
            //Inventory openTop = player.getOpenInventory().getTopInventory();
            //if (openTop != null && openTop.getTitle().equalsIgnoreCase(inv.getTitle())) {
              //  openTop.setContents(inv.getContents());
            //} else {
                player.openInventory(inv);
            //}
            register();
        }
    }

    private void register() {
        inventories.put(this.uuid, this);
    }

    private void unRegister() {
        inventories.remove(this.uuid);
    }

    @FunctionalInterface
    public interface ClickRunnable {
        public void run(InventoryClickEvent event);
    }

    @FunctionalInterface
    public interface CloseRunnable {
        public void run(InventoryCloseEvent event);
    }

}
