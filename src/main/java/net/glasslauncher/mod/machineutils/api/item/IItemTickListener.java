package net.glasslauncher.mod.machineutils.api.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;

public interface IItemTickListener {

    void onTick(PlayerBase entityplayer, ItemInstance itemstack);
}
