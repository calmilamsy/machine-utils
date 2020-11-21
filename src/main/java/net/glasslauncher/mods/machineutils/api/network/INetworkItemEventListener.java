// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.glasslauncher.mods.machineutils.api.network;

import net.minecraft.entity.player.PlayerBase;

public interface INetworkItemEventListener
{

    void onNetworkEvent(int i, PlayerBase entityplayer, int j);
}
