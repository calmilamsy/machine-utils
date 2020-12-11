package net.glasslauncher.mods.machineutils.common;

import net.modificationstation.stationloader.api.common.event.recipe.RecipeRegister;

public enum Machine {

    MACERATOR,
    COMPRESSOR,
    EXTRACTOR;

    public String type() {
        return modid + ":" + name().toLowerCase();
    }

    public static Machine fromType(String type) {
        for (Machine recipe : values())
            if (recipe.type().equals(type))
                return recipe;
        return null;
    }

    private static final String modid = "machineutils";
}
