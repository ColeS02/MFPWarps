package com.unclecole.mfpwarps.database;

import com.unclecole.mfpwarps.database.serializer.Persist;
import com.unclecole.mfpwarps.database.serializer.Serializer;
import com.unclecole.mfpwarps.objects.Warp;

import java.util.*;

public class WarpData {

    private static transient WarpData instance = new WarpData();

    public static transient LinkedHashMap<UUID, Set<Warp>> playerWarps = new LinkedHashMap<>();

    public static LinkedHashSet<Warp> warps = new LinkedHashSet<>();
    public static transient LinkedHashSet<Warp> allWarps = new LinkedHashSet<>();


    public static void save(String uuid) {
        warps = (LinkedHashSet<Warp>) playerWarps.get(UUID.fromString(uuid));
        new Persist().save(instance, "/playerdata/" + uuid);
    }

    public static void load(String uuid) {
        warps = new LinkedHashSet<>();
        new Serializer().load(instance, WarpData.class, "/playerdata/" + uuid);
        playerWarps.put(UUID.fromString(uuid), warps);
        allWarps.addAll(warps);
    }

}