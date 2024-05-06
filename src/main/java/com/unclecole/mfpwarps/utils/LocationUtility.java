package com.unclecole.mfpwarps.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtility {

    public Location parseToLocation(String string) {
        if (string == null) return null;
        String[] data = string.split(":");
        try {
            double x = Double.parseDouble(data[0]);
            double y = Double.parseDouble(data[1]);
            double z = Double.parseDouble(data[2]);
            World world = Bukkit.getServer().getWorld(data[3]);
            return new Location(world, x, y, z);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String parseToString(Location location) {
        return location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getWorld().getName();
    }

    public String parseToString(Chunk chunk) {
        return chunk.getX() + ":" + chunk.getZ() + ":" + chunk.getWorld().getName();
    }

}
