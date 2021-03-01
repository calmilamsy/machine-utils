package net.glasslauncher.mod.machineutils.api.block;

import net.minecraft.level.Level;

public interface IPaintableBlock {

    boolean colorBlock(Level world, int i, int j, int k, int l);
}
