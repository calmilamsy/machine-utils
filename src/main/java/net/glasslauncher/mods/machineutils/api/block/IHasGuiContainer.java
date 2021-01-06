package net.glasslauncher.mods.machineutils.api.block;

import net.minecraft.container.ContainerBase;
import net.minecraft.entity.player.PlayerInventory;

public interface IHasGuiContainer {

    ContainerBase getGuiContainer(PlayerInventory inventoryplayer);
}
