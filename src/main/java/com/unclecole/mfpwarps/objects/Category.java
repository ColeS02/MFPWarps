package com.unclecole.mfpwarps.objects;

public enum Category {
    ALL_CATEGORIES("All Categories"),
    MOB_GRINDERS("Mob Grinder"),
    SHOPS("Shop"),
    BOOKS("Books"),
    GAMES("Games"),
    ARENAS("Arena"),
    PARKOUR("Parkour");

    public String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
