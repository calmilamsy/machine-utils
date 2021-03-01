package net.glasslauncher.mod.machineutils.api.block;

import net.minecraft.entity.player.PlayerBase;

public interface IWrenchable {

    boolean wrenchSetFacing(PlayerBase entityplayer, int i);

    void setFacing(short word0);

    boolean wrenchRemove(PlayerBase entityplayer);

    float getWrenchDropRate();
}
