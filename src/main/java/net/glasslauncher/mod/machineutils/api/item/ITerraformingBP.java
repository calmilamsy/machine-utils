package net.glasslauncher.mod.machineutils.api.item;

import net.minecraft.level.Level;

public interface ITerraformingBP {

    int getConsume();

    int getRange();

    boolean terraform(Level world, int i, int j, int k);
}
