package net.glasslauncher.mod.machineutils.impl.common;

public enum MachineRecipe {

    MACERATOR,
    COMPRESSOR,
    EXTRACTOR;

    public static MachineRecipe fromType(String type) {
        for (MachineRecipe recipe : values())
            if (recipe.type().equals(type))
                return recipe;
        return null;
    }

    public String type() {
        return "machineutils:" + name().toLowerCase();
    }
}
