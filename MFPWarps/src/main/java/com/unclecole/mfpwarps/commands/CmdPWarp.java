package com.unclecole.mfpwarps.commands;

import com.unclecole.mfpwarps.MFPWarps;
import com.unclecole.mfpwarps.database.WarpData;
import com.unclecole.mfpwarps.objects.Category;
import com.unclecole.mfpwarps.objects.Warp;
import com.unclecole.mfpwarps.utils.*;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CmdPWarp implements CommandExecutor {

    public final MFPWarps INSTANCE;
    public final FileConfiguration CONFIG;

    public final int MENU_SIZE = 54;

    public CmdPWarp(MFPWarps instance) {
        this.INSTANCE = instance;
        this.CONFIG = instance.getConfig();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if(args.length < 1) {
            openWarps(player, 1, Category.ALL_CATEGORIES);
        }
        if(args.length == 1) {
            for(Warp warp : WarpData.allWarps) {
                if(args[0].equalsIgnoreCase(ChatColor.stripColor(C.color(warp.getName())))) {
                    player.teleport(warp.getLocation());
                    //TODO: Finish this stupid shit
                    break;
                }
            }
            return false;
        }
        if(args.length >= 2 && args[0].equalsIgnoreCase("set")) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null);

            if(claim == null) {
                TL.CANT_SET_WARP.send(player);
                return false;
            }

            String warpName = ChatColor.stripColor(C.color(args[1]));

            boolean found = false;
            for(Warp warp : WarpData.allWarps) {
                String name = ChatColor.stripColor(C.color(warp.getName()));
                if(name.equalsIgnoreCase(warpName)) {
                    TL.NAME_TAKEN.send(player);
                    found = true;
                    break;
                }
            }

            if(found) {
                return false;
            }


            Warp warp = new Warp(player.getUniqueId(), warpName, player.getLocation(), Category.ALL_CATEGORIES);

            WarpData.playerWarps.getOrDefault(player.getUniqueId(), new HashSet<>()).add(warp);
            WarpData.warps.add(warp);
            WarpData.allWarps.add(warp);
            WarpData.save(player.getUniqueId().toString());
            TL.SET_WARP.send(player,
                    new PlaceHolder("%player%", player.getName()),
                    new PlaceHolder("%warp%", warpName));
        }

        return false;
    }

    public void openWarps(Player player, int page, Category category) {
        MenuUtility menu = new MenuUtility(C.color("&7Player Warps"), MENU_SIZE, player.getUniqueId());
        ArrayList<Warp> list = new ArrayList(WarpData.allWarps);
        int slot = 0;
        for (int i = ((MENU_SIZE - 9) * (page - 1)); i < list.size(); i++) {
            if ((MENU_SIZE * page) - 9 <= i) break;

            Warp warp = list.get(i);

            if (!category.equals(Category.ALL_CATEGORIES) && !warp.getCategory().equals(category)) continue;

            List<String> lore = new ArrayList<>();

            ItemStack shops = new ItemStack(warp.getMaterial());

            ItemMeta shopMeta = shops.getItemMeta();
            shopMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            shopMeta.setDisplayName(C.color(warp.getName()));

            lore.add("Claim: " + warp.getClaim());
            lore.add("Visits: " + warp.getVisits());
            lore.add("Category: " + warp.getCategory().getName());
            lore.add("Location: " + warp.getX() + " " + warp.getY() + " " + warp.getZ());
            lore.add("");
            lore.add("Click to warp!");


            shopMeta.setLore(lore);
            shops.setItemMeta(shopMeta);
            menu.setItem(shops, slot, clickEvent -> {
                clickEvent.setCancelled(true);
                if (!warp.getUUID().equalsIgnoreCase(player.getUniqueId().toString()) || !clickEvent.getAction().equals(InventoryAction.PICKUP_HALF)) {
                    if (!warp.getUUID().equalsIgnoreCase(player.getUniqueId().toString())) warp.addVisit();
                    player.teleport(warp.getLocation());
                    return;
                }

                editWarp(player, warp);
            });
            slot++;
        }

        ItemBuilder placeholder = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
                .name(C.color("&a"));
        ItemMeta meta = placeholder.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        placeholder.setItemMeta(meta);

        menu.setItem(placeholder, 45, 46, 47, 48, 49, 50, 51, 52, 53);

        ItemBuilder categorySwitch = new ItemBuilder(Material.BOOK);
        categorySwitch.name(category.getName());

        menu.setItem(categorySwitch, MENU_SIZE - 1, event -> {
            openWarps(player, page, categorySwap(category));
        });

        ItemBuilder nextPage = new ItemBuilder(Material.GREEN_SHULKER_BOX).name(C.color("&a&lNEXT"));

        if (list.size() >= page * 45) {
            menu.setItem(nextPage, 50, event -> {
                openWarps(player, page + 1, category);
            });
        }

        if (page - 1 > 0) {
            ItemBuilder previousPage = new ItemBuilder(Material.RED_SHULKER_BOX).name(C.color("&c&lBACK"));
            menu.setItem(previousPage, 48, event -> {
                openWarps(player, page - 1, category);
            });
        }

        menu.openInventory(player);
    }

    public void editWarp(Player player, Warp warp) {

        MenuUtility menu = new MenuUtility(C.color("Warp Editor"), 27, player.getUniqueId(), new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(C.color("&a")));

        ItemBuilder blockChange = new ItemBuilder(warp.getMaterial())
                .name(C.color("&aBlock Change"));

        ItemBuilder categoryChange = new ItemBuilder(Material.WRITTEN_BOOK)
                .name(C.color("&aChange Category"))
                .lore("Current Category " + warp.getCategory().getName());

        ItemBuilder nameChange = new ItemBuilder(Material.PAPER)
                .name(C.color("&aName Change"));

        ItemBuilder deleteWarp = new ItemBuilder(Material.BARRIER)
                .name(C.color("&cRemove Warp"));

        menu.setItem(blockChange, 10, event -> {
            if(event.getCursor() == null) return;
            if(event.getCursor().getType().equals(Material.AIR)) return;

            warp.setMaterial(event.getCursor().getType());
            editWarp(player, warp);
        });

        menu.setItem(categoryChange, 12, event -> {
            warp.setCategory(categorySwap(warp.getCategory()));
            editWarp(player, warp);
        });

        menu.setItem(nameChange, 14, event -> {
            warp.setNameEditTime(System.currentTimeMillis() / 1000);
            MFPWarps.getInstance().getNameEditors().put(event.getView().getPlayer().getUniqueId(), warp);
            TL.WRITE_NAME.send(player);
            player.closeInventory();
        });

        menu.setItem(deleteWarp, 16, event -> {
            confirmationMenu(player,warp);
        });

        menu.openInventory(player);
    }

    public void confirmationMenu(Player player, Warp warp) {
        MenuUtility menu = new MenuUtility("Confirmation Menu", 27, player.getUniqueId(), new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(C.color("&a")));

        ItemBuilder confirm = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                .name(C.color("&a&lCONFIRM"));

        ItemBuilder deny = new ItemBuilder(Material.RED_STAINED_GLASS_PANE)
                .name(C.color("&c&lDENY"));

        menu.setItem(confirm, 11, event -> {
            WarpData.allWarps.remove(warp);
            WarpData.warps.remove(warp);
            WarpData.playerWarps.get(player.getUniqueId()).remove(warp);

            player.closeInventory();
            TL.PWARP_DELETED.send(player);
        });

        menu.setItem(deny, 15, event -> {
            editWarp(player, warp);
        });

        menu.openInventory(player);
    }

    public Category categorySwap(Category category) {
        switch (category) {
            case ALL_CATEGORIES:
                return Category.MOB_GRINDERS;
            case MOB_GRINDERS:
                return Category.SHOPS;
            case SHOPS:
                return Category.BOOKS;
            case BOOKS:
                return Category.GAMES;
            case GAMES:
                return Category.ARENAS;
            case ARENAS:
                return Category.PARKOUR;
            case PARKOUR:
                return Category.ALL_CATEGORIES;
        }
        return Category.ALL_CATEGORIES;
    }

}
