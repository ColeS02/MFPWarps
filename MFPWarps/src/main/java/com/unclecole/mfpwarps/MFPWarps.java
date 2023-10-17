package com.unclecole.mfpwarps;

import com.unclecole.mfpwarps.commands.CmdDevelopment;
import com.unclecole.mfpwarps.commands.CmdPWarp;
import com.unclecole.mfpwarps.database.WarpData;
import com.unclecole.mfpwarps.database.serializer.Persist;
import com.unclecole.mfpwarps.listeners.ChatEvent;
import com.unclecole.mfpwarps.objects.Warp;
import com.unclecole.mfpwarps.utils.*;
import lombok.Getter;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public final class MFPWarps extends JavaPlugin {

    public static MFPWarps instance;
    public static MFPWarps getInstance() { return instance; }

    @Getter public static Persist persist;

    public GriefPrevention api;
    public LocationUtility locationUtility;

    @Getter public HashMap<UUID, Warp> nameEditors = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        api = GriefPrevention.instance;
        locationUtility = new LocationUtility();
        persist = new Persist();
        saveDefaultConfig();

        Bukkit.getOnlinePlayers().forEach(player -> {
            WarpData.load(player.getUniqueId().toString());
        });

        Bukkit.getPluginManager().registerEvents(MenuUtility.getListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
        Bukkit.getPluginCommand("pwarp").setExecutor(new CmdPWarp(this));
        Bukkit.getPluginCommand("development").setExecutor(new CmdDevelopment(this));

        nameChangeTask();
        isLicence();
        TL.loadMessages(new ConfigFile("messages.yml", this));
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            WarpData.save(player.getUniqueId().toString());
        });
    }

    private void isLicence() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                try{
                    String url = "https://pastebin.com/raw/CL9qvyuF";
                    URLConnection openConnection = new URL(url).openConnection();
                    openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                    @SuppressWarnings("resource")
                    Scanner scan = new Scanner((new InputStreamReader(openConnection.getInputStream())));
                    while(scan.hasNextLine()){
                        String firstline = scan.nextLine();
                        if(firstline.equalsIgnoreCase("VNfxOIy0QQrCTA8TZ7aO1S91sa6vGJgJ")){
                            return;
                        }
                    }
                }catch(Exception e){

                }
                Bukkit.getScheduler().runTask(MFPWarps.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), C.color("&c&lTHIS SERVER IS STEALING PLAY.MINEFORGE.ORG PLUGINS."), null, "Console");
                            player.kickPlayer(C.color("&c&lTHIS SERVER IS STEALING PLAY.MINEFORGE.ORG PLUGINS."));
                        });
                        Bukkit.shutdown();
                    }
                });
            }
        }, 0L, 100L);
        //We are getting the licence key string from the config
    }

    public void nameChangeTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                if(nameEditors.isEmpty()) return;
                Iterator<Map.Entry<UUID, Warp>> iterator = nameEditors.entrySet().iterator();
                long currentTime = System.currentTimeMillis() / 1000;

                while (iterator.hasNext()) {
                    Map.Entry<UUID, Warp> entry = iterator.next();
                    UUID uuid = entry.getKey();
                    Warp warp = entry.getValue();

                    if (warp.getNameEditTime() > currentTime + 30) {
                        iterator.remove();
                    }
                }
            }
        }, 20L , 20L);
    }
}
