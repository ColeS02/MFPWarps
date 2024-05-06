package com.unclecole.mfpwarps.objects;

import com.unclecole.mfpwarps.MFPWarps;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.*;

import java.util.UUID;

public class Warp {

    private String uuid;
    private String name;
    private String claim;
    private Material material;
    private String location;
    private Category category;
    private int visits;

    private transient long nameEditTime;

    public Warp(UUID uuid, String name, Location location, Category category) {
        this.uuid = uuid.toString();
        this.name = name;
        Chunk chunk = location.getChunk();
        this.claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null).getOwnerName();
        this.location = MFPWarps.getInstance().locationUtility.parseToString(location);
        this.material = Material.GRASS_BLOCK;
        this.category = category;
        this.visits = 0;
    }

    public String getUUID() { return this.uuid; }

    public String getName() {
        return this.name;
    }

    public String getClaim() {
        return this.claim;
    }

    public int getVisits() {
        return this.visits;
    }

    public void addVisit() {
        this.visits += 1;
    }

    public Category getCategory() {
        return this.category;
    }

    public long getNameEditTime() { return this.nameEditTime; }

    public void setName(String name) { this.name = name; }

    public void setNameEditTime(long time) { this.nameEditTime = time; }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getX() {
        String[] data = location.split(":");
        return data[0];
    }

    public String getY() {
        String[] data = location.split(":");
        return data[1];
    }

    public String getZ() {
        String[] data = location.split(":");
        return data[2];
    }

    public Location getLocation() {
        return MFPWarps.getInstance().locationUtility.parseToLocation(location);
    }
}
