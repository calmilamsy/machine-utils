// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.api.item;

import net.minecraft.level.Level;

public interface ITerraformingBP {

    int getConsume();

    int getRange();

    boolean terraform(Level world, int i, int j, int k);
}
