// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.api.block;

import net.minecraft.entity.player.PlayerBase;

public interface IWrenchable
{

    boolean wrenchSetFacing(PlayerBase entityplayer, int i);

    void setFacing(short word0);

    boolean wrenchRemove(PlayerBase entityplayer);

    float getWrenchDropRate();
}
