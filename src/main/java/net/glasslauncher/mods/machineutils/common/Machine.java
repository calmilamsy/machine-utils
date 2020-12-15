package net.glasslauncher.mods.machineutils.common;

public enum Machine {

    MACERATOR,
    COMPRESSOR,
    EXTRACTOR;

    private static final String modid = "machineutils";

    public static Machine fromType(String type) {
        for (Machine recipe : values())
            if (recipe.type().equals(type))
                return recipe;
        return null;
    }

    public String type() {
        return modid + ":" + name().toLowerCase();
    }
}
