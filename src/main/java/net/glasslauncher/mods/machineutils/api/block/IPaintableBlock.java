// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.api.block;

import net.minecraft.level.Level;

public interface IPaintableBlock {

    boolean colorBlock(Level world, int i, int j, int k, int l);
}
