package net.glasslauncher.mods.machineutils.api.network;

import net.minecraft.entity.player.PlayerBase;

public interface INetworkItemEventListener {

    void onNetworkEvent(int i, PlayerBase entityplayer, int j);
}
