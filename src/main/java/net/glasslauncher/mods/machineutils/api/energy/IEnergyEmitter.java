package net.glasslauncher.mods.machineutils.api.energy;

import net.glasslauncher.mods.machineutils.api.Direction;
import net.minecraft.tileentity.TileEntityBase;



public interface IEnergyEmitter
        extends IEnergyTile {

    boolean emitsEnergyTo(TileEntityBase tileentity, Direction direction);
}
